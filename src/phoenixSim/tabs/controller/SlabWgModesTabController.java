package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import mathLib.plot.MatlabChart;
import phoenixSim.builder.WindowBuilder;
import phoenixSim.modules.PlotterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.DataCollectorController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.material.Silica;
import photonics.material.Silicon;
import photonics.slab.ModeSlabWgTE;
import photonics.slab.ModeSlabWgTM;
import photonics.slab.SlabWg;
import photonics.util.Wavelength;

public class SlabWgModesTabController extends AbstractTabController {

    // defining simulation database and the figures in the simulation
    SimulationDataBase simDataBase = new SimulationDataBase() ;
    MatlabChart figWgMode ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab slabWgModesTab ;
    @FXML Pane wgModeMatlabPlotPane ;
    @FXML TextField widthTextField ;
    @FXML TextField lambdaTextField ;
    @FXML Label widthLabel ;
    @FXML Label lambdaLabel ;
    @FXML ToggleGroup modePlot ;
    @FXML RadioButton neffLambdaRadioButton ;
    @FXML RadioButton neffWRadioButton ;
    @FXML RadioButton ngLambdaRadioButton ;
    @FXML RadioButton ngWRadioButton ;
    @FXML ToggleGroup selectedMode ;
    @FXML RadioButton TE0RadioButton ;
    @FXML RadioButton TE1RadioButton ;
    @FXML RadioButton TE2RadioButton ;
    @FXML RadioButton TE3RadioButton ;
    @FXML RadioButton TE4RadioButton ;
    @FXML RadioButton TE5RadioButton ;
    @FXML RadioButton TE6RadioButton ;
    @FXML RadioButton TE7RadioButton ;
    @FXML RadioButton TM0RadioButton ;
    @FXML RadioButton TM1RadioButton ;
    @FXML RadioButton TM2RadioButton ;
    @FXML RadioButton TM3RadioButton ;
    @FXML RadioButton TM4RadioButton ;
    @FXML RadioButton TM5RadioButton ;
    @FXML RadioButton TM6RadioButton ;
    @FXML RadioButton TM7RadioButton ;

    @FXML
    public void initialize(){
        widthLabel.setText("");
        lambdaLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figWgMode = fig ;
        showPlot(fig, wgModeMatlabPlotPane);
        wgModeMatlabPlotPane.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<none>");
    }

    private boolean wgModePlotToggleSelected(){
        if(neffLambdaRadioButton.isSelected() ||  neffWRadioButton.isSelected() ||
                 ngLambdaRadioButton.isSelected() || ngWRadioButton.isSelected()){
            return true;
        }
        else{
            return false ;
        }
    }

    @FXML
    public void setWidth(){
        // textbox is used for single entry
        if(!widthTextField.getText().isEmpty()){
            double width_nm = Double.parseDouble(widthTextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("width_(nm)", "Width (nm)", new double[]{width_nm}));
            widthLabel.setText("width is set to " + width_nm + " nm");
        }
        if(wgModePlotToggleSelected()){
            modePlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void sweepWidth() throws IOException {
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/dataCollector.fxml")) ;
        WindowBuilder window = new WindowBuilder(loader) ;
        window.setIcon("/phoenixSim/extras/dataCollector.png");
        window.build("Data Collector", false);
        DataCollectorController controller = loader.getController() ;
        controller.initialize();
        controller.getExitButton().setOnAction(e -> {
            try {
                if(simDataBase.variableExists("width_(nm)")){
                    simDataBase.removeVariable(simDataBase.getVariable("width_(nm)"));
                }
                simDataBase.addNewVariable(new SimulationVariable("width_(nm)", "Width (nm)", controller.getAllValues()));
                // clear text field and update label
                widthTextField.clear();
                widthLabel.setText("width is set to array values");
                window.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        if(wgModePlotToggleSelected()){
            modePlot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void setLambda(){
        // textbox is used for singnle entry
        if(!lambdaTextField.getText().isEmpty()){
            double lambda_nm = Double.parseDouble(lambdaTextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("lambda_(nm)", "Wavelength (nm)", new double[]{lambda_nm}));
            lambdaLabel.setText("lambda is set to " + lambda_nm + " nm");
        }
        if(wgModePlotToggleSelected()){
            modePlot.getSelectedToggle().setSelected(false);
        }
    }

    public void sweepLambda() throws IOException {
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/dataCollector.fxml")) ;
        WindowBuilder window = new WindowBuilder(loader) ;
        window.setIcon("/phoenixSim/extras/dataCollector.png");
        window.build("Data Collector", false);
        DataCollectorController controller = loader.getController() ;
        controller.initialize();
        controller.getExitButton().setOnAction(e -> {
            try {
                if(simDataBase.variableExists("lambda_(nm)")){
                    simDataBase.removeVariable(simDataBase.getVariable("lambda_(nm)"));
                }
                simDataBase.addNewVariable(new SimulationVariable("lambda_(nm)", "Wavelength (nm)", controller.getAllValues()));
                // clear text field and update label
                lambdaTextField.clear();
                lambdaLabel.setText("lambda is set to array values");
                window.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        if(wgModePlotToggleSelected()){
            modePlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setWgMode(){
        if(TE0RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TE0", new double[]{0}));
        }
        else if(TE1RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TE1", new double[]{1}));
        }
        else if(TE2RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TE2", new double[]{2}));
        }
        else if(TE3RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TE3", new double[]{3}));
        }
        else if(TE4RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TE4", new double[]{4}));
        }
        else if(TE5RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TE5", new double[]{5}));
        }
        else if(TE6RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TE6", new double[]{6}));
        }
        else if(TE7RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TE7", new double[]{7}));
        }
        else if(TM0RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TM0", new double[]{0}));
        }
        else if(TM1RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TM1", new double[]{1}));
        }
        else if(TM2RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TM2", new double[]{2}));
        }
        else if(TM3RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TM3", new double[]{3}));
        }
        else if(TM4RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TM4", new double[]{4}));
        }
        else if(TM5RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TM5", new double[]{5}));
        }
        else if(TM6RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TM6", new double[]{6}));
        }
        else if(TM7RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TM7", new double[]{7}));
        }
        if(wgModePlotToggleSelected()){
            modePlot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void plotWgMode(){
        String modeType = simDataBase.getVariable("mode").getAlias() ;
        if(modeType.contains("TE")){
            solveForTE() ;
        }
        else if(modeType.contains("TM")){
            solveForTM() ;
        }

    }

    private double getNeffTE(double width_nm, double lambda_nm, int m_index){
        Wavelength inputLambda = new Wavelength(lambda_nm) ;
        SlabWg slabWg = new SlabWg(inputLambda, width_nm, new Silica(), new Silicon(), new Silica()) ;
        ModeSlabWgTE modeSolver = new ModeSlabWgTE(slabWg) ;
        return modeSolver.findSpecificModeIndex(m_index) ;
    }

    private double getNgTE(double width_nm, double lambda_nm, int m_index){
        double dlambda_nm = 1e-1 ;
        double lambda_max_nm, lambda_min_nm, neff_max, neff_min ;
            lambda_max_nm = lambda_nm + dlambda_nm ;
            lambda_min_nm = lambda_nm ;
            neff_max = getNeffTE(width_nm, lambda_max_nm, m_index) ;
            neff_min = getNeffTE(width_nm, lambda_min_nm, m_index) ;
            double dneff_dlambda = (neff_max-neff_min)/(lambda_max_nm-lambda_min_nm) ;
            return (neff_min - lambda_nm * dneff_dlambda) ;
    }

    private double getNeffTM(double width_nm, double lambda_nm, int m_index){
        Wavelength inputLambda = new Wavelength(lambda_nm) ;
        SlabWg slabWg = new SlabWg(inputLambda, width_nm, new Silica(), new Silicon(), new Silica()) ;
        ModeSlabWgTM modeSolver = new ModeSlabWgTM(slabWg) ;
        return modeSolver.findSpecificModeIndex(m_index) ;
    }

    private double getNgTM(double width_nm, double lambda_nm, int m_index){
        double dlambda_nm = 1e-1 ;
        double lambda_max_nm, lambda_min_nm, neff_max, neff_min ;
        lambda_max_nm = lambda_nm + dlambda_nm ;
        lambda_min_nm = lambda_nm ;
        neff_max = getNeffTM(width_nm, lambda_max_nm, m_index) ;
        neff_min = getNeffTM(width_nm, lambda_min_nm, m_index) ;
        double dneff_dlambda = (neff_max-neff_min)/(lambda_max_nm-lambda_min_nm) ;
        return (neff_min - lambda_nm * dneff_dlambda) ;
    }

    private void solveForTE(){
        int m_index = (int) simDataBase.getVariable("mode").getValue(0) ;
        double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
        double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
        if(neffLambdaRadioButton.isSelected()){
            double[] neff = new double[lambda_nm.length] ;
            for(int i=0; i<neff.length; i++){
                neff[i] = getNeffTE(width_nm[0], lambda_nm[i], m_index) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("neff_()", "Effective Index", neff));
            figWgMode = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("neff_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(neffWRadioButton.isSelected()){
            double[] neff = new double[width_nm.length] ;
            for(int i=0; i<neff.length; i++){
                neff[i] = getNeffTE(width_nm[i], lambda_nm[0], m_index) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("neff_()", "Effective Index", neff));
            figWgMode = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("neff_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }

        else if(ngLambdaRadioButton.isSelected()){
            double[] ng = new double[lambda_nm.length] ;
            for(int i=0; i<ng.length; i++){
                ng[i] = getNgTE(width_nm[0], lambda_nm[i], m_index) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("ng_()", "Group Index", ng));
            figWgMode = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("ng_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(ngWRadioButton.isSelected()){
            double[] ng = new double[width_nm.length] ;
            for(int i=0; i<ng.length; i++){
                ng[i] = getNgTE(width_nm[i], lambda_nm[0], m_index) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("ng_()", "Group Index", ng));
            figWgMode = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("ng_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }

    }

    private void solveForTM(){
        int m_index = (int) simDataBase.getVariable("mode").getValue(0) ;
        double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
        double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
        if(neffLambdaRadioButton.isSelected()){
            double[] neff = new double[lambda_nm.length] ;
            for(int i=0; i<neff.length; i++){
                neff[i] = getNeffTM(width_nm[0], lambda_nm[i], m_index) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("neff_()", "Effective Index", neff));
            figWgMode = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("neff_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(neffWRadioButton.isSelected()){
            double[] neff = new double[width_nm.length] ;
            for(int i=0; i<neff.length; i++){
                neff[i] = getNeffTM(width_nm[i], lambda_nm[0], m_index) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("neff_()", "Effective Index", neff));
            figWgMode = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("neff_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(ngLambdaRadioButton.isSelected()){
            double[] ng = new double[lambda_nm.length] ;
            for(int i=0; i<ng.length; i++){
                ng[i] = getNgTM(width_nm[0], lambda_nm[i], m_index) ;
            }

            simDataBase.addNewVariable(new SimulationVariable("ng_()", "Group Index", ng));
            figWgMode = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("ng_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(ngWRadioButton.isSelected()){
            double[] ng = new double[width_nm.length] ;
            for(int i=0; i<ng.length; i++){
                ng[i] = getNgTM(width_nm[i], lambda_nm[0], m_index) ;
            }
            simDataBase.addNewVariable(new SimulationVariable("ng_()", "Group Index", ng));
            figWgMode = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("ng_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
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
//    	figWgMode.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
        new PlotterModule(simDataBase);
    }


	@Override
	public Tab getTab() {
		return slabWgModesTab ;
	}

	@Override
	public MatlabChart getFig() {
		return figWgMode ;
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
