package phoenixSim.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ImportDataGUI extends Application {

	public static void main(String[] args){
		launch(args);
	}
	
	Stage window ;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        window = primaryStage ;
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/ImportData/import_data.fxml")) ;
        Parent root = loader.load() ;
        Scene scene = new Scene(root) ;
        window.setScene(scene);
        window.setTitle("Import Data");
        window.setResizable(true);
        window.getIcons().add(new Image("/People/Meisam/GUI/Utilities/ImportData/Extras/import.png")) ;
        
        window.show();

        window.setOnCloseRequest(e -> {
        	System.exit(0);
        });

    }
	
	
}
