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
import phoenixSim.builder.intf.ActionInterface;
import phoenixSim.modules.PlotterModule;
import phoenixSim.modules.SweepParameterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.transfer.TransferMatrixTE;
import photonics.transfer.TransferMatrixTM;
import photonics.util.Wavelength;

public class InterfaceTabController extends AbstractTabController {

	StatusBar statusBar = new StatusBar() ;
	SimulationDataBase simDataBase = new SimulationDataBase() ;
	MatlabChart fig = new MatlabChart() ;

	@Override
	public void setSimDataBase(SimulationDataBase simDataBase) {
		this.simDataBase = simDataBase ;
	}

	@Override
	public SimulationDataBase getSimDataBase() {
		return simDataBase;
	}

	@FXML Tab interfaceTab ;
	@FXML Pane plotPane ;
	@FXML TextField n1TextField ;
	@FXML TextField n2TextField ;
	@FXML TextField thetaiTextField ;
	@FXML Label n1Label ;
	@FXML Label n2Label ;
	@FXML Label thetaiLabel ;
	@FXML RadioButton TE ;
	@FXML RadioButton TM ;
	@FXML RadioButton realThetatThetai ;
	@FXML RadioButton imagThetatThetai ;
	@FXML RadioButton Rthetai ;
	@FXML RadioButton Tthetai ;
	@FXML ToggleGroup plot ;
	@FXML ToggleGroup polarization ;

	@FXML
	public void initialize(){
		n1Label.setText("");
		n2Label.setText("");
		thetaiLabel.setText("");
		// status bar
		statusBar.setText("Dependencies:<none>");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.renderPlot();
        fig.xlabel("");
        fig.ylabel("");
        this.fig = fig ;
        showPlot(fig, plotPane);
        plotPane.getChildren().add(swingNode) ;
	}

    private boolean plotSelected(){
        if(realThetatThetai.isSelected() || Rthetai.isSelected() || Tthetai.isSelected() || imagThetatThetai.isSelected()){
            return true;
        }
        else{
            return false ;
        }
    }

//    private boolean polarizationSelected(){
//        if(TE.isSelected() || TM.isSelected()){
//            return true;
//        }
//        else{
//            return false ;
//        }
//    }

    @FXML
    public void setN1(){
        // textbox is used for singnle entry
        if(!n1TextField.getText().isEmpty()){
            double n1 = Double.parseDouble(n1TextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("n1_()", "n1 Index", new double[]{n1}));
            n1Label.setText("n1 is set to " + n1 + " ");
        }
        if(plotSelected()){
        	plot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setN2(){
        // textbox is used for singnle entry
        if(!n2TextField.getText().isEmpty()){
            double n2 = Double.parseDouble(n2TextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("n2_()", "n2 Index", new double[]{n2}));
            n2Label.setText("n2 is set to " + n2 + " ");
        }
        if(plotSelected()){
        	plot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setThetai(){
        // textbox is used for singnle entry
        if(!thetaiTextField.getText().isEmpty()){
            double thetai_degree = Double.parseDouble(thetaiTextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("thetai_()", "Incidence Angle (degree)", new double[]{thetai_degree}));
            thetaiLabel.setText("theta_i is set to " + thetai_degree + " degree");
        }
        if(plotSelected()){
        	plot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void sweepN1() throws IOException {
    	SweepParameterModule sweep = new SweepParameterModule() ;
    	sweep.setExitAction(new ActionInterface() {
			
			@Override
			public void setExitAction() {
                simDataBase.addNewVariable(new SimulationVariable("n1_()", "n1 Index", sweep.getController().getAllValues()));
                // clear text field and update label
                n1TextField.clear();
                n1Label.setText("n1 is set to array values");
			}
		});
    	
        if(plotSelected()){
        	plot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void sweepN2() throws IOException {
    	SweepParameterModule sweep = new SweepParameterModule() ;
    	sweep.setExitAction(new ActionInterface() {
			
			@Override
			public void setExitAction() {
                simDataBase.addNewVariable(new SimulationVariable("n2_()", "n2 Index", sweep.getController().getAllValues()));
                // clear text field and update label
                n2TextField.clear();
                n2Label.setText("n2 is set to array values");
			}
		});
    	
        if(plotSelected()){
        	plot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void sweepThetai() throws IOException {
    	SweepParameterModule sweep = new SweepParameterModule() ;
    	sweep.setExitAction(new ActionInterface() {
			
			@Override
			public void setExitAction() {
                simDataBase.addNewVariable(new SimulationVariable("thetai_(degree)", "Incidence Angle (degree)", sweep.getController().getAllValues()));
                // clear text field and update label
                thetaiTextField.clear();
                thetaiLabel.setText("theta_i is set to array values");
			}
		});

        if(plotSelected()){
        	plot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    private void setPolarization(){
    	calculatePlot() ;
    }

    @FXML
    private void calculatePlot(){
    	double[] n1 = simDataBase.getVariable("n1_()").getAllValues() ;
    	double[] n2 = simDataBase.getVariable("n2_()").getAllValues() ;
    	double[] thetai_degree = simDataBase.getVariable("thetai_(degree)").getAllValues() ;
    	if(realThetatThetai.isSelected()){
    		double[] thetat_degree = new double[thetai_degree.length] ;
    		for(int i=0; i<thetai_degree.length; i++){
        		if(TE.isSelected()){
        			TransferMatrixTE Q = new TransferMatrixTE(new Wavelength(1550), n1[0], n2[0], thetai_degree[i], 0) ;
        			thetat_degree[i] = Q.getComplexAngleOfRefractionDegree().re() ;
        		}
        		else if(TM.isSelected()){
        			TransferMatrixTM Q = new TransferMatrixTM(new Wavelength(1550), n1[0], n2[0], thetai_degree[i], 0) ;
        			thetat_degree[i] = Q.getComplexAngleOfRefractionDegree().re() ;
        		}
    		}
    		simDataBase.addNewVariable(new SimulationVariable("real_thetat_(degree)", "Re(Theta_t)", thetat_degree));
    		fig = createPlot(simDataBase.getVariable("thetai_(degree)"), simDataBase.getVariable("real_thetat_(degree)")) ;
    		showPlot(fig, plotPane);
    	}
    	else if(imagThetatThetai.isSelected()){
    		double[] thetat_degree = new double[thetai_degree.length] ;
    		for(int i=0; i<thetai_degree.length; i++){
        		if(TE.isSelected()){
        			TransferMatrixTE Q = new TransferMatrixTE(new Wavelength(1550), n1[0], n2[0], thetai_degree[i], 0) ;
        			thetat_degree[i] = Q.getComplexAngleOfRefractionDegree().im() ;
        		}
        		else if(TM.isSelected()){
        			TransferMatrixTM Q = new TransferMatrixTM(new Wavelength(1550), n1[0], n2[0], thetai_degree[i], 0) ;
        			thetat_degree[i] = Q.getComplexAngleOfRefractionDegree().im() ;
        		}
    		}
    		simDataBase.addNewVariable(new SimulationVariable("imag_thetat_(degree)", "Im(Theta_t)", thetat_degree));
    		fig = createPlot(simDataBase.getVariable("thetai_(degree)"), simDataBase.getVariable("imag_thetat_(degree)")) ;
    		showPlot(fig, plotPane);
    	}
    	else if(Rthetai.isSelected()){
    		double[] R = new double[thetai_degree.length] ;
    		for(int i=0; i<thetai_degree.length; i++){
        		if(TE.isSelected()){
        			TransferMatrixTE Q = new TransferMatrixTE(new Wavelength(1550), n1[0], n2[0], thetai_degree[i], 0) ;
        			R[i] = Q.getPowerReflection() ;
        		}
        		else if(TM.isSelected()){
        			TransferMatrixTM Q = new TransferMatrixTM(new Wavelength(1550), n1[0], n2[0], thetai_degree[i], 0) ;
        			R[i] = Q.getPowerReflection() ;
        		}
    		}
    		simDataBase.addNewVariable(new SimulationVariable("R_()", "Power Reflection Ratio", R));
    		fig = createPlot(simDataBase.getVariable("thetai_(degree)"), simDataBase.getVariable("R_()")) ;
    		showPlot(fig, plotPane);
    	}
    	else if(Tthetai.isSelected()){
    		double[] T = new double[thetai_degree.length] ;
    		for(int i=0; i<thetai_degree.length; i++){
        		if(TE.isSelected()){
        			TransferMatrixTE Q = new TransferMatrixTE(new Wavelength(1550), n1[0], n2[0], thetai_degree[i], 0) ;
        			T[i] = Q.getPowerTransmission();
        		}
        		else if(TM.isSelected()){
        			TransferMatrixTM Q = new TransferMatrixTM(new Wavelength(1550), n1[0], n2[0], thetai_degree[i], 0) ;
        			T[i] = Q.getPowerTransmission();
        		}
    		}
    		simDataBase.addNewVariable(new SimulationVariable("T_()", "Power Transmission Ratio", T));
    		fig = createPlot(simDataBase.getVariable("thetai_(degree)"), simDataBase.getVariable("T_()")) ;
    		showPlot(fig, plotPane);
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
//    	fig.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
    	new PlotterModule(simDataBase) ;
    }

	@Override
	public Tab getTab() {
		return interfaceTab;
	}

	@Override
	public MatlabChart getFig() {
		return fig;
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
