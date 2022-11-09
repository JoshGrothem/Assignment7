import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

public class MyMain {

	// SPECIFY THE APPLICATION ELEMENTS: UI AND OBJECTS
	private static JButton inputBtn;
	private static JButton outputBtn;
	private static JButton computeBtn;
	private static JFrame jframeWindow;
	private static JPanel panel;
	private static File fileToRead;
	private static File fileToSave;

	public static void main(String[] args) {
		// create GUI application window
		constructAppWindow();
		addListenerEvents();
	}

	private static void constructAppWindow() {
		jframeWindow = new JFrame();
		jframeWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// construct a panel container to store buttons, etc.
		panel = new JPanel(new GridLayout(3, 0)); // 3 ROWS NO COLUMNS
		panel.setPreferredSize(new Dimension(500, 150));
		panel.setBackground(Color.DARK_GRAY);

		// build buttons, etc. and add them to the panel
		inputBtn = new JButton("Specify Input Text File");
		outputBtn = new JButton("Specify Output Text File");
		computeBtn = new JButton("Perform Work");
		panel.add(inputBtn);
		panel.add(outputBtn);
		panel.add(computeBtn);

		// add panel to the application window
		jframeWindow.add(panel);

		// TASK 5: MAKE THE APPLICATION WINDOW VISIBLE TO THE USER
		jframeWindow.pack();
		jframeWindow.setVisible(true);
	}

	private static void addListenerEvents() {
		inputBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestInputFile();
			}
		});
		outputBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestSaveFile();
			}
		});

		computeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long startTime = System.currentTimeMillis();
				computeSomething();
				long endTime = System.currentTimeMillis();
				System.out.println("That took " + (endTime - startTime) + " milliseconds");
			}
		});

	}

	public static void requestSaveFile() {
		// parent component of the dialog
		JFrame parentFrame = new JFrame();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");
		int userSelection = fileChooser.showSaveDialog(parentFrame);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			fileToSave = fileChooser.getSelectedFile();
			System.out.println("Save as file: " + fileToSave.getAbsolutePath());
		}
	}

	public static void requestInputFile() {
		// parent component of the dialog
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			fileToRead = jfc.getSelectedFile();
			System.out.println(fileToRead.getAbsolutePath());
		}
	}

	public static void computeSomething() {
		int index = 0;
		String word = "";
		int g = 31;
		int size = 4;

		HashMap<Integer, String> keyWords = new HashMap<Integer, String>();
		word = "if";
		index = (word.length() + 26 * (word.charAt(0)) + g * (word.charAt(word.length() - 1))) % size;
		keyWords.put(index, word);
		word = "for";
		index = (word.length() + 26 * (word.charAt(0)) + g * (word.charAt(word.length() - 1))) % size;
		keyWords.put(index, word);
		word = "public";
		index = (word.length() + 26 * (word.charAt(0)) + g * (word.charAt(word.length() - 1))) % size;
		keyWords.put(index, word);
		System.out.println(keyWords);

		String fileInput = "";
		String program = "";
		String answer = "Hi. ";
		int locCount = 0;
		fileInput = fileToRead.toString();
		try {
			File myObj = new File(fileInput);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String str = myReader.nextLine();
				// accounting for lines that are commented out
				if (str.length() >= 2) {
					if (str.charAt(0) == '/' && str.charAt(1) == '/') {
						locCount--;
					}
				}

				locCount++;
				program += str;

			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		System.out.println("There are " + locCount + " many lines of code.");
		answer += "There are " + locCount + " many lines of code.";

		int c1 = program.split("\\b" + keyWords.get(1) + "\\b").length - 1;
		int c2 = program.split("\\b" + keyWords.get(2) + "\\b").length - 1;
		int c3 = program.split("\\b" + keyWords.get(3) + "\\b").length - 1;

		answer += "For loops: " + c1 + " if statements: " + c2 + " public: " + c3;

		try {
			FileWriter myWriter = new FileWriter(fileToSave);
			myWriter.write(answer);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

}