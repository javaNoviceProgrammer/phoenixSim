package phoenixSim.tabs.tab;

import java.io.IOException;
import org.controlsfx.control.StatusBar;
import People.Meisam.GUI.PhoenixSim.MainModule.PhoenixSimModule;
import People.Meisam.GUI.PhoenixSim.TabsLibrary.AbstractTab;
import People.Meisam.GUI.Utilities.SimulationDataBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

public class MaterialsTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/TabsLibrary/Materials/materials.fxml")) ;
//	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/TabsLibrary/Materials/materials_new.fxml")) ;
	MaterialsTabController controller ;

	public MaterialsTab(SimulationDataBase simDataBase){
		try {
			loader.load() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = loader.getController()  ;
		controller.setSimDataBase(simDataBase);
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
	public MaterialsTabController getController() {
		return controller;
	}

	@Override
	public String getName() {
		return "MaterialsTab" ;
	}

	@Override
	public void popUpTab() {
		PhoenixSimModule module = new PhoenixSimModule(controller.getSimDataBase()) ;
		module.getController().closeCurrentTabPressed(); // removing the "welcome tab"
		module.getController().addMaterialsTab();
	}


}
