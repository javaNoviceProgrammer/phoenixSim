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
import mathLib.util.MathUtils;
import phoenixSim.builder.WindowBuilder;
import phoenixSim.builder.intf.ActionInterface;
import phoenixSim.modules.VariableSelectorModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.PlotterController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.heater.struct.SelfHeating;
import photonics.heater.voltage.AbstractVoltage;

public class HeaterDCTabController extends AbstractTabController {

    SelfHeating selfHeating ;
    AbstractVoltage voltage ;
    SimulationDataBase simDataBase = new SimulationDataBase() ;
    public MatlabChart figDC ;
    StatusBar statusBar = new StatusBar() ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    @FXML Tab dcTab ;
    @FXML TextField KvTextField ;
    @FXML TextField RlinearTextField ;
    @FXML TextField aHTextField ;
    @FXML Pane dcMatlabPlotPane ;
    @FXML RadioButton viPlotRadioButton ;
    @FXML RadioButton tempVRadioButton ;
    @FXML RadioButton tempIRadioButton ;
    @FXML RadioButton ivPlotRadioButton ;
    @FXML ToggleGroup dcPlot ;
    @FXML Label kvLabel ;
    @FXML Label RlinearLabel ;
    @FXML Label aHLabel ;

    @FXML
    public void initialize(){
        kvLabel.setText("");
        RlinearLabel.setText("");
        aHLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figDC = fig ;
        showPlot(fig, dcMatlabPlotPane);
        dcMatlabPlotPane.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<none>");
    }

    private boolean checkFilledTextsInDCTab(){
        if(!kvLabel.getText().isEmpty() && !RlinearLabel.getText().isEmpty() && !aHLabel.getText().isEmpty()){
            return true ;
        }
        else{
            return false ;
        }
    }

    private boolean dcToggleIsSelected(){
        if(ivPlotRadioButton.isSelected() || viPlotRadioButton.isSelected() || tempIRadioButton.isSelected() || tempVRadioButton.isSelected()){
            return true ;
        }
        else{
            return false ;
        }
    }

    @FXML
    private void setKv(){
    	String st = KvTextField.getText() ;
    	if(!st.isEmpty()){
    		try {
        		double kv = Double.parseDouble(KvTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("kv", new double[]{kv}));
                kvLabel.setText("Kv is set to " + kv + " /Volt^2");
			} catch (Exception e) {
	    		double kv = MathUtils.evaluate(st) ;
	            simDataBase.addNewVariable(new SimulationVariable("kv", new double[]{kv}));
	            kvLabel.setText("Kv is set to " + String.format("%.4f", kv) + " /Volt^2");
			}
    	}
        if(dcToggleIsSelected()){
            dcPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    private void chooseKvFromDataBase(){
    	VariableSelectorModule varSelect = new VariableSelectorModule(simDataBase) ;
    	varSelect.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
	    		double kv = varSelect.getController().getVariable().getValue(0) ;
	    		simDataBase.addNewVariable(new SimulationVariable("kv", new double[]{kv}));
	            kvLabel.setText("Kv is set to " + String.format("%.4f", kv) + " /Volt^2");
	            KvTextField.clear();
	            if(dcToggleIsSelected()){
	                dcPlot.getSelectedToggle().setSelected(false);
	            }
			}
		});
    }

    @FXML
    private void setRlinear(){
    	String st = RlinearTextField.getText() ;
    	if(!st.isEmpty()){
    		try {
                double Rlinear = Double.parseDouble(RlinearTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("Rlinear", new double[]{Rlinear}));
                RlinearLabel.setText("R is set to " + Rlinear + " Ohms");
                if(dcToggleIsSelected()){
                    dcPlot.getSelectedToggle().setSelected(false);
                }
			} catch (Exception e) {
	            double Rlinear = MathUtils.evaluate(st) ;
	            simDataBase.addNewVariable(new SimulationVariable("Rlinear", new double[]{Rlinear}));
	            RlinearLabel.setText("R is set to " + String.format("%.4f", Rlinear) + " Ohms");
	            if(dcToggleIsSelected()){
	                dcPlot.getSelectedToggle().setSelected(false);
	            }
			}
    	}

    }

    @FXML
    private void chooseRlinearFromDataBase(){
    	VariableSelectorModule varSelect = new VariableSelectorModule(simDataBase) ;
    	varSelect.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
	    		double Rlinear = varSelect.getController().getVariable().getValue(0) ;
	    		simDataBase.addNewVariable(new SimulationVariable("Rlinear", new double[]{Rlinear}));
	            RlinearLabel.setText("R is set to " + String.format("%.4f", Rlinear) + " Ohms");
	            RlinearTextField.clear();
	            if(dcToggleIsSelected()){
	                dcPlot.getSelectedToggle().setSelected(false);
	            }
			}
		});
    }

    @FXML
    private void setaH(){
    	String st = aHTextField.getText() ;
    	if(!st.isEmpty()){
    		try {
                double alphaH = Double.parseDouble(aHTextField.getText()) ;
                simDataBase.addNewVariable(new SimulationVariable("alphaH", new double[]{alphaH}));
                aHLabel.setText("aH is set to " + alphaH + " /Kelvin");
			} catch (Exception e) {
	            double alphaH = MathUtils.evaluate(st) ;
	            simDataBase.addNewVariable(new SimulationVariable("alphaH", new double[]{alphaH}));
	            aHLabel.setText("aH is set to " + String.format("%.4f", alphaH) + " /Kelvin");
			}

    	}
        if(dcToggleIsSelected()){
            dcPlot.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    private void chooseaHFromDataBase(){
    	VariableSelectorModule varSelect = new VariableSelectorModule(simDataBase) ;
    	varSelect.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
	    		double alphaH = varSelect.getController().getVariable().getValue(0) ;
	    		simDataBase.addNewVariable(new SimulationVariable("alphaH", new double[]{alphaH}));
	            aHLabel.setText("aH is set to " + String.format("%.4f", alphaH) + " /Kelvin");
	            aHTextField.clear();
	            if(dcToggleIsSelected()){
	                dcPlot.getSelectedToggle().setSelected(false);
	            }
			}
		});
    }

    @FXML
    private void plotDC(){
        if(checkFilledTextsInDCTab()){
            double[] VH_V = MathUtils.linspace(0, 5, 1000) ;
            double alphaH = simDataBase.getVariable("alphaH").getValue(0) ;
            double kv = simDataBase.getVariable("kv").getValue(0) ;
            double Rlinear = simDataBase.getVariable("Rlinear").getValue(0) ;
            selfHeating = new SelfHeating(alphaH, kv, Rlinear) ;
            double[] IH_mA = selfHeating.getCurrent_mA(VH_V) ;
            double[] deltaTH_K = selfHeating.getDeltaT(VH_V) ;
            double[] heaterPower_mW = MathUtils.Arrays.times(VH_V, IH_mA) ;
            simDataBase.addNewVariable(new SimulationVariable("VH_(V)", "Heater Voltage (V)", VH_V));
            simDataBase.addNewVariable(new SimulationVariable("IH_(mA)", "Heater Current (mA)", IH_mA));
            simDataBase.addNewVariable(new SimulationVariable("PH_(mW)", "Heater Power (mW)", heaterPower_mW));
            simDataBase.addNewVariable(new SimulationVariable("deltaTH_(K)", "Heater Temperature (K)", deltaTH_K));
        }

        if(dcPlot.getSelectedToggle().equals(viPlotRadioButton)){
            figDC = createPlot(simDataBase.getVariable("IH_(mA)"), simDataBase.getVariable("VH_(V)")) ;
            showPlot(figDC, dcMatlabPlotPane);
        }
        else if(dcPlot.getSelectedToggle().equals(tempVRadioButton)){
            figDC = createPlot(simDataBase.getVariable("VH_(V)"), simDataBase.getVariable("deltaTH_(K)")) ;
            showPlot(figDC, dcMatlabPlotPane);
        }
        else if(dcPlot.getSelectedToggle().equals(tempIRadioButton)){
            figDC = createPlot(simDataBase.getVariable("IH_(mA)"), simDataBase.getVariable("deltaTH_(K)")) ;
            showPlot(figDC, dcMatlabPlotPane);
        }
        else if(dcPlot.getSelectedToggle().equals(ivPlotRadioButton)){
            figDC = createPlot(simDataBase.getVariable("VH_(V)"), simDataBase.getVariable("IH_(mA)"));
            showPlot(figDC, dcMatlabPlotPane);
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
//    	figDC.exportToMatlab();
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
		return dcTab ;
	}

	@Override
	public MatlabChart getFig() {
		return figDC ;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}



}
