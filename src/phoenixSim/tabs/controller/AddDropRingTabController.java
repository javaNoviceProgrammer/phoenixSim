package phoenixSim.tabs.controller;

import static java.lang.Math.PI;
import static mathLib.util.MathUtils.evaluate;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.function.Supplier;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.controlsfx.control.StatusBar;

import flanagan.interpolation.LinearInterpolation;
import gds.elements.AbstractElement;
import gds.elements.DataBase.Entry;
import gds.elements.basics.AddDropRing;
import gds.elements.positioning.Port;
import gds.elements.positioning.Position;
import gds.headers.Footer;
import gds.headers.Header;
import gds.layout.cells.Cell;
import gds.pdk.AbstractLayerMap;
import gds.pdk.generic.GeneralLayer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import mathLib.plot.MatlabChart;
import phoenixSim.modules.PlotterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.FileChooserFX;
import phoenixSim.util.OSDetector;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.ring.AddDropRingGeneral;

public class AddDropRingTabController extends AbstractTabController {

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
	@FXML TextField radiusTextField ;
	@FXML TextField lossTextField ;
	@FXML Label kappaInLabel ;
	@FXML Label kappaOutLabel ;
	@FXML Label radiusLabel ;
	@FXML Label lossLabel ;

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
		double radius_um = simDataBase.getVariable("radius_(um)").getValue(0) ;
		double loss_dBperCm = simDataBase.getVariable("loss_(dB/cm)").getValue(0) ;
		double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
		double[] neff = simDataBase.getVariable("neff_()").getAllValues() ;

		if(dropLambda.isSelected()){
			double[] drop_dB = new double[lambda_nm.length] ;
			for(int i=0; i<lambda_nm.length; i++){
				double phi_rad = 2*PI/(lambda_nm[i]*1e-9)*neff[i]*(2*PI*radius_um*1e-6) ;
				double loss_dB = loss_dBperCm*(2*PI*radius_um*1e-4) ;
				AddDropRingGeneral adr = new AddDropRingGeneral(kappaIn, kappaOut, loss_dB, phi_rad) ;
				drop_dB[i] = adr.getDropTransmissionDB() ;
			}
			simDataBase.addNewVariable(new SimulationVariable("drop_1stOrder_(dB)", "Drop (dB)", drop_dB));
			fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("drop_1stOrder_(dB)")) ;
			showPlot(fig, plotPane);
		}
		else if(thruLambda.isSelected()){
			double[] thru_dB = new double[lambda_nm.length] ;
			for(int i=0; i<lambda_nm.length; i++){
				double phi_rad = 2*PI/(lambda_nm[i]*1e-9)*neff[i]*(2*PI*radius_um*1e-6) ;
				double loss_dB = loss_dBperCm*(2*PI*radius_um*1e-4) ;
				AddDropRingGeneral adr = new AddDropRingGeneral(kappaIn, kappaOut, loss_dB, phi_rad) ;
				thru_dB[i] = adr.getThruTransmissionDB() ;
			}
			simDataBase.addNewVariable(new SimulationVariable("thru_1stOrder_(dB)", "Thru (dB)", thru_dB));
			fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("thru_1stOrder_(dB)")) ;
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
		try {
			// setp1: create gds object
			double width_nm = simDataBase.getVariable("width_(nm)").getValue(0) ;
			double width_um = width_nm*1e-3 ;
			double[] gaps_nm = simDataBase.getVariable("gap_ringwg_(nm)").getAllValues() ;
			double[] kappas = simDataBase.getVariable("kappa_ringwg_()").getAllValues() ;
			LinearInterpolation interpolator = new LinearInterpolation(kappas, gaps_nm) ;
			double inputKappa = simDataBase.getVariable("kappaIn_()").getValue(0) ;
			double outputKappa = simDataBase.getVariable("kappaOut_()").getValue(0) ;
			double inputGap_nm = interpolator.interpolate(inputKappa) ;
			double outputGap_nm = interpolator.interpolate(outputKappa) ;
			double radius_um = simDataBase.getVariable("radius_(um)").getValue(0) ;
			AddDropRing element = new AddDropRing("AddDropRing", new AbstractLayerMap[]{new GeneralLayer("general", 1, 0)}, "port1", new Port(new Position(0, 0), width_um, 180), new Entry(width_um), new Entry(radius_um), new Entry(inputGap_nm), new Entry(outputGap_nm)) ;
			// step2: file chooser
			FileChooserFX fc = new FileChooserFX() ;
			fc.setExtension("gds");
			fc.saveFile();
			String fullPath = fc.getFilePath_NoExtension() ;
			String cellName = fc.getFileName_NoExtension() ;
			// step3: create python file
			Cell cell = new Cell(cellName, new AbstractElement[]{element}) ;
			Header header = new Header(true, true, true, true, true) ;
			header.writeToFile(fullPath);
			cell.appendToFile(fullPath);
			Footer footer = new Footer() ;
			footer.appendToFile(cellName, fullPath);
			// step 4: compile the python file: check the OS first
			String command = "" ;
			if(OSDetector.isMac()){
				command = "/Library/Frameworks/Python.framework/Versions/3.5/bin/python3 " + fullPath + ".py" ;
			}
			else if(OSDetector.isWindows()){
				command = "python " + fullPath + ".py" ;
			}
			try {
				Runtime.getRuntime().exec(command) ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
	    	Runnable r = new Runnable(){
				@Override
				public void run() {
					JOptionPane.showMessageDialog(
			                 ((Supplier<JDialog>) () -> {final JDialog dialog = new JDialog(); dialog.setAlwaysOnTop(true); return dialog;}).get()
			                 , "You must first run Coupler Designer...");
				}
	    	} ;
	    	EventQueue.invokeLater(r);
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

}
