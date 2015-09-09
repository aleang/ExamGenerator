package examgenerator;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.Font;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.awt.Dialog.ModalExclusionType;

import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class ProgressWindow extends JFrame {

	private JPanel contentPane;
	private TestGenerator t;
	public JCheckBox chckbxReadingQuestionFiles,
		chckbxRandomisingQuestionOrder,
		chckbxWritingExamFile,
		chckbxWritingAnswerFile;
	private JButton btnOpenFile;
	private JButton btnOpenAnswerFile;
	private JButton btnOpenFolder;
	static Point mouseDownCompCoords;
	private JLabel lblReadiingQuestionFiles;
	private JLabel lblRandomisingQuestionOrder;
	private JLabel lblWritingExamFile;
	private JLabel lblWritingAnswerFile;
	
	/**
	 * Create the frame.
	 * @param t 
	 */
	public ProgressWindow(TestGenerator t) {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setAlwaysOnTop(true);
		this.t = t;
		setTitle("Generating...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 269, 280);
		contentPane = new JPanel();
		contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.setBackground(new Color(255, 250, 205));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		chckbxReadingQuestionFiles = new JCheckBox("");
		chckbxReadingQuestionFiles.setEnabled(false);
		chckbxReadingQuestionFiles.setBounds(38, 54, 18, 30);
		contentPane.add(chckbxReadingQuestionFiles);
		
		chckbxRandomisingQuestionOrder = new JCheckBox("");
		chckbxRandomisingQuestionOrder.setEnabled(false);
		chckbxRandomisingQuestionOrder.setBounds(38, 84, 18, 30);
		contentPane.add(chckbxRandomisingQuestionOrder);
		
		chckbxWritingExamFile = new JCheckBox("");
		chckbxWritingExamFile.setEnabled(false);
		chckbxWritingExamFile.setBounds(38, 114, 18, 30);
		contentPane.add(chckbxWritingExamFile);
		
		chckbxWritingAnswerFile = new JCheckBox("");
		chckbxWritingAnswerFile.setEnabled(false);
		chckbxWritingAnswerFile.setBounds(38, 144, 18, 30);
		contentPane.add(chckbxWritingAnswerFile);
		
		btnOpenFile = new JButton("Open Exam File");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(t.getGeneratedExamScript());
			}
		});
		btnOpenFile.setEnabled(false);
		btnOpenFile.setBounds(10, 185, 120, 30);
		contentPane.add(btnOpenFile);
		
		btnOpenAnswerFile = new JButton("Open Answer File");
		btnOpenAnswerFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(t.getAnswerSheet());
			}
		});
		btnOpenAnswerFile.setEnabled(false);
		btnOpenAnswerFile.setBounds(135, 185, 125, 30);
		contentPane.add(btnOpenAnswerFile);
		
		JButton btnOk = new JButton("Close");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeForm();
			}
		});
		btnOk.setBounds(135, 219, 125, 30);
		contentPane.add(btnOk);
		
		btnOpenFolder = new JButton("Open Folder");
		btnOpenFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(t.getOutputDirectory());
			}
		});
		btnOpenFolder.setEnabled(false);
		btnOpenFolder.setBounds(10, 219, 120, 30);
		contentPane.add(btnOpenFolder);
		
		JLabel lblProgress = new JLabel("Progress");
		lblProgress.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblProgress.setHorizontalAlignment(SwingConstants.CENTER);
		lblProgress.setBounds(6, 18, 254, 30);
		contentPane.add(lblProgress);
		
		lblReadiingQuestionFiles = new JLabel("Readiing question files");
		lblReadiingQuestionFiles.setBounds(68, 54, 177, 30);
		contentPane.add(lblReadiingQuestionFiles);
		
		lblRandomisingQuestionOrder = new JLabel("Randomising question order");
		lblRandomisingQuestionOrder.setBounds(68, 84, 177, 30);
		contentPane.add(lblRandomisingQuestionOrder);
		
		lblWritingExamFile = new JLabel("Writing exam file");
		lblWritingExamFile.setBounds(68, 114, 177, 30);
		contentPane.add(lblWritingExamFile);
		
		lblWritingAnswerFile = new JLabel("Writing answer file");
		lblWritingAnswerFile.setBounds(68, 144, 177, 30);
		contentPane.add(lblWritingAnswerFile);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnOpenFile, btnOpenAnswerFile, btnOk}));
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		setUndecorated(true);
		mouseDownCompCoords = null;
		addMouseListener(new MouseListener(){
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
            }
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }
            public void mouseExited(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseClicked(MouseEvent e) {
            }
        });

        addMouseMotionListener(new MouseMotionListener(){
            public void mouseMoved(MouseEvent e) {
            }

            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        });
	}
	
	protected void closeForm() {
		setVisible(false);
	}
	public void openForm() {
		chckbxReadingQuestionFiles.setSelected(false);
		chckbxRandomisingQuestionOrder.setSelected(false);
		chckbxWritingExamFile.setSelected(false);
		chckbxWritingAnswerFile.setSelected(false);
		btnOpenFolder.setEnabled(false);
		btnOpenFile.setEnabled(false);
		btnOpenAnswerFile.setEnabled(false);
		this.getContentPane().setBackground(new Color(255, 250, 205));
		setVisible(true);
		requestFocusInWindow();
	}
	public void openFile(File file) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
	        try {
	            desktop.open(file);
	        } catch (Exception ex) { 
	        	JOptionPane.showMessageDialog(null, "Error opening the file", "Error", JOptionPane.WARNING_MESSAGE); 
	        }
	    }	
	}

	public void formDisplaySuccess() {
		btnOpenFolder.setEnabled(true);
		btnOpenFile.setEnabled(true);
		btnOpenAnswerFile.setEnabled(true);
		this.getContentPane().setBackground(new Color(152, 251, 152));
	}
}
