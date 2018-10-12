package phoenixSim.tabs.tab;

import java.io.IOException;
import org.controlsfx.control.StatusBar;
import People.Meisam.GUI.PhoenixSim.MainModule.PhoenixSimModule;
import People.Meisam.GUI.PhoenixSim.TabsLibrary.AbstractTab;
import People.Meisam.GUI.Utilities.SimulationDataBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

public class DownSamplingTab extends AbstractTab {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/TabsLibrary/Measurements/DownSampling/down_sampling.fxml")) ;
	DownSamplingTabController controller ;

	public DownSamplingTab(SimulationDataBase simDataBase){
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
	public DownSamplingTabController getController(){
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
		return "DownSamplingTab" ;
	}

	@Override
	public void popUpTab() {
		PhoenixSimModule module = new PhoenixSimModule(controller.getSimDataBase(), false) ;
		module.getController().addDownSamplingTab() ;
	}

}
