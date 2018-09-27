package phoenixSim.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by meisam on 7/7/17.
 */
public class DatabaseTableGUI extends Application {

    public static void main(String[] args){
        launch(args);
    }

    Stage window ;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage ;
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/DataBaseTable/DataBase_v1_0/database_table.fxml")) ;
        Parent root = loader.load() ;
        Scene scene = new Scene(root) ;
        window.setScene(scene);
        window.setTitle("DataBase Viewer v1.0");
        window.setResizable(true);
        window.getIcons().add(new Image("/People/Meisam/GUI/Utilities/DataBaseTable/Extras/database.png")) ;
        window.show();

        window.setOnCloseRequest(e -> {
            System.exit(0);
        });

        DatabaseTableController controller = loader.getController() ;
        controller.updateParamTable();
        controller.paramCloseButton.setOnAction(e -> {
            window.close();
        });
    }


}
