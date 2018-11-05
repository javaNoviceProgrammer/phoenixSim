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
import photonics.heater.trans.TransientResponse_FFT;
import photonics.heater.voltage.AbstractVoltage;
import photonics.heater.voltage.StepVoltage;

public class HeaterStepTabController extends AbstractTabController {

    SelfHeating selfHeating ;
    ImpulseResponse1D_Modified_FFT impulse ;
    AbstractVoltage voltage ;
    TransientResponse_FFT transResponse ;

    SimulationDataBase simDataBase = new SimulationDataBase() ;
    MatlabChart figStep ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab stepTab ;
    @FXML Pane stepMatlabPlotPane ;
    @FXML TextField stept0TextField ;
    @FXML TextField stepV0TextField ;
    @FXML Label stept0Label ;
    @FXML Label stepV0Label ;
    @FXML ToggleGroup stepPlot ;
    @FXML RadioButton stepWaveguidePlotRadioButton ;
    @FXML RadioButton stepHeaterPlotRadioButton ;

    @FXML
    public void initialize(){
        stept0Label.setText("");
        stepV0Label.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.renderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figStep = fig ;
        showPlot(fig, stepMatlabPlotPane);
        stepMatlabPlotPane.getChildren().add(swingNode) ;
    }

    private boolean stepToggleIsSelected(){
        if(stepWaveguidePlotRadioButton.isSelected() || stepHeaterPlotRadioButton.isSelected()){
            return true ;
        }
        else{
            return false ;
        }
    }

    @FXML
    public void setT0(){
        double step_t0_usec = Double.parseDouble(stept0TextField.getText()) ;
        simDataBase.addNewVariable(new SimulationVariable("step_t0_(usec)", new double[]{step_t0_usec}));
        stept0Label.setText("t0 is set to " + step_t0_usec + " usec");
        if(stepToggleIsSelected()){
            stepPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setV0(){
        double step_V0_V = Double.parseDouble(stepV0TextField.getText()) ;
        simDataBase.addNewVariable(new SimulationVariable("step_V0_(V)", new double[]{step_V0_V}));
        stepV0Label.setText("V0 is set to " + step_V0_V + " Volt");
        if(stepToggleIsSelected()){
            stepPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    private void plotStep(){
        double t0 = simDataBase.getVariable("step_t0_(usec)").getValue(0) ;
        double V0 = simDataBase.getVariable("step_V0_(V)").getValue(0) ;
        double tStart_usec = simDataBase.getVariable("tStart_(usec)").getValue(0) ;
        double tEnd_usec = simDataBase.getVariable("tEnd_(usec)").getValue(0) ;
        double[] time_usec = simDataBase.getVariable("time_(usec)").getAllValues() ;
        int M = time_usec.length ;
        double[] heaterStepVoltage_V = new double[M] ;
        voltage = new StepVoltage(t0, 0, V0) ;
        for(int i=0; i<M; i++){
            heaterStepVoltage_V[i] = voltage.getVoltage(time_usec[i]) ;
        }
        double f0 = simDataBase.getVariable("f0").getValue(0) ;
        double nu = simDataBase.getVariable("nu").getValue(0) ;
        double alphaH = simDataBase.getVariable("alphaH").getValue(0) ;
        double kv = simDataBase.getVariable("kv").getValue(0) ;
        double Rlinear = simDataBase.getVariable("Rlinear").getValue(0) ;
        selfHeating = new SelfHeating(alphaH, kv, Rlinear) ;
        double[] deltaT_H = selfHeating.getDeltaT(heaterStepVoltage_V) ;
        double[] step_dc_response = MathUtils.Arrays.times(deltaT_H, (nu-1)/nu) ;
        simDataBase.addNewVariable(new SimulationVariable("heater_step_temperature_(K)", "Heater Temperature (K)", deltaT_H));
        simDataBase.addNewVariable(new SimulationVariable("wg_step_dc_temperature_(K)", step_dc_response));
        simDataBase.addNewVariable(new SimulationVariable("heater_step_voltage_(V)", heaterStepVoltage_V));

        if(stepPlot.getSelectedToggle().equals(stepWaveguidePlotRadioButton)){
            impulse = new ImpulseResponse1D_Modified_FFT(f0, nu) ; // I don't need to build it
            transResponse = new TransientResponse_FFT(selfHeating, impulse, voltage) ;
            transResponse.buildModel();
//            double[] step_response = transResponse.getTimeResponse(time_usec) ;
            double[] step_response = transResponse.transResponse ;

            simDataBase.addNewVariable(new SimulationVariable("wg_step_temperature_(K)", "Waveguide Temperature (K)", step_response));
            MatlabChart fig = new MatlabChart() ;
            fig.plot(simDataBase.getVariableValues("time_(usec)"), simDataBase.getVariableValues("wg_step_temperature_(K)"));
            fig.plot(simDataBase.getVariableValues("time_(usec)"), simDataBase.getVariableValues("wg_step_dc_temperature_(K)"), ":r", 1.0f, "");
            fig.renderPlot();
            fig.xlabel("Time (usec)");
            fig.ylabel("Waveguide Temperature (K)");
            fig.xlim(tStart_usec, tEnd_usec);
            figStep = fig ;
            showPlot(fig, stepMatlabPlotPane);
        }
        else if(stepPlot.getSelectedToggle().equals(stepHeaterPlotRadioButton)){
            MatlabChart fig = new MatlabChart() ;
            fig.plot(simDataBase.getVariableValues("time_(usec)"), simDataBase.getVariableValues("heater_step_temperature_(K)"));
            fig.renderPlot();
            fig.xlabel("Time (usec)");
            fig.ylabel("Heater Temperature (K)");
            fig.xlim(tStart_usec, tEnd_usec);
            figStep = fig ;
            showPlot(fig, stepMatlabPlotPane);
        }

    }

    @FXML
    public void exportToMatlabPressed() throws IOException {
//    	figStep.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
        new PlotterModule(simDataBase) ;
    }

	@Override
	public MatlabChart getFig() {
		return figStep;
	}

	@Override
	public StatusBar getStatusBar() {
		return statusBar ;
	}

	@Override
	public Tab getTab() {
		return stepTab ;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}

}
