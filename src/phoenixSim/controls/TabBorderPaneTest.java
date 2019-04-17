package phoenixSim.controls;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import phoenixSim.util.DatabaseTableController;
import phoenixSim.util.FileChooserFX;
import phoenixSim.util.FileListView;
import phoenixSim.util.SimulationDataBase;

public class TabBorderPaneTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(Object.class.getResource("/phoenixSim/fxmls/tabs/data_smoothing2.fxml")) ;
		loader.load() ;
		
		MenuBar menuBar = new MenuBar() ;
		Menu file = new Menu("File") ;
		MenuItem click = new MenuItem("Full screen") ;
		MenuItem small = new MenuItem("Small screen") ;
		file.getItems().addAll(click, small) ;
		menuBar.getMenus().add(file) ;
		
		BorderPane border = new BorderPane() ;
		border.setTop(menuBar);
		border.setCenter(loader.getRoot());
		border.setRight(null);
		border.setBottom(null);

		Scene scene = new Scene(border) ;
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e1 -> {
			System.exit(0);
		});
	
		
		
		// setting fullscreen mode
		click.setOnAction(e1 -> {
        	FileListView fileViewer = new FileListView() ;
        	fileViewer.setPath(FileChooserFX.path);
        	fileViewer.refreshFileViewer();
        	
        	border.setTop(menuBar);
        	border.setLeft(fileViewer.getListViewer());
        	border.setCenter(loader.getRoot());
        	TextArea txt = new TextArea() ;
        	txt.setPrefWidth(1000);
        	border.setBottom(txt);
        	
        	FXMLLoader loadDb = new FXMLLoader(Object.class.getResource("/phoenixSim/fxmls/database_table_fullscreen_mode.fxml")) ;
        	try {
				loadDb.load() ;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	DatabaseTableController dbcontroller = loadDb.getController() ;
        	dbcontroller.setDataBase(new SimulationDataBase());
        	dbcontroller.updateParamTable();
        	dbcontroller.getRefreshButton().setOnAction(e -> {
        		dbcontroller.refreshTable(new SimulationDataBase());
            });
        	
        	border.setRight(loadDb.getRoot());
        	
		});
		
		small.setOnAction(e2 -> {
        	border.setTop(menuBar);
        	border.setLeft(loader.getRoot());
        	border.setCenter(null);
        	border.setRight(null);
        	border.setBottom(null);
        	
		});
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	

}
