package phoenixSim.util;

import javafx.application.Application;
import javafx.stage.Stage;

public class ModuleCreatorGUI extends Application {

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ModuleCreator module = new ModuleCreator() ;
		module.createCouplerDesigner();
//		module.createThermalAnalyzer();
//		module.createAddDropFitting();

	}

}
