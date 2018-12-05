package phoenixSim.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ExportToMatlabGUI extends Application {

	public static void main(String[] args){
		launch(args) ;
	}
	
    Stage window ;
    ExportToMatlabController controller ;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/phoenixSim/fxmls/exportToMatlab.fxml")) ;
//        Parent root = FXMLLoader.load(getClass().getResource("/phoenixSim/fxmls/exportToMatlab.fxml")) ;
        Parent root = loader.load() ;
        Scene scene = new Scene(root) ;
        window.setScene(scene);
        window.setTitle("Configure Export to MATLAB");
        window.getIcons().add(new Image(getClass().getResourceAsStream("/phoenixSim/extras/Matlab_Logo.png"))) ;
        window.setResizable(false);
        window.show();
        
        controller = loader.getController() ;
    }
    
    public ExportToMatlabController getController() {
    	return controller ;
    }
	
}
