package phoenixSim.modules;

import java.io.IOException;
import People.Meisam.GUI.DataInput.MainGUI.DataCollectorController;
import People.Meisam.GUI.Utilities.Interfaces.ActionInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SweepParameterModule {

	DataCollectorController controller ;
	Stage window = new Stage() ;

	public SweepParameterModule(){
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/DataInput/MainGUI/dataCollector.fxml")) ;
        Parent root;
		try {
			root = loader.load();
	        Scene scene = new Scene(root) ;
	        window.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
        window.setTitle("Data Collector");
        window.setResizable(false);
        window.getIcons().add(new Image("/People/Meisam/GUI/DataInput/Extras/dataCollector.png")) ;
        controller = loader.getController() ;
        controller.initialize();
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();
	}

	public DataCollectorController getController(){
		return controller ;
	}

	public Stage getStage(){
		return window ;
	}

	public Button getExitButton(){
		return controller.getExitButton() ;
	}

	public void setExitAction(ActionInterface ex){
		controller.getExitButton().setOnAction(e -> {
			ex.setExitAction();
			window.close();
		});
	}

//	//****************** for test
//	public static class TestModule extends Application{
//
//		public static void main(String[] args){
//			launch(args);
//		}
//
//		@Override
//		public void start(Stage primaryStage) throws Exception {
//			SweepParameterModule sweep = new SweepParameterModule() ;
//			sweep.setExitAction(new ActionInterface() {
//				@Override
//				public void setExitAction() {
//					MoreMath.Arrays.show(sweep.getController().getAllValues());
//				}
//			});
//		}
//
//	}
//
//	//***************************

}
