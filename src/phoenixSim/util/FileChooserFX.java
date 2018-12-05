package phoenixSim.util;

import java.io.File;

import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Created by meisam on 7/2/17.
 */
public class FileChooserFX {

    FileChooser fc ;
    DirectoryChooser dc ;
    File selectedFile ;
    File[] selectedFiles ;
    
    // default path for opening and saving --> workspace
    public static String path = System.getProperty("user.home") + File.separator +"Desktop" ;

    public FileChooserFX(){
        fc = new FileChooser() ;
        dc = new DirectoryChooser() ;
        fc.setTitle("Select Path");
        // setting up default path
        setPath(path);
    }

    // constructor for just one extension
    public FileChooserFX(String extension){
        fc = new FileChooser() ;
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*."+extension, "*."+extension) ;
        fc.getExtensionFilters().addAll(extFilter) ;
        // setting up default path
        setPath(path);
    }

    public void setExtension(String ext){
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("*."+ext, "*."+ext)) ;

    }

    public void addExtension(String ext){
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("*."+ext, "*."+ext)) ;

    }

    public void saveFile(){
        selectedFile = fc.showSaveDialog(new Stage()) ;
    }

    // need to implement also open multiple files

    public void openFile(){
        selectedFile = fc.showOpenDialog(new Stage()) ;
    }
    
    public String openDirectory() {
    	dc.setInitialDirectory(new File(path));
    	return dc.showDialog(new Stage()) ;
    }

    public File getSelectedFile(){
        return selectedFile ;
    }

    public String getFileName(){
        return selectedFile.getName() ;
    }

    public boolean fileFound(){
        return selectedFile.exists() ;
    }

    public String getFilePath(){
        String st = selectedFile.getPath() ;
        return st ;
    }

    public String getFileExtension(){
    	String[] st = selectedFile.getName().split("\\.") ;
    	int M = st.length ;
    	return st[M-1] ;
    }

    public String getFileName_NoExtension(){
    	String[] st = selectedFile.getName().split("\\.") ;
    	return st[0] ;
    }

    public String getFilePath_NoExtension(){
    	String[] st = selectedFile.getPath().split("\\.") ;
    	return st[0] ;
    }

    public String getDirectoryPath(){
    	String st = getSelectedFile().getParent() ;
    	return st ;
    }

    public void setPath(String path){
    	File file = new File(path) ;
    	fc.setInitialDirectory(file);
    }

    //************** for test ********
    public static class testChooser extends Application{

    	public static void main(String[] args){
    		launch(args);
    	}

		@Override
		public void start(Stage primaryStage) throws Exception {
			FileChooserFX fc = new FileChooserFX() ;
//			fc.addExtension("txt");
//			fc.addExtension("jpeg");
////			String path = "C:\\Users\\Meisam\\Desktop\\icons" ;
////			fc.setPath(path);
//			fc.saveFile();
//			System.out.println(fc.getFileName());
//			System.out.println(fc.getFilePath());
//			System.out.println(fc.getFileName_NoExtension());
//			System.out.println(fc.getFilePath_NoExtension());
//			System.out.println(fc.getDirectoryPath());
			
			
			System.out.println(fc.openDirectory());
			
		}

    }
    //*********************************


}
