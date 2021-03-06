package examgenerator;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;

import java.awt.Color;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;

import javax.swing.JSeparator;

/**
 * Exam Generator 2
 * @version 1.07.22
 * @author Pheng
 *
 */
public class Application {

	private JFrame frmApplication;
	private TestGenerator t;
	private JButton btnBrowse;
	private JPopupMenu popup;
	private JMenuItem mntmGoToFolder;
	private JMenuItem mntmOpenFile;
	private JTextField txfExamName;
	private JLabel lblVersionNumber;
	private JTextField txfOutputFolder;
	private JSlider jslVersionNumber;
	private JToggleButton bttnRandomiseOrder;
	private JToggleButton bttnRandomVersionNumber;
	private JList jlQuestionFiles;
	private JLabel lblFileName;
	private JButton btnRemoveFile;
	private JButton btnMoveDown;
	private JButton btnMoveUp;
	private JButton btnGenerate;
	private JButton btnOutputFolder;
	private JMenuItem mntmOpenAnswerSheet;
	private ProgressWindow progWindow;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
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
		progWindow = new ProgressWindow(t);
		initialize();
		frmApplication.setTitle("Exam Generator \u00a9 2015 Pheng Taing");
		t.setComponents(jlQuestionFiles, txfOutputFolder, bttnRandomVersionNumber, progWindow);
		
		JMenuBar menuBar = new JMenuBar();
		frmApplication.setJMenuBar(menuBar);
		
		JMenu mnGenerator = new JMenu("Form");
		mnGenerator.setMnemonic('f');
		menuBar.add(mnGenerator);
		
		JMenuItem mntmClearFields = new JMenuItem("Clear Fields");
		mntmClearFields.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmClearFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				t.setExamName("ExamName");
				txfExamName.setText("ExamName");
				
				t.setVersionNumber(1);
				jslVersionNumber.setValue(1);
				
				t.setRandomiseQuestionOrder(true);
				bttnRandomiseOrder.setSelected(true);
				
				t.examFileListChange("clear list");
				
				t.setOutputDirectory(null);
				txfOutputFolder.setText("");
			}
		});
		mnGenerator.add(mntmClearFields);
		
		JMenu mnOutput = new JMenu("Open");
		mnOutput.setMnemonic('o');
		menuBar.add(mnOutput);
		
		mntmOpenFile = new JMenuItem("Open Exam File...");
		mntmOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFile(t.getGeneratedExamScript());
						
			}
		});
		mnOutput.add(mntmOpenFile);
		
		mntmOpenAnswerSheet = new JMenuItem("Open Answer Sheet...");
		mntmOpenAnswerSheet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mntmOpenAnswerSheet.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				openFile(t.getAnswerSheet());
			}
			
		});
		mnOutput.add(mntmOpenAnswerSheet);
		
		mntmGoToFolder = new JMenuItem("Open Output Folder...");
		mntmGoToFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmGoToFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFile(t.getOutputDirectory());
			}
		});
		mnOutput.add(mntmGoToFolder);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('h');
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelpDocumentation = new JMenuItem("Help...");
		mntmHelpDocumentation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mntmHelpDocumentation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				browseURI("http://aleang.github.io/ExamGenerator/");			
			}
		});
		mnHelp.add(mntmHelpDocumentation);
		
		JMenuItem mntmAboutAuthor = new JMenuItem("Visit Author's GitHub...");
		mnHelp.add(mntmAboutAuthor);
		mntmAboutAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				browseURI("https://github.com/aleang");
			}
		});
		makePopupMenu();
	}

	protected void browseURI(String uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	        	desktop.browse(new URI(uri));
	        } catch (Exception e) { 
	        	JOptionPane.showMessageDialog(null, e.getMessage()); 
	       	}
	    }
	}
	protected void openFile(File file) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
	        try {
	            desktop.open(file);
	        } catch (Exception e) { 
	        	JOptionPane.showMessageDialog(null, "Error opening the file", "Error", JOptionPane.WARNING_MESSAGE); 
	        }
	    }	
	}

	private void makePopupMenu() {
		popup = new JPopupMenu();
		JMenuItem menuItem;
		ActionListener action;

		menuItem = new JMenuItem("Add files...");
		menuItem.addActionListener(action = new ActionListener() {
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

		            t.addExamFiles(chooser.getSelectedFiles() );
		            //t.setOutputDirectory(chooser.getCurrentDirectory());

		        }
		        
			}
		});
		popup.add(menuItem);
		
		menuItem = new JMenuItem("Move up");
		action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.examFileListChange("move up");
			}
		};
		menuItem.addActionListener(action);
		btnMoveUp.addActionListener(action);
		popup.add(menuItem);
		
		menuItem = new JMenuItem("Move down");
		action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.examFileListChange("move down");
			}
		};
		menuItem.addActionListener(action);
		btnMoveDown.addActionListener(action);
		popup.add(menuItem);
		
		menuItem = new JMenuItem("Remove from list");
		action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.examFileListChange("remove file");
			}
		};
		menuItem.addActionListener(action);
		btnRemoveFile.addActionListener(action);
		popup.add(menuItem);
		
		menuItem = new JMenuItem("Set output folder to where this is");
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
		frmApplication.getContentPane().setBackground(new Color(224, 255, 255));
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(224, 255, 255));
		frmApplication.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel label = new JLabel("Exam Name");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(6, 6, 120, 30);
		panel.add(label);
		
		JLabel label_1 = new JLabel("Version Number");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(6, 41, 120, 30);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("Randomise Order?");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(6, 81, 120, 30);
		panel.add(label_2);
		
		JLabel label_3 = new JLabel("Question Files");
		
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(6, 125, 120, 15);
		panel.add(label_3);
		
		JLabel lbldragFilesHere = new JLabel("(drag files here)");
		lbldragFilesHere.setHorizontalAlignment(SwingConstants.RIGHT);
		lbldragFilesHere.setFont(new Font("SansSerif", Font.ITALIC, 11));
		lbldragFilesHere.setBounds(33, 145, 93, 15);
		panel.add(lbldragFilesHere);
		
		JLabel label_5 = new JLabel("Output Folder");
		
		label_5.setHorizontalAlignment(SwingConstants.RIGHT);
		label_5.setBounds(6, 200, 120, 30);
		panel.add(label_5);
		
		txfExamName = new JTextField();
		txfExamName.addKeyListener(new KeyAdapter() {
			
			public void keyReleased(KeyEvent e) {
				t.setExamName(txfExamName.getText());
				checkGenerateReadiness();
			}
		});
		txfExamName.addFocusListener(new FocusAdapter() {
			
			public void focusGained(FocusEvent e) {
				txfExamName.setSelectionStart(0);
				txfExamName.setSelectionEnd(Integer.MAX_VALUE);
			}
			
			public void focusLost(FocusEvent e) {
				checkGenerateReadiness();
			}
		});
		txfExamName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				t.setExamName(txfExamName.getText());
				checkGenerateReadiness();
			}
		});
		txfExamName.setToolTipText("Name your exam script, it will also be the file name");
		txfExamName.setText("ExamName");
		txfExamName.setColumns(10);
		txfExamName.setBounds(151, 6, 170, 30);
		panel.add(txfExamName);
		
		lblVersionNumber = new JLabel();
		lblVersionNumber.setText("1");
		lblVersionNumber.setBounds(296, 41, 25, 30);
		panel.add(lblVersionNumber);
		
		jslVersionNumber = new JSlider();
		jslVersionNumber.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				lblVersionNumber.setText(jslVersionNumber.getValue() + "");
				t.setVersionNumber(jslVersionNumber.getValue());
				checkGenerateReadiness();
			}
		});
		jslVersionNumber.setValue(1);
		jslVersionNumber.setToolTipText("Select the version number (from 1 to 100)");
		jslVersionNumber.setMinimum(1);
		jslVersionNumber.setBounds(151, 41, 140, 30);
		panel.add(jslVersionNumber);
		
		bttnRandomiseOrder = new JToggleButton("Yes");
		bttnRandomiseOrder.setToolTipText("Randomise questions order per section?");
		bttnRandomiseOrder.setSelected(true);
		bttnRandomiseOrder.setBounds(151, 81, 64, 30);
		panel.add(bttnRandomiseOrder);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setToolTipText("");
		scrollPane.setBounds(151, 121, 170, 77);
		panel.add(scrollPane);
		
		jlQuestionFiles = new JList();
		jlQuestionFiles.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(scrollPane, e.getX(), e.getY());
				} else if (e.getClickCount() == 2){
					t.examFileListChange("set output");
					checkGenerateReadiness();
				}
			}
		});
		scrollPane.setViewportView(jlQuestionFiles);
		
		txfOutputFolder = new JTextField();
		txfOutputFolder.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				openFolderChooser();
			}
		});
		txfOutputFolder.setToolTipText("Set the output folder by double clicking on one of the source file. Click here to choose a folder.");
		txfOutputFolder.setEditable(false);
		txfOutputFolder.setBounds(151, 200, 170, 30);
		panel.add(txfOutputFolder);
		
		btnMoveUp = new JButton("\u25B2");
		btnMoveUp.setToolTipText("Move the selected file up");
		btnMoveUp.setIconTextGap(0);
		btnMoveUp.setHorizontalTextPosition(SwingConstants.LEFT);
		btnMoveUp.setFont(new Font("Arial", Font.PLAIN, 12));
		btnMoveUp.setActionCommand("");
		btnMoveUp.setBounds(331, 121, 45, 25);
		panel.add(btnMoveUp);
		
		btnMoveDown = new JButton("\u25BC");
		btnMoveDown.setToolTipText("Move the selected file down");
		btnMoveDown.setBounds(331, 146, 45, 25);
		panel.add(btnMoveDown);
		
		btnRemoveFile = new JButton("\u00D7");
		btnRemoveFile.setToolTipText("Remove the selected file");
		btnRemoveFile.setFont(new Font("Arial", Font.PLAIN, 20));
		btnRemoveFile.setBackground(new Color(220, 20, 60));
		btnRemoveFile.setBounds(331, 171, 45, 25);
		panel.add(btnRemoveFile);
		
		btnOutputFolder = new JButton("...");
		btnOutputFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFolderChooser();
				
			}
		});
		btnOutputFolder.setToolTipText("Move selected file down");
		btnOutputFolder.setBounds(331, 200, 45, 30);
		panel.add(btnOutputFolder);
		
		bttnRandomVersionNumber = new JToggleButton("?");
		bttnRandomVersionNumber.setToolTipText("Randomise version number");
		bttnRandomVersionNumber.setBounds(333, 42, 45, 30);
		panel.add(bttnRandomVersionNumber);
		
		JLabel label_6 = new JLabel("Output File Name");
		label_6.setToolTipText("");
		label_6.setHorizontalAlignment(SwingConstants.RIGHT);
		label_6.setBounds(6, 240, 120, 30);
		panel.add(label_6);
		
		lblFileName = new JLabel("ExamName-v1-[Exam Script].html");
		lblFileName.setFont(new Font("SansSerif", Font.ITALIC, 12));
		lblFileName.setBounds(151, 240, 283, 30);
		panel.add(lblFileName);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 282, 428, 2);
		panel.add(separator);
		
		JLabel lblReady = new JLabel("Ready?");
		lblReady.setHorizontalAlignment(SwingConstants.RIGHT);
		lblReady.setBounds(6, 290, 120, 30);
		panel.add(lblReady);
		
		btnGenerate = new JButton("Generate");
		btnGenerate.setMnemonic(KeyEvent.VK_G);
		btnGenerate.setToolTipText("Generate when all the fields are valid and ready");
		btnGenerate.setEnabled(false);
		btnGenerate.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent a) {
				try {
					if (bttnRandomVersionNumber.isSelected()) {
						findRandomVersionNumber();
					}
					progWindow.openForm();
		            t.generateTest();
		            progWindow.formDisplaySuccess();
		        } catch (Exception e) {
		            e.printStackTrace();
		            JOptionPane.showMessageDialog(null, "An error occured while generating the exam script.\n"
		                    + "Error Message: " 
		                    + e.getMessage() + '\n'
		                    + "Program has aborted.",
		                    "Encountered an error", JOptionPane.ERROR_MESSAGE);
		            
		        }
			}
		});
		btnGenerate.setBounds(151, 290, 170, 28);
		panel.add(btnGenerate);
		frmApplication.setTitle("Exam Generator");
		frmApplication.setBounds(100, 100, 448, 399);
		frmApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MouseAdapter selectFolderListener = new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				if (t.getOutputDirectory() != null) {
					int userChoice = JOptionPane.showConfirmDialog(null, 
							String.format("The current output directory is %s%nPress OK to change.", t.getOutputDirectory().getAbsolutePath()), 
							"Change output directory?", 
							JOptionPane.OK_CANCEL_OPTION);
					
					if (userChoice == JOptionPane.CANCEL_OPTION) return;
				}
				
				JFileChooser folderChooser = new JFileChooser();
				folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        folderChooser.setMultiSelectionEnabled(true);

		        int returnVal = folderChooser.showOpenDialog(null);
		        if(returnVal == JFileChooser.APPROVE_OPTION) {
		            txfOutputFolder.setText(folderChooser.getSelectedFile().getAbsolutePath());
		            txfOutputFolder.setToolTipText(folderChooser.getSelectedFile().getAbsolutePath());
		            t.setOutputDirectory(folderChooser.getSelectedFile());
		        }
		        
		        checkGenerateReadiness();
			}
		};
		
		DropFileListener myDragDropListener = new DropFileListener(t, this);
		new DropTarget(frmApplication, myDragDropListener);
	}
	protected void openFolderChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			t.setOutputDirectory(chooser.getSelectedFile());
			txfOutputFolder.setText(chooser.getSelectedFile().getAbsolutePath());
		}
		checkGenerateReadiness();
	}

	protected void findRandomVersionNumber() {
		jslVersionNumber.setValue((int)(Math.random()*100 + 1));
		checkGenerateReadiness();
	}

	public void checkGenerateReadiness() {
		if (lblFileName == null) return;
		
		lblFileName.setText(String.format(
			"%s-v%d-[Exam Script].html",
				t.getExamName(),
				t.getVersionNumber()
				)
			);
		try {
			btnGenerate.setEnabled(t.isInputFieldValid());
		} catch (Exception e) {	
			btnGenerate.setEnabled(false);
		}
	}
}
