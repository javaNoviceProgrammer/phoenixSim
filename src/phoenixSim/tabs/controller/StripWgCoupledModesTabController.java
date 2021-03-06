package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import mathLib.plot.MatlabChart;
import phoenixSim.builder.intf.ActionInterface;
import phoenixSim.modules.ExportToMatlabModule;
import phoenixSim.modules.PlotterModule;
import phoenixSim.modules.SweepParameterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.DataCollectorController;
import phoenixSim.util.PlotterController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.strip.StripWg;
import photonics.util.Wavelength;

public class StripWgCoupledModesTabController extends AbstractTabController {

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
    @FXML RadioButton nEHRadioButton ;
    @FXML RadioButton nOLambdaRadioButton ;
    @FXML RadioButton nOgapRadioButton ;
    @FXML RadioButton nOWRadioButton ;
    @FXML RadioButton nOHRadioButton ;

    @FXML CheckBox loadComsolCheckBox ;
    @FXML ComboBox<String> loadComsolComboBox ;

    @FXML
    public void initialize(){
    	gapLabel.setText("");
    	loadComsolCheckBox.setSelected(false);
    	loadComsolComboBox.getItems().removeAll(loadComsolComboBox.getItems()) ;
    	loadComsolComboBox.getItems().addAll("400X220-TE00") ;
    	loadComsolComboBox.getItems().addAll("450X220-TE00") ;
    	loadComsolComboBox.getItems().addAll("500X220-TE00") ;

    	loadComsolCheckBox.pressedProperty().addListener(e -> {
            if(coupledModePlotToggleSelected()){
            	coupledModePlot.getSelectedToggle().setSelected(false);
            }
    	});

    	loadComsolComboBox.selectionModelProperty().addListener(e -> {
            if(coupledModePlotToggleSelected()){
            	coupledModePlot.getSelectedToggle().setSelected(false);
            }
    	});
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.renderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figCoupledModes = fig ;
        showPlot(fig, coupledModeMatlabPlotPane);
        coupledModeMatlabPlotPane.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<Strip WG Modes>");
    }

    private boolean coupledModePlotToggleSelected(){
        if(nELambdaRadioButton.isSelected() || nEgapRadioButton.isSelected() || nEWRadioButton.isSelected() || nEHRadioButton.isSelected() ||
        		nOLambdaRadioButton.isSelected() || nOgapRadioButton.isSelected() || nOWRadioButton.isSelected() || nOHRadioButton.isSelected()){
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
    	SweepParameterModule sweep = new SweepParameterModule() ;
    	sweep.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
                simDataBase.addNewVariable(new SimulationVariable("gap_(nm)", "Gap (nm)", sweep.getController().getAllValues()));
                // clear text field and update label
                gapTextField.clear();
                gapLabel.setText("gap is set to array values");
			}
		});
        if(coupledModePlotToggleSelected()){
        	coupledModePlot.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    public void plotCoupledMode(){
        double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
        double[] height_nm = simDataBase.getVariable("height_(nm)").getAllValues() ;
        double[] gap_nm = simDataBase.getVariable("gap_(nm)").getAllValues() ;
        double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
        double[] neffEven ;
        double[] neffOdd ;

        if(nELambdaRadioButton.isSelected()){
        	neffEven = new double[lambda_nm.length] ;
        	for(int i=0; i<neffEven.length; i++){
        		neffEven[i] = getNeffEven(lambda_nm[i], width_nm[0], height_nm[0], gap_nm[0]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_even_()", "Coupled Index (even)", neffEven));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("neff_even_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nEgapRadioButton.isSelected()){
        	neffEven = new double[gap_nm.length] ;
        	for(int i=0; i<neffEven.length; i++){
        		neffEven[i] = getNeffEven(lambda_nm[0], width_nm[0], height_nm[0], gap_nm[i]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_even_()", "Coupled Index (even)", neffEven));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("gap_(nm)"), simDataBase.getVariable("neff_even_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nEWRadioButton.isSelected()){
        	neffEven = new double[width_nm.length] ;
        	for(int i=0; i<neffEven.length; i++){
        		neffEven[i] = getNeffEven(lambda_nm[0], width_nm[i], height_nm[0], gap_nm[0]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_even_()", "Coupled Index (even)", neffEven));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("neff_even_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nEHRadioButton.isSelected()){
        	neffEven = new double[height_nm.length] ;
        	for(int i=0; i<neffEven.length; i++){
        		neffEven[i] = getNeffEven(lambda_nm[0], width_nm[0], height_nm[i], gap_nm[0]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_even_()", "Coupled Index (even)", neffEven));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("neff_even_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nOLambdaRadioButton.isSelected()){
        	neffOdd = new double[lambda_nm.length] ;
        	for(int i=0; i<neffOdd.length; i++){
        		neffOdd[i] = getNeffOdd(lambda_nm[i], width_nm[0], height_nm[0], gap_nm[0]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_odd_()", "Coupled Index (odd)", neffOdd));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("neff_odd_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nOgapRadioButton.isSelected()){
        	neffOdd = new double[gap_nm.length] ;
        	for(int i=0; i<neffOdd.length; i++){
        		neffOdd[i] = getNeffOdd(lambda_nm[0], width_nm[0], height_nm[0], gap_nm[i]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_odd_()", "Coupled Index (odd)", neffOdd));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("gap_(nm)"), simDataBase.getVariable("neff_odd_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nOWRadioButton.isSelected()){
        	neffOdd = new double[width_nm.length] ;
        	for(int i=0; i<neffOdd.length; i++){
        		neffOdd[i] = getNeffOdd(lambda_nm[0], width_nm[i], height_nm[0], gap_nm[0]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_odd_()", "Coupled Index (odd)", neffOdd));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("neff_odd_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }
        else if(nOHRadioButton.isSelected()){
        	neffOdd = new double[height_nm.length] ;
        	for(int i=0; i<neffOdd.length; i++){
        		neffOdd[i] = getNeffOdd(lambda_nm[0], width_nm[0], height_nm[i], gap_nm[0]) ;

        	}
            simDataBase.addNewVariable(new SimulationVariable("neff_odd_()", "Coupled Index (odd)", neffOdd));
            // finally plotting
            figCoupledModes = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("neff_odd_()")) ;
            showPlot(figCoupledModes, coupledModeMatlabPlotPane);
        }


    }

    private double loadNeffEvenFromCOMSOL(double lambda_nm, double gap_nm){
    	String choice = loadComsolComboBox.getSelectionModel().getSelectedItem() ;
    	double neff_even = Double.NaN ;
    	if(choice == "450X220-TE00"){
    		NeffCoupledStripWg_450X220_COMSOL db = new NeffCoupledStripWg_450X220_COMSOL() ;
    		neff_even = db.getNeffEven(lambda_nm, gap_nm) ;
    	}
    	else if(choice == "400X220-TE00"){
    		NeffCoupledStripWg_400X220_COMSOL db = new NeffCoupledStripWg_400X220_COMSOL() ;
    		neff_even = db.getNeffEven(lambda_nm, gap_nm) ;
    	}
    	else if(choice == "500X220-TE00"){
    		NeffCoupledStripWg_500X220_COMSOL db = new NeffCoupledStripWg_500X220_COMSOL() ;
    		neff_even = db.getNeffEven(lambda_nm, gap_nm) ;
    	}
    	return neff_even ;
    }

    private double loadNeffOddFromCOMSOL(double lambda_nm, double gap_nm){
    	String choice = loadComsolComboBox.getSelectionModel().getSelectedItem() ;
    	double neff_odd = Double.NaN ;
    	if(choice == "450X220-TE00"){
    		NeffCoupledStripWg_450X220_COMSOL db = new NeffCoupledStripWg_450X220_COMSOL() ;
    		neff_odd = db.getNeffOdd(lambda_nm, gap_nm) ;
    	}
    	else if(choice == "400X220-TE00"){
    		NeffCoupledStripWg_400X220_COMSOL db = new NeffCoupledStripWg_400X220_COMSOL() ;
    		neff_odd = db.getNeffOdd(lambda_nm, gap_nm) ;
    	}
    	else if(choice == "500X220-TE00"){
    		NeffCoupledStripWg_500X220_COMSOL db = new NeffCoupledStripWg_500X220_COMSOL() ;
    		neff_odd = db.getNeffOdd(lambda_nm, gap_nm) ;
    	}
    	return neff_odd ;
    }

    private double getNeffEven(double lambda_nm, double width_nm, double height_nm, double gap_nm){
    	double neffEven = Double.NaN ;
    	if(loadComsolCheckBox.isSelected() && !loadComsolComboBox.getSelectionModel().isEmpty()){
    		neffEven = loadNeffEvenFromCOMSOL(lambda_nm, gap_nm) ;
    	}
    	else{
            String modeType = simDataBase.getVariable("mode").getAlias() ;
            int m_index = (int) simDataBase.getVariable("mode").getValue(0) ;
            int n_index = (int) simDataBase.getVariable("mode").getValue(1) ;
            if(modeType == "TE00" || modeType == "TE10" || modeType == "TE20"){
                StripWg stripWg = new StripWg(new Wavelength(lambda_nm), width_nm, height_nm) ;
                ModeCoupledStripWgTE_fast modeSolver = new ModeCoupledStripWgTE_fast(stripWg, gap_nm, m_index, n_index) ;
                neffEven = modeSolver.getNeffEven() ;
            }
            else if(modeType == "TM00" || modeType == "TM10" || modeType == "TM20"){
                StripWg stripWg = new StripWg(new Wavelength(lambda_nm), width_nm, height_nm) ;
                ModeCoupledStripWgTM_fast modeSolver = new ModeCoupledStripWgTM_fast(stripWg, gap_nm, m_index, n_index) ;
                neffEven = modeSolver.getNeffEven() ;
            }
    	}
    	return neffEven ;
    }

    private double getNeffOdd(double lambda_nm, double width_nm, double height_nm, double gap_nm){
    	double neffOdd = Double.NaN ;
    	if(loadComsolCheckBox.isSelected() && !loadComsolComboBox.getSelectionModel().isEmpty()){
    		neffOdd = loadNeffOddFromCOMSOL(lambda_nm, gap_nm) ;
    	}
    	else{
            String modeType = simDataBase.getVariable("mode").getAlias() ;
            int m_index = (int) simDataBase.getVariable("mode").getValue(0) ;
            int n_index = (int) simDataBase.getVariable("mode").getValue(1) ;
            if(modeType == "TE00" || modeType == "TE10" || modeType == "TE20"){
                StripWg stripWg = new StripWg(new Wavelength(lambda_nm), width_nm, height_nm) ;
                ModeCoupledStripWgTE_fast modeSolver = new ModeCoupledStripWgTE_fast(stripWg, gap_nm, m_index, n_index) ;
                neffOdd = modeSolver.getNeffOdd() ;
            }
            else if(modeType == "TM00" || modeType == "TM10" || modeType == "TM20"){
                StripWg stripWg = new StripWg(new Wavelength(lambda_nm), width_nm, height_nm) ;
                ModeCoupledStripWgTM_fast modeSolver = new ModeCoupledStripWgTM_fast(stripWg, gap_nm, m_index, n_index) ;
                neffOdd = modeSolver.getNeffOdd() ;
            }
    	}
    	return neffOdd ;
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
    	new ExportToMatlabModule(getFig()) ;
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
        new PlotterModule(simDataBase) ;
    }

    public StatusBar getStatusBar(){
    	return statusBar ;
    }

	public Tab getTab() {
		return coupledModesTab ;
	}

	@Override
	public MatlabChart getFig() {
		return figCoupledModes ;
	}

	@Override
	public void generateGDS() {

	}


}
