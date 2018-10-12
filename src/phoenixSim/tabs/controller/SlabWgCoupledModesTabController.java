package phoenixSim.tabs.controller;

import java.io.IOException;
import org.controlsfx.control.StatusBar;
import People.Meisam.GUI.Builders.WindowBuilder;
import People.Meisam.GUI.DataInput.MainGUI.DataCollectorController;
import People.Meisam.GUI.PhoenixSim.TabsLibrary.AbstractTabController;
import People.Meisam.GUI.Plotters.MainGUI.PlotterController;
import People.Meisam.GUI.Plotters.MatlabPlot.MatlabChart;
import People.Meisam.GUI.Utilities.SimulationDataBase;
import People.Meisam.GUI.Utilities.SimulationVariable;
import PhotonicElements.EffectiveIndexMethod.ModeSolver.CoupledSlab.ModeCoupledSlabWgTE_fast;
import PhotonicElements.EffectiveIndexMethod.ModeSolver.CoupledSlab.ModeCoupledSlabWgTM_fast;
import PhotonicElements.EffectiveIndexMethod.Structures.CoupledSlabWg;
import PhotonicElements.Materials.Dielectric.Silica;
import PhotonicElements.Materials.Dielectric.Silicon;
import PhotonicElements.Utilities.Wavelength;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

public class SlabWgCoupledModesTabController extends AbstractTabController {

    // defining simulation database and the figures in the simulation
    SimulationDataBase simDataBase = new SimulationDataBase() ;

    public MatlabChart figCoupledModes  ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab coupledModesTab ;
    @FXML Pane coupledModeMatlabPlotPane ;
    @FXML TextField gapTextField ;
    @FXML Label gapLabel ;
    @FXML ToggleGroup coupledModePlot ;
    @FXML RadioButton nELambdaRadioButton ;
    @FXML RadioButton nEgapRadioButton ;
    @FXML RadioButton nEWRadioButton ;
    @FXML RadioButton nOLambdaRadioButton ;
    @FXML RadioButton nOgapRadioButton ;
    @FXML RadioButton nOWRadioButton ;

    @FXML
    public void initialize(){
    	gapLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figCoupledModes = fig ;
        showPlot(fig, coupledModeMatlabPlotPane);
        coupledModeMatlabPlotPane.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<Slab WG Modes>");
    }

    private boolean coupledModePlotToggleSelected(){
        if(nELambdaRadioButton.isSelected() || nEgapRadioButton.isSelected() || nEWRadioButton.isSelected() ||
        		nOLambdaRadioButton.isSelected() || nOgapRadioButton.isSelected() || nOWRadioButton.isSelected() ){
            return true;
        }
        else{
            return false ;
        }
    }

    @FXML
    public void setGap(){
        // textbox is used for singnle entry
        if(!gapTextField.getText().isEmpty()){
            double gap_nm = Double.parseDouble(gapTextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("gap_(nm)", "Gap (nm)", new double[]{gap_nm}));
            gapLabel.setText("gap is set to " + gap_nm + " nm");
        }
        if(coupledModePlotToggleSelected()){
        	coupledModePlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void sweepGap() throws IOException {
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/DataInput/MainGUI/dataCollector.fxml")) ;
        WindowBuilder window = new WindowBuilder(loader) ;
        window.setIcon("/People/Meisam/GUI/DataInput/Extras/dataCollector.png");
        window.build("Data Collector", false);
        DataCollectorController controller = loader.getController() ;
        controller.initialize();
        controller.getExitButton().setOnAction(e -> {
            try {
                if(simDataBase.variableExists("gap_(nm)")){
                    simDataBase.removeVariable(simDataBase.getVariable("gap_(nm)"));
                }
                simDataBase.addNewVariable(new SimulationVariable("gap_(nm)", "Gap (nm)", controller.getAllValues()));
                // clear text field and update label
                gapTextField.clear();
                gapLabel.setText("gap is set to array values");
                window.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        if(coupledModePlotToggleSelected()){
        	coupledModePlot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void plotCoupledMode(){
        double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
        double[] gap_nm = simDataBase.getVariable("gap_(nm)").getAllValues() ;
        double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
        double[] neffEven ;
        double[] neffOdd ;

        if(nELambdaRadioButton.isSelected()){
        	neffEven = new double[lambda_nm.length] ;
        	for(int i=0; i<neffEven.length; i++){
        		neffEven[i] = getNeffEven(lambda_nm[i], width_nm[0], gap_nm[0]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_even_()", "Coupled Index (even)", neffEven));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("neff_even_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nEgapRadioButton.isSelected()){
        	neffEven = new double[gap_nm.length] ;
        	for(int i=0; i<neffEven.length; i++){
        		neffEven[i] = getNeffEven(lambda_nm[0], width_nm[0], gap_nm[i]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_even_()", "Coupled Index (even)", neffEven));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("gap_(nm)"), simDataBase.getVariable("neff_even_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nEWRadioButton.isSelected()){
        	neffEven = new double[width_nm.length] ;
        	for(int i=0; i<neffEven.length; i++){
        		neffEven[i] = getNeffEven(lambda_nm[0], width_nm[i], gap_nm[0]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_even_()", "Coupled Index (even)", neffEven));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("neff_even_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nOLambdaRadioButton.isSelected()){
        	neffOdd = new double[lambda_nm.length] ;
        	for(int i=0; i<neffOdd.length; i++){
        		neffOdd[i] = getNeffOdd(lambda_nm[i], width_nm[0], gap_nm[0]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_odd_()", "Coupled Index (odd)", neffOdd));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("neff_odd_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nOgapRadioButton.isSelected()){
        	neffOdd = new double[gap_nm.length] ;
        	for(int i=0; i<neffOdd.length; i++){
        		neffOdd[i] = getNeffOdd(lambda_nm[0], width_nm[0], gap_nm[i]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_odd_()", "Coupled Index (odd)", neffOdd));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("gap_(nm)"), simDataBase.getVariable("neff_odd_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nOWRadioButton.isSelected()){
        	neffOdd = new double[width_nm.length] ;
        	for(int i=0; i<neffOdd.length; i++){
        		neffOdd[i] = getNeffOdd(lambda_nm[0], width_nm[i], gap_nm[0]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_odd_()", "Coupled Index (odd)", neffOdd));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("neff_odd_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
    }

    private double getNeffEven(double lambda_nm, double width_nm, double gap_nm){
    	double neffEven = Double.NaN ;
        String modeType = simDataBase.getVariable("mode").getAlias() ;
        int m_index = (int) simDataBase.getVariable("mode").getValue(0) ;
        Silicon si = new Silicon() ;
        Silica sio2 = new Silica() ;
        Wavelength inputLambda = new Wavelength(lambda_nm) ;
        if(modeType == "TE0" || modeType == "TE1" || modeType == "TE2" || modeType == "TE3"){
            CoupledSlabWg coupledSlab = new CoupledSlabWg(width_nm, width_nm, gap_nm, sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda)) ;
            ModeCoupledSlabWgTE_fast modeSolver = new ModeCoupledSlabWgTE_fast(inputLambda, coupledSlab) ;
            neffEven = modeSolver.findNeffEven(m_index) ;
        }
        else if(modeType == "TM0" || modeType == "TM1" || modeType == "TM2" || modeType == "TM3"){
            CoupledSlabWg coupledSlab = new CoupledSlabWg(width_nm, width_nm, gap_nm, sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda)) ;
            ModeCoupledSlabWgTM_fast modeSolver = new ModeCoupledSlabWgTM_fast(inputLambda, coupledSlab) ;
            neffEven = modeSolver.findNeffEven(m_index) ;
        }
    	return neffEven ;
    }

    private double getNeffOdd(double lambda_nm, double width_nm, double gap_nm){
    	double neffOdd = Double.NaN ;
        String modeType = simDataBase.getVariable("mode").getAlias() ;
        int m_index = (int) simDataBase.getVariable("mode").getValue(0) ;
        Silicon si = new Silicon() ;
        Silica sio2 = new Silica() ;
        Wavelength inputLambda = new Wavelength(lambda_nm) ;
        if(modeType == "TE0" || modeType == "TE1" || modeType == "TE2" || modeType == "TE3"){
            CoupledSlabWg coupledSlab = new CoupledSlabWg(width_nm, width_nm, gap_nm, sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda)) ;
            ModeCoupledSlabWgTE_fast modeSolver = new ModeCoupledSlabWgTE_fast(inputLambda, coupledSlab) ;
            neffOdd = modeSolver.findNeffOdd(m_index) ;
        }
        else if(modeType == "TM0" || modeType == "TM1" || modeType == "TM2" || modeType == "TM3"){
            CoupledSlabWg coupledSlab = new CoupledSlabWg(width_nm, width_nm, gap_nm, sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda)) ;
            ModeCoupledSlabWgTM_fast modeSolver = new ModeCoupledSlabWgTM_fast(inputLambda, coupledSlab) ;
            neffOdd = modeSolver.findNeffOdd(m_index) ;
        }
    	return neffOdd ;
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
    	figCoupledModes.exportToMatlab();
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

	@Override
	public Tab getTab() {
		return coupledModesTab ;
	}

	@Override
	public MatlabChart getFig() {
		return figCoupledModes;
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
