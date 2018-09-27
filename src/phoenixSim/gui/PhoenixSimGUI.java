package phoenixSim.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PhoenixSimGUI extends Application {

    public static void main(String[] args){
        launch(args);
    }

    Stage window ;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage ;
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/MainModule/phoenixSim_module.fxml")) ;
        Parent root = loader.load() ;
        Scene scene = new Scene(root) ;
//        Scene scene = new Scene(root, 1200,900) ;
        window.setScene(scene);
        window.setTitle("PhoenixSim v1.0 - Main Window");
        window.setResizable(false);
//        window.setResizable(true);
        window.getIcons().add(new Image("/People/Meisam/GUI/ThermalHeater/Extras/PhoenixSim_logo.png")) ;

        PhoenixSimModuleController controller = loader.getController() ;
        controller.addWelcomeTab();

        window.show();

        window.setOnCloseRequest(e -> {
        	System.exit(0);
        });

        controller.closeApp.setOnAction(e -> {
        	window.close();
        	System.exit(0);
        });
        
        controller.fullScreenMode.selectedProperty().addListener((v, ov, nv)->{
        	window.setResizable(nv);
        });

    }

}
