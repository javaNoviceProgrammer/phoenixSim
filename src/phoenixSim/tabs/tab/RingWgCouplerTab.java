package phoenixSim.tabs.tab;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import phoenixSim.gui.PhoenixSimModule;
import phoenixSim.tabs.AbstractTab;
import phoenixSim.tabs.controller.RingWgCouplerTabController;
import phoenixSim.util.SimulationDataBase;

public class RingWgCouplerTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/TabsLibrary/CouplerDesigner/RingWgCoupler/ring_wg_coupler.fxml")) ; ;
	RingWgCouplerTabController controller ;

	public RingWgCouplerTab(SimulationDataBase simDataBase){
		try {
			loader.load() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = loader.getController()  ;
		controller.setSimDataBase(simDataBase);
//		controller.initialize();
	}

	public Tab getTab(){
		return controller.getTab() ;
	}

	public StatusBar getStatusBar(){
		return controller.getStatusBar() ;
	}

	@SuppressWarnings("unchecked")
	public RingWgCouplerTabController getController(){
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
		return "RingWgCouplerTab" ;
	}

	@Override
	public void popUpTab() {
		PhoenixSimModule module = new PhoenixSimModule(controller.getSimDataBase(), false) ;
		module.getController().addRingWgCouplerTab();
	}
}
