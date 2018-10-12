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
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.Fitter;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.Function;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.fitters.NonLinearSolver;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class HeaterDCFittingTabController extends AbstractTabController {

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
		double[][] V_volt = new double[voltage_V.length][1] ;
		for(int i=0; i<voltage_V.length; i++){
			V_volt[i][0] = voltage_V[i] ;
		}

		Function I = new Function(){
			@Override
			public double evaluate(double[] values, double[] parameters) {
				double Kv = parameters[0] ; // 1/V^2 units
				double Rlinear = parameters[1] ; // kOhm units
				double voltage = values[0] ; // only one variable
				double current = voltage/Rlinear *2/(1+Math.sqrt(1+Kv*voltage*voltage)) ; // mA units
				return current ;
			}
			@Override
			public int getNParameters() {
				return 2;
			}
			@Override
			public int getNInputs() {
				return 1;
			}
		} ;

		Fitter fit = new NonLinearSolver(I) ;
//		Fitter fit = new MarquardtFitter(I) ;
		fit.setData(V_volt, current_mA);
		fit.setParameters(new double[]{0.1, 0.1});
		fit.fitData();

		double Kv = fit.getParameters()[0] ;
		double Rlinear_kOhm = fit.getParameters()[1] ;
		simDataBase.addNewVariable(new SimulationVariable("Kv-fit", "Kv (1/V^2)", new double[]{Kv}));
		simDataBase.addNewVariable(new SimulationVariable("Rlinear-fit", "Rlinear (Ohm)", new double[]{Rlinear_kOhm*1e3}));

		resultListView.getItems().removeAll(resultListView.getItems()) ;
		resultListView.getItems().add("Kv = " + String.format("%2.4f", Kv)) ;
		resultListView.getItems().add("Rlinear (Ohm) = " + String.format("%2.4f", Rlinear_kOhm*1e3)) ;

		double[] V_values = MoreMath.linspace(MoreMath.Arrays.FindMinimum.getValue(voltage_V), MoreMath.Arrays.FindMaximum.getValue(voltage_V), 1000) ;
		double[] I_values = new double[V_values.length] ;
		for(int i=0; i<V_values.length; i++){
			I_values[i] = V_values[i]/Rlinear_kOhm * 2/(1+Math.sqrt(1+Kv*V_values[i]*V_values[i])) ;
		}

		iFit = new SimulationVariable("I_fit_(mA)", "Fitted Current (mA)", I_values) ;
		vFit = new SimulationVariable("V_fit_(V)", "Fitted Voltage (V)", V_values) ;
		simDataBase.addNewVariable(iFit);
		simDataBase.addNewVariable(vFit);

		fig.plot(iFit.getAllValues(), vFit.getAllValues(), "r", 3f);
		fig.RenderPlot();
		fig.xlabel("Current (mA)");
		fig.ylabel("Voltage (V)");
		showPlot(fig, matlabPane);
	}

    private MatlabChart createPlot(SimulationVariable x, SimulationVariable y){
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x.getAllValues(), y.getAllValues());
        fig.RenderPlot();
        fig.xlabel("Current (mA)");
        fig.ylabel("Voltage (V)");
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
