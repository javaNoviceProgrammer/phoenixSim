package phoenixSim.tabs.tab;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import phoenixSim.gui.PhoenixSimModule;
import phoenixSim.tabs.AbstractTab;
import phoenixSim.tabs.controller.SlabWgModeProfileTabController;
import phoenixSim.util.SimulationDataBase;

public class SlabWgModeProfileTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/TabsLibrary/SlabWg/SlabWgModeProfile/slab_wg_mode_profile.fxml")) ; ;
	SlabWgModeProfileTabController controller ;

	public SlabWgModeProfileTab(SimulationDataBase simDataBase){
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
	public SlabWgModeProfileTabController getController() {
		return controller;
	}

	@Override
	public String getName() {
		return "SlabWgModeProfileTab";
	}

	@Override
	public void popUpTab() {
		PhoenixSimModule module = new PhoenixSimModule(controller.getSimDataBase()) ;
		module.getController().closeCurrentTabPressed(); // removing the "welcome tab"
		module.getController().addSlabWgModeProfileTab();
	}



}
