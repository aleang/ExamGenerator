package examgenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TestPrinter {
	 public static File outputFolder;
	 private PrintWriter printer;
	 public String outputFileName;
	 final private char EXAM_SCRIPT = 'E', ANSWERS = 'A', STYLES = 'S'; 
	 
	 public TestPrinter(File outputFolder, String examName, int versionNumber, char documentType) throws Exception {
		this.outputFolder = outputFolder;
		 
		if (documentType == STYLES) {
			outputFileName = String.format("%s/style.css", outputFolder.getAbsolutePath());
		} else {
			outputFileName = String.format("%s/%s-v%d-%s", 
							 outputFolder.getAbsolutePath(), 
							 examName, versionNumber,
							 (documentType == EXAM_SCRIPT ? "[Exam Script]" : "[Answers]") + ".html"
				 		);
		}
		try {
		    FileWriter fw = new FileWriter(outputFileName, false);
		    printer = new PrintWriter(fw);
		} catch (Exception e) {
		    throw new Exception("Error writing to file " + outputFileName);
		}
        
        if (documentType == EXAM_SCRIPT) setUpHTMLDocument();
	 }
	 

    private void setUpHTMLDocument() {
    	out("<!DOCTYPE html>"
    			+ "<!-- This HTML file has been created by "
    			+ "Exam Generator (c) Pheng Taing 2015 -->"
    			+ "<html>"
    			+ "<head>");
        out("<link rel='stylesheet' href='style.css' type='text/css' />");
        out("</head>"
        		+ "<body>");
	}


	// Printer methods ***************************************************
    public void out(String html){
    	// print the exact content of html
        printer.println(html);
    }
    public void out(String htmlTag, String content) {
    	// print the content with supplied html tag
        printer.printf("<%s>%s</%s>%n", htmlTag, content, htmlTag);
    }
    public void out(String htmlTag, String tagClass, String content) {
    	// print the content with supplied tag and class
        printer.printf("<%s class='%s'>%s</%s>", htmlTag, tagClass, content, htmlTag);
    }
    public void outf(String format, Object... o) {
		printer.printf(format, o);
	}
    public void outln() {
    	printer.println();
    }
    public void outln(String str) {
    	printer.println(str);
    }
    
	public void flush() {
		printer.flush();
	}


	public void close() {
		printer.close();
	}


	

}
