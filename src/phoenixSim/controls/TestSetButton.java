package phoenixSim.controls;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import phoenixSim.util.SimulationDataBase;

public class TestSetButton extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane pane = new Pane() ;
		SetButton btn = new SetButton("alpha", "a (dB/cm", new SimulationDataBase()) ;
		pane.getChildren().add(btn) ;
		Scene scene = new Scene(pane, 200, 200) ;
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e1 -> {
			System.exit(0);
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
