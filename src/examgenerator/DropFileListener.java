package examgenerator;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class DropFileListener implements DropTargetListener {
	TestGenerator t;
	Application a;
	
    public DropFileListener(TestGenerator t, Application a) {
    	this.t = t;
    	this.a = a;
	}
    
    public void drop(DropTargetDropEvent event) {
    	// Request focus to application
    	t.requestFocus();
    	
        // Accept copy drops
        event.acceptDrop(DnDConstants.ACTION_COPY);
        
        // Get the transfer which can provide the dropped item data
        Transferable transferable = event.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Create a list to hold rejected files
        ArrayList<File> rejectedFiles = new ArrayList<File>();
        
        // Loop through the flavors
        flavoursLoop : for (DataFlavor flavor : flavors) {
            try {
                // If the drop items are files
                if (flavor.isFlavorJavaFileListType()) {
                	
                    // Get all of the dropped files
                    List files = (List) transferable.getTransferData(flavor);
                    ArrayList<File> filesArray = new ArrayList<File>();
                    File file;
                    filesLoop : for (int i = 0; i < files.size(); i++) {
                    	file = (File) files.get(i);
                        
                        String extension = file.getName();
                        int index = extension.lastIndexOf('.');
                        
                        if (index > 0) {
                        	extension = extension.substring(index+1);
                        	if (extension.equalsIgnoreCase("txt")){
                            	
                            	filesArray.add(file);
                            } else {
                            	rejectedFiles.add(file);
                            	continue filesLoop;
                        	}
                        }
                    }
                    
                    t.addExamFiles(filesArray);
                    a.checkGenerateReadiness();
                    break flavoursLoop;
                }

            } catch (Exception e) { e.printStackTrace();}
        }

        // Notify user for any rejected files
        if (rejectedFiles.size() > 0) notifyUser(rejectedFiles);
        
        // Inform that the drop is complete
        event.dropComplete(true);
    }

    private void notifyUser(ArrayList<File> rejectedFiles) {
    	String files = "";
    	for (File f: rejectedFiles) {
    		files += '"' + f.getName() + "\"\n";
    	}
    	
    	JOptionPane.showMessageDialog(null, 
    			String.format("These files were not accepted... %n%s", files), 
    			"Rejected files", 
    			JOptionPane.WARNING_MESSAGE);
	}

	public void dragEnter(DropTargetDragEvent event) {}
    public void dragExit(DropTargetEvent event) {}
    public void dragOver(DropTargetDragEvent event) {}
    public void dropActionChanged(DropTargetDragEvent event) {}

}
