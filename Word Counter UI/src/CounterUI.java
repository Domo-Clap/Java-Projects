import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;

public class CounterUI {

	private JFrame frame;
	private JTextArea textField;
	private JDialog dia;
	
	public String total_string;
	public String[] parsed_string;
	public int numWords;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CounterUI window = new CounterUI();
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
	public CounterUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Basic Word Counter App");	
		frame.getContentPane().setLayout(null);
		
		JLabel mainLabel = new JLabel("Word Counter App");
		mainLabel.setBounds(110, 21, 214, 31);
		mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainLabel.setFont(new Font("Times New Roman", Font.PLAIN, 28));
		frame.getContentPane().add(mainLabel);
		
		textField = new JTextArea();
		textField.setBounds(77, 63, 279, 129);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton countBTN = new JButton("Count Words");
		countBTN.setBounds(87, 213, 103, 23);
		frame.getContentPane().add(countBTN);
		
		countBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				total_string = textField.getText();
				
				parsed_string = total_string.split(" ");
				
				numWords = parsed_string.length;
				
				dia = new JDialog(frame, "Total Words");
				
				JLabel mainDiaText = new JLabel("Word Count: " + numWords);
				mainDiaText.setHorizontalAlignment(SwingConstants.CENTER);
				
				dia.add(mainDiaText);
				
				dia.setSize(100, 100);
				
				dia.setVisible(true);
			}
				
		});
		
		JButton resetBTN = new JButton("Reset");
		resetBTN.setBounds(256, 213, 103, 23);
		frame.getContentPane().add(resetBTN);
		
		resetBTN.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					textField.setText("");
				}
				
		});
	}
}
