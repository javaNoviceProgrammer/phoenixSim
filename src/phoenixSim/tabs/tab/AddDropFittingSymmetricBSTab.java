package phoenixSim.tabs.tab;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import phoenixSim.gui.PhoenixSimModule;
import phoenixSim.tabs.AbstractTab;
import phoenixSim.tabs.controller.AddDropFittingSymmetricBSTabController;
import phoenixSim.util.SimulationDataBase;

public class AddDropFittingSymmetricBSTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/TabsLibrary/Measurements/AddDropFitting/SymmetricDropBackScattering/add_drop_fitting_symmetric_BS.fxml")) ; ;
	AddDropFittingSymmetricBSTabController controller ;

	public AddDropFittingSymmetricBSTab(SimulationDataBase simDataBase){
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
	public AddDropFittingSymmetricBSTabController getController(){
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
		return "AddDropFittingSymmetricBSTab" ;
	}

	@Override
	public void popUpTab() {
		PhoenixSimModule module = new PhoenixSimModule(controller.getSimDataBase(), false) ;
		module.getController().addAddDropFittingSymmetricTab();
	}

}
