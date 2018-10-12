package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import People.Meisam.GUI.Utilities.Interfaces.ActionInterface;
import PhotonicElements.RingStructures.BackScattering.AddDropBackScatteringAsymmetricAbstract;
import PhotonicElements.Utilities.Wavelength;
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import PhotonicElements.Utilities.MathLibraries.CurveFitting.LeastSquare.leastsquares.Fitter;
import PhotonicElements.Waveguides.TerminatorAndReflector.LumpedReflector;
import flanagan.interpolation.LinearInterpolation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
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

public class AddDropFittingSymmetricBSDoubleReflectorTabController extends AbstractTabController {

	AddDropBackScatteringAsymmetricAbstract adr ;

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
    @FXML RadioButton plusPhase ;
    @FXML RadioButton minusPhase ;
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
        // fitting choice
        plusPhase.setSelected(true);
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
		varSelect.setExitAction(new ActionInterface() {
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
		varSelect.setExitAction(new ActionInterface() {
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

		double lambda_min_nm = simDataBase.getVariable("minLambda_(nm)").getValue(0) ;
		double lambda_max_nm = simDataBase.getVariable("maxLambda_(nm)").getValue(0) ;
		double lambda_res_nm = simDataBase.getVariable("centerLambda_(nm)").getValue(0) ;
//		double[] lambda_nm = MoreMath.linspace(lambda_min_nm, lambda_max_nm, 100) ;
		double FSR_nm = simDataBase.getVariable("FSR_(nm)").getValue(0) ;

		// first interpolate data points
		LinearInterpolation interpolator = new LinearInterpolation(xData.getAllValues(), yData.getAllValues()) ;
		
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
		double[][] lambdaValues_nm = new double[lambda_nm.length][1] ;
		for(int i=0; i<y_dB.length; i++){
			y_dB[i] = interpolator.interpolate(lambda_nm[i]) ;
			lambdaValues_nm[i][0] = lambda_nm[i] ;
		}
		// step 2: setup curve fitting to the normalized measurement
		Function fitFunc = new Function(){
			@Override
			public double evaluate(double[] values, double[] parameters) {
				double lambdaNm = values[0] ; // wavelength
				double kappa = parameters[0] ; // input and output kappa
				double phi = parameters[1] ; // phase difference between lumped reflectors
				double L = 1 ;
				double r1 = parameters[2] ; // lumped reflector field reflection
//				double r2 = parameters[3] ; // lumped reflector field reflection
				double offset_dB = parameters[3] ;
				adr = new AddDropBackScatteringAsymmetricAbstract(new Wavelength(lambdaNm), lambda_res_nm, FSR_nm, kappa, kappa, L, new LumpedReflector(r1,0), new LumpedReflector(r1, phi)); 
				double transDrop_dB = MoreMath.Conversions.todB(adr.S41.absSquared()) ;
				return (transDrop_dB + offset_dB);
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
//		Fitter fit = new NonLinearSolver(fitFunc) ;
		Fitter fit = new MarquardtFitter(fitFunc) ;
		fit.setData(lambdaValues_nm, y_dB);
//		fit.setParameters(new double[]{0.1, 0.97, 0.01, -30});
		if(plusPhase.isSelected()){
			fit.setParameters(new double[]{0.1, 0.1, 0.1, -30});
		}
		else{
			fit.setParameters(new double[]{0.1, -0.1, 0.1, -30});
		}
		
		fit.fitData();

		double kappa = fit.getParameters()[0] ;
		double phi = fit.getParameters()[1] ;
		double r1 = fit.getParameters()[2] ;
//		double r2 = fit.getParameters()[3] ;
		double offset_dB = fit.getParameters()[3] ;

		resultListView.getItems().removeAll(resultListView.getItems()) ;
		resultListView.getItems().add("kappa = " + String.format("%2.4f", kappa)) ;
		resultListView.getItems().add("Reflector r1 = " + String.format("%2.4f", r1)) ;
		resultListView.getItems().add("Reflector r2 = " + String.format("%2.4f", r1)) ;
		resultListView.getItems().add("Reflector phase = " + String.format("%2.4f", phi)) ;
		resultListView.getItems().add("offset (dB) = " + String.format("%2.4f", offset_dB)) ;
		// finally plotting
		lambda_nm = xData.getAllValues() ;
		double[] fitted_plot = new double[lambda_nm.length] ;
		for(int i=0; i<fitted_plot.length; i++){
			adr = new AddDropBackScatteringAsymmetricAbstract(new Wavelength(lambda_nm[i]), lambda_res_nm, FSR_nm, kappa, kappa, 1, new LumpedReflector(r1, 0), new LumpedReflector(r1, phi)) ;
			fitted_plot[i] = MoreMath.Conversions.todB(adr.S41.absSquared()) + offset_dB ;
		}
		simDataBase.addNewVariable(new SimulationVariable("fitted_drop_(dBm)", "Drop Power (dBm)", fitted_plot));
		
		fig.plot(lambda_nm, fitted_plot, "r", 3f);
		fig.RenderPlot();
		fig.xlabel("Wavelength (nm)");
		fig.ylabel("Drop (dBm)");
		showPlot(fig, matlabPane);


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
