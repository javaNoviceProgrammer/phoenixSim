package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import PhotonicElements.Utilities.MathLibraries.MoreMath;
import flanagan.interpolation.LinearInterpolation;
import flanagan.roots.RealRoot;
import flanagan.roots.RealRootFunction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import mathLib.plot.MatlabChart;
import phoenixSim.modules.PlotterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import phoenixSim.util.VariableSelectorController;

public class AddDropFittingSymmetricTabController extends AbstractTabController {

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
    @FXML TextField drLossTextField ;
    @FXML Label drLossLabel ;

	@Override
	public void initialize() {
		xDataLabel.setText("");
		yDataLabel.setText("");
		minLambdaLabel.setText("");
		maxLambdaLabel.setText("");
		centerLambdaLabel.setText("");
		drLossLabel.setText("");
		fsrLabel.setText("");
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
		FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/VariableSelector/variable_selector.fxml")) ;
		WindowBuilder varSelect = new WindowBuilder(loader) ;
		varSelect.setIcon("/People/Meisam/GUI/Utilities/VariableSelector/Extras/icon.png");
		varSelect.build("Select Variable & Values", false);
		VariableSelectorController controller = loader.getController() ;
		controller.setSimDataBase(simDataBase);
		controller.initialize();
		controller.getSetExitButton().setOnAction(e -> {
			xData = new SimulationVariable(controller.getVariable().getName(), controller.getVariable().getAlias(), controller.getValues()) ;
			xDataLabel.setText("X data is set to '" + xData.getName() + "'");
			controller.getSetExitButton().getScene().getWindow().hide();
			if(xData != null && yData != null){
				fig = createPlot(xData, yData) ;
				showPlot(fig, matlabPane);
			}
		});


	}

	@FXML
	public void chooseYData() throws IOException{
		FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/VariableSelector/variable_selector.fxml")) ;
		WindowBuilder varSelect = new WindowBuilder(loader) ;
		varSelect.setIcon("/People/Meisam/GUI/Utilities/VariableSelector/Extras/icon.png");
		varSelect.build("Select Variable & Values", false);
		VariableSelectorController controller = loader.getController() ;
		controller.setSimDataBase(simDataBase);
		controller.initialize();
		controller.getSetExitButton().setOnAction(e -> {
			yData = new SimulationVariable(controller.getVariable().getName(), controller.getVariable().getAlias(), controller.getValues()) ;
			yDataLabel.setText("Y data is set to '" + yData.getName() + "'");
			controller.getSetExitButton().getScene().getWindow().hide();
			if(xData != null && yData != null){
				fig = createPlot(xData, yData) ;
				showPlot(fig, matlabPane);
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
	public void setDRLoss(){
		String st = drLossTextField.getText() ;
        if(!st.isEmpty()){
        	try {
                double drLoss_nm = Double.parseDouble(drLossTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("drLoss_(dB)", "Drop Loss (dB)", new double[]{drLoss_nm}));
                drLossLabel.setText("DR loss is set to " + drLoss_nm + " dB");
			} catch (Exception e) {
	            double drLoss_nm = MoreMath.evaluate(st) ;
	            simDataBase.addNewVariable(new SimulationVariable("drLoss_(dB)", "Drop Loss (dB)", new double[]{drLoss_nm}));
	            drLossLabel.setText("DR loss is set to " + String.format("%.4f", drLoss_nm) + " dB");
			}
        }
	}

	@FXML
	public void calculateKappa(){
//		fig = createPlot(xData, yData) ;
//		showPlot(fig, matlabPane);

		double lambda_min_nm = simDataBase.getVariable("minLambda_(nm)").getValue(0) ;
		double lambda_max_nm = simDataBase.getVariable("maxLambda_(nm)").getValue(0) ;
		double lambda_res_nm = simDataBase.getVariable("centerLambda_(nm)").getValue(0) ;
//		double[] lambda_nm = MoreMath.linspace(lambda_min_nm, lambda_max_nm, 1000) ;
		double FSR_nm = simDataBase.getVariable("FSR_(nm)").getValue(0) ;
		double drLoss_dB = simDataBase.getVariable("drLoss_(dB)").getValue(0) ;
		double drLoss = MoreMath.Conversions.fromdB(-drLoss_dB) ;

		if(fitTodB.isSelected()){
			// first interpolate data points
//			LinearInterpolation interpolator = new LinearInterpolation(xData.getAllValues(), yData.getAllValues()) ;
			LinearInterpolation.supress(); 
			int M_start = 0, M_end = xData.getLength()-1 ;
			while(M_start<=xData.getLength()){
				if(xData.getValue(M_start)<lambda_min_nm){M_start++ ;}
				else{break ;}
			}
			while(M_end>0){
				if(xData.getValue(M_end)>lambda_max_nm){M_end-- ;}
				else{break ;}
			}
			double[] xData_subset = new double[M_end-M_start+1] ;
			double[] yData_subset = new double[M_end-M_start+1] ;
			for(int i=0; i<M_end-M_start+1; i++){
				xData_subset[i] = xData.getValue(i+M_start) ;
				yData_subset[i] = yData.getValue(i+M_start) ;
			}
			lambda_min_nm = xData.getValue(M_start) ;
			lambda_max_nm = xData.getValue(M_end) ;
			double[] lambda_nm = MoreMath.linspace(lambda_min_nm, lambda_max_nm, 100) ;
			double[] y_dB = new double[lambda_nm.length] ;
			LinearInterpolation interpolator = new LinearInterpolation(xData_subset, yData_subset) ;
			for(int i=0; i<y_dB.length; i++){
				y_dB[i] = interpolator.interpolate(lambda_nm[i]) ;
			}
			double y_dB_max = MoreMath.Arrays.FindMaximum.getValue(y_dB) ;
			double[] y_dB_normalized = MoreMath.Arrays.plus(y_dB, -y_dB_max) ; // normalizing data ;
			LinearInterpolation interpolator_norm = new LinearInterpolation(lambda_nm, y_dB_normalized) ;
			// step 1: find x
			RealRootFunction func = new RealRootFunction(){
				@Override
				public double function(double lambdaNm) {
					return (interpolator_norm.interpolate(lambdaNm)+3) ;
				}
			} ;
			RealRoot solverBW = new RealRoot() ;
			double BW_nm = 2*( solverBW.bisect(func, lambda_res_nm, lambda_max_nm)-lambda_res_nm ) ;
			RealRootFunction func1 = new RealRootFunction(){
				@Override
				public double function(double x) {
					double y = 1-Math.pow(1-x, 2)/(2*x) - Math.cos(Math.PI*BW_nm/FSR_nm) ;
					return y;
				}
			} ;
			RealRoot solverX = new RealRoot() ;
			double x = solverX.bisect(func1, 0, 1) ;
			// step 2: find kappa
			double w = drLoss*Math.pow(1-x, 2)/x ;
			double w1 = Math.sqrt(w+(w/2)*(w/2)) ;
			double kappa_found = Math.sqrt(-w/2 + w1) ;
			double k = kappa_found ;
			// step 3: find alpha
			double L = Math.pow(x/(1-k*k), 2) ;
			resultListView.getItems().removeAll(resultListView.getItems()) ;
			resultListView.getItems().add("kappa = " + String.format("%2.4f", kappa_found)) ;
			double FWHM_nm = BW_nm ;
			resultListView.getItems().add("BW (nm) = " + String.format("%2.4f", FWHM_nm)) ;
			resultListView.getItems().add("L = " + String.format("%2.4f", L)) ;
			// finally plotting
			double[] fitted_plot = getLorentzianNormalized_dB(x, FSR_nm, xData.getAllValues(), lambda_res_nm) ;
			fitted_plot = MoreMath.Arrays.plus(fitted_plot, y_dB_max) ;
			simDataBase.addNewVariable(new SimulationVariable("fitted_drop_(dBm)", "Drop Power (dBm)", fitted_plot));
			fig.plot(xData.getAllValues(), fitted_plot, "r", 3f);
			fig.renderPlot();
			fig.xlabel("Wavelength (nm)");
			fig.ylabel("Drop (dBm)");
			showPlot(fig, matlabPane);
		}


	}

	private double getLorentzianNormalized(double x, double FSR_nm, double lambda_nm, double lambda_res_nm){
		double y = 2*Math.sqrt(x)/(1-x) ;
		double dphi = -(lambda_nm-lambda_res_nm)/FSR_nm * 2*Math.PI ;
		double s = Math.sin(dphi/2) ;
		double dr = 1/(1+Math.pow(y*s, 2)) ;
		return dr ;
	}

	@SuppressWarnings("unused")
	private double[] getLorentzianNormalized(double x, double FSR_nm, double[] lambda_nm, double lambda_res_nm){
		double[] y = new double[lambda_nm.length] ;
		for(int i=0; i<y.length; i++){
			y[i] = getLorentzianNormalized(x, FSR_nm, lambda_nm[i], lambda_res_nm) ;
		}
		return y ;
	}

	private double[] getLorentzianNormalized_dB(double x, double FSR_nm, double[] lambda_nm, double lambda_res_nm){
		double[] y = new double[lambda_nm.length] ;
		for(int i=0; i<y.length; i++){
			y[i] = getLorentzianNormalized(x, FSR_nm, lambda_nm[i], lambda_res_nm) ;
		}
		return MoreMath.Arrays.Conversions.todB(y) ;
	}

//	private double getLorentzianNormalized(double kappa, double FSR_nm, double lambda_nm, double lambda_res_nm){
//		double t = Math.sqrt(1-kappa*kappa) ;
//		double FWHM_nm = FSR_nm * 1/Math.PI * Math.acos(1-(1-t*t)*(1-t*t)/(2*t*t)) ;
//		double y = 1/(1+Math.pow(2/FWHM_nm * (lambda_nm-lambda_res_nm), 2)) ;
//		return y ;
//	}
//
//	@SuppressWarnings("unused")
//	private double[] getLorentzianNormalized(double kappa, double FSR_nm, double[] lambda_nm, double lambda_res_nm){
//		double[] y = new double[lambda_nm.length] ;
//		for(int i=0; i<y.length; i++){
//			y[i] = getLorentzianNormalized(kappa, FSR_nm, lambda_nm[i], lambda_res_nm) ;
//		}
//		return y ;
//	}
//
//	private double[] getLorentzianNormalized_dB(double kappa, double FSR_nm, double[] lambda_nm, double lambda_res_nm){
//		double[] y = new double[lambda_nm.length] ;
//		for(int i=0; i<y.length; i++){
//			y[i] = getLorentzianNormalized(kappa, FSR_nm, lambda_nm[i], lambda_res_nm) ;
//		}
//		return MoreMath.Arrays.Conversions.todB(y) ;
//	}

    private MatlabChart createPlot(SimulationVariable x, SimulationVariable y){
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x.getAllValues(), y.getAllValues());
        fig.renderPlot();
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
