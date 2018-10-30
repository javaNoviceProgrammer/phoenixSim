package phoenixSim.modules;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GeneralCreditsGUI extends Application {

    public static void main(String[] args){
        launch(args);
    }

    Stage window ;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage ;
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixsim/fxmls/general_credits.fxml")) ;
        Parent root = loader.load() ;
        Scene scene = new Scene(root) ;
        window.setScene(scene);
        window.setTitle("Credits");
        window.setResizable(false);
        window.getIcons().add(new Image("/phoenixSim/extras/gift-box.png")) ;
        window.show();
        
        window.setOnCloseRequest(e -> {
        	System.exit(0);
        });
        

    }
	
}
