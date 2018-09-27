package phoenixSim.tabs.controller;

import org.controlsfx.control.StatusBar;
import People.Meisam.GUI.PhoenixSim.TabsLibrary.AbstractTabController;
import People.Meisam.GUI.Plotters.MatlabPlot.MatlabChart;
import People.Meisam.GUI.Utilities.SimulationDataBase;
import People.Meisam.GUI.Utilities.ExportPlot.JavaFXFileChooser.FileChooserFX;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class WelcomeTabController extends AbstractTabController {

	SimulationDataBase simDataBase = new SimulationDataBase()  ;
	StatusBar statusBar = new StatusBar() ;

	@FXML TabPane tabs ;
	@FXML Tab welcomeTab ;

	public void setSimDataBase(SimulationDataBase simDataBase) {
		this.simDataBase = simDataBase ;
	}

	public SimulationDataBase getSimDataBase(){
		return simDataBase ;
	}

	public void initialize() {
		String st = "Default workspace is set to: " + FileChooserFX.path ;
		statusBar.setText(st);
	}

	public Tab getTab() {
		return welcomeTab ;
	}

	public StatusBar getStatusBar() {
		return statusBar ;
	}

	@Override
	public MatlabChart getFig() {
		return null;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}

}
