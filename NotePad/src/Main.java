import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

// Start of main class where all functionality takes place
public class Main {

	private JFrame frame;
	private JTextArea textArea;
	
	private File currentFile = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
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
	public Main() {
		// Initializes the frame and sets up everything in the UI
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Creates the frame
		frame = new JFrame();
		
		// Sets the size of the frame/window
		frame.setBounds(100, 100, 650, 800);
		
		// Sets the default close operation for the frame/window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Sets the current layout
		frame.getContentPane().setLayout(null);
		
		// Sets the title for the frame 
		frame.setTitle("Untitled");
		
		// Creates a menubar object to be placed at the top of the frame
		JMenuBar menuBar = new JMenuBar();
		
		// Actually places the menubar and sizes it
		menuBar.setBounds(0, 0, 634, 34);
		
		// Adds the menubar to the frame/window
		frame.getContentPane().add(menuBar);
		
		// Adds a menu to the menuBar. Acts as a menu tab/header
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		// Adds a menu item to the newly added file menu tab
		JMenuItem newFile = new JMenuItem("New File");
		fileMenu.add(newFile);
		
		// Adds a callback function to the newFile button.
		newFile.addActionListener(new ActionListener() {
			// The action performed in this callback function sets the textArea back to blank and resets the currentFile. Basically creates a new file.
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				
				currentFile = null;
				
				frame.setTitle("untitled");
			}
			
		});
		
		// Adds a menu item to the file menu tab
		JMenuItem newWindow = new JMenuItem("New Window");
		fileMenu.add(newWindow);
		
		// Adds a callback function to the newWindow button.
		newWindow.addActionListener(new ActionListener() {
			// The action performed in this callback function closes the current window via the closeWindow() call and then creates a new window by creating another Main object.
			public void actionPerformed(ActionEvent e) {
				closeWindow();
				
				Main newEditor = new Main();
                newEditor.frame.setVisible(true);
			}
		});
		
		// Adds a menu item to the file menu tab
		JMenuItem openFile = new JMenuItem("Open File");
		fileMenu.add(openFile);
		
		// Adds a callback function to the openFile button.
		openFile.addActionListener(new ActionListener() {
			// The action performed in this callback function opens a new fileChooser, which lets a user select a file from the file system.
			// Then, the window is either updated with the contents of the selected file, or nothing happens.
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(frame);
                
                if (result == JFileChooser.APPROVE_OPTION) {
                	File selectedFile = fileChooser.getSelectedFile();
                	
                	readFile(selectedFile);
                	
                	currentFile = selectedFile;
                	
                	updateWindowTitle();
                }
			}
		});
		
		// Adds a menu item to the file menu tab
		JMenuItem saveFile = new JMenuItem("Save");
		fileMenu.add(saveFile);
		
		// Adds a callback function to the saveFile button
		saveFile.addActionListener(new ActionListener() {
			// The action performed in this callback function calls the saveToFile() function, where a new file is created and content is saved, or the currentfile has its content saved.
			public void actionPerformed(ActionEvent e) {
				saveToFile();
			}
		});
		
		// Adds a menu item to the file menu tab
		JMenuItem saveFileAs = new JMenuItem("Save as");
		fileMenu.add(saveFileAs);
		
		// Adds a callback function to the saveFileAs button.
		saveFileAs.addActionListener(new ActionListener() {
			// The action performed in this callback function opens a fileChooser window for the user to select a file.
			// Then, the file is created and saved via the saveToFile(file) function. The window title is updated too.
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                fileChooser.setFileFilter(filter);
                
                int result = fileChooser.showSaveDialog(frame);
                
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    saveToFile(selectedFile);
                    
                    currentFile = selectedFile;
                	
                	updateWindowTitle();
                }
			}
		});
		
		// Adds a separator in the file menu tab between the exit button and the saveFileAs button
		fileMenu.add(new JSeparator());
		
		// Adds a menu item to the file menu tab
		JMenuItem exit = new JMenuItem("Exit");
		fileMenu.add(exit);
		
		// Adds the last callback function to the exit button.
		exit.addActionListener(new ActionListener() {
			// The action performed in this callback function just stops the execution of the app.
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		// This is the textArea which the user types in.
		textArea = new JTextArea();
		
		// Sets the size of the textArea
		textArea.setBounds(0, 34, 634, 727);
		
		// Adds the textArea to the frame/window.
		frame.getContentPane().add(textArea);
	}
	
	// Function used to close the window when needed. Used in the newWindow callback.
	private void closeWindow() {
		// Disposes of the current frame
        frame.dispose();
    }
	
	// Function used to read a file selected by the user. Used in the openFile callback.
	// Sets the text area to the stored content.
	private void readFile(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        textArea.setText(content.toString());
    }
	
	// Function used to update the frame title given the currentFile's filename minus the file extension
	private void updateWindowTitle() {
		
		//If the currentFile exists, aka not null, then the frame title gets changed
		if (currentFile != null) {
			frame.setTitle(currentFile.getName().replaceAll("\\..*$", ""));
		}
	}
	
	// Function to check if file to be saved has no text or is currently null.
	// Used in both cases where the file has a filename, and the case where it doesn't
	private void saveToFile() {
		
		// If there is no text in the current file, then nothing happens.
		if (textArea.getText().isEmpty()) {
            return;
        }
		
		//If the currentFile is null, aka, the file is not created yet in the file system, then the file is created via the saveToFileAs function.
		if (currentFile == null) {
            saveToFileAs();
            return;
        }
		
		// If the file is already created and has content, will then update the saved content of the file.
		saveToFile(currentFile);
	}
	
	// Function that takes in a file object that is selected/created by the user, and writes the content of the textArea to it. Used to update the saved content of the file
	// Only used once the file has a file name
	private void saveToFile(File file) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textArea.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	// Function that opens a file chooser via swing, that lets the user create a new file for later use. Updates the title of the window after the file has been saved as and given a file name.
	// Only used when the file does not already have a file name
	private void saveToFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showSaveDialog(frame);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveToFile(selectedFile);
            currentFile = selectedFile;
            updateWindowTitle();
        }
    }
}
