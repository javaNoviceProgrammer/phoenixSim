package phoenixSim.util;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.filechooser.FileSystemView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import mathLib.util.MathUtils;

public class FileListView {

    ListView<String> list = new ListView<String>();
    private String path ;
    private String[] filesAndFolders ;

    ObservableList<String> data ;

    public void setPath(String path){
    	this.path = path ;
    }

    public ListView<String> getListViewer(){
    	return list ;
    }

    public void refreshFileViewer(){
    	setFileNames();
    	populateList();
    	createFileList();
    }

    private void setFileNames(){
    	File folder = new File(path);
    	File[] listOfFiles = folder.listFiles();
    	filesAndFolders = new String[0];
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	if(listOfFiles[i].getName().charAt(0) != '~' && listOfFiles[i].getName().charAt(0) != '$' &&
	    			listOfFiles[i].getName().charAt(0) != '.' && !(listOfFiles[i].isHidden())){
	    		filesAndFolders = MathUtils.Arrays.append(filesAndFolders, listOfFiles[i].getName()) ;
	    	}
	    }
    }

    private void populateList(){
    	data = FXCollections.observableArrayList() ;
    	int N = filesAndFolders.length ;
    	for(int i=0; i<N; i++){
    		data.add(filesAndFolders[i]) ;
    	}
    }

    public void createFileList() {
        list.setItems(data);

        list.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new AttachmentListCell();
            }
        });
        
        list.setOnMouseClicked(e -> {
        	if(e.getButton().equals(MouseButton.PRIMARY)){
        		if(e.getClickCount() == 2){
        			try {
						Desktop.getDesktop().open(new File(path + File.separator + list.getSelectionModel().getSelectedItem()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
        		}
        	}
        });
        
    }

    public class AttachmentListCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
            } else {
                Image fxImage = getFileIcon(item);
                ImageView imageView = new ImageView(fxImage);
                setGraphic(imageView);
                setText(item);
            }
        }

    }

    static HashMap<String, Image> mapOfFileExtToSmallIcon = new HashMap<String, Image>();

    private static String getFileExt(String fname) {
        String ext = ".";
        int p = fname.lastIndexOf('.');
        if (p >= 0) {
            ext = fname.substring(p).toLowerCase();
        }
        else{
        	ext = null ;
        }
        return ext;
    }

    private static javax.swing.Icon getJSwingIconFromFileSystem(File file) {

    	javax.swing.Icon icon = null  ;

    	if(OSDetector.isWindows()){
	        // Windows
	        FileSystemView view = FileSystemView.getFileSystemView();
	        icon = view.getSystemIcon(file);
    	}
    	else if(OSDetector.isMac()){
	//     OS X
	       final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
	       icon = fc.getUI().getFileView(fc).getIcon(file);
    	}

        return icon;
    }

    private static Image getFileIcon(String fname) {
        final String ext = getFileExt(fname);
        Image fileIcon ;
        if(ext == null){
        	fileIcon = new Image(Object.class.getClass().getResourceAsStream("/People/Meisam/GUI/Utilities/Icons/folder.png")) ;
        }
        else{
	        fileIcon = mapOfFileExtToSmallIcon.get(ext);
	        if (fileIcon == null) {

	            javax.swing.Icon jswingIcon = null;

	            File file = new File(fname);
	            if (file.exists()) {
	                jswingIcon = getJSwingIconFromFileSystem(file);
	            }
	            else {
	                File tempFile = null;
	                try {
	                    tempFile = File.createTempFile("icon", ext);
	                    jswingIcon = getJSwingIconFromFileSystem(tempFile);
	                }
	                catch (IOException ignored) {
	                    // Cannot create temporary file.
	                }
	                finally {
	                    if (tempFile != null) tempFile.delete();
	                }
	            }

	            if (jswingIcon != null) {
	                fileIcon = jswingIconToImage(jswingIcon);
	                mapOfFileExtToSmallIcon.put(ext, fileIcon);
	            }
	        }
        }

        return fileIcon;
    }

    private static Image jswingIconToImage(javax.swing.Icon jswingIcon) {
        BufferedImage bufferedImage = new BufferedImage(jswingIcon.getIconWidth(), jswingIcon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        jswingIcon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

}
