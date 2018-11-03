package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import GDS.Elements.BasicElements.RingWg;
import GDS.Elements.Positioning.Port;
import GDS.Elements.Positioning.Position;
import GDS.Headers.Footer;
import GDS.PDK.AbstractLayerMap;
import GDS.PDK.AIMLayerMap.SiliconLevelMasks.SEAM;
import PhotonicElements.DirectionalCoupler.RingWgCoupling.RaceTrackCoupler;
import PhotonicElements.Waveguides.WaveguideProperties.WgProperties;
import flanagan.interpolation.CubicSpline;
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

public class RaceTrackCouplerTabController extends AbstractTabController {

    // defining simulation database and the figures in the simulation
    SimulationDataBase simDataBase = new SimulationDataBase() ;

    public MatlabChart figRingWg  ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab ringWgTab ;
    @FXML Pane ringWgMatlabPlotPane ;
    @FXML TextField radiusTextField ;
    @FXML TextField dTextField ;
    @FXML TextField lengthTextField ;
    @FXML Label radiusLabel ;
    @FXML Label dLabel ;
    @FXML Label lengthLabel ;
    @FXML ToggleGroup ringWgPlot ;
    @FXML RadioButton kLambdaRingWg ;
    @FXML RadioButton kRadiusRingWg ;
    @FXML RadioButton kdRingWg ;
    @FXML RadioButton tLambdaRingWg ;
    @FXML RadioButton tRadiusRingWg ;
    @FXML RadioButton tdRingWg ;

    @FXML
    public void initialize(){
    	radiusLabel.setText("");
    	dLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figRingWg = fig ;
        showPlot(fig, ringWgMatlabPlotPane);
        ringWgMatlabPlotPane.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<? WG Modes>,<? Coupled Modes>");
    }

    private boolean ringWgPlotToggleSelected(){
        if(kLambdaRingWg.isSelected() || kRadiusRingWg.isSelected() || kdRingWg.isSelected() ||
        		tLambdaRingWg.isSelected() || tRadiusRingWg.isSelected() || tdRingWg.isSelected() ){
            return true;
        }
        else{
            return false ;
        }
    }

    @FXML
    public void setRadius(){
        // textbox is used for singnle entry
        if(!radiusTextField.getText().isEmpty()){
            double radius_um = Double.parseDouble(radiusTextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("radius_(um)", "Radius (um)", new double[]{radius_um}));
            radiusLabel.setText("radius is set to " + radius_um + " um");
        }
        if(ringWgPlotToggleSelected()){
        	ringWgPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void sweepRadius() throws IOException {
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/DataInput/MainGUI/dataCollector.fxml")) ;
        WindowBuilder window = new WindowBuilder(loader) ;
        window.setIcon("/People/Meisam/GUI/DataInput/Extras/dataCollector.png");
        window.build("Data Collector", false);
        DataCollectorController controller = loader.getController() ;
        controller.initialize();
        controller.getExitButton().setOnAction(e -> {
            try {
                if(simDataBase.variableExists("radius_(um)")){
                    simDataBase.removeVariable(simDataBase.getVariable("radius_(um)"));
                }
                simDataBase.addNewVariable(new SimulationVariable("radius_(um)", "Radius (um)", controller.getAllValues()));
                // clear text field and update label
                radiusTextField.clear();
                radiusLabel.setText("radius is set to array values");
                window.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        if(ringWgPlotToggleSelected()){
        	ringWgPlot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void setD(){
        // textbox is used for singnle entry
        if(!dTextField.getText().isEmpty()){
            double d_nm = Double.parseDouble(dTextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("gap_racetrack_(nm)", "Gap-Racetrack (nm)", new double[]{d_nm}));
            dLabel.setText("d is set to " + d_nm + " nm");
        }
        if(ringWgPlotToggleSelected()){
        	ringWgPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void sweepD() throws IOException {
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/DataInput/MainGUI/dataCollector.fxml")) ;
        WindowBuilder window = new WindowBuilder(loader) ;
        window.setIcon("/People/Meisam/GUI/DataInput/Extras/dataCollector.png");
        window.build("Data Collector", false);
        DataCollectorController controller = loader.getController() ;
        controller.initialize();
        controller.getExitButton().setOnAction(e -> {
            try {
                if(simDataBase.variableExists("gap_racetrack_(nm)")){
                    simDataBase.removeVariable(simDataBase.getVariable("gap_racetrack_(nm)"));
                }
                simDataBase.addNewVariable(new SimulationVariable("gap_racetrack_(nm)", "Gap-Racetrack (nm)", controller.getAllValues()));
                // clear text field and update label
                dTextField.clear();
                dLabel.setText("d is set to array values");
                window.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        if(ringWgPlotToggleSelected()){
        	ringWgPlot.getSelectedToggle().setSelected(false);
        }

    }
    
    @FXML
    public void setLength(){
        // textbox is used for singnle entry
        if(!lengthTextField.getText().isEmpty()){
            double length_um = Double.parseDouble(lengthTextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("length_racetrack_(um)", "Length-Racetrack (um)", new double[]{length_um}));
            lengthLabel.setText("length is set to " + length_um + " um");
        }
        if(ringWgPlotToggleSelected()){
        	ringWgPlot.getSelectedToggle().setSelected(false);
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
                if(simDataBase.variableExists("length_racetrack_(um)")){
                    simDataBase.removeVariable(simDataBase.getVariable("length_racetrack_(um)"));
                }
                simDataBase.addNewVariable(new SimulationVariable("length_racetrack_(um)", "Length-Racetrack (um)", controller.getAllValues()));
                // clear text field and update label
                lengthTextField.clear();
                lengthLabel.setText("length is set to array values");
                window.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        if(ringWgPlotToggleSelected()){
        	ringWgPlot.getSelectedToggle().setSelected(false);
        }

    }

    private double getKappaRingWg(double lambda_nm, double radius_um, double d_nm, double length_um, double width_nm){
    	double[] gap_nm_fromCoupledTab = simDataBase.getVariableValues("gap_(nm)") ;
    	double[] neff_even_fromCoupledTab = simDataBase.getVariableValues("neff_even_()") ;
    	double[] neff_odd_fromCoupledTab = simDataBase.getVariableValues("neff_odd_()") ;
    	CubicSpline neff_even_interpolator = new CubicSpline(gap_nm_fromCoupledTab, neff_even_fromCoupledTab) ;
    	CubicSpline neff_odd_interpolator = new CubicSpline(gap_nm_fromCoupledTab, neff_odd_fromCoupledTab) ;
    	WgProperties wgProp = new WgProperties(1, 0, 1, null) ;
    	RaceTrackCoupler rwDC = new RaceTrackCoupler(new Wavelength(lambda_nm), wgProp, width_nm, radius_um, d_nm, length_um, neff_even_interpolator, neff_odd_interpolator) ;
    	return rwDC.getS31().abs() ;
    }

    private double getTRingWg(double lambda_nm, double radius_um, double d_nm, double length_um, double width_nm){
    	double[] gap_nm_fromCoupledTab = simDataBase.getVariableValues("gap_(nm)") ;
    	double[] neff_even_fromCoupledTab = simDataBase.getVariableValues("neff_even_()") ;
    	double[] neff_odd_fromCoupledTab = simDataBase.getVariableValues("neff_odd_()") ;
    	CubicSpline neff_even_interpolator = new CubicSpline(gap_nm_fromCoupledTab, neff_even_fromCoupledTab) ;
    	CubicSpline neff_odd_interpolator = new CubicSpline(gap_nm_fromCoupledTab, neff_odd_fromCoupledTab) ;
    	WgProperties wgProp = new WgProperties(1, 0, 1, null) ;
    	RaceTrackCoupler rwDC = new RaceTrackCoupler(new Wavelength(lambda_nm), wgProp, width_nm, radius_um, d_nm, length_um, neff_even_interpolator, neff_odd_interpolator) ;
    	return rwDC.getS21().abs() ;
    }

    @FXML
    private void plotRingWg(){
    	double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
        double[] radius_um = simDataBase.getVariable("radius_(um)").getAllValues() ;
        double[] d_nm = simDataBase.getVariable("gap_racetrack_(nm)").getAllValues() ;
        double[] length_um = simDataBase.getVariable("length_racetrack_(um)").getAllValues() ;
        double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
    	if(kLambdaRingWg.isSelected()){
    		double[] kappa = new double[lambda_nm.length] ;
            for(int i=0; i<kappa.length; i++){
            	kappa[i] = getKappaRingWg(lambda_nm[i], radius_um[0], d_nm[0], length_um[0], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("kappa_racetrack_()", "Kappa", kappa));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("kappa_racetrack_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
    	}
    	else if(kRadiusRingWg.isSelected()){
    		double[] kappa = new double[length_um.length] ;
            for(int i=0; i<kappa.length; i++){
            	kappa[i] = getKappaRingWg(lambda_nm[0], radius_um[0], d_nm[0], length_um[i], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("kappa_racetrack_()", "Kappa", kappa));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("length_racetrack_(um)"), simDataBase.getVariable("kappa_racetrack_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
    	}
    	else if(kdRingWg.isSelected()){
    		double[] kappa = new double[d_nm.length] ;
            for(int i=0; i<kappa.length; i++){
            	kappa[i] = getKappaRingWg(lambda_nm[0], radius_um[0], d_nm[i], length_um[0], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("kappa_racetrack_()", "Kappa", kappa));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("gap_racetrack_(nm)"), simDataBase.getVariable("kappa_racetrack_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
    	}
    	else if(tLambdaRingWg.isSelected()){
    		double[] t = new double[lambda_nm.length] ;
            for(int i=0; i<t.length; i++){
            	t[i] = getTRingWg(lambda_nm[i], radius_um[0], d_nm[0], length_um[0], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("t_racetrack_()", "t", t));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("t_racetrack_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
    	}
       	else if(tRadiusRingWg.isSelected()){
    		double[] t = new double[length_um.length] ;
            for(int i=0; i<t.length; i++){
            	t[i] = getTRingWg(lambda_nm[0], radius_um[0], d_nm[0], length_um[i], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("t_racetrack_()", "t", t));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("length_racetrack_(um)"), simDataBase.getVariable("t_racetrack_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
    	}
       	else if(tdRingWg.isSelected()){
    		double[] t = new double[d_nm.length] ;
            for(int i=0; i<t.length; i++){
            	t[i] = getTRingWg(lambda_nm[0], radius_um[0], d_nm[i], length_um[0], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("t_racetrack_()", "t", t));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("gap_racetrack_(nm)"), simDataBase.getVariable("t_racetrack_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
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
        fig.RenderPlot();
        fig.xlabel(x.getAlias());
        fig.ylabel(y.getAlias());
        return fig ;
    }

    @FXML
    public void exportToMatlabPressed() throws IOException {
    	figRingWg.exportToMatlab();
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
		return ringWgTab ;
	}

	@Override
	public MatlabChart getFig() {
		return figRingWg ;
	}

	@Override
	public void generateGDS() {
		// step1: file chooser
		FileChooserFX fc = new FileChooserFX() ;
		fc.setExtension("gds");
		fc.saveFile();
		String fullPath = fc.getFilePath_NoExtension() ;
		String cellName = fc.getFileName_NoExtension() ;
		// setp2: create gds object
		double width_nm = simDataBase.getVariable("width_(nm)").getValue(0) ;
		double width_um = width_nm/1e3 ;
		double gap_nm = simDataBase.getVariable("gap_ringwg_(nm)").getValue(0) ;
		double radius_um = simDataBase.getVariable("radius_(um)").getValue(0) ;
		RingWg element = new RingWg("ringWg", new AbstractLayerMap[]{new SEAM()}, "port1", new Port(new Position(0, 0), width_um, 180), new Entry(width_um), new Entry(radius_um), new Entry(gap_nm)) ;
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

	}


}
