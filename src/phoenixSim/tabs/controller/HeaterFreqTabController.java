package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import PhotonicElements.Heater.Model.ImpulseResponseModel.ImpulseResponse1D_Modified_FFT;
import PhotonicElements.Heater.Model.Structure.SelfHeating;
import PhotonicElements.Heater.Model.TransientResponseModel.TransientResponse;
import PhotonicElements.Heater.Model.VoltageFunc.AbstractVoltage;
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import mathLib.plot.MatlabChart;
import phoenixSim.builder.intf.ActionInterface;
import phoenixSim.modules.PlotterModule;
import phoenixSim.modules.VariableSelectorModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;

public class HeaterFreqTabController extends AbstractTabController {

    SelfHeating selfHeating ;
    ImpulseResponse1D_Modified_FFT impulse ;
    AbstractVoltage voltage ;
    TransientResponse transResponse ;
    SimulationDataBase simDataBase = new SimulationDataBase() ;
    public MatlabChart figFreq ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab freqTab ;
    @FXML Pane freqMatlabPlotPane ;
    @FXML TextField f0TextField ;
    @FXML TextField nuTextField ;
    @FXML Label f0Label ;
    @FXML Label nuLabel ;
    @FXML ToggleGroup freqPlot ;
    @FXML RadioButton ampdBPlotRadioButton ;
    @FXML RadioButton ampPlotRadioButton ;
    @FXML RadioButton phaseRadRadioButton ;
    @FXML RadioButton phaseDegreeRadioButton ;

    @FXML
    public void initialize(){
        f0Label.setText("");
        nuLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figFreq = fig ;
        showPlot(fig, freqMatlabPlotPane);
        freqMatlabPlotPane.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<none>");
    }

    private boolean freqToggleIsSelected(){
        if(ampdBPlotRadioButton.isSelected() || ampPlotRadioButton.isSelected() || phaseRadRadioButton.isSelected() || phaseDegreeRadioButton.isSelected()){
            return true ;
        }
        else{
            return false ;
        }
    }

    @FXML
    public void setf0(){
    	String st = f0TextField.getText() ;
    	if(!st.isEmpty()){
    		try {
    	        double f0 = Double.parseDouble(f0TextField.getText()) ;
    	        simDataBase.addNewVariable(new SimulationVariable("f0", new double[]{f0}));
    	        f0Label.setText("f0 is set to " + f0 + " kHz");
			} catch (Exception e) {
    	        double f0 = MoreMath.evaluate(st) ;
    	        simDataBase.addNewVariable(new SimulationVariable("f0", new double[]{f0}));
    	        f0Label.setText("f0 is set to " + String.format("%.4f", f0) + " kHz");
			}
    	}

        if(freqToggleIsSelected()){
            freqPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void chooseF0(){
    	VariableSelectorModule varSelect = new VariableSelectorModule(simDataBase) ;
    	varSelect.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
				double f0 = varSelect.getController().getVariable().getValue(0) ;
    	        simDataBase.addNewVariable(new SimulationVariable("f0", new double[]{f0}));
    	        f0Label.setText("f0 is set to " + String.format("%.4f", f0) + " kHz");
    	        if(freqToggleIsSelected()){
    	            freqPlot.getSelectedToggle().setSelected(false);
    	        }
			}
		});
    }

    @FXML
    public void setNu(){
    	String st = nuTextField.getText() ;
    	if(!st.isEmpty()){
    		try {
    	        double nu = Double.parseDouble(nuTextField.getText()) ;
    	        simDataBase.addNewVariable(new SimulationVariable("nu", new double[]{nu}));
    	        nuLabel.setText("nu is set to " + nu + " ");
			} catch (Exception e) {
    	        double nu = MoreMath.evaluate(st) ;
    	        simDataBase.addNewVariable(new SimulationVariable("nu", new double[]{nu}));
    	        nuLabel.setText("nu is set to " + String.format("%.4f", nu) + " ");
			}
    	}

        if(freqToggleIsSelected()){
            freqPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void chooseNu(){
    	VariableSelectorModule varSelect = new VariableSelectorModule(simDataBase) ;
    	varSelect.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
    	        double nu = varSelect.getController().getVariable().getValue(0) ;
    	        simDataBase.addNewVariable(new SimulationVariable("nu", new double[]{nu}));
    	        nuLabel.setText("nu is set to " + String.format("%.4f", nu) + " ");
    	        if(freqToggleIsSelected()){
    	            freqPlot.getSelectedToggle().setSelected(false);
    	        }
			}
		});
    }

    @FXML
    private void plotFreq(){
        double[] freq_kHz = MoreMath.linspace(1e-3, 500, 1000) ;
        double[] freq_Hz = MoreMath.Arrays.times(freq_kHz, 1e3) ;
        double f0 = simDataBase.getVariable("f0").getValue(0) ;
        double nu = simDataBase.getVariable("nu").getValue(0) ;
        impulse = new ImpulseResponse1D_Modified_FFT(f0, nu) ; // don't need to build it at this point
        double[] amp = impulse.getFreqResponse(freq_Hz) ;
        double[] amp_dB = impulse.getFreqResponsedB(freq_Hz) ;
        double[] phase_rad = impulse.getfreqResponsePhaseRad(freq_Hz) ;
        double[] phase_degree = impulse.getfreqResponsePhaseDegree(freq_Hz) ;
        simDataBase.addNewVariable(new SimulationVariable("freq_(kHz)", "Frequency (kHz)", freq_kHz));
        simDataBase.addNewVariable(new SimulationVariable("amp_(dB)", "Amplitude (dB)", amp_dB));
        simDataBase.addNewVariable(new SimulationVariable("amp", "Amplitude", amp));
        simDataBase.addNewVariable(new SimulationVariable("phase_(Rad)", "Phase (Rad)", phase_rad));
        simDataBase.addNewVariable(new SimulationVariable("phase_(Degree)", "Phase (Degree)", phase_degree));

        if(freqPlot.getSelectedToggle().equals(ampdBPlotRadioButton)){
            figFreq = createPlot(simDataBase.getVariable("freq_(kHz)"), simDataBase.getVariable("amp_(dB)")) ;
            showPlot(figFreq, freqMatlabPlotPane);
        }
        else if(freqPlot.getSelectedToggle().equals(ampPlotRadioButton)){
            figFreq = createPlot(simDataBase.getVariable("freq_(kHz)"), simDataBase.getVariable("amp")) ;
            showPlot(figFreq, freqMatlabPlotPane);
        }
        else if(freqPlot.getSelectedToggle().equals(phaseRadRadioButton)){
            figFreq = createPlot(simDataBase.getVariable("freq_(kHz)"), simDataBase.getVariable("phase_(Rad)")) ;
            showPlot(figFreq, freqMatlabPlotPane);
        }
        else if(freqPlot.getSelectedToggle().equals(phaseDegreeRadioButton)){
            figFreq = createPlot(simDataBase.getVariable("freq_(kHz)"), simDataBase.getVariable("phase_(Degree)")) ;
            showPlot(figFreq, freqMatlabPlotPane);
        }

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
    	figFreq.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() {
        new PlotterModule(simDataBase) ;
    }

    public StatusBar getStatusBar(){
    	return statusBar ;
    }

	public Tab getTab() {
		return freqTab ;
	}

	@Override
	public MatlabChart getFig() {
		return figFreq ;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}




}
