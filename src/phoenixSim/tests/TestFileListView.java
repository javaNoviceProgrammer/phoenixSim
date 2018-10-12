package phoenixSim.tests;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import phoenixSim.util.FileListView;

public class TestFileListView extends Application {

	public static void main(String[] args){
		launch(args);
	}

	Stage stage ;

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage ;
		VBox pane = new VBox() ;
		FileListView fileList = new FileListView() ;
		DirectoryChooser dc = new DirectoryChooser() ;
		fileList.setPath(dc.showDialog(null).getAbsolutePath());
		fileList.refreshFileViewer();
//		fileList.list.setPrefSize(500, 500);
		pane.getChildren().add(fileList.getListViewer()) ;
		VBox.setVgrow(pane, Priority.ALWAYS);
		Scene scene = new Scene(pane) ;
		stage.setScene(scene);
		stage.show();

	}

}
