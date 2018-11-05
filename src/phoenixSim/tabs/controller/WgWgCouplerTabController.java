package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import GDS.Elements.BasicElements.DirectionalCoupler;
import GDS.Elements.Positioning.Port;
import GDS.Elements.Positioning.Position;
import GDS.Headers.Footer;
import GDS.PDK.AbstractLayerMap;
import GDS.PDK.AIMLayerMap.SiliconLevelMasks.SEAM;
import PhotonicElements.DirectionalCoupler.DistributedCoupler.DistributedCouplerStripWg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import mathLib.plot.MatlabChart;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.DataCollectorController;
import phoenixSim.util.FileChooserFX;
import phoenixSim.util.OSDetector;
import phoenixSim.util.PlotterController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.util.Wavelength;

public class WgWgCouplerTabController extends AbstractTabController {


    // defining simulation database and the figures in the simulation
    SimulationDataBase simDataBase = new SimulationDataBase() ;

    public MatlabChart figWgWg  ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab wgWgTab ;
    @FXML Pane wgWgMatlabPlotPane ;
    @FXML TextField lengthTextField ;
    @FXML TextField lossTextField ;
    @FXML Label lengthLabel ;
    @FXML Label lossLabel ;
    @FXML ToggleGroup wgWgPlot ;
    @FXML RadioButton kLambdaRadioButton ;
    @FXML RadioButton kLRadioButton ;
    @FXML RadioButton kgapRadioButton ;
    @FXML RadioButton kWRadioButton ;
    @FXML RadioButton kHRadioButton ;
    @FXML RadioButton tLambdaRadioButton ;
    @FXML RadioButton tLRadioButton ;
    @FXML RadioButton tgapRadioButton ;
    @FXML RadioButton tWRadioButton ;
    @FXML RadioButton tHRadioButton ;

    @FXML
    public void initialize(){
    	lengthLabel.setText("");
    	lossLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.renderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figWgWg = fig ;
        showPlot(fig, wgWgMatlabPlotPane);
        wgWgMatlabPlotPane.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<? WG Modes>,<? Coupled Modes>");
    }

    private boolean wgWgPlotToggleSelected(){
        if(kLambdaRadioButton.isSelected() || kLRadioButton.isSelected() || kgapRadioButton.isSelected() || kWRadioButton.isSelected() || kHRadioButton.isSelected() ||
        		tLambdaRadioButton.isSelected() || tLRadioButton.isSelected() || tgapRadioButton.isSelected() || tWRadioButton.isSelected() || tHRadioButton.isSelected()){
            return true;
        }
        else{
            return false ;
        }
    }

    @FXML
    public void setLength(){
        // textbox is used for singnle entry
        if(!lengthTextField.getText().isEmpty()){
            double length_nm = Double.parseDouble(lengthTextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("length_(um)", "Length (um)", new double[]{length_nm}));
            lengthLabel.setText("length is set to " + length_nm + " um");
        }
        if(wgWgPlotToggleSelected()){
        	wgWgPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void sweepLength() throws IOException {
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/DataInput/MainGUI/dataCollector.fxml")) ;
        WindowBuilder window = new WindowBuilder(loader) ;
        window.setIcon("/People/Meisam/GUI/DataInput/Extras/dataCollector.png");
        window.build("Data Collector", false);
        DataCollectorController controller = loader.getController() ;
        controller.initialize();
        controller.getExitButton().setOnAction(e -> {
            try {
                if(simDataBase.variableExists("length_(um)")){
                    simDataBase.removeVariable(simDataBase.getVariable("length_(um)"));
                }
                simDataBase.addNewVariable(new SimulationVariable("length_(um)", "Length (um)", controller.getAllValues()));
                // clear text field and update label
                lengthTextField.clear();
                lengthLabel.setText("length is set to array values");
                window.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        if(wgWgPlotToggleSelected()){
        	wgWgPlot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void setLoss(){
        // textbox is used for singnle entry
        if(!lossTextField.getText().isEmpty()){
            double alpha_dBperCm = Double.parseDouble(lossTextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("wgLoss_(dB/cm)", "WG Loss (dB/cm)", new double[]{alpha_dBperCm}));
            lossLabel.setText("loss is set to " + alpha_dBperCm + " dB/cm");
        }
        if(wgWgPlotToggleSelected()){
        	wgWgPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void sweepLoss() throws IOException {
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/DataInput/MainGUI/dataCollector.fxml")) ;
        WindowBuilder window = new WindowBuilder(loader) ;
        window.setIcon("/People/Meisam/GUI/DataInput/Extras/dataCollector.png");
        window.build("Data Collector", false);
        DataCollectorController controller = loader.getController() ;
        controller.initialize();
        controller.getExitButton().setOnAction(e -> {
            try {
                if(simDataBase.variableExists("wgLoss_(dB/cm)")){
                    simDataBase.removeVariable(simDataBase.getVariable("wgLoss_(dB/cm)"));
                }
                simDataBase.addNewVariable(new SimulationVariable("wgLoss_(dB/cm)", "WG Loss (dB/cm)", controller.getAllValues()));
                // clear text field and update label
                lossTextField.clear();
                lossLabel.setText("loss is set to array values");
                window.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        if(wgWgPlotToggleSelected()){
        	wgWgPlot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void plotWgWg(){
        if(kLambdaRadioButton.isSelected()){
//            double width_nm = simDataBase.getVariable("width_(nm)").getValue(0) ;
//            double height_nm = simDataBase.getVariable("height_(nm)").getValue(0) ;
            double gap_nm = simDataBase.getVariable("gap_(nm)").getValue(0) ;
            double alpha_dBperCm = simDataBase.getVariable("wgLoss_(dB/cm)").getValue(0) ;
            double length_um = simDataBase.getVariable("length_(um)").getValue(0) ;
            double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
            double[] kappa = new double[lambda_nm.length] ;
            for(int i=0; i<kappa.length; i++){
            	double neffEven = simDataBase.getVariable("neff_even_()").getValue(i) ;
            	double neffOdd = simDataBase.getVariable("neff_odd_()").getValue(i) ;
            	DistributedCouplerStripWg dc = new DistributedCouplerStripWg(new Wavelength(lambda_nm[i]), alpha_dBperCm, length_um, gap_nm, neffEven, neffOdd) ;
            	kappa[i] = dc.S31.abs() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("kappa_wgwg_()", "Kappa", kappa));
            // finally plotting
            figWgWg = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("kappa_wgwg_()")) ;
            showPlot(figWgWg, wgWgMatlabPlotPane);
        }
        else if(kLRadioButton.isSelected()){
//            double width_nm = simDataBase.getVariable("width_(nm)").getValue(0) ;
//            double height_nm = simDataBase.getVariable("height_(nm)").getValue(0) ;
            double gap_nm = simDataBase.getVariable("gap_(nm)").getValue(0) ;
            double alpha_dBperCm = simDataBase.getVariable("wgLoss_(dB/cm)").getValue(0) ;
            double[] length_um = simDataBase.getVariable("length_(um)").getAllValues() ;
            double lambda_nm = simDataBase.getVariable("lambda_(nm)").getValue(0) ;
            double[] kappa = new double[length_um.length] ;
            for(int i=0; i<kappa.length; i++){
            	double neffEven = simDataBase.getVariable("neff_even_()").getValue(0) ;
            	double neffOdd = simDataBase.getVariable("neff_odd_()").getValue(0) ;
            	DistributedCouplerStripWg dc = new DistributedCouplerStripWg(new Wavelength(lambda_nm), alpha_dBperCm, length_um[i], gap_nm, neffEven, neffOdd) ;
            	kappa[i] = dc.S31.abs() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("kappa_wgwg_()", "Kappa", kappa));
            // finally plotting
            figWgWg = createPlot(simDataBase.getVariable("length_(um)"), simDataBase.getVariable("kappa_wgwg_()")) ;
            showPlot(figWgWg, wgWgMatlabPlotPane);
        }
        else if(kgapRadioButton.isSelected()){
//            double width_nm = simDataBase.getVariable("width_(nm)").getValue(0) ;
//            double height_nm = simDataBase.getVariable("height_(nm)").getValue(0) ;
            double[] gap_nm = simDataBase.getVariable("gap_(nm)").getAllValues() ;
            double alpha_dBperCm = simDataBase.getVariable("wgLoss_(dB/cm)").getValue(0) ;
            double length_um = simDataBase.getVariable("length_(um)").getValue(0) ;
            double lambda_nm = simDataBase.getVariable("lambda_(nm)").getValue(0) ;
            double[] kappa = new double[gap_nm.length] ;
            for(int i=0; i<kappa.length; i++){
            	double neffEven = simDataBase.getVariable("neff_even_()").getValue(i) ;
            	double neffOdd = simDataBase.getVariable("neff_odd_()").getValue(i) ;
            	DistributedCouplerStripWg dc = new DistributedCouplerStripWg(new Wavelength(lambda_nm), alpha_dBperCm, length_um, gap_nm[i], neffEven, neffOdd) ;
            	kappa[i] = dc.S31.abs() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("kappa_wgwg_()", "Kappa", kappa));
            // finally plotting
            figWgWg = createPlot(simDataBase.getVariable("gap_(nm)"), simDataBase.getVariable("kappa_wgwg_()")) ;
            showPlot(figWgWg, wgWgMatlabPlotPane);
        }
        else if(kWRadioButton.isSelected()){
            double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
//            double height_nm = simDataBase.getVariable("height_(nm)").getValue(0) ;
            double gap_nm = simDataBase.getVariable("gap_(nm)").getValue(0) ;
            double alpha_dBperCm = simDataBase.getVariable("wgLoss_(dB/cm)").getValue(0) ;
            double length_um = simDataBase.getVariable("length_(um)").getValue(0) ;
            double lambda_nm = simDataBase.getVariable("lambda_(nm)").getValue(0) ;
            double[] kappa = new double[width_nm.length] ;
            for(int i=0; i<kappa.length; i++){
            	double neffEven = simDataBase.getVariable("neff_even_()").getValue(i) ;
            	double neffOdd = simDataBase.getVariable("neff_odd_()").getValue(i) ;
            	DistributedCouplerStripWg dc = new DistributedCouplerStripWg(new Wavelength(lambda_nm), alpha_dBperCm, length_um, gap_nm, neffEven, neffOdd) ;
            	kappa[i] = dc.S31.abs() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("kappa_wgwg_()", "Kappa", kappa));
            // finally plotting
            figWgWg = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("kappa_wgwg_()")) ;
            showPlot(figWgWg, wgWgMatlabPlotPane);
        }
        else if(kHRadioButton.isSelected()){
//            double width_nm = simDataBase.getVariable("width_(nm)").getValue(0) ;
            double[] height_nm = simDataBase.getVariable("height_(nm)").getAllValues() ;
            double gap_nm = simDataBase.getVariable("gap_(nm)").getValue(0) ;
            double alpha_dBperCm = simDataBase.getVariable("wgLoss_(dB/cm)").getValue(0) ;
            double length_um = simDataBase.getVariable("length_(um)").getValue(0) ;
            double lambda_nm = simDataBase.getVariable("lambda_(nm)").getValue(0) ;
            double[] kappa = new double[height_nm.length] ;
            for(int i=0; i<kappa.length; i++){
            	double neffEven = simDataBase.getVariable("neff_even_()").getValue(i) ;
            	double neffOdd = simDataBase.getVariable("neff_odd_()").getValue(i) ;
            	DistributedCouplerStripWg dc = new DistributedCouplerStripWg(new Wavelength(lambda_nm), alpha_dBperCm, length_um, gap_nm, neffEven, neffOdd) ;
            	kappa[i] = dc.S31.abs() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("kappa_wgwg_()", "Kappa", kappa));
            // finally plotting
            figWgWg = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("kappa_wgwg_()")) ;
            showPlot(figWgWg, wgWgMatlabPlotPane);
        }
        else if(tLambdaRadioButton.isSelected()){
//            double width_nm = simDataBase.getVariable("width_(nm)").getValue(0) ;
//            double height_nm = simDataBase.getVariable("height_(nm)").getValue(0) ;
            double gap_nm = simDataBase.getVariable("gap_(nm)").getValue(0) ;
            double alpha_dBperCm = simDataBase.getVariable("wgLoss_(dB/cm)").getValue(0) ;
            double length_um = simDataBase.getVariable("length_(um)").getValue(0) ;
            double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
            double[] t = new double[lambda_nm.length] ;
            for(int i=0; i<t.length; i++){
            	double neffEven = simDataBase.getVariable("neff_even_()").getValue(i) ;
            	double neffOdd = simDataBase.getVariable("neff_odd_()").getValue(i) ;
            	DistributedCouplerStripWg dc = new DistributedCouplerStripWg(new Wavelength(lambda_nm[i]), alpha_dBperCm, length_um, gap_nm, neffEven, neffOdd) ;
            	t[i] = dc.S21.abs() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("t_wgwg_()", "t", t));
            // finally plotting
            figWgWg = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("t_wgwg_()")) ;
            showPlot(figWgWg, wgWgMatlabPlotPane);
        }
        else if(tLRadioButton.isSelected()){
//            double width_nm = simDataBase.getVariable("width_(nm)").getValue(0) ;
//            double height_nm = simDataBase.getVariable("height_(nm)").getValue(0) ;
            double gap_nm = simDataBase.getVariable("gap_(nm)").getValue(0) ;
            double alpha_dBperCm = simDataBase.getVariable("wgLoss_(dB/cm)").getValue(0) ;
            double[] length_um = simDataBase.getVariable("length_(um)").getAllValues() ;
            double lambda_nm = simDataBase.getVariable("lambda_(nm)").getValue(0) ;
            double[] t = new double[length_um.length] ;
            for(int i=0; i<t.length; i++){
            	double neffEven = simDataBase.getVariable("neff_even_()").getValue(0) ;
            	double neffOdd = simDataBase.getVariable("neff_odd_()").getValue(0) ;
            	DistributedCouplerStripWg dc = new DistributedCouplerStripWg(new Wavelength(lambda_nm), alpha_dBperCm, length_um[i], gap_nm, neffEven, neffOdd) ;
            	t[i] = dc.S21.abs() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("t_wgwg_()", "t", t));
            // finally plotting
            figWgWg = createPlot(simDataBase.getVariable("length_(um)"), simDataBase.getVariable("t_wgwg_()")) ;
            showPlot(figWgWg, wgWgMatlabPlotPane);
        }
        else if(tgapRadioButton.isSelected()){
//            double width_nm = simDataBase.getVariable("width_(nm)").getValue(0) ;
//            double height_nm = simDataBase.getVariable("height_(nm)").getValue(0) ;
            double[] gap_nm = simDataBase.getVariable("gap_(nm)").getAllValues() ;
            double alpha_dBperCm = simDataBase.getVariable("wgLoss_(dB/cm)").getValue(0) ;
            double length_um = simDataBase.getVariable("length_(um)").getValue(0) ;
            double lambda_nm = simDataBase.getVariable("lambda_(nm)").getValue(0) ;
            double[] t = new double[gap_nm.length] ;
            for(int i=0; i<t.length; i++){
            	double neffEven = simDataBase.getVariable("neff_even_()").getValue(i) ;
            	double neffOdd = simDataBase.getVariable("neff_odd_()").getValue(i) ;
            	DistributedCouplerStripWg dc = new DistributedCouplerStripWg(new Wavelength(lambda_nm), alpha_dBperCm, length_um, gap_nm[i], neffEven, neffOdd) ;
            	t[i] = dc.S21.abs() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("t_wgwg_()", "t", t));
            // finally plotting
            figWgWg = createPlot(simDataBase.getVariable("gap_(nm)"), simDataBase.getVariable("t_wgwg_()")) ;
            showPlot(figWgWg, wgWgMatlabPlotPane);
        }
        else if(tWRadioButton.isSelected()){
            double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
//            double height_nm = simDataBase.getVariable("height_(nm)").getValue(0) ;
            double gap_nm = simDataBase.getVariable("gap_(nm)").getValue(0) ;
            double alpha_dBperCm = simDataBase.getVariable("wgLoss_(dB/cm)").getValue(0) ;
            double length_um = simDataBase.getVariable("length_(um)").getValue(0) ;
            double lambda_nm = simDataBase.getVariable("lambda_(nm)").getValue(0) ;
            double[] t = new double[width_nm.length] ;
            for(int i=0; i<t.length; i++){
            	double neffEven = simDataBase.getVariable("neff_even_()").getValue(i) ;
            	double neffOdd = simDataBase.getVariable("neff_odd_()").getValue(i) ;
            	DistributedCouplerStripWg dc = new DistributedCouplerStripWg(new Wavelength(lambda_nm), alpha_dBperCm, length_um, gap_nm, neffEven, neffOdd) ;
            	t[i] = dc.S21.abs() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("t_wgwg_()", "t", t));
            // finally plotting
            figWgWg = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("t_wgwg_()")) ;
            showPlot(figWgWg, wgWgMatlabPlotPane);
        }
        else if(tHRadioButton.isSelected()){
//            double width_nm = simDataBase.getVariable("width_(nm)").getValue(0) ;
            double[] height_nm = simDataBase.getVariable("height_(nm)").getAllValues() ;
            double gap_nm = simDataBase.getVariable("gap_(nm)").getValue(0) ;
            double alpha_dBperCm = simDataBase.getVariable("wgLoss_(dB/cm)").getValue(0) ;
            double length_um = simDataBase.getVariable("length_(um)").getValue(0) ;
            double lambda_nm = simDataBase.getVariable("lambda_(nm)").getValue(0) ;
            double[] t = new double[height_nm.length] ;
            for(int i=0; i<t.length; i++){
            	double neffEven = simDataBase.getVariable("neff_even_()").getValue(i) ;
            	double neffOdd = simDataBase.getVariable("neff_odd_()").getValue(i) ;
            	DistributedCouplerStripWg dc = new DistributedCouplerStripWg(new Wavelength(lambda_nm), alpha_dBperCm, length_um, gap_nm, neffEven, neffOdd) ;
            	t[i] = dc.S21.abs() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("t_wgwg_()", "t", t));
            // finally plotting
            figWgWg = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("t_wgwg_()")) ;
            showPlot(figWgWg, wgWgMatlabPlotPane);
        }

    }


//    private void showPlot(MatlabChart fig, Pane pane){
//        int width = 500, height = 400 ;
//        pane.getChildren().remove(fig.getChartSwingNode(width, height)) ;
//        pane.getChildren().add(fig.getChartSwingNode(width, height)) ;
//        pane.setPrefSize((double) width, (double) height);
//    }

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
    	figWgWg.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Plotters/MainGUI/plotter.fxml")) ;
        WindowBuilder plotter = new WindowBuilder(loader) ;
        plotter.setIcon("/People/Meisam/GUI/Plotters/MainGUI/Extras/plotter.png");
        plotter.build("Plotter v0.5 Beta", true);
        PlotterController controller = (PlotterController) plotter.getController() ;
        controller.setDataBase(simDataBase);
        controller.initialize();
    }

    public StatusBar getStatusBar(){
    	return statusBar ;
    }

	public Tab getTab() {
		return wgWgTab ;
	}

	@Override
	public MatlabChart getFig() {
		return figWgWg ;
	}

	@FXML
	public void generateGDS(){
		// step1: file chooser
		FileChooserFX fc = new FileChooserFX() ;
		fc.setExtension("gds");
		fc.saveFile();
		String fullPath = fc.getFilePath_NoExtension() ;
		String cellName = fc.getFileName_NoExtension() ;
		// setp2: create gds object
		double width_nm = simDataBase.getVariable("width_(nm)").getValue(0) ;
		double width_um = width_nm/1e3 ;
		double length_um = simDataBase.getVariable("length_(um)").getValue(0) ;
		double gap_nm = simDataBase.getVariable("gap_(nm)").getValue(0) ;
		DirectionalCoupler dc = new DirectionalCoupler("dc", new AbstractLayerMap[]{new SEAM()}, "port1", new Port(new Position(0,0), width_um, 180), new Entry(length_um), new Entry(gap_nm), new Entry(10), new Entry(10)) ;
		// step3: create python file
		Cell cell = new Cell(cellName, new AbstractElement[]{dc}) ;
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
	}



}
