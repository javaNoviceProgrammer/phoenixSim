package phoenixSim.tabs.controller;

import java.io.IOException;

import org.apache.commons.math3.special.Erf;
import org.controlsfx.control.StatusBar;

import People.Meisam.GUI.Utilities.Interfaces.ActionInterface;
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.Fitter;
import flanagan.interpolation.LinearInterpolation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import mathLib.fitting.lmse.MarquardtFitter;
import mathLib.plot.MatlabChart;
import phoenixSim.modules.PlotterModule;
import phoenixSim.modules.VariableSelectorModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;

public class BERPenaltyTabController extends AbstractTabController {

	SimulationDataBase simDataBase = new SimulationDataBase() ;
    public MatlabChart fig = new MatlabChart()  ;

    SimulationVariable xDataCase1 = null , yDataCase1 = null ;
    SimulationVariable xDataCase1Fitted = null , yDataCase1Fitted = null ;
    SimulationVariable xDataCase2 = null , yDataCase2 = null ;
    SimulationVariable xDataCase2Fitted = null , yDataCase2Fitted = null ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab tab ;
    @FXML Pane matlabPane ;
    @FXML TextField xDataCase1TextField ;
    @FXML Label xDataCase1Label ;
    @FXML TextField yDataCase1TextField ;
    @FXML Label yDataCase1Label ;
    @FXML TextField xDataCase2TextField ;
    @FXML Label xDataCase2Label ;
    @FXML TextField yDataCase2TextField ;
    @FXML Label yDataCase2Label ;
    @FXML ListView<String> resultListView ;

	@Override
	public void initialize() {
		xDataCase1Label.setText("");
		yDataCase1Label.setText("");
		xDataCase2Label.setText("");
		yDataCase2Label.setText("");
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
	private void setXDataCase1(){
		String st = xDataCase1TextField.getText() ;
		if(simDataBase.variableExists(st)){
			xDataCase1 = simDataBase.getVariable(st) ;
			xDataCase1Label.setText("X data is set to '" + xDataCase1.getName() + "'");
		}
		if(xDataCase1 != null && yDataCase1 != null){
			fig = createPlot() ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	private void chooseXDataCase1(){
		VariableSelectorModule varModule = new VariableSelectorModule(simDataBase) ;
		varModule.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
				xDataCase1 = new SimulationVariable(varModule.getController().getVariable().getName(), varModule.getController().getVariable().getAlias(), varModule.getController().getValues()) ;
				xDataCase1Label.setText("X data is set to '" + xDataCase1.getName() + "'");
				if(xDataCase1 != null && yDataCase1 != null){
					fig = createPlot() ;
					showPlot(fig, matlabPane);
				}
			}
		});
	}

	@FXML
	private void setYDataCase1(){
		String st = yDataCase1TextField.getText() ;
		if(simDataBase.variableExists(st)){
			yDataCase1 = simDataBase.getVariable(st) ;
			yDataCase1Label.setText("Y data is set to '" + yDataCase1.getName() + "'");
		}
		if(xDataCase1 != null && yDataCase1 != null){
			fig = createPlot() ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	private void chooseYDataCase1(){
		VariableSelectorModule varModule = new VariableSelectorModule(simDataBase) ;
		varModule.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
				yDataCase1 = new SimulationVariable(varModule.getController().getVariable().getName(), varModule.getController().getVariable().getAlias(), varModule.getController().getValues()) ;
				yDataCase1Label.setText("Y data is set to '" + yDataCase1.getName() + "'");
				if(xDataCase1 != null && yDataCase1 != null){
					fig = createPlot() ;
					showPlot(fig, matlabPane);
				}
			}
		});
	}

	@FXML
	private void setXDataCase2(){
		String st = xDataCase1TextField.getText() ;
		if(simDataBase.variableExists(st)){
			xDataCase2 = simDataBase.getVariable(st) ;
			xDataCase2Label.setText("X data is set to '" + xDataCase2.getName() + "'");
		}
		if(xDataCase2 != null && yDataCase2 != null){
			fig = createPlot() ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	private void chooseXDataCase2(){
		VariableSelectorModule varModule = new VariableSelectorModule(simDataBase) ;
		varModule.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
				xDataCase2 = new SimulationVariable(varModule.getController().getVariable().getName(), varModule.getController().getVariable().getAlias(), varModule.getController().getValues()) ;
				xDataCase2Label.setText("X data is set to '" + xDataCase2.getName() + "'");
				if(xDataCase2 != null && yDataCase2 != null){
					fig = createPlot() ;
					showPlot(fig, matlabPane);
				}
			}
		});
	}

	@FXML
	private void setYDataCase2(){
		String st = yDataCase1TextField.getText() ;
		if(simDataBase.variableExists(st)){
			yDataCase2 = simDataBase.getVariable(st) ;
			yDataCase2Label.setText("Y data is set to '" + yDataCase2.getName() + "'");
		}
		if(xDataCase2 != null && yDataCase2 != null){
			fig = createPlot() ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	private void chooseYDataCase2(){
		VariableSelectorModule varModule = new VariableSelectorModule(simDataBase) ;
		varModule.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
				yDataCase2 = new SimulationVariable(varModule.getController().getVariable().getName(), varModule.getController().getVariable().getAlias(), varModule.getController().getValues()) ;
				yDataCase2Label.setText("Y data is set to '" + yDataCase2.getName() + "'");
				if(xDataCase2 != null && yDataCase2 != null){
					fig = createPlot() ;
					showPlot(fig, matlabPane);
				}
			}
		});
	}

	@FXML
	private void calculate(){
//		fig = createPlot() ;
//		showPlot(fig, matlabPane);

		double qBER_min = getQberFromBER(Math.pow(10, -1)) ;
		double qBER_max = getQberFromBER(Math.pow(10, -13)) ;
		double[] qBER_dense = MoreMath.linspace(qBER_min, qBER_max, 1000) ;
		// step1: fit Gaussian BER to case 1
		double[] prec1_dBm = xDataCase1.getAllValues() ;
		double[] logBerCase1 = yDataCase1.getAllValues() ;
		int M1 = xDataCase1.getLength() ;
		double[] berMeasuredCase1 = new double[M1] ;
		double[] qMeasuredCase1 = new double[M1] ;
		for(int i=0; i<M1; i++){
			berMeasuredCase1[i] = Math.pow(10, logBerCase1[i]) ;
			qMeasuredCase1[i] = getQberFromBER(berMeasuredCase1[i]) ;
		}
		double[] fittingCoeffsCase1 = getPrecFittingCoeffs(prec1_dBm, qMeasuredCase1) ;
		double[] prec1_dense_dBm = new double[qBER_dense.length] ;
		double[] BER1_dense = new double[qBER_dense.length] ;
		double[] logBER1_dense = new double[qBER_dense.length] ;
		for(int i=0; i<qBER_dense.length; i++){
			for(int j=0; j<fittingCoeffsCase1.length; j++){
				prec1_dense_dBm[i] += fittingCoeffsCase1[j] * Math.pow(qBER_dense[i], j) ;
			}
			BER1_dense[i] = getBerFromQber(qBER_dense[i]) ;
			logBER1_dense[i] = Math.log10(BER1_dense[i]) ;
		}
		xDataCase1Fitted = new SimulationVariable(xDataCase1.getName()+"-fit", xDataCase1.getAlias()+"-fit", prec1_dense_dBm) ;
		yDataCase1Fitted = new SimulationVariable(yDataCase1.getName()+"-fit", yDataCase1.getAlias()+"-fit", logBER1_dense) ;
		simDataBase.addNewVariable(xDataCase1Fitted);
		simDataBase.addNewVariable(yDataCase1Fitted);

		fig.plot(prec1_dense_dBm, logBER1_dense, "r.", 3f);
		fig.RenderPlot();
		fig.xlabel("Prec (dBm)");
		fig.ylabel("Log10(BER)");
		// step2: create interpolator for fitted case 1
		LinearInterpolation interpolateCase1 = new LinearInterpolation(logBER1_dense, prec1_dense_dBm) ;
		// step3: fit Gaussian BER to case 2
		double[] prec2_dBm = xDataCase2.getAllValues() ;
		double[] logBerCase2 = yDataCase2.getAllValues() ;
		int M2 = xDataCase2.getLength() ;
		double[] berMeasuredCase2 = new double[M2] ;
		double[] qMeasuredCase2 = new double[M2] ;
		for(int i=0; i<M2; i++){
			berMeasuredCase2[i] = Math.pow(10, logBerCase2[i]) ;
			qMeasuredCase2[i] = getQberFromBER(berMeasuredCase2[i]) ;
		}
		double[] fittingCoeffsCase2 = getPrecFittingCoeffs(prec2_dBm, qMeasuredCase2) ;
		double[] prec2_dense_dBm = new double[qBER_dense.length] ;
		double[] BER2_dense = new double[qBER_dense.length] ;
		double[] logBER2_dense = new double[qBER_dense.length] ;
		for(int i=0; i<qBER_dense.length; i++){
			for(int j=0; j<fittingCoeffsCase2.length; j++){
				prec2_dense_dBm[i] += fittingCoeffsCase2[j] * Math.pow(qBER_dense[i], j) ;
			}
			BER2_dense[i] = getBerFromQber(qBER_dense[i]) ;
			logBER2_dense[i] = Math.log10(BER2_dense[i]) ;
		}
		xDataCase2Fitted = new SimulationVariable(xDataCase2.getName()+"-fit", xDataCase2.getAlias()+"-fit", prec2_dense_dBm) ;
		yDataCase2Fitted = new SimulationVariable(yDataCase2.getName()+"-fit", yDataCase2.getAlias()+"-fit", logBER2_dense) ;
		simDataBase.addNewVariable(xDataCase2Fitted);
		simDataBase.addNewVariable(yDataCase2Fitted);

		fig.plot(prec2_dense_dBm, logBER2_dense, "r.", 3f);
		fig.RenderPlot();
//		fig.xlabel("Prec (dBm)");
//		fig.ylabel("Log10(BER)");
		// step4: create interpolator for fitted case 2
		LinearInterpolation interpolateCase2 = new LinearInterpolation(logBER2_dense, prec2_dense_dBm) ;
		// step5: find 1e-9 power penalty
		double prec_case1_dBm_interpolated = interpolateCase1.interpolate(-9) ;
		double prec_case2_dBm_interpolated = interpolateCase2.interpolate(-9) ;
		double pp_atE9 = Math.abs(prec_case2_dBm_interpolated-prec_case1_dBm_interpolated) ;
		resultListView.getItems().removeAll(resultListView.getItems()) ;
		resultListView.getItems().add("PP(at 1E-9) = " + String.format("%.4f", pp_atE9)) ;
		// step6: find 1e-12 power penalty
		prec_case1_dBm_interpolated = interpolateCase1.interpolate(-12) ;
		prec_case2_dBm_interpolated = interpolateCase2.interpolate(-12) ;
		double pp_atE12 = Math.abs(prec_case2_dBm_interpolated-prec_case1_dBm_interpolated) ;
		resultListView.getItems().add("PP(at 1E-12) = " + String.format("%.4f", pp_atE12)) ;

	}

	private double getQberFromBER(double ber){
		double qBER = Math.sqrt(2) * Erf.erfcInv(2*ber) ;
		return qBER ;
	}

	private double getBerFromQber(double qBER){
		double ber = 0.5 * Erf.erfc(qBER/Math.sqrt(2)) ;
		return ber ;
	}

	private double[] getPrecFittingCoeffs(double[] prec_dBm, double[] qMeasured){
		// step1: finding the number of data points
		int numParams = 2 ;
		int M = qMeasured.length ;
		double[][] qValues = new double[M][1] ;
		for(int i=0; i<M; i++){
			qValues[i][0] = qMeasured[i] ;
		}
		// step2: setting up fitting polynomial of degree M by least square fitting
		Function Prec = new Function(){
			@Override
			public double evaluate(double[] values, double[] parameters) {
				double x = values[0]; // only one variable
				double y = 0 ;
				for(int i=0; i<numParams; i++){
					y += parameters[i]*Math.pow(x, i) ;
				}
				return y;
			}

			@Override
			public int getNParameters() {
				return numParams;
			}

			@Override
			public int getNInputs() {
				return 1;
			}
		} ;
		Fitter fit = new MarquardtFitter(Prec) ;
		fit.setData(qValues, prec_dBm);
		fit.setParameters(MoreMath.Arrays.setValue(0, numParams));
		fit.fitData();
		return fit.getParameters() ;
	}

	private MatlabChart createPlot(){
		MatlabChart fig = new MatlabChart() ;
		if(xDataCase1 != null && yDataCase1 != null && xDataCase1.getLength() == yDataCase1.getLength()){
			fig.plot(xDataCase1.getAllValues(), yDataCase1.getAllValues(), 3f);
			fig.RenderPlot();
			fig.xlabel("Prec (dBm)");
			fig.ylabel("Log10(BER)");
		}
		if(xDataCase2 != null && yDataCase2 != null && xDataCase2.getLength() == yDataCase2.getLength()){
			fig.plot(xDataCase2.getAllValues(), yDataCase2.getAllValues(), 3f);
			fig.RenderPlot();
			fig.xlabel("Prec (dBm)");
			fig.ylabel("Log10(BER)");
		}

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
