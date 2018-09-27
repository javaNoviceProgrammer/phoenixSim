package phoenixSim.modules;

import java.io.IOException;

import People.Meisam.GUI.Builders.WindowBuilder;
import javafx.fxml.FXMLLoader;

public class ExportCompleted {
	
	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/ExportPlot/ExportCompleted/exportCompleted.fxml")) ;
	WindowBuilder builder = new WindowBuilder(loader) ;
	
	public ExportCompleted(){
		builder.setIcon("/People/Meisam/GUI/Utilities/ExportPlot/ExportCompleted/export.png");
		try {
			builder.build("Export Status", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
