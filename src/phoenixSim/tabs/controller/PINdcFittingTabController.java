package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import PhotonicElements.PNJunction.PINDiode.PINModelDC;
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.Fitter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import mathLib.fitting.lmse.MarquardtFitter;
import mathLib.plot.MatlabChart;
import phoenixSim.modules.PlotterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import phoenixSim.util.VariableSelectorController;

public class PINdcFittingTabController extends AbstractTabController {

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
        fig.renderPlot();
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
			iDataLabel.setText("I data is set to '" + iData.getName() + "'");
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
			iDataLabel.setText("I data is set to '" + iData.getName() + "'");
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
			vDataLabel.setText("V data is set to '" + vData.getName() + "'");
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
			vDataLabel.setText("V data is set to '" + vData.getName() + "'");
			controller.getSetExitButton().getScene().getWindow().hide();
			if(iData != null && vData != null){
				fig = createPlot(iData, vData) ;
				showPlot(fig, matlabPane);
			}
		});
	}

	@FXML
	public void calculate(){
		double[] current_mA = iData.getAllValues() ;
		double[] voltage_V = vData.getAllValues() ;
		double[][] I_mA = new double[current_mA.length][1] ;
		for(int i=0; i<current_mA.length; i++){
			I_mA[i][0] = current_mA[i] ;
		}

		Function V = new Function(){
			@Override
			public double evaluate(double[] values, double[] parameters) {
				double Vbi_V = parameters[0] ; // Vbi
				double R_KOhm = parameters[1] ; // kOhm units
				double Is_nA = parameters[2] ; // nA units
				double n = parameters[3] ; // nA units
				double I_mA = values[0] ; // only one variable
				PINModelDC pinModelDC = new PINModelDC(Vbi_V, R_KOhm, Is_nA, n) ;
				double voltage_V = pinModelDC.getVoltage(I_mA) ;
				return voltage_V ;
			}
			@Override
			public int getNParameters() {
				return 4;
			}
			@Override
			public int getNInputs() {
				return 1;
			}
		} ;

//		Fitter fit = new NonLinearSolver(V) ;
		Fitter fit = new MarquardtFitter(V) ;
		fit.setData(I_mA, voltage_V);
		fit.setParameters(new double[]{0.7, 0.5, 90, 0.7});
		fit.fitData();

		double Vbi_V = fit.getParameters()[0] ;
		double R_kOhm = fit.getParameters()[1] ;
		double Is_nA = fit.getParameters()[2] ;
		double n = fit.getParameters()[3] ;
		simDataBase.addNewVariable(new SimulationVariable("Vbi-fit", "Vbi (V)", new double[]{Vbi_V}));
		simDataBase.addNewVariable(new SimulationVariable("R-fit", "R (KOhm)", new double[]{R_kOhm}));
		simDataBase.addNewVariable(new SimulationVariable("Is-fit", "Is (nA)", new double[]{Is_nA}));
		simDataBase.addNewVariable(new SimulationVariable("n-fit", "n ()", new double[]{n}));

		resultListView.getItems().removeAll(resultListView.getItems()) ;
		resultListView.getItems().add("Vbi = " + String.format("%2.4f", Vbi_V)) ;
		resultListView.getItems().add("R (KOhm) = " + String.format("%2.4f", R_kOhm)) ;
		resultListView.getItems().add("Is (nA) = " + String.format("%2.4f", Is_nA)) ;
		resultListView.getItems().add("n = " + String.format("%2.4f", n)) ;

		double[] V_values = MoreMath.linspace(MoreMath.Arrays.FindMinimum.getValue(voltage_V), MoreMath.Arrays.FindMaximum.getValue(voltage_V), 1000) ;
		double[] I_values = new double[V_values.length] ;
		PINModelDC pinModelDC = new PINModelDC(Vbi_V, R_kOhm, Is_nA, n) ;
		for(int i=0; i<I_values.length; i++){
			I_values[i] = pinModelDC.getCurrent(V_values[i]) ;
		}

		iFit = new SimulationVariable("I_fit_(mA)", "Fitted Current (mA)", I_values) ;
		vFit = new SimulationVariable("V_fit_(V)", "Fitted Voltage (V)", V_values) ;
		simDataBase.addNewVariable(iFit);
		simDataBase.addNewVariable(vFit);

		fig.plot(vFit.getAllValues(), iFit.getAllValues(), "r", 3f);
		fig.renderPlot();
		fig.ylabel("Current (mA)");
		fig.xlabel("Voltage (V)");
		showPlot(fig, matlabPane);
	}

    private MatlabChart createPlot(SimulationVariable x, SimulationVariable y){
        MatlabChart fig = new MatlabChart() ;
        fig.plot(y.getAllValues(), x.getAllValues());
        fig.renderPlot();
        fig.ylabel("Current (mA)");
        fig.xlabel("Voltage (V)");
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
