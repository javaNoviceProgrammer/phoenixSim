package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import PhotonicElements.DirectionalCoupler.RingWgCoupling.RingRingCoupler;
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
import phoenixSim.util.PlotterController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.util.Wavelength;

public class RingRingCouplerTabController extends AbstractTabController {

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
    @FXML Label radiusLabel ;
    @FXML Label dLabel ;
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
            simDataBase.addNewVariable(new SimulationVariable("gap_ringring_(nm)", "Gap-RingRing (nm)", new double[]{d_nm}));
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
                if(simDataBase.variableExists("gap_ringring_(nm)")){
                    simDataBase.removeVariable(simDataBase.getVariable("gap_ringring_(nm)"));
                }
                simDataBase.addNewVariable(new SimulationVariable("gap_ringring_(nm)", "Gap-RingRing (nm)", controller.getAllValues()));
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

    private double getKappaRingWg(double lambda_nm, double radius_um, double d_nm, double width_nm){
    	double[] gap_nm_fromCoupledTab = simDataBase.getVariableValues("gap_(nm)") ;
    	double[] neff_even_fromCoupledTab = simDataBase.getVariableValues("neff_even_()") ;
    	double[] neff_odd_fromCoupledTab = simDataBase.getVariableValues("neff_odd_()") ;
    	CubicSpline neff_even_interpolator = new CubicSpline(gap_nm_fromCoupledTab, neff_even_fromCoupledTab) ;
    	CubicSpline neff_odd_interpolator = new CubicSpline(gap_nm_fromCoupledTab, neff_odd_fromCoupledTab) ;
    	WgProperties wgProp = new WgProperties(1, 0, 1, null) ;
    	RingRingCoupler rwDC = new RingRingCoupler(new Wavelength(lambda_nm), wgProp, width_nm, radius_um, d_nm, neff_even_interpolator, neff_odd_interpolator) ;
    	return rwDC.getS31().abs() ;
    }

    private double getTRingWg(double lambda_nm, double radius_um, double d_nm, double width_nm){
    	double[] gap_nm_fromCoupledTab = simDataBase.getVariableValues("gap_(nm)") ;
    	double[] neff_even_fromCoupledTab = simDataBase.getVariableValues("neff_even_()") ;
    	double[] neff_odd_fromCoupledTab = simDataBase.getVariableValues("neff_odd_()") ;
    	CubicSpline neff_even_interpolator = new CubicSpline(gap_nm_fromCoupledTab, neff_even_fromCoupledTab) ;
    	CubicSpline neff_odd_interpolator = new CubicSpline(gap_nm_fromCoupledTab, neff_odd_fromCoupledTab) ;
    	WgProperties wgProp = new WgProperties(1, 0, 1, null) ;
    	RingRingCoupler rwDC = new RingRingCoupler(new Wavelength(lambda_nm), wgProp, width_nm, radius_um, d_nm, neff_even_interpolator, neff_odd_interpolator) ;
    	return rwDC.getS21().abs() ;
    }

    @FXML
    private void plotRingWg(){
    	double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
        double[] radius_um = simDataBase.getVariable("radius_(um)").getAllValues() ;
        double[] d_nm = simDataBase.getVariable("gap_ringring_(nm)").getAllValues() ;
        double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
    	if(kLambdaRingWg.isSelected()){
    		double[] kappa = new double[lambda_nm.length] ;
            for(int i=0; i<kappa.length; i++){
            	kappa[i] = getKappaRingWg(lambda_nm[i], radius_um[0], d_nm[0], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("kappa_ringring_()", "Kappa", kappa));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("kappa_ringring_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
    	}
    	else if(kRadiusRingWg.isSelected()){
    		double[] kappa = new double[radius_um.length] ;
            for(int i=0; i<kappa.length; i++){
            	kappa[i] = getKappaRingWg(lambda_nm[0], radius_um[i], d_nm[0], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("kappa_ringring_()", "Kappa", kappa));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("radius_(um)"), simDataBase.getVariable("kappa_ringring_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
    	}
    	else if(kdRingWg.isSelected()){
    		double[] kappa = new double[d_nm.length] ;
            for(int i=0; i<kappa.length; i++){
            	kappa[i] = getKappaRingWg(lambda_nm[0], radius_um[0], d_nm[i], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("kappa_ringring_()", "Kappa", kappa));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("gap_ringring_(nm)"), simDataBase.getVariable("kappa_ringring_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
    	}
    	else if(tLambdaRingWg.isSelected()){
    		double[] t = new double[lambda_nm.length] ;
            for(int i=0; i<t.length; i++){
            	t[i] = getTRingWg(lambda_nm[i], radius_um[0], d_nm[0], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("t_ringring_()", "t", t));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("t_ringring_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
    	}
       	else if(tRadiusRingWg.isSelected()){
    		double[] t = new double[radius_um.length] ;
            for(int i=0; i<t.length; i++){
            	t[i] = getTRingWg(lambda_nm[0], radius_um[i], d_nm[0], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("t_ringring_()", "t", t));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("radius_(um)"), simDataBase.getVariable("t_ringring_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
    	}
       	else if(tdRingWg.isSelected()){
    		double[] t = new double[d_nm.length] ;
            for(int i=0; i<t.length; i++){
            	t[i] = getTRingWg(lambda_nm[0], radius_um[0], d_nm[i], width_nm[0]) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("t_ringring_()", "t", t));
            // finally plotting
            figRingWg = createPlot(simDataBase.getVariable("gap_ringring_(nm)"), simDataBase.getVariable("t_ringring_()")) ;
            showPlot(figRingWg, ringWgMatlabPlotPane);
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

	}

}
