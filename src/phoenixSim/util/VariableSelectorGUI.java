package phoenixSim.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mathLib.util.MathUtils;

public class VariableSelectorGUI extends Application {

    public static void main(String[] args){
        launch(args);
    }

    Stage window ;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        window = primaryStage ;
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/variable_selector.fxml")) ;
        Parent root = loader.load() ;
        Scene scene = new Scene(root) ;
        window.setScene(scene);
        window.setTitle("Select Variable & Values");
        window.setResizable(false);
        window.getIcons().add(new Image("/phoenixSim/extras/icon.png")) ;
        window.show();
        
        window.setOnCloseRequest(e -> {
        	System.exit(1);
        });
        
        VariableSelectorController controller = loader.getController() ;
        controller.getSetExitButton().setOnAction(e -> {
        	MathUtils.Arrays.show(controller.getValues());
        	System.exit(1);
        });
		
	}

}
