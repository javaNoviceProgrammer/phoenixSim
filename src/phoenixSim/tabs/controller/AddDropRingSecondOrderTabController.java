package phoenixSim.tabs.controller;

import static java.lang.Math.PI;
import static mathLib.util.MathUtils.evaluate;

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
import mathLib.util.MathUtils.Conversions;
import phoenixSim.modules.ExportToMatlabModule;
import phoenixSim.modules.PlotterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.ring.AddDropSecondOrder;

public class AddDropRingSecondOrderTabController extends AbstractTabController {

	StatusBar statusBar = new StatusBar() ;
	SimulationDataBase simDataBase = new SimulationDataBase() ;
	MatlabChart fig = new MatlabChart() ;

	@FXML Tab tab ;
	@FXML Pane plotPane ;
	@FXML RadioButton dropLambda ;
	@FXML RadioButton thruLambda ;
	@FXML ToggleGroup matlabPlot ;
	@FXML TextField kappaInTextField ;
	@FXML TextField kappaOutTextField ;
	@FXML TextField kappaMiddleTextField ;
	@FXML TextField radiusTextField ;
	@FXML TextField lossTextField ;
	@FXML Label kappaInLabel ;
	@FXML Label kappaOutLabel ;
	@FXML Label radiusLabel ;
	@FXML Label lossLabel ;
	@FXML Label kappaMiddleLabel ;

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
		kappaInLabel.setText("");
		kappaOutLabel.setText("");
		radiusLabel.setText("");
		lossLabel.setText("");
		kappaMiddleLabel.setText("");
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
        if(dropLambda.isSelected() || thruLambda.isSelected() ){
            return true;
        }
        else{
            return false ;
        }
    }

    @FXML
    public void setInputKappa(){
        if(!kappaInTextField.getText().isEmpty()){
        	try {
                double kappaIn = evaluate(kappaInTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("kappaIn_()", "Input Kappa ()", new double[]{kappaIn}));
                kappaInLabel.setText("kappa_in is set to " + kappaIn + "");
			} catch (Exception e) {
				kappaInTextField.setStyle("-fx-text-inner-color: red;");
				kappaInTextField.setOnKeyTyped(event->{
					kappaInTextField.setStyle("-fx-text-inner-color: black");
				});
			}

        }
        if(toggleSelected()){
            matlabPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setOutputKappa(){
        if(!kappaOutTextField.getText().isEmpty()){
        	try {
                double kappaOut = evaluate(kappaOutTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("kappaOut_()", "Output Kappa ()", new double[]{kappaOut}));
                kappaOutLabel.setText("kappa_out is set to " + kappaOut + "");
			} catch (Exception e) {
				kappaOutTextField.setStyle("-fx-text-inner-color: red;");
				kappaOutTextField.setOnKeyTyped(event->{
					kappaOutTextField.setStyle("-fx-text-inner-color: black");
				});
			}

        }
        if(toggleSelected()){
            matlabPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setMIddleKappa(){
        if(!kappaMiddleTextField.getText().isEmpty()){
        	try {
                double kappaMiddle = evaluate(kappaMiddleTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("kappaMiddle_()", "Middle Kappa ()", new double[]{kappaMiddle}));
                kappaMiddleLabel.setText("kappa_middle is set to " + kappaMiddle + "");
			} catch (Exception e) {
				kappaMiddleTextField.setStyle("-fx-text-inner-color: red;");
				kappaMiddleTextField.setOnKeyTyped(event->{
					kappaMiddleTextField.setStyle("-fx-text-inner-color: black");
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
                double R_um = evaluate(radiusTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("radius_(um)", "Radius (um)", new double[]{R_um}));
                radiusLabel.setText("R is set to " + R_um + " um");
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
    public void setLoss(){
        if(!lossTextField.getText().isEmpty()){
        	try {
                double loss_dBperCm = evaluate(lossTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("loss_(dB/cm)", "Loss (dB/cm)", new double[]{loss_dBperCm}));
                lossLabel.setText("Loss is set to " + loss_dBperCm + " dB/cm");
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
	public void calculateSpectrum(){
		double kappaIn = simDataBase.getVariable("kappaIn_()").getValue(0) ;
		double kappaOut = simDataBase.getVariable("kappaOut_()").getValue(0) ;
		double kappaMiddle = simDataBase.getVariable("kappaMiddle_()").getValue(0) ;
		double radius_um = simDataBase.getVariable("radius_(um)").getValue(0) ;
		double loss_dBperCm = simDataBase.getVariable("loss_(dB/cm)").getValue(0) ;
		double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
		double[] neff = simDataBase.getVariable("neff_()").getAllValues() ;

		if(dropLambda.isSelected()){
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
