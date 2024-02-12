package com.domobot.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class mainGameClass extends ApplicationAdapter {
	
	// Used to store the texture for the water drop
	private Texture dropImage;
	
	// Used to store the texture for the bucket
	private Texture bucketImage;
	
	// Used to store the sound file for the water drop
	private Sound dropSound;
	
	// Used to store the music file for the rain sounds
	private Music rainMusic;
	
	// Holds the entity to be rendered for the bucket
	private Rectangle bucket;
	
	// An array that holds all of the raindrops that were displayed
	private Array<Rectangle> raindrops;
	
	// An int based variable that holds the time for when a drop was last dropped
	private long lastDropTime;
	
	// Basic camera setup
	private OrthographicCamera camera;
	
	// Basic SpriteBatch setup
	private SpriteBatch batch;
	
	// Used to tell the num of drops collected
	int dropsCollected;
	
	// Used to tell the num of drops lost/not collected
	int dropsLost;
	
	// Used to display the num of drops collected text to screen
	private BitmapFont font;
	
	// Used to tell if the user has lost the game
	boolean gameOver;
	
	@Override
	public void create () {
		
		// Loads the water droplet and bucket images
		dropImage = new Texture(Gdx.files.internal("drop.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		
		// Loads the water droplet collection sound and the background music
		dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop_sound.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain_sound.mp3"));
		
		// Initializes the gameOver variable for the future loop
		gameOver = false;
		
		// Sets the music to loop and starts playing it
		rainMusic.setLooping(true);
		rainMusic.play();
		 
		font = new BitmapFont();
		
		// Sets up the camera and size of the camera scope
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		// Creates the SpriteBatch where the bucket and raindrops will appear
		batch = new SpriteBatch();
		
		// Defines the bucket's x and y positions, as well as the width and height of the sprite
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;
		
		// Defines an array for the raindrops to be held in
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
		
	}
	
	// Function used to spawn in raindrops randomly at the top of the screen every set interval
	private void spawnRaindrop() {
		  // Creates a raindrop object via a rectangle
	      Rectangle raindrop = new Rectangle();
	      
	      // Sets the x and y coords of the raindrop. X pos is a random num along the top of the screen
	      raindrop.x = MathUtils.random(0, 800-64);
	      raindrop.y = 480;
	      
	      // Sets the width and height of the raindrop
	      raindrop.width = 64;
	      raindrop.height = 64;
	      
	      // Adds the raindrop to the list of raindrops
	      raindrops.add(raindrop);
	      
	      // Assigns the time at which the drop was dropped on screen
	      lastDropTime = TimeUtils.nanoTime();
	}
	
	@Override
	public void render() {
		
		if (!gameOver) {
			
			// Sets up the background of the screen
			ScreenUtils.clear(0, 0, 0.2f, 1);
			
			// Makes sure the camera updates every time
			camera.update();
			
			// Starts the batch/screen
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			
			// Draws a font/text in the top of the screen showing the num of drops collected
			font.draw(batch, "Drops Collected: " + dropsCollected, 0, Gdx.graphics.getHeight());
			
			// Draws the bucket image into the screen at set x and y positions
			batch.draw(bucketImage, bucket.x, bucket.y);
			
			// Loop that spawns in the raindrops via the raindrop list
			for (Rectangle raindrop: raindrops) {
				batch.draw(dropImage, raindrop.x, raindrop.y);
			}
			
			batch.end();
			
			// Creates touch control for the bucket
			if(Gdx.input.isTouched()) {
			      Vector3 touchPos = new Vector3();
			      touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			      camera.unproject(touchPos);
			      bucket.x = touchPos.x - 64 / 2;
			}
			
			// Creates keyboard control for the bucket
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
			
			// Prevents the bucket from moving outside of the screen if the bucket is moved outside of the x-axis
			if(bucket.x < 0) bucket.x = 0;
			if(bucket.x > 800 - 64) bucket.x = 800 - 64;
			
			// If the current time minus the last time a drop was dropped is less than a set value, another raindrop is spawned
			if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
			
			// Loop used to control the movement of the raindrops
			for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
			      Rectangle raindrop = iter.next();
			      raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			      if(raindrop.y + 64 < 0) {
			    	  iter.remove();
			    	  dropsLost++;
			      }
			      
			      if(raindrop.overlaps(bucket)) {
			    	  dropsCollected++;
			          dropSound.play();
			          iter.remove();
			      }
			}
			
			// If the user lets 10 raindrops go past the screen, then they will get a game over. -- Still WIP -- 
			if (dropsLost >= 10) {
				gameOver = true;
				renderGameOverScreen();
			}
		}
		
		else {
			renderGameOverScreen();
		}
		
	}
	
	// Used to display a text saying game over
	private void renderGameOverScreen() {
		raindrops.clear();
		
        batch.begin();
        font.draw(batch, "Game Over", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        batch.end();
    }
	
	@Override
	public void dispose() {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}
	
	
}
