package phoenixSim.tabs.tab;

import java.io.IOException;
import org.controlsfx.control.StatusBar;
import People.Meisam.GUI.PhoenixSim.MainModule.PhoenixSimModule;
import People.Meisam.GUI.PhoenixSim.TabsLibrary.AbstractTab;
import People.Meisam.GUI.Utilities.SimulationDataBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

public class HeaterFreqTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/TabsLibrary/Heater/HeaterFreq/heater_freq.fxml")) ; ;
	HeaterFreqTabController controller ;

	public HeaterFreqTab(SimulationDataBase simDataBase){
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
	public HeaterFreqTabController getController(){
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
		return "HeaterFreqTab" ;
	}

	@Override
	public void popUpTab() {
		PhoenixSimModule module = new PhoenixSimModule(controller.getSimDataBase()) ;
		module.getController().closeCurrentTabPressed(); // removing the "welcome tab"
		module.getController().addHeaterFreqTab();
	}

}
