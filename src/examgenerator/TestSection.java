/**
 * TestSection will consume a text file and create
 * a section of questions for an exam
 * @author Pheng Taing
 * @version 2015_05_16
 * @see bit.ly/pixport
 */

package examgenerator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.out;

public class TestSection {
    Scanner scan;
    String sectionHeaderOrTitle;
    ArrayList<TestQuestion> questions;
	private static TestPrinter printer;
    
    
    public TestSection(File section, boolean randomiseQuestionOrder, TestPrinter printer) throws Exception {
        // Attempt to open file using scanner
    	try {
            scan = new Scanner(section);
        } catch (FileNotFoundException e) {
            throw new Exception("Scanner can't read the file named " + section.getName());
        }
        
    	// Read file header information
        int numberOfQs = 0;
        try {
        	questions = new ArrayList<TestQuestion>();
            sectionHeaderOrTitle = scan.nextLine();
            numberOfQs = scan.nextInt(); scan.nextLine();
        } catch (Exception e) {
        	throw new Exception(section.getName() + "'s heading section may contain a syntax error.");
        }
        
        // Read questions
        TestQuestion tq;
        for (int i = 1; i <= numberOfQs; i++) {
            try {
	        	String questionLine = scan.nextLine();
	            tq = new TestQuestion(questionLine, scan, printer);
	            questions.add(tq);
            } catch (Exception e) {
            	throw new Exception(
        			String.format("Error with question %d from file %s", 
        					i, 
        					section.getName()
        			)
            	);
            }
        }
        
        // Call randomise method if needed
        if (randomiseQuestionOrder) randomiseQuestionOrder();
        
        this.printer = printer;
    }

    void writeToFile() {
        if (sectionHeaderOrTitle.contains("<h1>")) printer.out(sectionHeaderOrTitle);  
        else printer.out("h1", sectionHeaderOrTitle);
        out.print(sectionHeaderOrTitle);
        
        for (TestQuestion tq : questions) {
            tq.writeToFile();
        }
    }

    private void randomiseQuestionOrder() {
        Collections.shuffle(questions);
        
        int i = 1;
        for (TestQuestion q : questions) {
            q.questionNumber = i++;
        }
    }

}
