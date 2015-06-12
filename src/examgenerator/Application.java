package examgenerator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.wb.swing.FocusTraversalOnArray;

/**
 * Exam Generator 2
 * @version 1.06.07
 * @author Pheng
 *
 */
public class Application {

	private JFrame frmApplication;
	private TestGenerator t;
	private JButton btnBrowse, btnGo, btnVisitMe;
	private JToggleButton bttnRandomiseOrder;
	private JProgressBar jpBar;
	private JTextField txfExamName, txfVersionNumber;
	private JSlider jslVersionNumber;
	private JToggleButton bttnHelp;
	private JInternalFrame frameHelp;
	private JButton btnOpenFolder;
	private JPopupMenu popup;
	private JList lstExamFiles;
	private JTextField txfOutputFolder;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
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
	public Application() {
		/**
		 * initialise components in the application
		 */
		t = new TestGenerator();
		initialize();
		frmApplication.setTitle("Exam Generator \u00a9 2015 Pheng Taing");
		t.setComponents(jpBar, lstExamFiles, txfOutputFolder);
		
		makePopupMenu();
	}

	private void makePopupMenu() {
		popup = new JPopupMenu();
		JMenuItem menuItem;
		 // clear all
		menuItem = new JMenuItem("Move up");
		menuItem.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.examFileListChange("move up");
			}
		});
		popup.add(menuItem);
		
		menuItem = new JMenuItem("Move down");
		menuItem.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.examFileListChange("move down");
			}
		});
		popup.add(menuItem);
		
		menuItem = new JMenuItem("Remove from list");
		menuItem.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.examFileListChange("remove file");
			}
		});
		popup.add(menuItem);
		
		menuItem = new JMenuItem("Set output folder to the folder of this file");
		menuItem.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.examFileListChange("set output");
			}
		});
		popup.add(menuItem);
		
		menuItem = new JMenuItem("Clear list");
		menuItem.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.examFileListChange("clear list");
			}
		});
		popup.add(menuItem);
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
		
		panelBottom.add(bttnHelp, BorderLayout.WEST);
		
		JPanel panelBody = new JPanel();
		desktopPane.add(panelBody, BorderLayout.CENTER);
		panelBody.setLayout(null);
		
		JInternalFrame frameMain = new JInternalFrame("Exam Script Generator");
		frameMain.setVisible(true);
		frameMain.setVerifyInputWhenFocusTarget(false);
		frameMain.setBounds(167, 6, 442, 301);
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
		btnOpenFolder.setBounds(294, 206, 121, 30);
		frameMain.getContentPane().add(btnOpenFolder);
		
		jpBar = new JProgressBar();
		jpBar.setBounds(19, 241, 396, 25);
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
		bttnRandomiseOrder.setBounds(140, 75, 64, 25);
		frameMain.getContentPane().add(bttnRandomiseOrder);
		
		JLabel lblQuestionFiles = new JLabel("Question Files");
		lblQuestionFiles.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQuestionFiles.setBounds(5, 110, 120, 25);
		frameMain.getContentPane().add(lblQuestionFiles);
		
		JScrollPane scrollPaneQuestionFiles = new JScrollPane();
		scrollPaneQuestionFiles.setToolTipText("");
		scrollPaneQuestionFiles.setBounds(140, 112, 142, 90);
		frameMain.getContentPane().add(scrollPaneQuestionFiles);
		
		lstExamFiles = new JList();
		lstExamFiles.setToolTipText("Right click for more options");
		scrollPaneQuestionFiles.setViewportView(lstExamFiles);
		lstExamFiles.addMouseListener(new MouseAdapter() {
			
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					lstExamFiles.setSelectedIndex(lstExamFiles.locationToIndex(e.getPoint()));
					popup.show(e.getComponent(), e.getX(), e.getY());
					someSettingsHasChanged();
				} else if (e.getClickCount() >= 2) {
					t.examFileListChange("set output");
					someSettingsHasChanged();
				}
			}
		});
		lstExamFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
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

		            t.addExamFiles( convert(chooser.getSelectedFiles()) );
		            //t.setOutputDirectory(chooser.getCurrentDirectory());

		        }
		        someSettingsHasChanged();
			}
			public ArrayList<File> convert(File[] files) {
				ArrayList<File> a = new ArrayList<File>();
				for (File f: files) {
					a.add(f);
				}
				return a;
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
		            jpBar.setValue(0);
		            someSettingsHasChanged();
		        }
			}
		});
		btnGo.setBounds(294, 170, 121, 30);
		frameMain.getContentPane().add(btnGo);
		
		JLabel lblOutputFolder = new JLabel("Output Folder");
		lblOutputFolder.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOutputFolder.setBounds(5, 206, 120, 25);
		frameMain.getContentPane().add(lblOutputFolder);
		
		txfOutputFolder = new JTextField();
		txfOutputFolder.setToolTipText("Set the output folder by double clicking on one of the source file");
		txfOutputFolder.setEditable(false);
		txfOutputFolder.setBounds(140, 206, 142, 30);
		frameMain.getContentPane().add(txfOutputFolder);
		txfOutputFolder.setColumns(10);
		frameMain.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txfExamName, jslVersionNumber, bttnRandomiseOrder, btnBrowse, txfVersionNumber, lblVersionNumber, lblExamName, lblRandomiseQs, lblQuestionFiles, scrollPaneQuestionFiles}));
		
		frameHelp = new JInternalFrame("Help");
		frameHelp.setMaximizable(true);
		
		frameHelp.setClosable(true);
		frameHelp.setBounds(23, 6, 763, 316);
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
		frameHelp.addComponentListener(new ComponentAdapter() {
			
			public void componentHidden(ComponentEvent arg0) {
				bttnHelp.setSelected(false);
				frameMain.show();
			}
		});
		
		DropFileListener myDragDropListener = new DropFileListener(t);
		new DropTarget(frmApplication, myDragDropListener);
		
		bttnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (bttnHelp.isSelected()){
					frameHelp.show();
					frameMain.hide();
				} else {
					frameHelp.hide();
					frameMain.show();
				}
			}
		});
	}
	public void someSettingsHasChanged() {
		jpBar.setValue(0);
		btnOpenFolder.setEnabled(false);
	}
}
