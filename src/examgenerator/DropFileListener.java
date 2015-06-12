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

public class DropFileListener implements DropTargetListener {
	TestGenerator t;
	
    public DropFileListener(TestGenerator t) {
    	this.t = t;
	}
    

	@Override
    public void drop(DropTargetDropEvent event) {
        // Accept copy drops
        event.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = event.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        flavoursLoop : for (DataFlavor flavor : flavors) {
            try {
                // If the drop items are files
                if (flavor.isFlavorJavaFileListType()) {
                	
                    // Get all of the dropped files
                    List files = (List) transferable.getTransferData(flavor);
                    ArrayList<File> filesArray = new ArrayList<File>();
                    File file;
                    for (int i = 0; i < files.size(); i++) {
                    	file = (File) files.get(i);
                        
                        String extension = file.getName();
                        int index = extension.lastIndexOf('.');
                        if (index > 0) extension = extension.substring(index+1);
                        
                        if (extension.equals("txt") || extension.equals("TXT")){
                        	//System.out.println("File path is '" + file.getPath() + "'.");
                        	//flipperPanel.loadDataFromFile(file);
                        	filesArray.add(file);
                        }
                    	
                    }
                    
                    t.addExamFiles(filesArray);
			         
                    break flavoursLoop;
                }

            } catch (Exception e) { e.printStackTrace();}
        }

        // Inform that the drop is complete
        event.dropComplete(true);
    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {}

    @Override
    public void dragExit(DropTargetEvent event) {}

    @Override
    public void dragOver(DropTargetDragEvent event) {}

    @Override
    public void dropActionChanged(DropTargetDragEvent event) {}

}
