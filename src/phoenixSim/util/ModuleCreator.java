package phoenixSim.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import phoenixSim.builder.WindowBuilder;
import phoenixSim.gui.PhoenixSimModuleController;

public class ModuleCreator {

	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/stand_alone_module.fxml")) ; ;
	PhoenixSimModuleController controller ;
	SimulationDataBase simDataBase ;

	public ModuleCreator(){
		simDataBase = null ;
	}

	public ModuleCreator(SimulationDataBase simDataBase){
		this.simDataBase = simDataBase ;
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

	// now creating different modules

	public void createCouplerDesigner(){
		// loading the GUI
		WindowBuilder builder = new WindowBuilder(loader) ;
		builder.setIcon("/phoenixSim/extras/coupler_designer_logo.png");
		try {
			builder.build_NoModality("Coupler Designer v1.0", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = loader.getController()  ;
		if(!(simDataBase == null)){
			controller.setSimDataBase(simDataBase);
		}
		// adding necessary tabs
		controller.addCouplerDesignerWelcomeTab();
		controller.addStripWgModesTab();
		controller.addStripWgCoupledModesTab();
		controller.addWgWgCouplerTab();
		controller.addRingWgCouplerTab();
		controller.addRingRingCouplerTab();
		// select welcome tab
		controller.tabs.getSelectionModel().select(0);
		// set close policy for clearing JRE from memory
		if(simDataBase == null){
			controller.closeApp.setOnAction(e -> {
				System.exit(1);
			});
			controller.tabs.getScene().getWindow().setOnCloseRequest(e -> {
				System.exit(1);
			});
		}
	}

	public void createThermalAnalyzer(){
		// loading the GUI
		WindowBuilder builder = new WindowBuilder(loader) ;
		builder.setIcon("/phoenixSim/extras/PhoenixSim_logo.png");
		try {
			builder.build_NoModality("Thermo-Optic Analyzer v1.0", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = loader.getController()  ;
		if(!(simDataBase == null)){
			controller.setSimDataBase(simDataBase);
		}
		// adding necessary tabs
		controller.addHeaterWelcomeTab();
		controller.addHeaterDCTab();
		controller.addHeaterFreqTab();
		controller.addHeaterImpulseTab();
		controller.addHeaterStepTab();
		controller.addHeaterSinTab();
		controller.addHeaterPWMTab();
		// select welcome tab
		controller.tabs.getSelectionModel().select(0);
		// set close policy for clearing JRE from memory
		if(simDataBase == null){
			controller.closeApp.setOnAction(e -> {
				System.exit(1);
			});
			controller.tabs.getScene().getWindow().setOnCloseRequest(e -> {
				System.exit(1);
			});
		}
	}

	public void createAddDropFitting(){
		// loading the GUI
		WindowBuilder builder = new WindowBuilder(loader) ;
		builder.setIcon("/phoenixSim/extras/PhoenixSim_logo.png");
		try {
			builder.build_NoModality("Add Drop Fitting Module", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = loader.getController()  ;
		if(!(simDataBase == null)){
			controller.setSimDataBase(simDataBase);
		}
		// adding necessary tabs
		controller.addWelcomeTab();
		controller.addAddDropFittingSymmetricTab();
		// select welcome tab
		controller.tabs.getSelectionModel().select(0);
		// set close policy for clearing JRE from memory
		if(simDataBase == null){
			controller.closeApp.setOnAction(e -> {
				System.exit(1);
			});
			controller.tabs.getScene().getWindow().setOnCloseRequest(e -> {
				System.exit(1);
			});
		}
	}



}
