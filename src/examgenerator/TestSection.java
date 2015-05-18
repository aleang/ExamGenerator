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
import java.util.Random;
import java.util.Scanner;
import static java.lang.System.out;

public class TestSection {
    Scanner scan;
    String sectionName;
    ArrayList<TestQuestion> questions;
    
    public TestSection(File section, boolean randomiseQuestionOrder) {
        try {
            scan = new Scanner(section);
        } catch (FileNotFoundException e) {
            System.err.println("Can't read file section given" + section.toString());
            e.printStackTrace();
        }
        
        questions = new ArrayList<TestQuestion>();
        sectionName = scan.nextLine();
        int numberOfQs = scan.nextInt(); scan.nextLine();
        TestQuestion tq;
        
        // read questions
        while (numberOfQs-- > 0) {
            String questionLine = scan.nextLine();
            tq = new TestQuestion(questionLine, scan);
            questions.add(tq);
        }

        if (randomiseQuestionOrder) randomiseQuestionOrder();
    }

    void print(TestGenerator tg) {
        tg.print("h1", sectionName);
        out.print(sectionName);
        
        for (TestQuestion tq : questions) {
            tq.print(tg);
        }
    }

    private void randomiseQuestionOrder() {
        ArrayList<TestQuestion> newList = new ArrayList<TestQuestion>();
        Random random = new Random();
        
        while (questions.size() > 0) {
            newList.add(
               questions.remove(random.nextInt(questions.size()))
            );
        }
        
        this.questions = newList;
        int i = 1;
        for (TestQuestion tq : questions) {
            tq.qNumber = i++;
        }
    }

}
