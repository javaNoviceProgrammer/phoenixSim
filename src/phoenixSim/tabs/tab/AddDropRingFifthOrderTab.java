package phoenixSim.tabs.tab;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import phoenixSim.gui.PhoenixSimModule;
import phoenixSim.tabs.AbstractTab;
import phoenixSim.tabs.controller.AddDropRingFifthOrderTabController;
import phoenixSim.util.SimulationDataBase;

public class AddDropRingFifthOrderTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/TabsLibrary/RingResonator/AddDropFifthOrder/add_drop_ring_fifth_order.fxml")) ;
	AddDropRingFifthOrderTabController controller ;

	public AddDropRingFifthOrderTab(SimulationDataBase simDataBase){
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
	public AddDropRingFifthOrderTabController getController(){
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
		return "AddDropRingFifthOrderTab" ;
	}

	@Override
	public void popUpTab() {
		PhoenixSimModule module = new PhoenixSimModule(controller.getSimDataBase(), false) ;
		module.getController().addAddDropRingFifthhOrderTab();
	}

}
