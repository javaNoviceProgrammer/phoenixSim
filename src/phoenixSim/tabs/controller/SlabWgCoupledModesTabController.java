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
import photonics.material.Silicon;
import photonics.slab.CoupledSlabWg;
import photonics.slab.ModeCoupledSlabWgTE;
import photonics.slab.ModeCoupledSlabWgTM;
import photonics.util.Wavelength;

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
    	SweepParameterModule sweepModule = new SweepParameterModule() ;
    	sweepModule.setExitAction(() -> {
                simDataBase.addNewVariable(new SimulationVariable("gap_(nm)", "Gap (nm)", sweepModule.getController().getAllValues()));
                // clear text field and update label
                gapTextField.clear();
                gapLabel.setText("gap is set to array values");
    	});
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
            ModeCoupledSlabWgTE modeSolver = new ModeCoupledSlabWgTE(inputLambda, coupledSlab) ;
            neffEven = modeSolver.findNeffEven(m_index) ;
        }
        else if(modeType == "TM0" || modeType == "TM1" || modeType == "TM2" || modeType == "TM3"){
            CoupledSlabWg coupledSlab = new CoupledSlabWg(width_nm, width_nm, gap_nm, sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda)) ;
            ModeCoupledSlabWgTM modeSolver = new ModeCoupledSlabWgTM(inputLambda, coupledSlab) ;
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
            ModeCoupledSlabWgTE modeSolver = new ModeCoupledSlabWgTE(inputLambda, coupledSlab) ;
            neffOdd = modeSolver.findNeffOdd(m_index) ;
        }
        else if(modeType == "TM0" || modeType == "TM1" || modeType == "TM2" || modeType == "TM3"){
            CoupledSlabWg coupledSlab = new CoupledSlabWg(width_nm, width_nm, gap_nm, sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda), si.getIndex(inputLambda), sio2.getIndex(inputLambda)) ;
            ModeCoupledSlabWgTM modeSolver = new ModeCoupledSlabWgTM(inputLambda, coupledSlab) ;
            neffOdd = modeSolver.findNeffOdd(m_index) ;
        }
    	return neffOdd ;
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
//    	figCoupledModes.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
        new PlotterModule(simDataBase) ;
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
