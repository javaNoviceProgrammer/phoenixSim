package phoenixSim.modules;

import java.io.IOException;

import People.Meisam.GUI.Utilities.Interfaces.ActionInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.VariableSelectorController;

public class VariableSelectorModule {

	VariableSelectorController controller ;
	Stage window = new Stage() ;

	public VariableSelectorModule(SimulationDataBase simDataBase){
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/VariableSelector/variable_selector.fxml")) ;
        Parent root;
		try {
			root = loader.load();
	        Scene scene = new Scene(root) ;
	        window.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
        window.setTitle("Select Variable & Values");
        window.setResizable(false);
        window.getIcons().add(new Image("/People/Meisam/GUI/Utilities/VariableSelector/Extras/icon.png")) ;
        controller = loader.getController() ;
        controller.setSimDataBase(simDataBase);
        controller.initialize();
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();
	}

	public VariableSelectorController getController(){
		return controller ;
	}

	public Stage getStage(){
		return window ;
	}

	public Button getExitButton(){
		return controller.getSetExitButton() ;
	}

	public void setExitAction(ActionInterface ex){
		controller.getSetExitButton().setOnAction(e -> {
			ex.setExitAction();
			window.close();
		});
	}

}
