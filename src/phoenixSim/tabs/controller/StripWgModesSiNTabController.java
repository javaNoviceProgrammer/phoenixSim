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
import phoenixSim.modules.PlotterModule;
import phoenixSim.modules.SweepParameterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.material.Silica;
import photonics.material.SiliconNitride;
import photonics.strip.ModeStripWgTE;
import photonics.strip.ModeStripWgTM;
import photonics.strip.StripWg;
import photonics.util.Wavelength;

/*
 * This tab interacts with the database with the following simulation variables:
 *
 */

public class StripWgModesSiNTabController extends AbstractTabController {

    // defining simulation database and the figures in the simulation
    SimulationDataBase simDataBase = new SimulationDataBase() ;

    public MatlabChart figWgMode  ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

  //********* parameters related to wg Mode tab ****************************************************************
    @FXML Tab stripWgModesTab ;
    @FXML Pane wgModeMatlabPlotPane ;
    @FXML TextField widthTextField ;
    @FXML TextField heightTextField ;
    @FXML TextField lambdaTextField ;
    @FXML Label widthLabel ;
    @FXML Label heightLabel ;
    @FXML Label lambdaLabel ;
    @FXML ToggleGroup modePlot ;
    @FXML RadioButton neffLambdaRadioButton ;
    @FXML RadioButton neffWRadioButton ;
    @FXML RadioButton neffHRadioButton ;
    @FXML RadioButton ngLambdaRadioButton ;
    @FXML RadioButton ngWRadioButton ;
    @FXML RadioButton ngHRadioButton ;
    @FXML ToggleGroup selectedMode ;
    @FXML RadioButton TE00RadioButton ;
    @FXML RadioButton TE10RadioButton ;
    @FXML RadioButton TE20RadioButton ;
    @FXML RadioButton TM00RadioButton ;
    @FXML RadioButton TM10RadioButton ;
    @FXML RadioButton TM20RadioButton ;

    public Tab getTab(){
    	return stripWgModesTab ;
    }

    @FXML
    public void initialize(){
        widthLabel.setText("");
        heightLabel.setText("");
        lambdaLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.renderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figWgMode = fig ;
        showPlot(fig, wgModeMatlabPlotPane);
        wgModeMatlabPlotPane.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<none>");
    }

    @SuppressWarnings("unused")
	private boolean checkAllVariablesSet_wgModeTab(){
        if(simDataBase.variableExists("width_(nm)") && simDataBase.variableExists("height_(nm)") &&
                            simDataBase.variableExists("lambda_(nm)") && simDataBase.variableExists("mode")){
            return true ;
        }
        else{
            return false ;
        }
    }

    private boolean wgModePlotToggleSelected(){
        if(neffLambdaRadioButton.isSelected() || neffHRadioButton.isSelected() || neffWRadioButton.isSelected() ||
                ngHRadioButton.isSelected() || ngLambdaRadioButton.isSelected() || ngLambdaRadioButton.isSelected()){
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
    	SweepParameterModule sweep = new SweepParameterModule() ;
    	sweep.setExitAction(() -> {
            simDataBase.addNewVariable(new SimulationVariable("width_(nm)", "Width (nm)", sweep.getController().getAllValues()));
            widthTextField.clear();
            widthLabel.setText("width is set to array values");
    	});

        if(wgModePlotToggleSelected()){
            modePlot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void setHeight(){
        if(!heightTextField.getText().isEmpty()){
            double height_nm = Double.parseDouble(heightTextField.getText()) ;
            simDataBase.addNewVariable(new SimulationVariable("height_(nm)", "Height (nm)", new double[]{height_nm}));
            heightLabel.setText("height is set to " + height_nm + " nm");
        }
        if(wgModePlotToggleSelected()){
            modePlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void sweepHeight() throws IOException {
    	SweepParameterModule sweep = new SweepParameterModule() ;
    	sweep.setExitAction(() -> {
            simDataBase.addNewVariable(new SimulationVariable("height_(nm)", "Height (nm)", sweep.getController().getAllValues()));
            heightTextField.clear();
            heightLabel.setText("height is set to array values");
    	});

        if(wgModePlotToggleSelected()){
            modePlot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void setLambda(){
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
    	SweepParameterModule sweep = new SweepParameterModule() ;
    	sweep.setExitAction(() -> {
            simDataBase.addNewVariable(new SimulationVariable("lambda_(nm)", "Wavelength (nm)", sweep.getController().getAllValues()));
            lambdaTextField.clear();
            lambdaLabel.setText("lambda is set to array values");
    	});
    	
        if(wgModePlotToggleSelected()){
            modePlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    public void setWgMode(){
        if(TE00RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TE00", new double[]{0,0}));
        }
        else if(TE10RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TE10", new double[]{1,0}));
        }
        else if(TE20RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TE20", new double[]{2,0}));
        }
        else if(TM00RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TM00", new double[]{0,0}));
        }
        else if(TM10RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TM10", new double[]{1,0}));
        }
        else if(TM20RadioButton.isSelected()){
            simDataBase.addNewVariable(new SimulationVariable("mode", "TM20", new double[]{2,0}));
        }

        if(wgModePlotToggleSelected()){
            modePlot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void plotWgMode(){
        String modeType = simDataBase.getVariable("mode").getAlias() ;
        if(modeType == "TE00" || modeType == "TE10" || modeType == "TE20"){
            solveForTE() ;
        }
        else if(modeType == "TM00" || modeType == "TM10" || modeType == "TM20"){
            solveForTM() ;
        }

    }

    private void solveForTE(){
        int m_index = (int) simDataBase.getVariable("mode").getValue(0) ;
        int n_index = (int) simDataBase.getVariable("mode").getValue(1) ;
        double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
        double[] height_nm = simDataBase.getVariable("height_(nm)").getAllValues() ;
        double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
        if(neffLambdaRadioButton.isSelected()){
            double[] neff = new double[lambda_nm.length] ;
            for(int i=0; i<neff.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[i]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[0], height_nm[0], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTE modeSolver = new ModeStripWgTE(stripWg, m_index, n_index) ;
                neff[i] = modeSolver.getEffectiveIndex() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("neff_()", "Effective Index", neff));
            figWgMode = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("neff_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(neffWRadioButton.isSelected()){
            double[] neff = new double[width_nm.length] ;
            for(int i=0; i<neff.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[0]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[i], height_nm[0], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTE modeSolver = new ModeStripWgTE(stripWg, m_index, n_index) ;
                neff[i] = modeSolver.getEffectiveIndex() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("neff_()", "Effective Index", neff));
            figWgMode = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("neff_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(neffHRadioButton.isSelected()){
            double[] neff = new double[height_nm.length] ;
            for(int i=0; i<neff.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[0]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[0], height_nm[i], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTE modeSolver = new ModeStripWgTE(stripWg, m_index, n_index) ;
                neff[i] = modeSolver.getEffectiveIndex() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("neff_()", "Effective Index", neff));
            figWgMode = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("neff_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(ngLambdaRadioButton.isSelected()){
            double[] ng = new double[lambda_nm.length] ;
            for(int i=0; i<ng.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[i]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[0], height_nm[0], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTE modeSolver = new ModeStripWgTE(stripWg, m_index, n_index) ;
                ng[i] = modeSolver.getGroupIndex() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("ng_()", "Group Index", ng));
            figWgMode = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("ng_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(ngWRadioButton.isSelected()){
            double[] ng = new double[width_nm.length] ;
            for(int i=0; i<ng.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[0]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[i], height_nm[0], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTE modeSolver = new ModeStripWgTE(stripWg, m_index, n_index) ;
                ng[i] = modeSolver.getGroupIndex() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("ng_()", "Group Index", ng));
            figWgMode = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("ng_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(ngHRadioButton.isSelected()){
            double[] ng = new double[height_nm.length] ;
            for(int i=0; i<ng.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[0]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[0], height_nm[i], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTE modeSolver = new ModeStripWgTE(stripWg, m_index, n_index) ;
                ng[i] = modeSolver.getGroupIndex() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("ng_()", "Group Index", ng));
            figWgMode = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("ng_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }

    }

    private void solveForTM(){
        int m_index = (int) simDataBase.getVariable("mode").getValue(0) ;
        int n_index = (int) simDataBase.getVariable("mode").getValue(1) ;
        double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
        double[] height_nm = simDataBase.getVariable("height_(nm)").getAllValues() ;
        double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
        if(neffLambdaRadioButton.isSelected()){
            double[] neff = new double[lambda_nm.length] ;
            for(int i=0; i<neff.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[i]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[0], height_nm[0], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTM modeSolver = new ModeStripWgTM(stripWg, m_index, n_index) ;
                neff[i] = modeSolver.getEffectiveIndex() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("neff_()", "Effective Index", neff));
            figWgMode = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("neff_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(neffWRadioButton.isSelected()){
            double[] neff = new double[width_nm.length] ;
            for(int i=0; i<neff.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[0]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[i], height_nm[0], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTM modeSolver = new ModeStripWgTM(stripWg, m_index, n_index) ;
                neff[i] = modeSolver.getEffectiveIndex() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("neff_()", "Effective Index", neff));
            figWgMode = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("neff_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(neffHRadioButton.isSelected()){
            double[] neff = new double[height_nm.length] ;
            for(int i=0; i<neff.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[0]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[0], height_nm[i], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTM modeSolver = new ModeStripWgTM(stripWg, m_index, n_index) ;
                neff[i] = modeSolver.getEffectiveIndex() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("neff_()", "Effective Index", neff));
            figWgMode = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("neff_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(ngLambdaRadioButton.isSelected()){
            double[] ng = new double[lambda_nm.length] ;
            for(int i=0; i<ng.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[i]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[0], height_nm[0], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTM modeSolver = new ModeStripWgTM(stripWg, m_index, n_index) ;
                ng[i] = modeSolver.getGroupIndex() ;
            }

            simDataBase.addNewVariable(new SimulationVariable("ng_()", "Group Index", ng));
            figWgMode = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("ng_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(ngWRadioButton.isSelected()){
            double[] ng = new double[width_nm.length] ;
            for(int i=0; i<ng.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[0]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[i], height_nm[0], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTM modeSolver = new ModeStripWgTM(stripWg, m_index, n_index) ;
                ng[i] = modeSolver.getGroupIndex() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("ng_()", "Group Index", ng));
            figWgMode = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("ng_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
        }
        else if(ngHRadioButton.isSelected()){
            double[] ng = new double[height_nm.length] ;
            for(int i=0; i<ng.length; i++){
                Wavelength inputLambda = new Wavelength(lambda_nm[0]) ;
                StripWg stripWg = new StripWg(inputLambda, width_nm[0], height_nm[i], new Silica(), new SiliconNitride(), new Silica()) ;
                ModeStripWgTM modeSolver = new ModeStripWgTM(stripWg, m_index, n_index) ;
                ng[i] = modeSolver.getGroupIndex() ;
            }
            simDataBase.addNewVariable(new SimulationVariable("ng_()", "Group Index", ng));
            figWgMode = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("ng_()")) ;
            showPlot(figWgMode, wgModeMatlabPlotPane);
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
//    	figWgMode.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
        new PlotterModule(simDataBase) ;
    }

    public StatusBar getStatusBar(){
    	return statusBar ;
    }

	@Override
	public MatlabChart getFig() {
		return figWgMode ;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}


}
