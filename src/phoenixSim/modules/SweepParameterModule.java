package phoenixSim.modules;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import phoenixSim.builder.intf.ActionInterface;
import phoenixSim.util.DataCollectorController;

public class SweepParameterModule {

	DataCollectorController controller ;
	Stage window = new Stage() ;

	public SweepParameterModule(){
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/dataCollector.fxml")) ;
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
        window.getIcons().add(new Image("/phoenixSim/extras/dataCollector.png")) ;
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
