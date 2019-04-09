package phoenixSim.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import phoenixSim.util.DatabaseTableController;
import phoenixSim.util.FileChooserFX;
import phoenixSim.util.FileListView;

public class PhoenixSimGUIFullScreen extends Application {

    public static void main(String[] args){
        launch(args);
    }

    Stage window ;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage ;
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/phoenixSim_module.fxml")) ;
        Parent root = loader.load() ;
        Scene scene = new Scene(root) ;

        window.setScene(scene);
        window.setTitle("PhoenixSim v1.0 - Main Window");
        window.setResizable(false);

        window.getIcons().add(new Image("/phoenixSim/extras/PhoenixSim_logo.png")) ;

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

        	controller.fullScreenMode.setDisable(true);
        	controller.tabs.setSide(Side.TOP);


        	FileListView fileViewer = new FileListView() ;
        	fileViewer.setPath(FileChooserFX.path);
        	fileViewer.refreshFileViewer();

        	TabPane leftPane = new TabPane() ;
        	Tab workDir = new Tab("Workspace") ;
        	workDir.setContent(fileViewer.getListViewer());
        	leftPane.getTabs().add(workDir) ;
        	leftPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        	leftPane.setSide(Side.LEFT);

        	controller.mainWindow.setLeft(leftPane);

        	TabPane rightPane = new TabPane() ;
        	FXMLLoader databaseTab = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/database_table_fullscreen_mode.fxml")) ;
        	try {
				databaseTab.load() ;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        	DatabaseTableController dbcontroller = databaseTab.getController() ;
        	dbcontroller.setDataBase(controller.simDataBase);
        	dbcontroller.updateParamTable();
        	dbcontroller.getRefreshButton().setOnAction(e -> {
        		dbcontroller.refreshTable(controller.simDataBase);
            });
        	rightPane.getTabs().add(dbcontroller.tab) ;
        	rightPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        	rightPane.setSide(Side.RIGHT);

        	controller.mainWindow.setRight(rightPane);

        });

    }

}
