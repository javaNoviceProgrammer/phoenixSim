package phoenixSim.modules;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import phoenixSim.util.PlotterController;
import phoenixSim.util.SimulationDataBase;

public class PlotterModule {

	public PlotterModule(SimulationDataBase simDataBase){
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Plotters/MainGUI/plotter.fxml")) ;
        WindowBuilder plotter = new WindowBuilder(loader) ;
        plotter.setIcon("/People/Meisam/GUI/Plotters/MainGUI/Extras/plotter.png");
        try {
			plotter.build_NoModality("Plotter v0.5 Beta", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        PlotterController controller = (PlotterController) plotter.getController() ;
        controller.setDataBase(simDataBase);
        controller.initialize();
	}

}
