package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;
import phoenixSim.modules.PlotterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.heater.impulse.ImpulseResponse1D_Modified_FFT;
import photonics.heater.struct.SelfHeating;
import photonics.heater.voltage.AbstractVoltage;

public class HeaterImpulseTabController extends AbstractTabController {

    SelfHeating selfHeating ;
    ImpulseResponse1D_Modified_FFT impulse ;
    AbstractVoltage voltage ;

    SimulationDataBase simDataBase = new SimulationDataBase() ;
    MatlabChart figImpulse ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab impulseTab ;
    @FXML Pane impulseMatlabPlotPane ;
    @FXML TextField impulseStartTimeTextField ;
    @FXML TextField impulseEndTimeTextField ;
    @FXML Label impulseStartTimeLabel ;
    @FXML Label impulseEndTimeLabel ;
    @FXML ToggleGroup impulsePlot ;
    @FXML RadioButton impulsePlotRadioButton ;
    @FXML RadioButton impulseNormalPlotRadioButton ;

    @FXML
    public void initialize(){
        impulseStartTimeLabel.setText("");
        impulseEndTimeLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.renderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figImpulse = fig ;
        showPlot(fig, impulseMatlabPlotPane);
        impulseMatlabPlotPane.getChildren().add(swingNode) ;
    }

    private boolean impulseToggleIsSelected(){
        if(impulsePlotRadioButton.isSelected() || impulseNormalPlotRadioButton.isSelected()){
            return true ;
        }
        else{
            return false ;
        }
    }

    @FXML
    public void setImpulseStartTime(){
        double impulse_tStart_usec = Double.parseDouble(impulseStartTimeTextField.getText()) ;
        simDataBase.addNewVariable(new SimulationVariable("tStart_(usec)", new double[]{impulse_tStart_usec}));
        impulseStartTimeLabel.setText("Start time is set to " + impulse_tStart_usec + " usec");
        if(impulseToggleIsSelected()){
            impulsePlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setImpulseEndTime(){
        double impulse_tEnd_usec = Double.parseDouble(impulseEndTimeTextField.getText()) ;
        simDataBase.addNewVariable(new SimulationVariable("tEnd_(usec)", new double[]{impulse_tEnd_usec}));
        impulseEndTimeLabel.setText("End time is set to " + impulse_tEnd_usec + " usec");
        if(impulseToggleIsSelected()){
            impulsePlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    private void plotImpulse(){
        double tStart_usec = simDataBase.getVariable("tStart_(usec)").getValue(0) ;
        double tEnd_usec = simDataBase.getVariable("tEnd_(usec)").getValue(0) ;
        double f0 = simDataBase.getVariable("f0").getValue(0) ;
        double nu = simDataBase.getVariable("nu").getValue(0) ;
        impulse = new ImpulseResponse1D_Modified_FFT(f0, nu) ;
        impulse.buildModel(); // building the time response
//        double[] time_usec = MathUtils.linspace(tStart_usec, tEnd_usec, 1000) ;
        double[] time_usec = impulse.time_usec ;
//        double[] Iwg = impulse.getTimeResponse(time_usec) ;
        double[] Iwg = impulse.Iwg ;
        double Iwg_max = MathUtils.Arrays.FindMaximum.getValue(Iwg) ;
        double[] Iwg_normalized = MathUtils.Arrays.times(Iwg, 1/Iwg_max) ;
        simDataBase.addNewVariable(new SimulationVariable("Iwg", "Impulse Response", Iwg));
        simDataBase.addNewVariable(new SimulationVariable("Iwg_normalized", "Normalized Impulse Response", Iwg_normalized));
        simDataBase.addNewVariable(new SimulationVariable("time_(usec)", "Time (usec)", time_usec));

        if(impulsePlot.getSelectedToggle().equals(impulsePlotRadioButton)){
            figImpulse = createPlot(simDataBase.getVariable("time_(usec)"), simDataBase.getVariable("Iwg")) ;
            figImpulse.xlim(tStart_usec, tEnd_usec);
            showPlot(figImpulse, impulseMatlabPlotPane);
        }
        else if(impulsePlot.getSelectedToggle().equals(impulseNormalPlotRadioButton)){
            figImpulse = createPlot(simDataBase.getVariable("time_(usec)"), simDataBase.getVariable("Iwg_normalized")) ;
            figImpulse.xlim(tStart_usec, tEnd_usec);
            showPlot(figImpulse, impulseMatlabPlotPane);
        }

    }

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
//    	figImpulse.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
        new PlotterModule(simDataBase) ;
    }

    public StatusBar getStatusBar(){
    	return statusBar ;
    }

	public Tab getTab() {
		return impulseTab ;
	}

	@Override
	public MatlabChart getFig() {
		return figImpulse ;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}

}
