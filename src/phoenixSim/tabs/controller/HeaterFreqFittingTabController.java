package phoenixSim.tabs.controller;

import java.io.IOException;
import org.controlsfx.control.StatusBar;
import People.Meisam.GUI.PhoenixSim.ModulesLibrary.PlotterModule.PlotterModule;
import People.Meisam.GUI.PhoenixSim.ModulesLibrary.VariableSelectorModule.VariableSelectorModule;
import People.Meisam.GUI.PhoenixSim.TabsLibrary.AbstractTabController;
import People.Meisam.GUI.Plotters.MatlabPlot.MatlabChart;
import People.Meisam.GUI.Utilities.SimulationDataBase;
import People.Meisam.GUI.Utilities.SimulationVariable;
import People.Meisam.GUI.Utilities.Interfaces.ActionInterface;
import PhotonicElements.Heater.Model.ImpulseResponseModel.ImpulseResponse1D_Modified_new;
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.Fitter;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.Function;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.fitters.NonLinearSolver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class HeaterFreqFittingTabController extends AbstractTabController {

    SimulationDataBase simDataBase = new SimulationDataBase() ;

    public MatlabChart fig  ;
    SimulationVariable freqData = null , vData = null ;
    SimulationVariable freqFit = null , vFit = null ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab tab ;
    @FXML Pane matlabPane ;
    @FXML TextField freqDataTextField ;
    @FXML Label freqDataLabel ;
    @FXML TextField vDataTextField ;
    @FXML Label vDataLabel ;
    @FXML ListView<String> resultListView ;

	@Override
	public void initialize() {
		freqDataLabel.setText("");
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

	@SuppressWarnings("unused")
	private SimulationVariable simVarTodB(SimulationVariable var){
		double[] values = MoreMath.Arrays.Conversions.todB(var.getAllValues()) ;
		return new SimulationVariable(var.getName(), var.getAlias(), values) ;
	}

	private SimulationVariable simVarTodB_normalized(SimulationVariable var){
		double[] values = MoreMath.Arrays.Conversions.todB(var.getAllValues());
		double maxVal = MoreMath.Arrays.FindMaximum.getValue(values) ;
		double[] values_normalized = MoreMath.Arrays.plus(values, -maxVal) ;
		return new SimulationVariable(var.getName(), var.getAlias(), values_normalized) ;
	}

	@FXML
	public void setFreqData(){
		String st = freqDataTextField.getText() ;
		if(simDataBase.variableExists(st)){
			freqData = simDataBase.getVariable(st) ;
			freqDataLabel.setText("Freq data is set to '" + freqData.getName() + "'");
		}
		if(freqData != null && vData != null){
//			fig = createPlot(freqData, vData) ;
			fig = createPlot(freqData, simVarTodB_normalized(vData)) ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	public void chooseFreqData() throws IOException{
		VariableSelectorModule varModule = new VariableSelectorModule(simDataBase) ;
		ActionInterface ex = new ActionInterface() {
			@Override
			public void setExitAction() {
				freqData = new SimulationVariable(varModule.getController().getVariable().getName(), varModule.getController().getVariable().getAlias(), varModule.getController().getValues()) ;
				freqDataLabel.setText("Freq data is set to '" + freqData.getName() + "'");
				if(freqData != null && vData != null){
//					fig = createPlot(freqData, vData) ;
					fig = createPlot(freqData, simVarTodB_normalized(vData)) ;
					showPlot(fig, matlabPane);
				}
			}
		};
		varModule.setExitAction(ex);
	}

	@FXML
	public void setVData(){
		String st = vDataTextField.getText() ;
		if(simDataBase.variableExists(st)){
			vData = simDataBase.getVariable(st) ;
			vDataLabel.setText("V data is set to '" + vData.getName() + "'");
		}
		if(freqData != null && vData != null){
//			fig = createPlot(freqData, vData) ;
			fig = createPlot(freqData, simVarTodB_normalized(vData)) ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	public void chooseVData(){
		VariableSelectorModule varModule = new VariableSelectorModule(simDataBase) ;
		varModule.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
				vData = new SimulationVariable(varModule.getController().getVariable().getName(), varModule.getController().getVariable().getAlias(), varModule.getController().getValues()) ;
				vDataLabel.setText("V data is set to '" + vData.getName() + "'");
				if(freqData != null && vData != null){
//					fig = createPlot(freqData, vData) ;
					fig = createPlot(freqData, simVarTodB_normalized(vData)) ;
					showPlot(fig, matlabPane);
				}
			}
		});
	}

	@FXML
	public void calculate(){
//		fig = createPlot(freqData, simVarTodB_normalized(vData)) ;
//		showPlot(fig, matlabPane);

		double[] freq_kHz = freqData.getAllValues() ;
		double[] voltage_V = vData.getAllValues() ;
		double[] voltage_V_normalized = MoreMath.Arrays.times(voltage_V, 1/MoreMath.Arrays.FindMaximum.getValue(voltage_V)) ;
		double[][] freqValues_kHz = new double[freq_kHz.length][1] ;
		for(int i=0; i<freq_kHz.length; i++){
			freqValues_kHz[i][0] = freq_kHz[i] ;
		}

		// normalized frequency response
		Function freqResponse = new Function(){
			@Override
			public double evaluate(double[] values, double[] parameters) {
				double f0 = parameters[0] ; // kHz units
				double nu = parameters[1] ; // no units
				double freqKHz = values[0] ; // kHz, only one variable
				ImpulseResponse1D_Modified_new impulseResponse = new ImpulseResponse1D_Modified_new(f0, nu) ;
				double voltageResponse = impulseResponse.getFreqResponse(freqKHz*1e3)*nu/(nu-1) ;
				return voltageResponse ;
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

		Fitter fit = new NonLinearSolver(freqResponse) ;
//		Fitter fit = new MarquardtFitter(freqResponse) ;
		fit.setData(freqValues_kHz, voltage_V_normalized);
		fit.setParameters(new double[]{100, 10});
		fit.fitData();

		double f0_kHz = fit.getParameters()[0] ;
		double nu = fit.getParameters()[1] ;
		simDataBase.addNewVariable(new SimulationVariable("f0_(kHz)", "f0 (kHz)", new double[]{f0_kHz}));
		simDataBase.addNewVariable(new SimulationVariable("nu_()", "nu", new double[]{nu}));

		resultListView.getItems().removeAll(resultListView.getItems()) ;
		resultListView.getItems().add("f0 (kHz) = " + String.format("%2.4f", f0_kHz)) ;
		resultListView.getItems().add("v = " + String.format("%2.4f", nu)) ;

		ImpulseResponse1D_Modified_new impulseResponse = new ImpulseResponse1D_Modified_new(f0_kHz, nu) ;

		double[] freq_values_kHz = MoreMath.linspace(MoreMath.Arrays.FindMinimum.getValue(freq_kHz), MoreMath.Arrays.FindMaximum.getValue(freq_kHz), 1000) ;
		double[] V_values = new double[freq_values_kHz.length] ;
		V_values = impulseResponse.getFreqResponse(MoreMath.Arrays.times(freq_values_kHz, 1e3)) ;

		freqFit = new SimulationVariable("freq_fit_(kHz)", "Fitted Freq (kHz)", freq_values_kHz) ;
		vFit = new SimulationVariable("V_fit_(V)", "Fitted Voltage (V)", V_values) ;
		simDataBase.addNewVariable(freqFit);
		simDataBase.addNewVariable(vFit);

		fig.plot(freqFit.getAllValues(), simVarTodB_normalized(vFit).getAllValues(), "r", 3f);
		fig.RenderPlot();
		fig.xlabel("Frequency (kHz)");
		fig.ylabel("Voltage Response (dB-volt)");
		fig.setXAxis_to_Log();
		showPlot(fig, matlabPane);
	}

    private MatlabChart createPlot(SimulationVariable x, SimulationVariable y){
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x.getAllValues(), y.getAllValues());
        fig.RenderPlot();
        fig.xlabel("Frequency (kHz)");
        fig.ylabel("Voltage Response (dB-volt)");
        fig.setXAxis_to_Log();
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
