package phoenixSim.tabs.tab;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import phoenixSim.tabs.AbstractTab;
import phoenixSim.tabs.controller.CouplerDesignerWecomeTabController;
import phoenixSim.util.SimulationDataBase;

public class CouplerDesignerWelcomeTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/tabs/coupler_designer_welcome_tab.fxml")) ;
	CouplerDesignerWecomeTabController controller ;

	public CouplerDesignerWelcomeTab(SimulationDataBase simDataBase){
		try {
			loader.load() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = loader.getController()  ;
		controller.setSimDataBase(simDataBase);
//		controller.initialize();
	}

	@Override
	public Tab getTab() {
		return controller.getTab();
	}

	@Override
	public StatusBar getStatusBar() {
		return controller.getStatusBar();
	}

	@Override
	public void setSimDataBase(SimulationDataBase simDataBase) {
		controller.setSimDataBase(simDataBase);
	}

	@Override
	public SimulationDataBase getSimDataBase() {
		return controller.getSimDataBase();
	}

	@SuppressWarnings("unchecked")
	@Override
	public CouplerDesignerWecomeTabController getController() {
		return controller;
	}

	@Override
	public String getName() {
		return "CouplerDesignerWelcomeTab" ;
	}

	@Override
	public void popUpTab() {

	}

}
