package phoenixSim.tabs.controller;

import static mathLib.util.MathUtils.*;
import static java.lang.Math.PI;

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
import mathLib.util.StringUtils;
import phoenixSim.modules.ExportToMatlabModule;
import phoenixSim.modules.PlotterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.ring.AddDropSecondOrder;

public class AllPassRingSelfHeatingTabController extends AbstractTabController {

	StatusBar statusBar = new StatusBar() ;
	SimulationDataBase simDataBase = new SimulationDataBase() ;
	MatlabChart fig = new MatlabChart() ;

	@FXML Tab tab ;
	@FXML Pane plotPane ;
	@FXML RadioButton driftLambda ;
	@FXML RadioButton thruLambda ;
	@FXML ToggleGroup matlabPlot ;
	@FXML TextField kappaTextField ;
	@FXML TextField lossTextField ;
	@FXML TextField radiusTextField ;
	@FXML TextField fsrTextField ;
	@FXML TextField efficiencyTextField ;
	@FXML TextField powerTextField ;
	@FXML Label kappaLabel ;
	@FXML Label lossLabel ;
	@FXML Label radiusLabel ;
	@FXML Label fsrLabel ;
	@FXML Label efficiencyLabel ;
	@FXML Label powerLabel ;

	@Override
	public void setSimDataBase(SimulationDataBase simDataBase) {
		this.simDataBase = simDataBase ;
	}

	@Override
	public SimulationDataBase getSimDataBase() {
		return simDataBase;
	}

	@Override
	public void initialize() {
		kappaLabel.setText("");
		lossLabel.setText("");
		radiusLabel.setText("");
		fsrLabel.setText("");
		efficiencyLabel.setText("");
		powerLabel.setText("");
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
        // status bar
        statusBar.setText("Dependencies:<WG Modes>");
	}

    private boolean toggleSelected(){
        if(driftLambda.isSelected() || thruLambda.isSelected() ){
            return true;
        }
        else{
            return false ;
        }
    }

    @FXML
    public void setKappa(){
        if(!kappaTextField.getText().isEmpty()){
        	try {
                double kappa = evaluate(kappaTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("kappa_()", "Kappa ()", new double[]{kappa}));
                kappaLabel.setText("kappa is set to " + StringUtils.fixedWidthDoubletoString(kappa, 4) + "");
			} catch (Exception e) {
				kappaTextField.setStyle("-fx-text-inner-color: red");
				kappaTextField.setOnKeyTyped(event->{
					kappaTextField.setStyle("-fx-text-inner-color: black");
				});
			}

        }
        if(toggleSelected()){
            matlabPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setLoss(){
        if(!lossTextField.getText().isEmpty()){
        	try {
                double loss = evaluate(lossTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("loss_(dB/cm)", "Loss (dB/cm)", new double[]{loss}));
                lossLabel.setText("loss is set to " + StringUtils.fixedWidthDoubletoString(loss, 4) + " dB/cm");
			} catch (Exception e) {
				lossTextField.setStyle("-fx-text-inner-color: red;");
				lossTextField.setOnKeyTyped(event->{
				lossTextField.setStyle("-fx-text-inner-color: black");
				});
			}

        }
        if(toggleSelected()){
            matlabPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setRadius(){
        if(!radiusTextField.getText().isEmpty()){
        	try {
                double radius = evaluate(radiusTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("radius_(um)", "Radius (um)", new double[]{radius}));
                radiusLabel.setText("radius is set to " + StringUtils.fixedWidthDoubletoString(radius, 4) + " um");
			} catch (Exception e) {
				radiusTextField.setStyle("-fx-text-inner-color: red;");
				radiusTextField.setOnKeyTyped(event->{
				radiusTextField.setStyle("-fx-text-inner-color: black");
				});
			}

        }
        if(toggleSelected()){
            matlabPlot.getSelectedToggle().setSelected(false);
        }
    }


    @FXML
    public void setFSR(){
        if(!fsrTextField.getText().isEmpty()){
        	try {
                double fsr_nm = evaluate(fsrTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("fsr_(nm)", "FSR (nm)", new double[]{fsr_nm}));
                fsrLabel.setText("FSR is set to " + StringUtils.fixedWidthDoubletoString(fsr_nm, 4) + " nm");
			} catch (Exception e) {
				fsrTextField.setStyle("-fx-text-inner-color: red;");
				fsrTextField.setOnKeyTyped(event->{
				fsrTextField.setStyle("-fx-text-inner-color: black");
				});
			}

        }
        if(toggleSelected()){
            matlabPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setEfficiency(){
        if(!efficiencyTextField.getText().isEmpty()){
        	try {
                double eff = evaluate(efficiencyTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("eff_(nm/mW)", "Efficiency (nm/mW)", new double[]{eff}));
                efficiencyLabel.setText("Efficiency is set to " + StringUtils.fixedWidthDoubletoString(eff, 4) + " nm/mW");
			} catch (Exception e) {
				efficiencyTextField.setStyle("-fx-text-inner-color: red;");
				efficiencyTextField.setOnKeyTyped(event->{
				efficiencyTextField.setStyle("-fx-text-inner-color: black");
				});
			}

        }
        if(toggleSelected()){
            matlabPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setPower(){
        if(!powerTextField.getText().isEmpty()){
        	try {
                double power = evaluate(powerTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("power_(dBm)", "Power (dBm)", new double[]{power}));
                powerLabel.setText("Power is set to " + StringUtils.fixedWidthDoubletoString(power, 4) + " dBm");
			} catch (Exception e) {
				powerTextField.setStyle("-fx-text-inner-color: red;");
				powerTextField.setOnKeyTyped(event->{
				powerTextField.setStyle("-fx-text-inner-color: black");
				});
			}

        }
        if(toggleSelected()){
            matlabPlot.getSelectedToggle().setSelected(false);
        }
    }

	@FXML
	public void calculateSpectrum(){
		double kappaIn = simDataBase.getVariable("kappaIn_()").getValue(0) ;
		double kappaOut = simDataBase.getVariable("kappaOut_()").getValue(0) ;
		double kappaMiddle = simDataBase.getVariable("kappaMiddle_()").getValue(0) ;
		double radius_um = simDataBase.getVariable("radius_(um)").getValue(0) ;
		double loss_dBperCm = simDataBase.getVariable("loss_(dB/cm)").getValue(0) ;
		double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
		double[] neff = simDataBase.getVariable("neff_()").getAllValues() ;

		if(driftLambda.isSelected()){
			double[] drop_dB = new double[lambda_nm.length] ;
			for(int i=0; i<lambda_nm.length; i++){
				double phi_rad = 2*PI/(lambda_nm[i]*1e-9)*neff[i]*(2*PI*radius_um*1e-6) ;
				double loss_dB = loss_dBperCm*(2*PI*radius_um*1e-4) ;
				double loss = Conversions.fromdB(-loss_dB) ;
				AddDropSecondOrder adr = new AddDropSecondOrder(phi_rad, loss, kappaIn, kappaOut, kappaMiddle) ;
				drop_dB[i] = Conversions.todB(adr.getS31().absSquared()) ;
			}
			simDataBase.addNewVariable(new SimulationVariable("drop_2ndOrder_(dB)", "Drop (dB)", drop_dB));
			fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("drop_2ndOrder_(dB)")) ;
			showPlot(fig, plotPane);
		}
		else if(thruLambda.isSelected()){
			double[] thru_dB = new double[lambda_nm.length] ;
			for(int i=0; i<lambda_nm.length; i++){
				double phi_rad = 2*PI/(lambda_nm[i]*1e-9)*neff[i]*(2*PI*radius_um*1e-6) ;
				double loss_dB = loss_dBperCm*(2*PI*radius_um*1e-4) ;
				double loss = Conversions.fromdB(-loss_dB) ;
				AddDropSecondOrder adr = new AddDropSecondOrder(phi_rad, loss, kappaIn, kappaOut, kappaMiddle) ;
				thru_dB[i] = Conversions.todB(adr.getS21().absSquared()) ;
			}
			simDataBase.addNewVariable(new SimulationVariable("thru_2ndOrder_(dB)", "Thru (dB)", thru_dB));
			fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("thru_2ndOrder_(dB)")) ;
			showPlot(fig, plotPane);
		}


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

    @FXML
    public void exportToMatlabPressed() throws IOException {
    	new ExportToMatlabModule(getFig()) ;
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
        new PlotterModule(simDataBase) ;
    }

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}

    private MatlabChart createPlot(SimulationVariable x, SimulationVariable y){
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x.getAllValues(), y.getAllValues());
        fig.renderPlot();
        fig.xlabel(x.getAlias());
        fig.ylabel(y.getAlias());
        return fig ;
    }

}
