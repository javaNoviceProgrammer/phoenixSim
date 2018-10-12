package phoenixSim.tabs.tab;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import phoenixSim.gui.PhoenixSimModule;
import phoenixSim.tabs.AbstractTab;
import phoenixSim.tabs.controller.WelcomeTabController;
import phoenixSim.util.SimulationDataBase;

public class WelcomeTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/tabs/welcome_tab.fxml")) ; ;
	WelcomeTabController controller ;

	public WelcomeTab(SimulationDataBase simDataBase){
		try {
			loader.load() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = loader.getController()  ;
		controller.setSimDataBase(simDataBase);
//		controller.initialize();
	}

	public Tab getTab() {
		return controller.getTab() ;
	}

	public StatusBar getStatusBar(){
		return controller.getStatusBar() ;
	}

	@SuppressWarnings("unchecked")
	public WelcomeTabController getController() {
		return controller ;
	}

	public void setSimDataBase(SimulationDataBase simDataBase) {
		controller.setSimDataBase(simDataBase);
	}

	public SimulationDataBase getSimDataBase() {
		return controller.getSimDataBase() ;
	}

	@Override
	public String getName() {
		return "WelcomeTab" ;
	}

	@Override
	public void popUpTab() {
		PhoenixSimModule module = new PhoenixSimModule(controller.getSimDataBase()) ;
		module.getController().closeCurrentTabPressed(); // removing the "welcome tab"
		module.getController().addWelcomeTab();
	}

}
