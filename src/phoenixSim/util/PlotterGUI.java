package phoenixSim.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PlotterGUI extends Application {

	public static void main(String[] args){
		launch(args) ;
	}
	
    Stage window ;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage ;
        Parent root = FXMLLoader.load(getClass().getResource("/phoenixSim/fxmls/plotter.fxml")) ;
        Scene scene = new Scene(root) ;
        window.setScene(scene);
        window.setTitle("Plotter v1.0");
        window.setResizable(true);
        window.getIcons().add(new Image("/phoenixSim/extras/plotter.png")) ;
        window.show();
        window.setOnCloseRequest(e -> {
        	System.exit(1);
        });
    }

}
