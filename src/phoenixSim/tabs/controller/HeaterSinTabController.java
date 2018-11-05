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
import photonics.heater.voltage.SinVoltage;
import photonics.heater.voltage.StepVoltage;

public class HeaterSinTabController extends AbstractTabController {

    SelfHeating selfHeating ;
    ImpulseResponse1D_Modified_FFT impulse ;
    AbstractVoltage voltage ;
    TransientResponse_FFT transResponse ;

    SimulationDataBase simDataBase = new SimulationDataBase() ;
    public MatlabChart figSin ;
    StatusBar statusBar = new StatusBar() ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    @FXML Tab sinTab ;
    @FXML Pane sinMatlabPlotPane ;
    @FXML TextField sint0TextField ;
    @FXML TextField sinV0TextField ;
    @FXML TextField sinFreqTextField ;
    @FXML TextField sinVpTextField ;
    @FXML Label sint0Label ;
    @FXML Label sinV0Label ;
    @FXML Label sinFreqLabel ;
    @FXML Label sinVpLabel ;
    @FXML ToggleGroup sinPlot ;
    @FXML RadioButton sinWaveguidePlotRadioButton ;
    @FXML RadioButton sinHeaterPlotRadioButton ;

    @FXML
    public void initialize(){
        sint0Label.setText("");
        sinV0Label.setText("");
        sinFreqLabel.setText("");
        sinVpLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figSin = fig ;
        showPlot(fig, sinMatlabPlotPane);
        sinMatlabPlotPane.getChildren().add(swingNode) ;
    }

    private boolean sinToggleIsSelected(){
        if(sinWaveguidePlotRadioButton.isSelected() || sinHeaterPlotRadioButton.isSelected()){
            return true ;
        }
        else{
            return false ;
        }
    }

    @FXML
    public void setV0Sin(){
        double sin_V0_V = Double.parseDouble(sinV0TextField.getText()) ;
        simDataBase.addNewVariable(new SimulationVariable("sin_V0_(V)", new double[]{sin_V0_V}));
        sinV0Label.setText("V0 is set to " + sin_V0_V + " Volt");
        if(sinToggleIsSelected()){
            sinPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setT0Sin(){
        double sin_t0_usec = Double.parseDouble(sint0TextField.getText()) ;
        simDataBase.addNewVariable(new SimulationVariable("sin_t0_(usec)", new double[]{sin_t0_usec}));
        sint0Label.setText("t0 is set to " + sin_t0_usec + " usec");
        if(sinToggleIsSelected()){
            sinPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setFreqSin(){
        double sin_freq_kHz = Double.parseDouble(sinFreqTextField.getText()) ;
        simDataBase.addNewVariable(new SimulationVariable("sin_freq_(kHz)", new double[]{sin_freq_kHz}));
        sinFreqLabel.setText("freq is set to " + sin_freq_kHz + " kHz");
        if(sinToggleIsSelected()){
            sinPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setVpSin(){
        double sin_Vp_V = Double.parseDouble(sinVpTextField.getText()) ;
        simDataBase.addNewVariable(new SimulationVariable("sin_Vp_(V)", new double[]{sin_Vp_V}));
        sinVpLabel.setText("Vp is set to " + sin_Vp_V + " Volt");
        if(sinToggleIsSelected()){
            sinPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    private void plotSin(){
        double t0 = simDataBase.getVariable("sin_t0_(usec)").getValue(0) ;
        double V0 = simDataBase.getVariable("sin_V0_(V)").getValue(0) ;
        double freq = simDataBase.getVariable("sin_freq_(kHz)").getValue(0) ;
        double Vp = simDataBase.getVariable("sin_Vp_(V)").getValue(0) ;
        double[] time_usec = simDataBase.getVariable("time_(usec)").getAllValues() ;
        double tStart_usec = simDataBase.getVariable("tStart_(usec)").getValue(0) ;
        double tEnd_usec = simDataBase.getVariable("tEnd_(usec)").getValue(0) ;
        int M = time_usec.length ;
        double[] heaterSinVoltage_V = new double[M] ;
        double[] v_dc = new double[M] ;
        voltage = new SinVoltage(t0, V0, Vp, freq) ;
        StepVoltage vDC = new StepVoltage(t0, 0, V0) ;
        for(int i=0; i<M; i++){
            heaterSinVoltage_V[i] = voltage.getVoltage(time_usec[i]) ;
            v_dc[i] = vDC.getVoltage(time_usec[i]) ;
        }
        double f0 = simDataBase.getVariable("f0").getValue(0) ;
        double nu = simDataBase.getVariable("nu").getValue(0) ;
        double alphaH = simDataBase.getVariable("alphaH").getValue(0) ;
        double kv = simDataBase.getVariable("kv").getValue(0) ;
        double Rlinear = simDataBase.getVariable("Rlinear").getValue(0) ;
        selfHeating = new SelfHeating(alphaH, kv, Rlinear) ;
        double[] deltaT_H = selfHeating.getDeltaT(heaterSinVoltage_V) ;
        double[] deltaT_H_dc = selfHeating.getDeltaT(v_dc) ;
        double[] sin_dc_response = MathUtils.Arrays.times(deltaT_H_dc, (nu-1)/nu) ;
        simDataBase.addNewVariable(new SimulationVariable("heater_sin_temperature_(K)", "Heater Temperature (K)", deltaT_H));
        simDataBase.addNewVariable(new SimulationVariable("wg_sin_dc_temperature_(K)", sin_dc_response));
        simDataBase.addNewVariable(new SimulationVariable("heater_sin_voltage_(V)", heaterSinVoltage_V));

        if(sinPlot.getSelectedToggle().equals(sinWaveguidePlotRadioButton)){
            impulse = new ImpulseResponse1D_Modified_FFT(f0, nu) ;
            transResponse = new TransientResponse_FFT(selfHeating, impulse, voltage) ;
            transResponse.buildModel();
//            double[] response = transResponse.getTimeResponse(time_usec) ;
            double[] response = transResponse.transResponse ;
            simDataBase.addNewVariable(new SimulationVariable("wg_sin_temperature_(K)", "Waveguide Temperature (K)", response));
            MatlabChart fig = new MatlabChart() ;
            fig.plot(simDataBase.getVariableValues("time_(usec)"), simDataBase.getVariableValues("wg_sin_temperature_(K)"));
            fig.plot(simDataBase.getVariableValues("time_(usec)"), simDataBase.getVariableValues("wg_sin_dc_temperature_(K)"), ":r", 1.0f, "");
            fig.RenderPlot();
            fig.xlabel("Time (usec)");
            fig.ylabel("Waveguide Temperature");
            fig.xlim(tStart_usec, tEnd_usec);
            figSin = fig ;
            showPlot(fig, sinMatlabPlotPane);
        }
        else if(sinPlot.getSelectedToggle().equals(sinHeaterPlotRadioButton)){
            MatlabChart fig = new MatlabChart() ;
            fig.plot(simDataBase.getVariableValues("time_(usec)"), simDataBase.getVariableValues("heater_sin_temperature_(K)"));
            fig.RenderPlot();
            fig.xlabel("Time (usec)");
            fig.ylabel("Heater Temperature");
            fig.xlim(tStart_usec, tEnd_usec);
            figSin = fig ;
            showPlot(fig, sinMatlabPlotPane);
        }
    }

    @FXML
    public void exportToMatlabPressed() throws IOException {
//    	figSin.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
        new PlotterModule(simDataBase) ;
    }

    public StatusBar getStatusBar(){
    	return statusBar ;
    }

	@Override
	public Tab getTab() {
		return sinTab ;
	}

	@Override
	public MatlabChart getFig() {
		return figSin ;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}

}
