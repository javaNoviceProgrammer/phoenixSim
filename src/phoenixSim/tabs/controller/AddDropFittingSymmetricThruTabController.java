package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import PhotonicElements.Utilities.MathLibraries.MoreMath;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.Fitter;
import flanagan.interpolation.LinearInterpolation;
import flanagan.roots.RealRoot;
import flanagan.roots.RealRootFunction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import mathLib.fitting.lmse.MarquardtFitter;
import mathLib.plot.MatlabChart;
import phoenixSim.builder.intf.ActionInterface;
import phoenixSim.modules.PlotterModule;
import phoenixSim.modules.VariableSelectorModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;

public class AddDropFittingSymmetricThruTabController extends AbstractTabController {

    // defining simulation database and the figures in the simulation
    SimulationDataBase simDataBase = new SimulationDataBase() ;

    public MatlabChart fig  ;
    SimulationVariable xData = null , yData = null ;
    SimulationVariable xFit = null , yFit = null ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab tab ;
    @FXML Pane matlabPane ;
    @FXML TextField xDataTextField ;
    @FXML Label xDataLabel ;
    @FXML TextField yDataTextField ;
    @FXML Label yDataLabel ;
    @FXML TextField minLambdaTextField ;
    @FXML Label minLambdaLabel ;
    @FXML TextField maxLambdaTextField ;
    @FXML Label maxLambdaLabel ;
    @FXML TextField centerLambdaTextField ;
    @FXML Label centerLambdaLabel ;
    @FXML ListView<String> resultListView ;
    @FXML RadioButton fitTodB ;
    @FXML RadioButton fitToNondB ;
    @FXML TextField fsrTextField ;
    @FXML Label fsrLabel ;

	@Override
	public void initialize() {
		xDataLabel.setText("");
		yDataLabel.setText("");
		minLambdaLabel.setText("");
		maxLambdaLabel.setText("");
		centerLambdaLabel.setText("");
		fsrLabel.setText("");
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
        // fitting choise
        fitTodB.setSelected(true);
	}

	@FXML
	public void setXData(){
		String st = xDataTextField.getText() ;
		if(simDataBase.variableExists(st)){
			xData = simDataBase.getVariable(st) ;
			xDataLabel.setText("X data is set to '" + xData.getName() + "'");
		}
		if(xData != null && yData != null){
			fig = createPlot(xData, yData) ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	public void setYData(){
		String st = yDataTextField.getText() ;
		if(simDataBase.variableExists(st)){
			yData = simDataBase.getVariable(st) ;
			yDataLabel.setText("Y data is set to '" + yData.getName() + "'");
		}
		if(xData != null && yData != null){
			fig = createPlot(xData, yData) ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	public void chooseXData() throws IOException{
		VariableSelectorModule varSelect = new VariableSelectorModule(simDataBase) ;
		varSelect.setExitAction(new ActionInterface(){
			@Override
			public void setExitAction() {
				xData = new SimulationVariable(varSelect.getController().getVariable().getName(), varSelect.getController().getVariable().getAlias(), varSelect.getController().getValues()) ;
				xDataLabel.setText("X data is set to '" + xData.getName() + "'");
				varSelect.getController().getSetExitButton().getScene().getWindow().hide();
				if(xData != null && yData != null){
					fig = createPlot(xData, yData) ;
					showPlot(fig, matlabPane);
				}
			}
		});
	}

	@FXML
	public void chooseYData() throws IOException{
		VariableSelectorModule varSelect = new VariableSelectorModule(simDataBase) ;
		varSelect.setExitAction(new ActionInterface(){
			@Override
			public void setExitAction() {
				yData = new SimulationVariable(varSelect.getController().getVariable().getName(), varSelect.getController().getVariable().getAlias(), varSelect.getController().getValues()) ;
				yDataLabel.setText("Y data is set to '" + yData.getName() + "'");
				varSelect.getController().getSetExitButton().getScene().getWindow().hide();
				if(xData != null && yData != null){
					fig = createPlot(xData, yData) ;
					showPlot(fig, matlabPane);
				}
			}
		});
	}

	@FXML
	public void setMinLambda(){
		String st = minLambdaTextField.getText() ;
		if(!st.isEmpty()){
			try {
	            double minLambda_nm = Double.parseDouble(minLambdaTextField.getText()) ;
	            simDataBase.addNewVariable(new SimulationVariable("minLambda_(nm)", "min Lambda (nm)", new double[]{minLambda_nm}));
	            minLambdaLabel.setText("min is set to " + minLambda_nm + " nm");
			} catch (Exception e) {
	            double minLambda_nm = MoreMath.evaluate(st) ;
	            simDataBase.addNewVariable(new SimulationVariable("minLambda_(nm)", "min Lambda (nm)", new double[]{minLambda_nm}));
	            minLambdaLabel.setText("min is set to " + String.format("%.4f", minLambda_nm) + " nm");
			}
		}
	}

	@FXML
	public void setMaxLambda(){
		String st = maxLambdaTextField.getText() ;
		if(!st.isEmpty()){
			try {
	            double maxLambda_nm = Double.parseDouble(maxLambdaTextField.getText()) ;
	            simDataBase.addNewVariable(new SimulationVariable("maxLambda_(nm)", "max Lambda (nm)", new double[]{maxLambda_nm}));
	            maxLambdaLabel.setText("max is set to " + maxLambda_nm + " nm");
			} catch (Exception e) {
	            double maxLambda_nm = MoreMath.evaluate(st) ;
	            simDataBase.addNewVariable(new SimulationVariable("maxLambda_(nm)", "max Lambda (nm)", new double[]{maxLambda_nm}));
	            maxLambdaLabel.setText("max is set to " + String.format("%.4f", maxLambda_nm) + " nm");
			}
		}
	}

	@FXML
	public void setCenterLambda(){
		String st = centerLambdaTextField.getText() ;
		if(!st.isEmpty()){
			try {
	            double centerLambda_nm = Double.parseDouble(centerLambdaTextField.getText()) ;
	            simDataBase.addNewVariable(new SimulationVariable("centerLambda_(nm)", "center Lambda (nm)", new double[]{centerLambda_nm}));
	            centerLambdaLabel.setText("center is set to " + centerLambda_nm + " nm");
			} catch (Exception e) {
	            double centerLambda_nm = MoreMath.evaluate(st) ;
	            simDataBase.addNewVariable(new SimulationVariable("centerLambda_(nm)", "center Lambda (nm)", new double[]{centerLambda_nm}));
	            centerLambdaLabel.setText("center is set to " + String.format("%.4f", centerLambda_nm) + " nm");
			}
		}
	}

	@FXML
	public void setFSR(){
		String st = fsrTextField.getText() ;
        if(!st.isEmpty()){
        	try {
                double fsr_nm = Double.parseDouble(fsrTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("FSR_(nm)", "FSR (nm)", new double[]{fsr_nm}));
                fsrLabel.setText("FSR is set to " + fsr_nm + " nm");
			} catch (Exception e) {
	            double fsr_nm = MoreMath.evaluate(st) ;
	            simDataBase.addNewVariable(new SimulationVariable("FSR_(nm)", "FSR (nm)", new double[]{fsr_nm}));
	            fsrLabel.setText("FSR is set to " + String.format("%.4f", fsr_nm) + " nm");
			}
        }
	}

	@FXML
	public void calculateKappa(){
		fig = createPlot(xData, yData) ;
		showPlot(fig, matlabPane);

		double lambda_min_nm = simDataBase.getVariable("minLambda_(nm)").getValue(0) ;
		double lambda_max_nm = simDataBase.getVariable("maxLambda_(nm)").getValue(0) ;
		double lambda_res_nm = simDataBase.getVariable("centerLambda_(nm)").getValue(0) ;
		double[] lambda_nm = MoreMath.linspace(lambda_min_nm, lambda_max_nm, 1000) ;
		double FSR_nm = simDataBase.getVariable("FSR_(nm)").getValue(0) ;

		if(fitTodB.isSelected()){
			// first interpolate data points
			LinearInterpolation interpolator = new LinearInterpolation(xData.getAllValues(), yData.getAllValues()) ;
			double[] y_dB = new double[lambda_nm.length] ;
			double[] y_nondB = new double[lambda_nm.length] ;
			double[][] lambda_nm_values = new double[lambda_nm.length][1] ;
			for(int i=0; i<y_dB.length; i++){
				y_dB[i] = interpolator.interpolate(lambda_nm[i]) ;
				y_nondB[i] = MoreMath.Conversions.fromdB(y_dB[i]) ;
				lambda_nm_values[i][0] = lambda_nm[i] ;
			}
			double y_dB_min = MoreMath.Arrays.FindMinimum.getValue(y_dB) ;
//			double y_dB_max = MoreMath.Arrays.FindMaximum.getValue(y_dB) ;
			// step 1: find BW & ER
//			double ER_dB = (interpolator.interpolate(lambda_min_nm) + y_dB_max)/2 - interpolator.interpolate(lambda_res_nm) ;
//			double er = MoreMath.Conversions.fromdB(ER_dB) ;
			Function fitFunc = new Function(){
				@Override
				public double evaluate(double[] values, double[] parameters) {
					double BW = parameters[0] ; // nm units
					double er = parameters[1] ; // no units (non-dB)
					double ER_dB = MoreMath.Conversions.todB(er) ;
					double lambda_nm = values[0] ; // only one variable - nm units
					double tr_dB = MoreMath.Conversions.todB(getLorentzian(BW, er, lambda_nm, lambda_res_nm)) + y_dB_min + ER_dB ;
					return tr_dB ;
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
//			Fitter fit = new NonLinearSolver(fitFunc) ;
			Fitter fit = new MarquardtFitter(fitFunc) ;
			fit.setData(lambda_nm_values, y_dB);
			fit.setParameters(new double[]{1, 10});
			fit.fitData();
			double BW_nm = Math.abs(fit.getParameters()[0]) ;
			double er = Math.abs(fit.getParameters()[1]) ;
			double ER_dB = MoreMath.Conversions.todB(er) ;
			// step 2: find x = t*t*sqrt(L)
			RealRootFunction func1 = new RealRootFunction(){
				@Override
				public double function(double x) {
					double y = 1-Math.pow(1-x, 2)/(2*x) - Math.cos(Math.PI*BW_nm/FSR_nm) ;
					return y;
				}
			} ;
			RealRoot solverX = new RealRoot() ;
			double x = solverX.bisect(func1, 0, 1) ;
			// step 3: find L
			double A = er * Math.pow((1+x)/(1-x), 2) ;
			double L = Math.pow((Math.sqrt(A)-1)/(Math.sqrt(A)+1), 2) ;
			// step 4: find t, kappa
			double t = Math.sqrt(x/Math.sqrt(L)) ;
			double kappa = Math.sqrt(1-t*t) ;
			// displaying the results
			resultListView.getItems().removeAll(resultListView.getItems()) ;
			resultListView.getItems().add("t = " + String.format("%2.4f", t)) ;
			resultListView.getItems().add("kappa = " + String.format("%2.4f", kappa)) ;
			resultListView.getItems().add("BW (nm) = " + String.format("%2.4f", BW_nm)) ;
			resultListView.getItems().add("L = " + String.format("%2.4f", L)) ;
			resultListView.getItems().add("ER (dB) = " + String.format("%2.4f", ER_dB)) ;
			resultListView.getItems().add("Q = " + String.format("%2.4f", lambda_res_nm/BW_nm)) ;
			resultListView.getItems().add("t^2*sqrt(L) = " + String.format("%2.4f", x)) ;
			// finally plotting
			double[] fitted_plot = MoreMath.Arrays.Conversions.todB(getLorentzian(BW_nm, er, lambda_nm, lambda_res_nm)) ;
			fitted_plot = MoreMath.Arrays.plus(fitted_plot, y_dB_min+ER_dB) ;
			simDataBase.addNewVariable(new SimulationVariable("fitted_thru_(dBm)", "Thru Power (dBm)", fitted_plot));
			fig.plot(lambda_nm, fitted_plot, "r", 3f);
			fig.RenderPlot();
			fig.xlabel("Wavelength (nm)");
			fig.ylabel("Thru (dBm)");
			showPlot(fig, matlabPane);
		}
	}

	private double getLorentzian(double BW_nm, double er, double lambda_nm, double lambda_res_nm){
		double s = 2/BW_nm * (lambda_nm-lambda_res_nm) ;
		double num = (s*s) + 1/er ;
		double denum = (s*s) + 1 ;
		double tr = num/denum ;
		return tr ;
	}

	private double[] getLorentzian(double BW_nm, double er, double[] lambda_nm, double lambda_res_nm){
		double[] y = new double[lambda_nm.length] ;
		for(int i=0; i<y.length; i++){
			y[i] = getLorentzian(BW_nm, er, lambda_nm[i], lambda_res_nm) ;
		}
		return y ;
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
