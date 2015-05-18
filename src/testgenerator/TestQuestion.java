/**
 * TestQuestion will consume a question line and a scanner
 * which contains all the information for the question
 * @author Pheng Taing
 * @version 2015_05_16
 * @see bit.ly/pixport
 */

package testgenerator;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import static java.lang.System.out;

public class TestQuestion {
    final String NOA = "None of the above", AOA = "All of the above";
    String question, correctOption;
    ArrayList<String> options;
    int qNumber, numOfOptions;
    boolean keepAnswerAtBotton, keepFakeAtBottom; // only if NOA or AOA exist
    boolean NOAorAOAisWrong; // true for NOA, false for AOA
    HashMap<String, String> htmlConversion;
    
    public TestQuestion(String questionLine, Scanner scan) {
        htmlConversion = getHtmlCodes();
        String[] bits = questionLine.split(":");
        qNumber = Integer.parseInt(bits[0]);
        numOfOptions = Integer.parseInt(bits[1]);
        question = convertToHtml(bits[2]);
        options = new ArrayList<String>();
        
        keepFakeAtBottom = false;
        for (int i = 0; i < numOfOptions; i++) {
            String option = convertToHtml(scan.nextLine().trim());
            keepFakeAtBottom |= (option.equalsIgnoreCase(AOA) || option.equalsIgnoreCase(NOA)) && i > 0;
            
            if (keepFakeAtBottom) {
                NOAorAOAisWrong = option.equalsIgnoreCase(NOA);
                continue;
            }
            
            options.add(option);
            
            
        }
        correctOption = options.get(0);
        keepAnswerAtBotton = correctOption.equalsIgnoreCase(AOA) || correctOption.equalsIgnoreCase(NOA);
        
        shuffleOptions();
        
    }

    void print(TestGenerator tg) {
        int currentNumber = TestGenerator.staticQuestionNumber++;
        tg.print("<div>");
        tg.print("span", "questionNumber", "Question " + currentNumber);
        tg.print("span", "questionTitle", question);
        tg.print("<table>");
        
        char optCode = 'A';
        for (String opt:  options) {
            if (opt.equals(correctOption)){
                TestGenerator.answerSheet.add(optCode);
            }
            
            tg.print("<tr>");
            tg.print("td", optCode+"");
            tg.print("td", opt);
            tg.print("</tr>");
            optCode++;
        }
        tg.print("</table></div>");
        
        out.printf("%d. %s%n", currentNumber, question);
        optCode = 'A';
        for (String opt:  options) {
            out.printf("[%s] %s%n", optCode++, opt);
        }
    }
    
    private String convertToHtml(String text) {
        for (String s: htmlConversion.keySet()){
            if (text.indexOf(s) > -1){
                text = text.replace(s, htmlConversion.get(s));
            }
        }
        return text;
    }
    private HashMap<String, String> getHtmlCodes() {
        /*
         * #[ means begin code <pre>
         * #{ means begin inline code <pre class="l">
         * ]# or }# means end of code </pre>
         * #T means tab "    " (4 spaces)
         * #B means break <br>
         */
        HashMap<String, String> codes = new HashMap<String, String>();
        codes.put("#[", "<pre>");
        codes.put("#{", "<pre class='l'>");
        codes.put("]#", "</pre>");
        codes.put("}#", "</pre>");
        codes.put("#T", "    ");
        codes.put("#B", "<br />");
        codes.put("#N", "<br />    ");
        return codes;
    }
    private void shuffleOptions() {
        if (keepAnswerAtBotton) options.remove(0);
        
        if (Math.random() < 0.5) Collections.sort(options);
        else Collections.sort(options, Collections.reverseOrder());
        
        String swapped;
        Random random = new Random();
        
        swapped = options.remove(random.nextInt(options.size()));
        options.add(0, swapped);
        swapped = options.remove(random.nextInt(options.size()));
        options.add(0, swapped);
        
        if (keepFakeAtBottom) options.add(NOAorAOAisWrong ? NOA : AOA);
        else if (keepAnswerAtBotton) options.add(correctOption);
    }

}
