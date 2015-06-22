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

import java.awt.Color;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

/**
 * Exam Generator 2
 * @version 1.06.21
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
	private JProgressBar jpbProgress;
	private JSlider jslVersionNumber;
	private JToggleButton bttnRandomiseOrder;
	private JToggleButton bttnRandomVersionNumber;
	private JList jlQuestionFiles;
	private JLabel lblFileName;
	private JButton btnRemoveFile;
	private JButton btnMoveDown;
	private JButton btnMoveUp;
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
		initialize();
		frmApplication.setTitle("Exam Generator \u00a9 2015 Pheng Taing");
		t.setComponents(jlQuestionFiles, txfOutputFolder, bttnRandomVersionNumber);
		
		JMenuBar menuBar = new JMenuBar();
		frmApplication.setJMenuBar(menuBar);
		
		JMenu mnGenerator = new JMenu("Exam");
		mnGenerator.setMnemonic('e');
		menuBar.add(mnGenerator);
		
		JMenuItem mntmClearFields = new JMenuItem("Clear Fields");
		mntmClearFields.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmClearFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				t.setExamName("");
				txfExamName.setText("");
				
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
		
		JMenuItem mntmGenerate = new JMenuItem("Generate!");
		mntmGenerate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
		mntmGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				try {
					if (bttnRandomVersionNumber.isSelected()) {
						findRandomVersionNumber();
					}
		            t.generateTest();
		            jpbProgress.setValue(100);
		            //JOptionPane.showMessageDialog(null, "Exam Script generated successfully.\nPress Ctrl+O to open the file.\nPress Ctrl+Shift+O to open the output folder", "Success", JOptionPane.INFORMATION_MESSAGE);
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
		mnGenerator.add(mntmGenerate);
		
		JMenu mnOutput = new JMenu("Output");
		mnOutput.setMnemonic('o');
		menuBar.add(mnOutput);
		
		mntmGoToFolder = new JMenuItem("Open Output Folder...");
		mntmGoToFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmGoToFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			    if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
			        try {
			            desktop.open(t.getOutputDirectory());
			        } catch (Exception e) { 
			        	JOptionPane.showMessageDialog(null, "Error opening the output folder", "Error", JOptionPane.WARNING_MESSAGE); 
			        }
			    }
			}
		});
		mnOutput.add(mntmGoToFolder);
		
		mntmOpenFile = new JMenuItem("Open Generated File...");
		mntmOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			    if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
			        try {
			            desktop.open(t.getGeneratedExamScript());
			        } catch (Exception e) { 
			        	JOptionPane.showMessageDialog(null, "Error opening the generated file", "Error", JOptionPane.WARNING_MESSAGE); 
			        }
			    }			
			}
		});
		mnOutput.add(mntmOpenFile);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('h');
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelpDocumentation = new JMenuItem("Help...");
		mntmHelpDocumentation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mntmHelpDocumentation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			/*	String[] sourceFiles = {"help.html"};
				File helpFile = null;
				Desktop desktop = Desktop.getDesktop();
				for (String filename: sourceFiles) {
				
					if (Desktop.isDesktopSupported())   
					{   
					    InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/"+filename);
					    try
					    {
					    	File file = File.createTempFile(
					    			filename.substring(0, filename.lastIndexOf('.')), 
					    			filename.substring(filename.lastIndexOf('.')));
					        file.deleteOnExit();
					        if (filename.equals("help.html")) helpFile = file;
					        
					        PrintWriter tempFile = new PrintWriter(file);
					        OutputStream out = new FileOutputStream(file);
					        try
					        {
					            Scanner origSource = new Scanner(resource);
					            while (origSource.hasNextLine()) {
					            	tempFile.println(origSource.nextLine());
					            }
					            tempFile.flush();
					        }
					        finally
					        {
					        	tempFile.close();
					            out.close();
					        }
					        
					        resource.close();
					    } catch (Exception e) {
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
					}
				}
				if (Desktop.isDesktopSupported())
					try {
						desktop.open(helpFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				*/
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			    if (desktop != null && desktop.isDesktopSupported()) {
			        try {
			        	//desktop.open(new File("resources/help.html"));
			            desktop.browse(new URI("http://aleang.github.io/ExamGenerator/"));
			        } catch (Exception e) { 
			        	JOptionPane.showMessageDialog(null, e.getMessage()); 
			       	}
			    }
				
			
			}
		});
		mnHelp.add(mntmHelpDocumentation);
		
		JMenu mnAboutAuthor = new JMenu("About Exam Geneator");
		mnHelp.add(mnAboutAuthor);
		
		JMenuItem mntmAboutAuthor = new JMenuItem("Visit Author's Page...");
		mntmAboutAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			        try {
			            desktop.browse(new URI("https://github.com/aleang"));
			        } catch (Exception e) { e.printStackTrace(); }
			    }
			}
		});
		mnAboutAuthor.add(mntmAboutAuthor);
		makePopupMenu();
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
		label_3.setToolTipText("");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(6, 125, 120, 15);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("(drag files here)");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setFont(new Font("SansSerif", Font.ITALIC, 11));
		label_4.setBounds(33, 145, 93, 15);
		panel.add(label_4);
		
		JLabel label_5 = new JLabel("Output Folder");
		label_5.setToolTipText("click the text box to select the output folder");
		label_5.setHorizontalAlignment(SwingConstants.RIGHT);
		label_5.setBounds(6, 201, 120, 30);
		panel.add(label_5);
		
		txfExamName = new JTextField();
		txfExamName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				txfExamName.setSelectionStart(0);
				txfExamName.setSelectionEnd(Integer.MAX_VALUE);
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				t.setExamName(txfExamName.getText());
				updateSampleFileName();
			}
		});
		txfExamName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				t.setExamName(txfExamName.getText());
				updateSampleFileName();
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
				updateSampleFileName();
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
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(scrollPane, e.getX(), e.getY());
				} else if (e.getClickCount() == 2){
					t.examFileListChange("set output");
				}
			}
		});
		scrollPane.setViewportView(jlQuestionFiles);
		
		txfOutputFolder = new JTextField();
		txfOutputFolder.setToolTipText("Set the output folder by double clicking on one of the source file. Click here to choose a folder.");
		txfOutputFolder.setEditable(false);
		txfOutputFolder.setBounds(151, 201, 170, 30);
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
		btnRemoveFile.setToolTipText("Remove the selected file. Right-click to remove all");
		btnRemoveFile.setFont(new Font("Arial", Font.PLAIN, 20));
		btnRemoveFile.setBackground(new Color(220, 20, 60));
		btnRemoveFile.setBounds(331, 171, 45, 25);
		panel.add(btnRemoveFile);
		
		JButton btnOutputFolder = new JButton("...");
		btnOutputFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnOutputFolder.setToolTipText("Move selected file down");
		btnOutputFolder.setBounds(331, 201, 45, 30);
		panel.add(btnOutputFolder);
		
		bttnRandomVersionNumber = new JToggleButton("Random");
		bttnRandomVersionNumber.setToolTipText("Set this to get a random version number at 'Generate'");
		bttnRandomVersionNumber.setBounds(333, 42, 80, 30);
		panel.add(bttnRandomVersionNumber);
		
		JLabel label_6 = new JLabel("Output File Name");
		label_6.setToolTipText("click the text box to select the output folder");
		label_6.setHorizontalAlignment(SwingConstants.RIGHT);
		label_6.setBounds(6, 240, 120, 30);
		panel.add(label_6);
		
		lblFileName = new JLabel("ExamName-v1-[Exam Script].html");
		lblFileName.setFont(new Font("SansSerif", Font.ITALIC, 12));
		lblFileName.setBounds(151, 240, 283, 30);
		panel.add(lblFileName);
		
		JLabel lblProgress = new JLabel("Progress");
		lblProgress.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProgress.setBounds(6, 280, 120, 30);
		panel.add(lblProgress);
		
		jpbProgress = new JProgressBar();
		jpbProgress.setBounds(151, 280, 170, 30);
		panel.add(jpbProgress);
		frmApplication.setTitle("Exam Generator");
		frmApplication.setBounds(100, 100, 448, 367);
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
			}
		};
		
		DropFileListener myDragDropListener = new DropFileListener(t);
		new DropTarget(frmApplication, myDragDropListener);
	}
	protected void findRandomVersionNumber() {
		jslVersionNumber.setValue((int)(Math.random()*100 + 1));
		updateSampleFileName();
		
	}

	protected void updateSampleFileName() {
		if (lblFileName == null) return;
		lblFileName.setText(String.format(
			"%s-v%d-[Exam Script].html",
				t.getExamName() == null || t.getExamName().length() == 0 ? "ExamName" : t.getExamName(),
				t.getVersionNumber()
				)
			);
		jpbProgress.setValue(0);
	}
}
