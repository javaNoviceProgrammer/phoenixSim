package phoenixSim.builder;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowBuilder {

    FXMLLoader fxmlLoader ;
    Stage stage = new Stage() ;
    Image icon = new Image("file:dummy.png") ; // empty image

    public WindowBuilder(
            FXMLLoader fxmlLoader
    ){
        this.fxmlLoader = fxmlLoader ;
        stage.setOnCloseRequest(e -> {
        	stage.close();
        });
    }

    public void setIcon(Image icon){
    	this.icon = icon ;
    }

    public void setIcon(String iconPath){
    	icon = new Image(iconPath) ;
    }

    public Stage getStage(){
    	return stage ;
    }

    // building the window with application modality

	public void build(String windowTitle, boolean isResizable) throws IOException{
        Parent root = fxmlLoader.load();
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root));
        stage.setResizable(isResizable);
        stage.getIcons().add(icon) ;
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
	}

	public void build(String windowTitle) throws IOException{
        Parent root = fxmlLoader.load();
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root));
        stage.getIcons().add(icon) ;
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
	}

	public void build(boolean isResizable) throws IOException{
        Parent root = fxmlLoader.load();
        stage.setTitle("");
        stage.setScene(new Scene(root));
        stage.setResizable(isResizable);
        stage.getIcons().add(icon) ;
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
	}

	public void build() throws IOException{
        Parent root = fxmlLoader.load();
        stage.setTitle("");
        stage.setScene(new Scene(root));
        stage.getIcons().add(icon) ;
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
	}

    // building the window with window modality

	public void build_NoModality(String windowTitle, boolean isResizable) throws IOException{
        Parent root = fxmlLoader.load();
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root));
        stage.setResizable(isResizable);
        stage.getIcons().add(icon) ;
        stage.show();
	}

	public void build_NoModality(String windowTitle) throws IOException{
        Parent root = fxmlLoader.load();
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root));
        stage.getIcons().add(icon) ;
        stage.show();
	}

	public void build_NoModality(boolean isResizable) throws IOException{
        Parent root = fxmlLoader.load();
        stage.setTitle("");
        stage.setScene(new Scene(root));
        stage.setResizable(isResizable);
        stage.getIcons().add(icon) ;
        stage.show();
	}

	public void build_NoModality() throws IOException{
        Parent root = fxmlLoader.load();
        stage.setTitle("");
        stage.setScene(new Scene(root));
        stage.getIcons().add(icon) ;
        stage.show();
	}


	public AbstractController getController(){
		return fxmlLoader.getController() ;
	}

    public void close() throws IOException{
        stage.close();
    }


}
