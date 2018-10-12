package phoenixSim.modules;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import phoenixSim.util.SimulationDataBase;

public class ImportDataModule {

	ImportDataController controller ;
	Stage window = new Stage() ;
	
	public ImportDataModule(SimulationDataBase simDataBase){	
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/ImportData/import_data.fxml")) ;
        Parent root;
		try {
			root = loader.load();
	        Scene scene = new Scene(root) ;
	        window.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
        window.setTitle("Import Data");
        window.setResizable(true);
        window.getIcons().add(new Image("/People/Meisam/GUI/Utilities/ImportData/Extras/import.png")) ;
        controller = loader.getController() ;
        controller.setSimDataBase(simDataBase);
        controller.initialize();
//        window.initModality(Modality.APPLICATION_MODAL);
        window.show();
	}
	
	public ImportDataController getController(){
		return controller ;
	}
	
	public Stage getStage(){
		return window ;
	}
	
	
}
