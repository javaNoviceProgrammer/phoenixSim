package phoenixSim.tabs.tab;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import phoenixSim.gui.PhoenixSimModule;
import phoenixSim.tabs.AbstractTab;
import phoenixSim.tabs.controller.StripWgCoupledModesSiNTabController;
import phoenixSim.util.SimulationDataBase;

public class StripWgCoupledModesSiNTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/TabsLibrary/StripWg/silicon_nitride/coupled_modes/strip_wg_coupled_modes_SiN.fxml")) ;
	StripWgCoupledModesSiNTabController controller ;

	public StripWgCoupledModesSiNTab(SimulationDataBase simDataBase){
		try {
			loader.load() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = loader.getController()  ;
		controller.setSimDataBase(simDataBase);
	}

	public Tab getTab(){
		return controller.getTab() ;
	}

	public StatusBar getStatusBar(){
		return controller.getStatusBar() ;
	}

	@SuppressWarnings("unchecked")
	public StripWgCoupledModesSiNTabController getController(){
		return controller ;
	}

	public void setSimDataBase(SimulationDataBase simDataBase){
		controller.setSimDataBase(simDataBase);
	}

	public SimulationDataBase getSimDataBase(){
		return controller.getSimDataBase() ;
	}

	@Override
	public String getName() {
		return "StripWgCoupledModesSiNTab" ;
	}

	@Override
	public void popUpTab() {
		PhoenixSimModule module = new PhoenixSimModule(controller.getSimDataBase()) ;
		module.getController().closeCurrentTabPressed(); // removing the "welcome tab"
		module.getController().addStripWgCoupledModesSiNTab();
	}

}
