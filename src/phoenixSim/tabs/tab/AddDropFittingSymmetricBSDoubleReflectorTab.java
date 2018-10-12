package phoenixSim.tabs.tab;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import phoenixSim.gui.PhoenixSimModule;
import phoenixSim.tabs.AbstractTab;
import phoenixSim.tabs.controller.AddDropFittingSymmetricBSDoubleReflectorTabController;
import phoenixSim.util.SimulationDataBase;

public class AddDropFittingSymmetricBSDoubleReflectorTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/TabsLibrary/Measurements/AddDropFitting/SymmetricDropBackScatteringDoubleReflector/add_drop_fitting_symmetric_BS_double_reflector.fxml")) ; ;
	AddDropFittingSymmetricBSDoubleReflectorTabController controller ;

	public AddDropFittingSymmetricBSDoubleReflectorTab(SimulationDataBase simDataBase){
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
	public AddDropFittingSymmetricBSDoubleReflectorTabController getController(){
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
		return "AddDropFittingSymmetricBSDoubleReflectorTab" ;
	}

	@Override
	public void popUpTab() {
		PhoenixSimModule module = new PhoenixSimModule(controller.getSimDataBase(), false) ;
		module.getController().addAddDropFittingSymmetricBSDoubleReflectorTab() ;
	}

}
