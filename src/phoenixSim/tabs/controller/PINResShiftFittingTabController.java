package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import mathLib.fitting.lmse.LeastSquareFitter;
import mathLib.fitting.lmse.LeastSquareFunction;
import mathLib.fitting.lmse.MarquardtFitter;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;
import phoenixSim.modules.ExportToMatlabModule;
import phoenixSim.modules.PlotterModule;
import phoenixSim.modules.VariableSelectorModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.pnjunc.pin.PINShiftResDC;

public class PINResShiftFittingTabController extends AbstractTabController {

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
		VariableSelectorModule var = new VariableSelectorModule(simDataBase) ;
		var.setExitAction(() -> {
			iData = new SimulationVariable(var.getController().getVariable().getName(), var.getController().getVariable().getAlias(), 
					var.getController().getValues()) ;
			iDataLabel.setText("I data is set to '" + iData.getName() + "'");
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
			vDataLabel.setText("Res data is set to '" + vData.getName() + "'");
		}
		if(iData != null && vData != null){
			fig = createPlot(iData, vData) ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	public void chooseVData() throws IOException{
		VariableSelectorModule var = new VariableSelectorModule(simDataBase) ;
		var.setExitAction(() -> {
			vData = new SimulationVariable(var.getController().getVariable().getName(), var.getController().getVariable().getAlias(), 
					var.getController().getValues()) ;
			vDataLabel.setText("Res data is set to '" + vData.getName() + "'");
			if(iData != null && vData != null){
				fig = createPlot(iData, vData) ;
				showPlot(fig, matlabPane);
			}
		});
	}

	@FXML
	public void calculate(){
		double[] current_mA = iData.getAllValues() ;
		double[] resShift_nm = vData.getAllValues() ;
		double[][] I_mA = new double[current_mA.length][1] ;
		for(int i=0; i<current_mA.length; i++){
			I_mA[i][0] = current_mA[i] ;
		}

		LeastSquareFunction V = new LeastSquareFunction(){
			@Override
			public double evaluate(double[] values, double[] parameters) {
				double a = parameters[0] ; 
				double c = parameters[1] ; 
				double I0 = parameters[2] ; // mA units
				double I_mA = values[0] ;
				PINShiftResDC pinShiftResDC = new PINShiftResDC(a, c, I0) ;
				double resShift_nm = pinShiftResDC.getDlambdaNm(I_mA) ;
				return resShift_nm ;
			}
			@Override
			public int getNParameters() {
				return 3;
			}
			@Override
			public int getNInputs() {
				return 1;
			}
		} ;

//		LeastSquareFitter fit = new NonLinearSolver(V) ;
		LeastSquareFitter fit = new MarquardtFitter(V) ;
		fit.setData(I_mA, resShift_nm);
		fit.setParameters(new double[]{0.5, 0.05, 0.05});
		fit.fitData();

		double a = fit.getParameters()[0] ;
		double c = fit.getParameters()[1] ;
		double I0 = fit.getParameters()[2] ;
		simDataBase.addNewVariable(new SimulationVariable("a-fit", "a ()", new double[]{a}));
		simDataBase.addNewVariable(new SimulationVariable("c-fit", "c (nm/mA^2)", new double[]{c}));
		simDataBase.addNewVariable(new SimulationVariable("I0-fit", "I0 (mA)", new double[]{I0}));

		resultListView.getItems().removeAll(resultListView.getItems()) ;
		resultListView.getItems().add("a = " + String.format("%2.4f", a)) ;
		resultListView.getItems().add("c = " + String.format("%2.4f", c)) ;
		resultListView.getItems().add("I0 (mA) = " + String.format("%2.4f", I0)) ;

		double[] I_values = MathUtils.linspace(MathUtils.Arrays.FindMinimum.getValue(current_mA), 
											   MathUtils.Arrays.FindMaximum.getValue(current_mA), 1000) ;
		double[] shift_values = new double[I_values.length] ;
		PINShiftResDC pinShiftResDC = new PINShiftResDC(a, c, I0) ;
		for(int i=0; i<I_values.length; i++){
			shift_values[i] = pinShiftResDC.getDlambdaNm(I_values[i]) ;
		}

		iFit = new SimulationVariable("I_fit_(mA)", "Fitted Current (mA)", I_values) ;
		vFit = new SimulationVariable("resShift_fit_(nm)", "Fitted Res Shift (nm)", shift_values) ;
		simDataBase.addNewVariable(iFit);
		simDataBase.addNewVariable(vFit);

		fig.plot(iFit.getAllValues(), vFit.getAllValues(), "r", 3f);
		fig.renderPlot();
		fig.xlabel("Current (mA)");
		fig.ylabel("Res Shift (nm)");
		showPlot(fig, matlabPane);
	}

    private MatlabChart createPlot(SimulationVariable x, SimulationVariable y){
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x.getAllValues(), y.getAllValues());
        fig.renderPlot();
        fig.xlabel("Current (mA)");
        fig.ylabel("Res Shift (nm)");
        return fig ;
    }

    @FXML
    public void exportToMatlabPressed() {
    	new ExportToMatlabModule(getFig()) ;
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
