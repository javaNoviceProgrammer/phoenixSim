package phoenixSim.modules;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import phoenixSim.builder.WindowBuilder;

public class ExportCompleted {
	
	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/exportCompleted.fxml")) ;
	WindowBuilder builder = new WindowBuilder(loader) ;
	
	public ExportCompleted(){
		builder.setIcon("/phoenixSim/extras/export.png");
		try {
			builder.build("Export Status", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
