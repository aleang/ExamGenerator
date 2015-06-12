/**
 * TestGenerator will consume questions from text files and 
 * generate a formatted HTML document containing randomised
 * questions and answer options. 
 * @author Pheng Taing
 * @version 2015_05_16
 * @see bit.ly/pixport
 */

package examgenerator;
import static java.lang.System.out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.ListModel;

public class TestGenerator extends JFrame {
    public static ArrayList<Character> answerSheet = new ArrayList<Character>();
    public static int staticQuestionNumber;
    
    String examName;
    int versionNumber;
    boolean randomiseQuestionOrder;
    TestSection[] sections;
    PrintWriter printer;
    ArrayList<File> examFiles;
    File outputFolder;
    private JProgressBar jpBar;
	private JList<String> lstExamFiles;
	private JTextField txfOutputFolder;
            
    public TestGenerator() {
        examName = "Test";
        versionNumber = 1;
        examFiles = new ArrayList<File>();
    }

    
    public void generateTest() {

        // Step 1: Set up printer to write files to
        printer = setUpPrinter();
        
        // Step 2: Set up HTML document
        prepareHTMLFile(printer);
        
        // Step 3: Create sections with questions
        createQuestionsAndRandomise();

        // Step 4: Print out sections to HTML document
        staticQuestionNumber = 1;
        for (TestSection sec: sections){
            sec.print(this);
        }
        printer.flush();
        printer.close();
        
        // Step 5: Print out answers!
        printAnswer();
    }
    
    private void createQuestionsAndRandomise() {
        sections = new TestSection[examFiles.size()];
        answerSheet.clear();
        try {
            for (int i = 0; i < examFiles.size(); i++) {
                sections[i] = new TestSection(examFiles.get(i), randomiseQuestionOrder);
                jpBar.setValue((int)(i * 50.0 /examFiles.size()));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void printAnswer() {
        String outputFile = String.format("%s/%s-version-%d-solution.txt", outputFolder.getAbsolutePath(), examName, versionNumber);
        PrintWriter printedFile;
        try {
            printedFile = new PrintWriter(new File(outputFile));
            printedFile.println(String.format("%s-version-%d-solutions", examName, versionNumber));
            int numberOfQs = answerSheet.size();
            
            for (int i = 1; ! answerSheet.isEmpty(); i++){
                char optCode = answerSheet.remove(0);
                int spaces = (optCode - 'A')*4 + 1;
                printedFile.printf("%3d: %"+spaces+"s\n", i, optCode+"");
                if (i % 5 == 0) printedFile.println();
                jpBar.setValue(50+(int)(i * 50.0 /numberOfQs));
            }
            jpBar.setValue(100);
            printedFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        outputFile = String.format("%s/style.css", outputFolder.getAbsolutePath());
        try {
            printedFile = new PrintWriter(new File(outputFile));
            
            Scanner cssFileScanner = new Scanner(getClass().getResourceAsStream("/resources/style.css"));
            while (cssFileScanner.hasNextLine()) {
            	printedFile.println(cssFileScanner.nextLine());
            }
            printedFile.flush();
            printedFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * setUpPrinter() will create a print writer that writes
     * to a file that has a name based on exam name and version number
     * @return
     */
    private PrintWriter setUpPrinter() {
        String outputFile = String.format("%s/%s-version-%d.html", outputFolder.getAbsolutePath(), examName, versionNumber);
        try {
            FileWriter fw = new FileWriter(outputFile, false);
            return new PrintWriter(fw);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * prepareHTMLFile() set up the HTML document with HTML5
     * @param outputFile PrintWriter object to print with
     */
    private void prepareHTMLFile(PrintWriter pw) {
        print("<!DOCTYPE html><html><head>");
        print("<link rel='stylesheet' href='style.css' type='text/css' />");
        print("</head><body>");
    }
    
    // Setters
    public void setTestVersionNumber(int i) { versionNumber = i; }
    public void setTestName(String str) { examName = str; }
    public void setRandomiseQuestionOrder(boolean yes) {randomiseQuestionOrder = yes; }
	public void setOutputDirectory(File currentDirectory) { outputFolder = currentDirectory; }
    
    
    // Getters
    public File getOutputDirectory() { return this.outputFolder; }
    
    
    // Printer methods
    public void print(String html){
        printer.println(html);
    }
    public void print(String htmlTag, String content) {
        printer.printf("<%s>%s</%s>%n", htmlTag, content, htmlTag);
    }
    public void print(String htmlTag, String tagClass, String content) {
        printer.printf("<%s class='%s'>%s</%s>", htmlTag, tagClass, content, htmlTag);
    }

	public void setComponents(JProgressBar j, JList l, JTextField t) {
		this.jpBar = j;
		this.lstExamFiles = l;
		this.txfOutputFolder = t;
	}

	// Exam file management
    public void addExamFiles(ArrayList<File> filesArray) { 
    	for (File f: filesArray) {
    		if (! examFiles.contains(f)){
    			examFiles.add(f);
    		}
    	}
    	updateExamFilesDisplay();
    }
    private void updateExamFilesDisplay() {
    	lstExamFiles.setModel(new AbstractListModel<String>() {
			String[] values = convert(examFiles);
			
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
			public String[] convert(ArrayList<File> examFiles) {
				String[] arr = new String[examFiles.size()];
				for (int i = 0; i < arr.length; i++) {
					arr[i] = examFiles.get(i).getName();
				}
				return arr;
			}
		});
	}
	public void examFileListChange(String action) {
		File file = getFileFromArrayList(lstExamFiles.getSelectedValue());
		switch (action) {
			case "move down" : {
				int currIndex = Collections.binarySearch(examFiles, file);
				if (currIndex < examFiles.size()-1) {
					examFiles.remove(file);
					examFiles.add(currIndex+1, file);
					lstExamFiles.setSelectedIndex(currIndex+1);
				}
				break;
			}
			case "move up" : {
				int currIndex = Collections.binarySearch(examFiles, file);
				if (currIndex > 0) {
					examFiles.remove(file);
					examFiles.add(currIndex-1, file);
					lstExamFiles.setSelectedIndex(currIndex-1);
				}
				break;
			}
			case "remove file" : {
				if (JOptionPane.showConfirmDialog(null, 
						"Press OK to remove \"" + file.getName() + "\". This cannot be undone.", 
						"Remove?", 
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
					examFiles.remove(file);
				}
				break;
			}
			case "set output" : {
				this.outputFolder = file.getParentFile();
				
				txfOutputFolder.setText(outputFolder.getAbsolutePath());
				break;
			}
			case "clear list" : {
				if (JOptionPane.showConfirmDialog(null, 
						"Press OK to remove all. This cannot be undone.", 
						"Remove?", 
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
					examFiles.clear();
				}
				break;
			}
		}
		updateExamFilesDisplay();
	}

	public File getFileFromArrayList(String filename) {
		for (File f: examFiles) {
			if (f.getName().equals(filename)) return f;
		}
		return null;
	}
    
}
