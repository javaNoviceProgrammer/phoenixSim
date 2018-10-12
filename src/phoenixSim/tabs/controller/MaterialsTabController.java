package phoenixSim.tabs.controller;

import java.io.IOException;
import org.controlsfx.control.StatusBar;
import People.Meisam.GUI.Builders.WindowBuilder;
import People.Meisam.GUI.DataInput.MainGUI.DataCollectorController;
import People.Meisam.GUI.PhoenixSim.ModulesLibrary.PlotterModule.PlotterModule;
import People.Meisam.GUI.PhoenixSim.TabsLibrary.AbstractTabController;
import People.Meisam.GUI.Plotters.MatlabPlot.MatlabChart;
import People.Meisam.GUI.Utilities.SimulationDataBase;
import People.Meisam.GUI.Utilities.SimulationVariable;
import PhotonicElements.Materials.Dielectric.AbstractDielectric;
import PhotonicElements.Materials.Dielectric.Air;
import PhotonicElements.Materials.Dielectric.LiNbO3_ExtraOrdinary;
import PhotonicElements.Materials.Dielectric.LiNbO3_Ordinary;
import PhotonicElements.Materials.Dielectric.Silica;
import PhotonicElements.Materials.Dielectric.Silicon;
import PhotonicElements.Materials.Dielectric.SiliconNitride;
import PhotonicElements.Utilities.Wavelength;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

public class MaterialsTabController extends AbstractTabController {

	StatusBar statusBar = new StatusBar() ;
	SimulationDataBase simDataBase = new SimulationDataBase() ;
	MatlabChart fig = new MatlabChart() ;

	@Override
	public void setSimDataBase(SimulationDataBase simDataBase) {
		this.simDataBase = simDataBase ;
	}

	@Override
	public SimulationDataBase getSimDataBase() {
		return simDataBase;
	}

	AbstractDielectric material ;

	@FXML Tab materialsTab ;
	@FXML Pane plotPane ;
	@FXML TextField lambdaTextField ;
	@FXML Label lambdaLabel ;
	@FXML ComboBox<String> materialComboBox ;
	@FXML RadioButton nLambda ;
	@FXML RadioButton ngLambda ;
	@FXML RadioButton epsilonLambda ;
	@FXML ToggleGroup plot ;

	Button runButton ;

	@FXML
	public void initialize(){

		lambdaLabel.setText("");
		// status bar
		statusBar.setText("Dependencies:<none>");
		// adding run button
//    	javafx.scene.image.Image runImage = new javafx.scene.image.Image("/People/Meisam/GUI/Utilities/Extras/run-button.png") ;
//    	ImageView run = new ImageView(runImage) ;
//    	Button runButton = new Button("Run", run) ;
//    	runButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//    	runButton.setPrefSize(20, 20);
//    	this.statusBar.getRightItems().add(new Separator(Orientation.VERTICAL)) ;
//    	this.statusBar.getRightItems().add(runButton) ;
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        this.fig = fig ;
        showPlot(fig, plotPane);
        plotPane.getChildren().add(swingNode) ;
        // initialize material combo box
        initializeMaterials() ;
	}

	private void initializeMaterials(){
		materialComboBox.getItems().removeAll(materialComboBox.getItems()) ;
		String[] materials = {"Air", "Silicon", "SiO2", "Silicon Nitride", "LiNbO3-Ordinary", "LiNbO3-ExtraOrdinary"} ;
		materialComboBox.getItems().addAll(materials) ;
	}

    private boolean plotToggleSelected(){
        if(ngLambda.isSelected() || nLambda.isSelected() ||  epsilonLambda.isSelected()){
            return true;
        }
        else{
            return false ;
        }
    }

    @SuppressWarnings("unchecked")
	private <T extends AbstractDielectric> T getMaterial(String materialName){
    	switch(materialName){
    		case "Air": {return (T) new Air();}
    		case "Silicon": {return (T) new Silicon(); }
    		case "SiO2": {return (T) new Silica(); }
    		case "Silicon Nitride": {return (T) new SiliconNitride(); }
    		case "LiNbO3-Ordinary": {return (T) new LiNbO3_Ordinary(); }
    		case "LiNbO3-ExtraOrdinary": {return (T) new LiNbO3_ExtraOrdinary(); }
    		default : {return (T) new Air() ;}
    	}
    }

    @FXML
    private void setMaterial(){
    	String name = materialComboBox.getSelectionModel().getSelectedItem() ;
    	material = getMaterial(name) ;
        if(plotToggleSelected()){
        	calculatePlot();
        }
    }

    @FXML
    public void setLambda(){
        // textbox is used for singnle entry
        if(!lambdaTextField.getText().isEmpty()){
            double lambda_nm = Double.parseDouble(lambdaTextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("lambda_(nm)", "Wavelength (nm)", new double[]{lambda_nm}));
            lambdaLabel.setText("lambda is set to " + lambda_nm + " nm");
        }
        if(plotToggleSelected()){
            plot.getSelectedToggle().setSelected(false);
        }
    }

    public void sweepLambda() throws IOException {
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/DataInput/MainGUI/dataCollector.fxml")) ;
        WindowBuilder window = new WindowBuilder(loader) ;
        window.setIcon("/People/Meisam/GUI/DataInput/Extras/dataCollector.png");
        window.build("Data Collector", false);
        DataCollectorController controller = loader.getController() ;
        controller.initialize();
        controller.getExitButton().setOnAction(e -> {
            try {
                if(simDataBase.variableExists("lambda_(nm)")){
                    simDataBase.removeVariable(simDataBase.getVariable("lambda_(nm)"));
                }
                simDataBase.addNewVariable(new SimulationVariable("lambda_(nm)", "Wavelength (nm)", controller.getAllValues()));
                // clear text field and update label
                lambdaTextField.clear();
                lambdaLabel.setText("lambda is set to array values");
                window.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        if(plotToggleSelected()){
            plot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    private void calculatePlot(){
    	double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
    	if(nLambda.isSelected()){
    		double[] n = new double[lambda_nm.length] ;
    		for(int i=0; i<n.length; i++){
    			n[i] = material.getIndex(new Wavelength(lambda_nm[i])) ;
    		}
    		simDataBase.addNewVariable(new SimulationVariable("n_("+material.getMaterialName()+")", material.getMaterialName() + " Index", n));
    		fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("n_("+material.getMaterialName()+")")) ;
    		showPlot(fig, plotPane);
    	}
    	else if(epsilonLambda.isSelected()){
    		double[] epsilon = new double[lambda_nm.length] ;
    		for(int i=0; i<epsilon.length; i++){
    			epsilon[i] = material.getIndex(new Wavelength(lambda_nm[i])) * material.getIndex(new Wavelength(lambda_nm[i])) ;
    		}
    		simDataBase.addNewVariable(new SimulationVariable("epsilon_("+material.getMaterialName()+")", material.getMaterialName() + " Epsilon", epsilon));
    		fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("epsilon_("+material.getMaterialName()+")")) ;
    		showPlot(fig, plotPane);
    	}
    	else if(ngLambda.isSelected()){
    		double[] ng = new double[lambda_nm.length] ;
    		for(int i=0; i<ng.length; i++){
    			ng[i] = material.getGroupIndex(new Wavelength(lambda_nm[i])) ;
    		}
    		simDataBase.addNewVariable(new SimulationVariable("ng_("+material.getMaterialName()+")", material.getMaterialName() + " Group Index", ng));
    		fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("ng_("+material.getMaterialName()+")")) ;
    		showPlot(fig, plotPane);
    	}
    }

    private MatlabChart createPlot(SimulationVariable x, SimulationVariable y){
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x.getAllValues(), y.getAllValues());
        fig.RenderPlot();
        fig.xlabel(x.getAlias());
        fig.ylabel(y.getAlias());
        return fig ;
    }

    @FXML
    public void exportToMatlabPressed() throws IOException {
    	fig.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
    	new PlotterModule(simDataBase) ;
    }

	@Override
	public Tab getTab() {
		return materialsTab;
	}

	@Override
	public MatlabChart getFig() {
		return fig;
	}

	@Override
	public StatusBar getStatusBar() {
		return statusBar ;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}


}
