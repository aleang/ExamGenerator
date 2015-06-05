package examgenerator;

import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JDesktopPane;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Component;

import javax.swing.JProgressBar;

import java.awt.dnd.DropTarget;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import static java.lang.System.out;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;

import javax.swing.JTabbedPane;

import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Exam Generator 2
 * @version 2.0
 * @author Pheng
 *
 */
public class ExamGenerator {

	private JFrame frmApplication;
	private TestGenerator t;
	private JTextArea txaQuestionFiles;
	private JButton btnBrowse, btnGo, btnVisitMe;
	private JToggleButton bttnRandomiseOrder;
	private JProgressBar jpBar;
	private JTextField txfExamName, txfVersionNumber;
	private JSlider jslVersionNumber;
	private JToggleButton bttnHelp;
	private JInternalFrame frameHelp;
	private JButton btnOpenFolder;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExamGenerator window = new ExamGenerator();
					window.frmApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ExamGenerator() {
		/**
		 * initialise components in the application
		 */
		t = new TestGenerator();
		initialize();
		frmApplication.setTitle("Exam Generator \u00a9 2015 Pheng Taing");
		t.setProgressFeedback(jpBar);
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmApplication = new JFrame();
		frmApplication.setTitle("Exam Generator");
		frmApplication.setBounds(100, 100, 806, 397);
		frmApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		JDesktopPane desktopPane = new JDesktopPane();
		frmApplication.getContentPane().add(desktopPane, BorderLayout.CENTER);
		desktopPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelTop = new JPanel();
		desktopPane.add(panelTop, BorderLayout.NORTH);
		
		JPanel panelBottom = new JPanel();
		desktopPane.add(panelBottom, BorderLayout.SOUTH);
		panelBottom.setLayout(new BorderLayout(0, 0));
		
		btnVisitMe = new JButton("Visit My Page");
		btnVisitMe.setToolTipText("");
		btnVisitMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			        try {
			            desktop.browse(new URI("http://bit.ly/pixport"));
			        } catch (Exception e) { e.printStackTrace(); }
			    }
			}
		});
		panelBottom.add(btnVisitMe, BorderLayout.EAST);
		
		bttnHelp = new JToggleButton("Help");
		bttnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (bttnHelp.isSelected()){
					frameHelp.show();
				} else {
					frameHelp.hide();
				}
			}
		});
		panelBottom.add(bttnHelp, BorderLayout.WEST);
		
		JPanel panelBody = new JPanel();
		desktopPane.add(panelBody, BorderLayout.CENTER);
		panelBody.setLayout(null);
		
		frameHelp = new JInternalFrame("Help");
		frameHelp.setMaximizable(true);
		frameHelp.setIconifiable(true);
		frameHelp.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent arg0) {
				bttnHelp.setSelected(false);
			}
		});
		frameHelp.setClosable(true);
		frameHelp.setBounds(344, 6, 437, 301);
		frameHelp.setResizable(true);
		panelBody.add(frameHelp);
		frameHelp.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frameHelp.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		
		JScrollPane scrollPaneIntro = new JScrollPane();
		tabbedPane.addTab("Exam Generator", null, scrollPaneIntro, null);
		
		JTextArea txaIntro = new JTextArea();
		txaIntro.setText("Welcome to Exam Generator!\r\n\r\nThis application is free and open source. Feel free to distribute this application. Use, copy, share or change as you like but please credit me as the original creator. You can find more about me by clicking the \"Visit My Page\" button below.\r\n\r\nHow to use:\r\n1. Enter the Exam Name. This will be used to name the output file.\r\n2. Select the Version Number. This will also be used to name the output file.\r\n3. Press Yes or No to set the generator to randomise the order of the questions.\r\n4. Select the Question Files. You can select multiple text files (.txt). You can also drag-and-drop text files onto this application. For each file given, a section will be created in the exam script.\r\n5. Press \"Generate Exam\". If all goes well, the progress bar should display 100% and the \"Open Folder...\" button becomes active.\r\n6. Press the \"Open Folder...\" button to see the generated HTML files.");
		txaIntro.setEditable(false);
		txaIntro.setWrapStyleWord(true);
		txaIntro.setLineWrap(true);
		scrollPaneIntro.setViewportView(txaIntro);
		
		JScrollPane scrollPaneFormat = new JScrollPane();
		scrollPaneFormat.setAutoscrolls(false);
		tabbedPane.addTab("File Format", null, scrollPaneFormat, null);
		
		JTextArea txaFormat = new JTextArea();
		txaFormat.setEditable(false);
		txaFormat.setVerifyInputWhenFocusTarget(false);
		txaFormat.setFont(new Font("Consolas", Font.PLAIN, 14));
		txaFormat.setText("Line 1: Section Name\r\nLine 2: Number of Questions\r\n[\r\n  Line 3: Question Data (Q. Number : num. of Options : Title)\r\n  [\r\n    Line 4: Correct answer first\r\n    Line 5: Incorrect option\r\n    Line 6: More incorrect options\r\n    ... (determined by Num. of Options set above\r\n  ]\r\n]\r\n\r\n========= Code Question Syntax =========\r\nCode paragraph:\t  #[code here]#\r\nCode inline:      #{in line code here}#\r\nIndent/Tab:       #T\r\nBreak/New Line:   #B\r\nBreak and Indent: #N\r\n\r\n======= All or None of the Above =======\r\nIf \"None of the above\" or \"All of the above\" is \r\ncorrect, add it first.\r\nOtherwise add it last.");
		scrollPaneFormat.setViewportView(txaFormat);
				
		JScrollPane scrollPaneEx1 = new JScrollPane();
		scrollPaneEx1.setAutoscrolls(false);
		tabbedPane.addTab("Example 1", null, scrollPaneEx1, null);
		
		
		JTextArea txaExample1 = new JTextArea();
		txaExample1.setEditable(false);
		txaExample1.setFont(new Font("Consolas", Font.PLAIN, 14));
		txaExample1.setText("Multi-Choice Questions\r\n2\r\n1:4:What is the first letter of the Greek alphabet?\r\nAlpha\r\nOmega\r\nGamma\r\nPi\r\n2:5:Which of the following are prime numbers?\r\nAll of the above\r\n37\r\n11\r\n3\r\n23");
		scrollPaneEx1.setViewportView(txaExample1);
		
		JScrollPane scrollPaneEx2 = new JScrollPane();
		scrollPaneEx2.setAutoscrolls(false);
		tabbedPane.addTab("Example 2", null, scrollPaneEx2, null);
		
		JTextArea txtrJavaOperators = new JTextArea();
		txtrJavaOperators.setEditable(false);
		txtrJavaOperators.setText("Java Basics\r\n2\r\n1:5:How do you declare a string variable called #{myName}# in Java?\r\n#{String myName;}#\r\n#{String: myName}#\r\n#{myName : String;}#\r\n#{@myName String}#\r\n#{var myName;}#\r\n2:4:What is the output? #[int hour = 5; #BSystem.out.println(\"The hour is\" + hour);]#\r\nThe hour is 5\r\nThe hour is hour\r\n5\r\nThe hour is 5 p.m.");
		txtrJavaOperators.setFont(new Font("Consolas", Font.PLAIN, 14));
		scrollPaneEx2.setViewportView(txtrJavaOperators);
		
		JInternalFrame frameMain = new JInternalFrame("Exam Script Generator");
		frameMain.setVisible(true);
		frameMain.setVerifyInputWhenFocusTarget(false);
		frameMain.setIconifiable(true);
		frameMain.setBounds(25, 6, 307, 301);
		panelBody.add(frameMain);
		frameMain.getContentPane().setLayout(null);
		
		btnOpenFolder = new JButton("Open Folder...");
		btnOpenFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			    if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
			        try {
			            desktop.open(t.getOutputDirectory());
			        } catch (Exception e) { e.printStackTrace(); }
			    }
			}
		});
		btnOpenFolder.setEnabled(false);
		btnOpenFolder.setBounds(152, 209, 130, 30);
		frameMain.getContentPane().add(btnOpenFolder);
		
		jpBar = new JProgressBar();
		jpBar.setBounds(19, 241, 263, 25);
		frameMain.getContentPane().add(jpBar);
		
		JLabel lblVersionNumber = new JLabel("Version Number");
		lblVersionNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVersionNumber.setBounds(5, 40, 120, 25);
		frameMain.getContentPane().add(lblVersionNumber);
		
		JLabel lblExamName = new JLabel("Exam Name");
		lblExamName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExamName.setBounds(5, 5, 120, 25);
		frameMain.getContentPane().add(lblExamName);
		
		txfExamName = new JTextField();
		txfExamName.setText("Exam Script");
		txfExamName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				t.setTestName(txfExamName.getText());
				someSettingsHasChanged();
			}
		});
		txfExamName.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent a) {
				txfExamName.setSelectionStart(0);
				txfExamName.setSelectionEnd(Integer.MAX_VALUE);
			}
			public void focusLost(FocusEvent arg0) {
				t.setTestName(txfExamName.getText());
				someSettingsHasChanged();
			}
		});
		txfExamName.setBounds(140, 5, 142, 25);
		frameMain.getContentPane().add(txfExamName);
		txfExamName.setColumns(10);
		
		txfVersionNumber = new JTextField();
		txfVersionNumber.setEditable(false);
		txfVersionNumber.setText("1");
		txfVersionNumber.setBounds(244, 40, 36, 25);
		frameMain.getContentPane().add(txfVersionNumber);
		
		jslVersionNumber = new JSlider();
		jslVersionNumber.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				txfVersionNumber.setText(jslVersionNumber.getValue() + "");
				t.setTestVersionNumber(jslVersionNumber.getValue());
				someSettingsHasChanged();
			}
		});
		jslVersionNumber.setValue(1);
		jslVersionNumber.setMinimum(1);
		jslVersionNumber.setBounds(140, 40, 97, 24);
		frameMain.getContentPane().add(jslVersionNumber);
		
		JLabel lblRandomiseQs = new JLabel("Randomise Order?");
		lblRandomiseQs.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRandomiseQs.setBounds(5, 75, 120, 25);
		frameMain.getContentPane().add(lblRandomiseQs);
		
		bttnRandomiseOrder = new JToggleButton("Yes");
		bttnRandomiseOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				boolean isSet = bttnRandomiseOrder.isSelected();
				bttnRandomiseOrder.setText(isSet ? "Yes" : "No");
				t.setRandomiseQuestionOrder(isSet);
				someSettingsHasChanged();
			}
		});
		bttnRandomiseOrder.setSelected(true);
		bttnRandomiseOrder.setBounds(140, 75, 51, 25);
		frameMain.getContentPane().add(bttnRandomiseOrder);
		
		JLabel lblQuestionFiles = new JLabel("Question Files");
		lblQuestionFiles.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQuestionFiles.setBounds(5, 110, 120, 25);
		frameMain.getContentPane().add(lblQuestionFiles);
		
		JScrollPane scrollPaneQuestionFiles = new JScrollPane();
		scrollPaneQuestionFiles.setBounds(140, 112, 142, 73);
		frameMain.getContentPane().add(scrollPaneQuestionFiles);
		
		txaQuestionFiles = new JTextArea();
		txaQuestionFiles.setEditable(false);
		scrollPaneQuestionFiles.setViewportView(txaQuestionFiles);
		
		DropFileListener myDragDropListener = new DropFileListener(t, txaQuestionFiles);
		new DropTarget(frmApplication, myDragDropListener);
		new DropTarget(txaQuestionFiles, myDragDropListener);
		
		btnBrowse = new JButton("...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
		        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
		        chooser.setFileFilter(filter);
		        chooser.setAcceptAllFileFilterUsed(false);
		        chooser.setMultiSelectionEnabled(true);

		        int returnVal = chooser.showOpenDialog(null);
		        if(returnVal == JFileChooser.APPROVE_OPTION) {
		            String selectedFilesString = "";
		            for (File f: chooser.getSelectedFiles()) {
		                selectedFilesString += String.format("%s%n", f.getName());
		            }

		            t.setChosenExamFiles(chooser.getSelectedFiles());
		            t.setOutputDirectory(chooser.getCurrentDirectory());

		            //out.println("You chose these files: " + selectedFilesString);
		            //out.println("Output will be in: " + chooser.getCurrentDirectory().getAbsolutePath());

		            txaQuestionFiles.setText(selectedFilesString);
		        }
		        someSettingsHasChanged();
			}
		});
		btnBrowse.setBounds(88, 136, 40, 30);
		frameMain.getContentPane().add(btnBrowse);
		
		btnGo = new JButton("Generate Exam");
		btnGo.setHorizontalAlignment(SwingConstants.LEFT);
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				try {
		            t.generateTest();
		            btnOpenFolder.setEnabled(true);
		        } catch (Exception e) {
		            e.printStackTrace();
		            btnOpenFolder.setEnabled(false);
		            JOptionPane.showMessageDialog(null, "An error occured while generating the exam script. "
		                    + "Please check the question files for incorrect syntax. Read more from \"Help\"", 
		                    "Error", JOptionPane.ERROR_MESSAGE);
		        }
			}
		});
		btnGo.setBounds(19, 209, 130, 30);
		frameMain.getContentPane().add(btnGo);
		
		
		frameMain.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txfExamName, jslVersionNumber, bttnRandomiseOrder, btnBrowse, txfVersionNumber, lblVersionNumber, lblExamName, lblRandomiseQs, lblQuestionFiles, scrollPaneQuestionFiles, txaQuestionFiles}));
	}
	public void someSettingsHasChanged() {
		jpBar.setValue(0);
		btnOpenFolder.setEnabled(false);
	}
}
