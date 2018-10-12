package phoenixSim.tabs.controller;

import java.io.IOException;
import org.controlsfx.control.StatusBar;
import People.Meisam.GUI.PhoenixSim.ModulesLibrary.PlotterModule.PlotterModule;
import People.Meisam.GUI.PhoenixSim.TabsLibrary.AbstractTabController;
import People.Meisam.GUI.Plotters.MatlabPlot.MatlabChart;
import People.Meisam.GUI.Utilities.SimulationDataBase;
import People.Meisam.GUI.Utilities.SimulationVariable;
import PhotonicElements.Heater.Model.ImpulseResponseModel.ImpulseResponse1D_Modified_FFT;
import PhotonicElements.Heater.Model.Structure.SelfHeating;
import PhotonicElements.Heater.Model.TransientResponseModel.TransientResponse_FFT;
import PhotonicElements.Heater.Model.VoltageFunc.AbstractVoltage;
import PhotonicElements.Heater.Model.VoltageFunc.PulseTrainVoltage;
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

public class HeaterPWMTabController extends AbstractTabController {

    SelfHeating selfHeating ;
    ImpulseResponse1D_Modified_FFT impulse ;
    AbstractVoltage voltage ;
    TransientResponse_FFT transResponse ;

    SimulationDataBase simDataBase = new SimulationDataBase() ;
    MatlabChart figPWM ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

	@FXML Tab pwmTab ;
    @FXML Pane pwmMatlabPlotPane ;
    @FXML TextField pwmVpTextField ;
    @FXML TextField pwmDTextField ;
    @FXML TextField pwmFreqTextField ;
    @FXML Label pwmVpLabel ;
    @FXML Label pwmDLabel ;
    @FXML Label pwmFreqLabel ;
    @FXML ToggleGroup pwmPlot ;
    @FXML RadioButton pwmWaveguidePlotRadioButton ;
    @FXML RadioButton pwmHeaterPlotRadioButton ;

    @FXML
    public void initialize(){
        pwmVpLabel.setText("");
        pwmDLabel.setText("");
        pwmFreqLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figPWM = fig ;
        showPlot(fig, pwmMatlabPlotPane);
        pwmMatlabPlotPane.getChildren().add(swingNode) ;
    }

    private boolean pwmToggleIsSelected(){
        if(pwmWaveguidePlotRadioButton.isSelected() || pwmHeaterPlotRadioButton.isSelected()){
            return true ;
        }
        else{
            return false ;
        }
    }

    @FXML
    public void setVpPWM(){
        double PWM_Vp_V = Double.parseDouble(pwmVpTextField.getText()) ;
        simDataBase.addNewVariable(new SimulationVariable("PWM_Vp_(V)", new double[]{PWM_Vp_V}));
        pwmVpLabel.setText("Vp is set to " + PWM_Vp_V + " Volt");
        if(pwmToggleIsSelected()){
            pwmPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setDPWM(){
        double PWM_D = Double.parseDouble(pwmDTextField.getText()) ;
        simDataBase.addNewVariable(new SimulationVariable("PWM_D", new double[]{PWM_D}));
        pwmDLabel.setText("D is set to " + (100*PWM_D) + " %");
        if(pwmToggleIsSelected()){
            pwmPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setFreqPWM(){
        double PWM_freq_kHz = Double.parseDouble(pwmFreqTextField.getText()) ;
        simDataBase.addNewVariable(new SimulationVariable("PWM_freq_(kHz)", new double[]{PWM_freq_kHz}));
        pwmFreqLabel.setText("freq is set to " + PWM_freq_kHz + " kHz");
        if(pwmToggleIsSelected()){
            pwmPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    private void plotPWM(){
        double D = simDataBase.getVariable("PWM_D").getValue(0) ;
        double freq = simDataBase.getVariable("PWM_freq_(kHz)").getValue(0) ;
        double Vp = simDataBase.getVariable("PWM_Vp_(V)").getValue(0) ;
        double[] time_usec = simDataBase.getVariable("time_(usec)").getAllValues() ;
        double tStart_usec = simDataBase.getVariable("tStart_(usec)").getValue(0) ;
        double tEnd_usec = simDataBase.getVariable("tEnd_(usec)").getValue(0) ;
        double tPeriod_usec = (1.0/(freq*1e3))*1e6 ;
        int numPeriods = (int) (MoreMath.Arrays.FindMaximum.getValue(time_usec)/tPeriod_usec)+1 ;
        int M = time_usec.length ;
        double[] heaterPWMVoltage_V = new double[M] ;
        voltage = new PulseTrainVoltage(D, tPeriod_usec, numPeriods, 0, Vp) ;
        for(int i=0; i<M; i++){
            heaterPWMVoltage_V[i] = voltage.getVoltage(time_usec[i]) ;
        }
        double f0 = simDataBase.getVariable("f0").getValue(0) ;
        double nu = simDataBase.getVariable("nu").getValue(0) ;
        double alphaH = simDataBase.getVariable("alphaH").getValue(0) ;
        double kv = simDataBase.getVariable("kv").getValue(0) ;
        double Rlinear = simDataBase.getVariable("Rlinear").getValue(0) ;
        selfHeating = new SelfHeating(alphaH, kv, Rlinear) ;
        double[] deltaT_H = selfHeating.getDeltaT(heaterPWMVoltage_V) ;
        simDataBase.addNewVariable(new SimulationVariable("heater_pwm_temperature_(K)", "Heater Temperature (K)", deltaT_H));
        simDataBase.addNewVariable(new SimulationVariable("heater_pwm_voltage_(V)", heaterPWMVoltage_V));
        double[] deltaT_wg = MoreMath.Arrays.times(deltaT_H, (nu-1)/nu) ;
        simDataBase.addNewVariable(new SimulationVariable("wg_pwm_ideal_(K)", "Waveguide PWM Ideal (K)", deltaT_wg));

        if(pwmPlot.getSelectedToggle().equals(pwmWaveguidePlotRadioButton)){
            impulse = new ImpulseResponse1D_Modified_FFT(f0, nu) ;
            transResponse = new TransientResponse_FFT(selfHeating, impulse, voltage) ;
            transResponse.buildModel();
//            double[] response = transResponse.getTimeResponse(time_usec) ;
            double[] response = transResponse.transResponse ;
            simDataBase.addNewVariable(new SimulationVariable("wg_pwm_temperature_(K)", "Waveguide Temperature (K)", response));
            MatlabChart fig = new MatlabChart() ;
            fig.plot(simDataBase.getVariableValues("time_(usec)"), simDataBase.getVariableValues("wg_pwm_temperature_(K)"));
            fig.RenderPlot();
            fig.xlabel("Time (usec)");
            fig.ylabel("Waveguide Temperature");
            fig.xlim(tStart_usec, tEnd_usec);
            figPWM = fig ;
            showPlot(fig, pwmMatlabPlotPane);
        }
        else if(pwmPlot.getSelectedToggle().equals(pwmHeaterPlotRadioButton)){
            MatlabChart fig = new MatlabChart() ;
            fig.plot(simDataBase.getVariableValues("time_(usec)"), simDataBase.getVariableValues("heater_pwm_temperature_(K)"));
            fig.RenderPlot();
            fig.xlabel("Time (usec)");
            fig.ylabel("Heater Temperature");
            fig.xlim(tStart_usec, tEnd_usec);
            figPWM = fig ;
            showPlot(fig, pwmMatlabPlotPane);
        }

    }

    @FXML
    public void exportToMatlabPressed() throws IOException {
    	figPWM.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
        new PlotterModule(simDataBase) ;
    }

	@Override
	public Tab getTab() {
		return pwmTab ;
	}

	@Override
	public MatlabChart getFig() {
		return figPWM ;
	}

	@Override
	public StatusBar getStatusBar() {
		return statusBar ;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}

}
