package phoenixSim.util;

import PhotonicElements.Utilities.MathLibraries.MoreMath;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DataCollectorGUI extends Application {

	// main method to run the GUI
	public static void main(String[] args){
		launch(args) ;
	}

	// start method to run the GUI
	Stage window ;

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage ;
		FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/DataInput/MainGUI/dataCollector.fxml")) ;
        Parent root = loader.load() ;
        Scene scene = new Scene(root) ;
        window.setScene(scene);
        // setting the title of the window GUI
        window.setTitle("Data Collector GUI");
        // better to disable resizable
        window.setResizable(false);
        window.getIcons().add(new Image("/People/Meisam/GUI/DataInput/Extras/dataCollector.png")) ;
        window.show();
        DataCollectorController controller = loader.getController() ;
        controller.getExitButton().setOnAction(e -> {
        	window.close();
        	MoreMath.Arrays.show(controller.getAllValues());
		});
	}

}
