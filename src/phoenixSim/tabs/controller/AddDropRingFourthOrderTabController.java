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
import phoenixSim.modules.PlotterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.ring.AddDropFourthOrder;

public class AddDropRingFourthOrderTabController extends AbstractTabController {

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
	@FXML TextField kappa12TextField ;
	@FXML TextField kappa23TextField ;
	@FXML TextField kappa34TextField ;
	@FXML TextField radiusTextField ;
	@FXML TextField lossTextField ;
	@FXML Label kappaInLabel ;
	@FXML Label kappaOutLabel ;
	@FXML Label radiusLabel ;
	@FXML Label lossLabel ;
	@FXML Label kappa12Label ;
	@FXML Label kappa23Label ;
	@FXML Label kappa34Label ;

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
		kappa12Label.setText("");
		kappa23Label.setText("");
		kappa34Label.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
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
    public void set12Kappa(){
        if(!kappa12TextField.getText().isEmpty()){
        	try {
                double kappa12 = evaluate(kappa12TextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("kappa12_()", "Kappa12 ()", new double[]{kappa12}));
                kappa12Label.setText("kappa_12 is set to " + kappa12 + "");
			} catch (Exception e) {
				kappa12TextField.setStyle("-fx-text-inner-color: red;");
				kappa12TextField.setOnKeyTyped(event->{
					kappa12TextField.setStyle("-fx-text-inner-color: black");
				});
			}

        }
        if(toggleSelected()){
            matlabPlot.getSelectedToggle().setSelected(false);
        }
    }
    
    @FXML
    public void set23Kappa(){
        if(!kappa23TextField.getText().isEmpty()){
        	try {
                double kappa23 = evaluate(kappa23TextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("kappa23_()", "Kappa23 ()", new double[]{kappa23}));
                kappa23Label.setText("kappa_23 is set to " + kappa23 + "");
			} catch (Exception e) {
				kappa23TextField.setStyle("-fx-text-inner-color: red;");
				kappa23TextField.setOnKeyTyped(event->{
					kappa23TextField.setStyle("-fx-text-inner-color: black");
				});
			}

        }
        if(toggleSelected()){
            matlabPlot.getSelectedToggle().setSelected(false);
        }
    }
    
    @FXML
    public void set34Kappa(){
        if(!kappa34TextField.getText().isEmpty()){
        	try {
                double kappa34 = evaluate(kappa34TextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("kappa34_()", "Kappa34 ()", new double[]{kappa34}));
                kappa34Label.setText("kappa_34 is set to " + kappa34 + "");
			} catch (Exception e) {
				kappa34TextField.setStyle("-fx-text-inner-color: red;");
				kappa34TextField.setOnKeyTyped(event->{
					kappa34TextField.setStyle("-fx-text-inner-color: black");
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
		double kappa12 = simDataBase.getVariable("kappa12_()").getValue(0) ;
		double kappa23 = simDataBase.getVariable("kappa23_()").getValue(0) ;
		double kappa34 = simDataBase.getVariable("kappa34_()").getValue(0) ;
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
				AddDropFourthOrder adr = new AddDropFourthOrder(phi_rad, loss, kappaIn, kappaOut, kappa12, kappa23, kappa34) ;
				drop_dB[i] = Conversions.todB(adr.getS31().absSquared()) ;
			}
			simDataBase.addNewVariable(new SimulationVariable("drop_4thOrder_(dB)", "Drop (dB)", drop_dB));
			fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("drop_4thOrder_(dB)")) ;
			showPlot(fig, plotPane);
		}
		else if(thruLambda.isSelected()){
			double[] thru_dB = new double[lambda_nm.length] ;
			for(int i=0; i<lambda_nm.length; i++){
				double phi_rad = 2*PI/(lambda_nm[i]*1e-9)*neff[i]*(2*PI*radius_um*1e-6) ;
				double loss_dB = loss_dBperCm*(2*PI*radius_um*1e-4) ;
				double loss = Conversions.fromdB(-loss_dB) ;
				AddDropFourthOrder adr = new AddDropFourthOrder(phi_rad, loss, kappaIn, kappaOut, kappa12, kappa23, kappa34) ;
				thru_dB[i] = Conversions.todB(adr.getS21().absSquared()) ;
			}
			simDataBase.addNewVariable(new SimulationVariable("thru_4thOrder_(dB)", "Thru (dB)", thru_dB));
			fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("thru_4thOrder_(dB)")) ;
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
//    	fig.exportToMatlab();
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
        fig.RenderPlot();
        fig.xlabel(x.getAlias());
        fig.ylabel(y.getAlias());
        return fig ;
    }

}
