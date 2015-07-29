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
import java.util.Scanner;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class TestGenerator extends JFrame {
    public static ArrayList<Character> answerSheet = new ArrayList<Character>();
    public static int staticQuestionNumber;
    
    String examName;
    int versionNumber;
    boolean randomiseQuestionOrder;
    TestSection[] sections;
    private static TestPrinter printer;
    ArrayList<File> examFiles;
    private File outputFolder;
	private JList<String> lstExamFiles;
	private JTextField txfOutputFolder;
	final private char EXAM_SCRIPT = 'E', ANSWERS = 'A', STYLES = 'S';
	private JToggleButton bttnRandomVersionNumber; 
	
	public TestGenerator() {
        examName = "ExamName";
        versionNumber = 1;
        examFiles = new ArrayList<File>();
        randomiseQuestionOrder = true;
    }
    
    public void generateTest() throws Exception {
        // Validate fields
    	isInputFieldValid();
    	
    	// Set up printer to write files to
        printer = new TestPrinter(outputFolder, examName, versionNumber, EXAM_SCRIPT);
        
        // Create sections from question files
        readQuestionFiles();

        // Print out sections to HTML document
        staticQuestionNumber = 1;
        for (TestSection sec: sections){
            sec.writeToFile();
        }
        printer.flush();
        
        printAnswersAndStyles();
    }
    
    public boolean isInputFieldValid() throws Exception {
    	String message = "";
    	if (examName.length() < 0) message += "(Exam name invalid)";
    	if (versionNumber < 1 || versionNumber > 100) message += "(Version Number out of allowed range)";
    	if (examFiles.size() < 1) message += "(No question files given)";
    	if (outputFolder == null) message += "(Output folder not set)";
    	
    	if (message.length() > 0) throw new Exception(message);
    	else return true;
	}

	private void readQuestionFiles() throws Exception {
        sections = new TestSection[examFiles.size()];
        answerSheet.clear();
        try {
            for (int i = 0; i < examFiles.size(); i++) {
                sections[i] = new TestSection(examFiles.get(i), randomiseQuestionOrder, printer);
            }
        } catch(Exception e) {
            throw e;
        }
        
    }
    
    private void printAnswersAndStyles() throws Exception {
    	
        try {
        	// Try print out answer sheet
        	TestPrinter printer = new TestPrinter(outputFolder, examName, versionNumber, ANSWERS);
        	printer.outln("<html><body><pre>");
            for (int i = 1; ! answerSheet.isEmpty(); i++){
                char optCode = answerSheet.remove(0);
                int spaces = (optCode - 'A')*4 + 1;
                String line = String.format("%3d: %"+spaces+"s", i, optCode+"");
                printer.outln(line);
                if (i % 5 == 0) printer.outln();
            }
            printer.outln("</pre></body></html>");
            printer.flush();
            printer.close();
        } catch (Exception e) {
            throw new Exception("Error printing out solution file");
        }
        
        try {
        	// Try create copy of style.css file
            TestPrinter printer = new TestPrinter(outputFolder, examName, versionNumber, STYLES);
            
            Scanner cssFileScanner = new Scanner(getClass().getResourceAsStream("/resources/style.css"));
            while (cssFileScanner.hasNextLine()) {
            	printer.outln(cssFileScanner.nextLine());
            }
            printer.flush();
            printer.close();
        } catch (Exception e) {
            throw new Exception("Error printint out CSS file");
        }
    }
        
    // Setters *************************************************
    public void setVersionNumber(int i) { 
    	versionNumber = i; 
    }
    public void setExamName(String str) { 
    	examName = str; 
    }
    public void setRandomiseQuestionOrder(boolean b) {
    	randomiseQuestionOrder = b; 
    }
	public void setOutputDirectory(File dir) { 
		outputFolder = dir;
	}
	public void setComponents(JList l, JTextField t, JToggleButton b) {
		this.lstExamFiles = l;
		this.txfOutputFolder = t;
		this.bttnRandomVersionNumber = b;
	}
    
    // Getters ***************************************************
	public String getExamName() { return examName; }
	public int getVersionNumber() { return versionNumber; }
    public File getOutputDirectory() { return this.outputFolder; }
	public File getFileFromArrayList(String filename) throws Exception {
		if (filename == null) throw new NullPointerException("invalid file");
		for (File f: examFiles) {
			if (f.getName().equals(filename)) return f;
		}
		return null;
	}
	public File getGeneratedExamScript() {
		return new File(printer.outputFileName);
	} 
	public File getAnswerSheet() {
		return new File(printer.outputFileName.replace("Exam Script", "Answers"));
	}

    public int getFileIndex(File file) {
    	for (int i = 0; i < examFiles.size(); i++) {
    		if (examFiles.get(i) == file) return i;
    	}
    	return -1;
    }
	
	// Exam file management **************************************
    public void addExamFiles(ArrayList<File> filesArray) { 
    	File lastFile = null;
    	for (File f: filesArray) {
    		if (! examFiles.contains(f)){
    			examFiles.add(f);
    			lastFile = f;
    		}
    	}
    	updateExamFilesDisplay(lastFile.getName());
    	if (outputFolder == null) {
    		outputFolder = lastFile.getParentFile();
    		txfOutputFolder.setText(outputFolder.getAbsolutePath());
    	}
    }
    public void addExamFiles(File[] files) {
		ArrayList<File> a = new ArrayList<File>();
		for (File f: files) {
			a.add(f);
		}
		addExamFiles(a);
	}
    private void updateExamFilesDisplay(String lastFile) {
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
    	if (lastFile != null) {
    		lstExamFiles.setSelectedValue(lastFile, true);
    	}
    	
	}
	public void examFileListChange(String action) {
		String selFile = null;
		
		if (action.equals("clear list")){
			if (JOptionPane.showConfirmDialog(null, 
					"Press OK to remove all files. This cannot be undone.", 
					"Remove?", 
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
				examFiles.clear();
			}
		} else  {
			File file = null;
			try {
				file = getFileFromArrayList(lstExamFiles.getSelectedValue());
			} catch (NullPointerException e) { 
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Please select a file", "No file selected!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int currIndex = getFileIndex(file);
			selFile = file.getName();
			switch(action) {
				case "move down" : {
					if (currIndex < examFiles.size()-1) {
						examFiles.remove(file);
						examFiles.add(currIndex+1, file);
					}
					break;
				}
				case "move up" : {
					if (currIndex > 0) {
						examFiles.remove(file);
						examFiles.add(currIndex-1, file);
						
					}
					break;
				}
				case "remove file" : {
					if (JOptionPane.showConfirmDialog(null, 
							"Press OK to remove \"" + file.getName() + "\".", 
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
			}
		}
		updateExamFilesDisplay(selFile);
	}

}
