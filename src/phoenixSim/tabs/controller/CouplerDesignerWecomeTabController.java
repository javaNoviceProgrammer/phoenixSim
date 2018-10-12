package phoenixSim.tabs.controller;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import mathLib.plot.MatlabChart;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;

public class CouplerDesignerWecomeTabController extends AbstractTabController {

	@FXML Tab tab ;
	StatusBar statusBar = new StatusBar() ;

	SimulationDataBase simDataBase = new SimulationDataBase() ;

	@FXML
	public void initialize(){
		statusBar.setText("Ready");
	}

	@Override
	public Tab getTab() {
		return tab;
	}

	@Override
	public MatlabChart getFig() {
		return null;
	}

	@Override
	public StatusBar getStatusBar() {
		return statusBar;
	}

	@Override
	public void setSimDataBase(SimulationDataBase simDataBase) {
		this.simDataBase = simDataBase ;
	}

	@Override
	public SimulationDataBase getSimDataBase() {
		return simDataBase;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}

}
