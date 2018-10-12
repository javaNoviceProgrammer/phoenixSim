package phoenixSim.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mathLib.util.MathUtils;

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
		FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/dataCollector.fxml")) ;
        Parent root = loader.load() ;
        Scene scene = new Scene(root) ;
        window.setScene(scene);
        // setting the title of the window GUI
        window.setTitle("Data Collector GUI");
        // better to disable resizable
        window.setResizable(false);
        window.getIcons().add(new Image("/phoenixSim/extras/dataCollector.png")) ;
        window.show();
        DataCollectorController controller = loader.getController() ;
        controller.getExitButton().setOnAction(e -> {
        	window.close();
        	MathUtils.Arrays.show(controller.getAllValues());
		});
	}

}
