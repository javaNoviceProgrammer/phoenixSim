package phoenixSim.gui.full_screen;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import phoenixSim.builder.WindowBuilder;
import phoenixSim.util.SimulationDataBase;

public class PhoenixSimModule {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/PhoenixSim/MainModule/FullScreenMode/phoenixSim_module_fullScreen.fxml")) ; ;
	PhoenixSimModuleController controller ;

	// this constructor is used if database is not shared
	public PhoenixSimModule(){
		WindowBuilder builder = new WindowBuilder(loader) ;
		builder.setIcon("/People/Meisam/GUI/ThermalHeater/Extras/PhoenixSim_logo.png");
		try {
			builder.build_NoModality("PhoenixSim v1.0", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = loader.getController()  ;
		controller.addWelcomeTab();
        controller.fullScreenMode.selectedProperty().addListener((v, ov, nv)->{
        	builder.getStage().setResizable(nv);
        });
	}

	// this constructor is used if database is shared between modules
	public PhoenixSimModule(SimulationDataBase simDataBase){
		WindowBuilder builder = new WindowBuilder(loader) ;
		builder.setIcon("/People/Meisam/GUI/ThermalHeater/Extras/PhoenixSim_logo.png");
		try {
			builder.build_NoModality("PhoenixSim v1.0", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = loader.getController()  ;
		controller.setSimDataBase(simDataBase);
		controller.addWelcomeTab();
        controller.fullScreenMode.selectedProperty().addListener((v, ov, nv)->{
        	builder.getStage().setResizable(nv);
        });
	}

	public PhoenixSimModule(SimulationDataBase simDataBase, boolean addWelcomeTab){
		WindowBuilder builder = new WindowBuilder(loader) ;
		builder.setIcon("/People/Meisam/GUI/ThermalHeater/Extras/PhoenixSim_logo.png");
		try {
			builder.build_NoModality("PhoenixSim v1.0", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = loader.getController()  ;
		controller.setSimDataBase(simDataBase);
		if(addWelcomeTab){
			controller.addWelcomeTab();
		}
        controller.fullScreenMode.selectedProperty().addListener((v, ov, nv)->{
        	builder.getStage().setResizable(nv);
        });
	}

	public Tab getSelectedTab(){
		return controller.tabs.getSelectionModel().getSelectedItem() ;
	}

	public PhoenixSimModuleController getController(){
		return controller ;
	}

	public void setSimDataBase(SimulationDataBase simDataBase){
		controller.setSimDataBase(simDataBase);
	}

	public SimulationDataBase getSimDataBase(){
		return controller.getSimDataBase() ;
	}

}
