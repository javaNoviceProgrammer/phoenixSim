package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import People.Meisam.GUI.Builders.WindowBuilder;
import People.Meisam.GUI.PhoenixSim.ModulesLibrary.PlotterModule.PlotterModule;
import People.Meisam.GUI.PhoenixSim.TabsLibrary.AbstractTabController;
import People.Meisam.GUI.Plotters.MatlabPlot.MatlabChart;
import People.Meisam.GUI.Utilities.SimulationDataBase;
import People.Meisam.GUI.Utilities.SimulationVariable;
import People.Meisam.GUI.Utilities.VariableSelector.VariableSelectorController;
import PhotonicElements.PNJunction.PINDiode.PINModelAC;
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.Fitter;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.Function;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.fitters.MarquardtFitter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class PINacFittingTabController extends AbstractTabController {

    // defining simulation database and the figures in the simulation
    SimulationDataBase simDataBase = new SimulationDataBase() ;

    public MatlabChart fig  ;
    SimulationVariable iData = null , vData = null ;
    SimulationVariable iFit = null , vFit = null ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab tab ;
    @FXML Pane matlabPane ;
    @FXML TextField iDataTextField ;
    @FXML Label iDataLabel ;
    @FXML TextField vDataTextField ;
    @FXML Label vDataLabel ;
    @FXML ListView<String> resultListView ;

	@Override
	public void initialize() {
		iDataLabel.setText("");
		vDataLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        this.fig = fig ;
        showPlot(fig, matlabPane);
        matlabPane.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<none>");
	}

	@FXML
	public void setIData(){
		String st = iDataTextField.getText() ;
		if(simDataBase.variableExists(st)){
			iData = simDataBase.getVariable(st) ;
			iDataLabel.setText("freq data is set to '" + iData.getName() + "'");
		}
		if(iData != null && vData != null){
			fig = createPlot(iData, vData) ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	public void chooseIData() throws IOException{
		FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/VariableSelector/variable_selector.fxml")) ;
		WindowBuilder varSelect = new WindowBuilder(loader) ;
		varSelect.setIcon("/People/Meisam/GUI/Utilities/VariableSelector/Extras/icon.png");
		varSelect.build("Select Variable & Values", false);
		VariableSelectorController controller = loader.getController() ;
		controller.setSimDataBase(simDataBase);
		controller.initialize();
		controller.getSetExitButton().setOnAction(e -> {
			iData = new SimulationVariable(controller.getVariable().getName(), controller.getVariable().getAlias(), controller.getValues()) ;
			iDataLabel.setText("freq data is set to '" + iData.getName() + "'");
			controller.getSetExitButton().getScene().getWindow().hide();
			if(iData != null && vData != null){
				fig = createPlot(iData, vData) ;
				showPlot(fig, matlabPane);
			}
		});
	}

	@FXML
	public void setVData(){
		String st = vDataTextField.getText() ;
		if(simDataBase.variableExists(st)){
			vData = simDataBase.getVariable(st) ;
			vDataLabel.setText("ZL data is set to '" + vData.getName() + "'");
		}
		if(iData != null && vData != null){
			fig = createPlot(iData, vData) ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	public void chooseVData() throws IOException{
		FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/VariableSelector/variable_selector.fxml")) ;
		WindowBuilder varSelect = new WindowBuilder(loader) ;
		varSelect.setIcon("/People/Meisam/GUI/Utilities/VariableSelector/Extras/icon.png");
		varSelect.build("Select Variable & Values", false);
		VariableSelectorController controller = loader.getController() ;
		controller.setSimDataBase(simDataBase);
		controller.initialize();
		controller.getSetExitButton().setOnAction(e -> {
			vData = new SimulationVariable(controller.getVariable().getName(), controller.getVariable().getAlias(), controller.getValues()) ;
			vDataLabel.setText("ZL data is set to '" + vData.getName() + "'");
			controller.getSetExitButton().getScene().getWindow().hide();
			if(iData != null && vData != null){
				fig = createPlot(iData, vData) ;
				showPlot(fig, matlabPane);
			}
		});
	}

	@FXML
	public void calculate(){
		double[] freq_Ghz = iData.getAllValues() ;
		double[] ZL_ohm = vData.getAllValues() ;
		double[][] freqGhz = new double[freq_Ghz.length][1] ;
		for(int i=0; i<freq_Ghz.length; i++){
			freqGhz[i][0] = freq_Ghz[i] ;
		}

		Function ZL = new Function(){
			@Override
			public double evaluate(double[] values, double[] parameters) {
				double RD_ohm = parameters[0] ; 
				double CD_pF = parameters[1] ; 
				double Rs2_ohm = parameters[2] ; 
				double Cox_pF = parameters[3] ; 
				double Rs1_ohm = parameters[4] ; 
				double Cp_fF = parameters[5] ; 
				double freq_Ghz = values[0] ; // only one variable
				PINModelAC pinModelAC = new PINModelAC(RD_ohm, CD_pF, Rs2_ohm, Cox_pF, Rs1_ohm, Cp_fF) ;
				double ZL_amp = pinModelAC.getZL(freq_Ghz).abs() ;
				return ZL_amp ;
			}
			@Override
			public int getNParameters() {
				return 6;
			}
			@Override
			public int getNInputs() {
				return 1;
			}
		} ;

//		Fitter fit = new NonLinearSolver(ZL) ;
		Fitter fit = new MarquardtFitter(ZL) ;
		fit.setData(freqGhz, ZL_ohm);
		fit.setParameters(new double[]{25, 6, 64, 0.2, 100, 100});
		fit.fitData();

		double RD_ohm = fit.getParameters()[0] ;
		double CD_pF = fit.getParameters()[1] ;
		double Rs2_ohm = fit.getParameters()[2] ;
		double Cox_pF = fit.getParameters()[3] ;
		double Rs1_ohm = fit.getParameters()[4] ;
		double Cp_fF = fit.getParameters()[5] ;
		simDataBase.addNewVariable(new SimulationVariable("RD-fit", "RD (Ohm)", new double[]{RD_ohm}));
		simDataBase.addNewVariable(new SimulationVariable("CD-fit", "CD (pF)", new double[]{CD_pF}));
		simDataBase.addNewVariable(new SimulationVariable("Rs2-fit", "Rs2 (Ohm)", new double[]{Rs2_ohm}));
		simDataBase.addNewVariable(new SimulationVariable("Cox-fit", "Cox (pF)", new double[]{Cox_pF}));
		simDataBase.addNewVariable(new SimulationVariable("Rs1-fit", "Rs1 (Ohm)", new double[]{Rs1_ohm}));
		simDataBase.addNewVariable(new SimulationVariable("Cp-fit", "Cp (fF)", new double[]{Cp_fF}));

		resultListView.getItems().removeAll(resultListView.getItems()) ;
		resultListView.getItems().add("RD (ohm) = " + String.format("%2.4f", RD_ohm)) ;
		resultListView.getItems().add("CD (pF) = " + String.format("%2.4f", CD_pF)) ;
		resultListView.getItems().add("Rs2 (ohm) = " + String.format("%2.4f", Rs2_ohm)) ;
		resultListView.getItems().add("Cox (pF) = " + String.format("%2.4f", Cox_pF)) ;
		resultListView.getItems().add("Rs1 (ohm) = " + String.format("%2.4f", Rs1_ohm)) ;
		resultListView.getItems().add("Cp (fF) = " + String.format("%2.4f", Cp_fF)) ;

		double[] freq_values = MoreMath.linspace(MoreMath.Arrays.FindMinimum.getValue(freq_Ghz), MoreMath.Arrays.FindMaximum.getValue(freq_Ghz), 1000) ;
		double[] ZL_values = new double[freq_values.length] ;
		PINModelAC pinModelAC = new PINModelAC(RD_ohm, CD_pF, Rs2_ohm, Cox_pF, Rs1_ohm, Cp_fF) ;
		for(int i=0; i<freq_values.length; i++){
			ZL_values[i] = pinModelAC.getZL(freq_values[i]).abs() ;
		}

		iFit = new SimulationVariable("freq_fit_(Ghz)", "Fitted Freq (Ghz)", freq_values) ;
		vFit = new SimulationVariable("ZL_fit_(Ohm)", "Fitted ZL (Ohm)", ZL_values) ;
		simDataBase.addNewVariable(iFit);
		simDataBase.addNewVariable(vFit);

		fig.plot(iFit.getAllValues(), vFit.getAllValues(), "r", 3f);
		fig.RenderPlot();
		fig.xlabel("Freq (Ghz)");
		fig.ylabel("ZL (Ohm)");
		showPlot(fig, matlabPane);
	}

    private MatlabChart createPlot(SimulationVariable x, SimulationVariable y){
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x.getAllValues(), y.getAllValues());
        fig.RenderPlot();
		fig.xlabel("Freq (Ghz)");
		fig.ylabel("ZL (Ohm)");
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
		return tab;
	}

	@Override
	public MatlabChart getFig() {
		return fig;
	}

	@Override
	public StatusBar getStatusBar() {
		return statusBar;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}

}