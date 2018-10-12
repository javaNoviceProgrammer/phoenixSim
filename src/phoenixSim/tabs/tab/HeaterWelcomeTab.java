package phoenixSim.tabs.tab;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import phoenixSim.gui.PhoenixSimModule;
import phoenixSim.tabs.AbstractTab;
import phoenixSim.tabs.controller.HeaterWelcomeTabController;
import phoenixSim.util.SimulationDataBase;

public class HeaterWelcomeTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/TabsLibrary/Heater/HeaterWelcome/heater_welcome.fxml")) ;
	HeaterWelcomeTabController controller ;

	public HeaterWelcomeTab(SimulationDataBase simDataBase){
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
	public HeaterWelcomeTabController getController() {
		return controller;
	}

	@Override
	public String getName() {
		return "HeaterWelcomeTab" ;
	}

	@Override
	public void popUpTab() {
		PhoenixSimModule module = new PhoenixSimModule(controller.getSimDataBase()) ;
		module.getController().closeCurrentTabPressed(); // removing the "welcome tab"
		module.getController().addInterfaceTab();
	}

}
