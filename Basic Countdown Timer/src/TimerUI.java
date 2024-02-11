import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.BorderLayout;
import javax.swing.JButton;

public class TimerUI {

	private Timer timer;
	private JFrame frame;
	
	// Counts the number of seconds/time in the timer
	private int seconds = 0;
	private JLabel mainLabel;
	private JButton startButton;
	public boolean isTimerRunning;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TimerUI window = new TimerUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TimerUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Creates the new frame/window
		frame = new JFrame();
		
		// Sets the size of the frame/window
		frame.setBounds(100, 100, 450, 300);
		
		// Sets the default close operation for the frame/window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Sets the title of the frame/window
		frame.setTitle("Basic Timer UI");
		frame.getContentPane().setLayout(null);
		
		// Creates a new label
		mainLabel = new JLabel("00:00:00");
		// Sets the size of the label
		mainLabel.setBounds(141, 107, 151, 46);
		// Sets the horizontal alignment of the label text. Centers the text in the label
		mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		// Sets the font of the label text
		mainLabel.setFont(new Font("Digital-7", Font.PLAIN, 28));
		// Adds the label to the frame/window
		frame.getContentPane().add(mainLabel);
		
		// Creates a new button with set text
		startButton = new JButton("Start Timer");
		
		// Sets the size of the button
		startButton.setBounds(162, 190, 109, 23);
		
		// Adds the button to the frame/window
		frame.getContentPane().add(startButton);
		
		// Adds a callback to the startButton
		startButton.addActionListener(new ActionListener() {
			// The action performed checks to see if the timer is running, and either stops the timer from counting or starts it
			public void actionPerformed(ActionEvent e) {
				
				if (!isTimerRunning) {
					startTimer();
				}
				else {
					stopTimer();
				}
				
			}
		});
		
		// Creates a timer and attaches a callback that calls the updateTimer function
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTimerLabel();
			}
			
		});
			
	}
	
	// Function that starts the timer
	private void startTimer() {
		timer.start();
		// Sets the timerRunning bool to true to show the timer is running
		isTimerRunning = true;
		// Changes the button text
		startButton.setText("Stop Timer");
	}
	
	// Function that stops the timer
	private void stopTimer() {
		timer.stop();
		// Sets the timerRunning bool to false to show the timer is stopped
		isTimerRunning = false;
		// Changes the button text
		startButton.setText("Start Timer");
	}
	
	// Function that updates the timer label.
	// Basically, this function updates the time being counted in the UI
	private void updateTimerLabel() {
		seconds++;
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		int secs = seconds % 60;
		mainLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, secs));
		
	}
	
	
}
