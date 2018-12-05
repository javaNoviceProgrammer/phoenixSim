package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import mathLib.plot.MatlabChart;
import phoenixSim.builder.intf.ActionInterface;
import phoenixSim.modules.ExportToMatlabModule;
import phoenixSim.modules.PlotterModule;
import phoenixSim.modules.VariableSelectorModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;

public class DownSamplingTabController extends AbstractTabController {

	SimulationDataBase simDataBase = new SimulationDataBase() ;
    public MatlabChart fig  ;

    SimulationVariable xData = null , yData = null ;
    SimulationVariable xDataDownSampled = null , yDataDownSampled = null ;
    int nOrder ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab tab ;
    @FXML Pane matlabPane ;
    @FXML TextField xDataTextField ;
    @FXML Label xDataLabel ;
    @FXML TextField yDataTextField ;
    @FXML Label yDataLabel ;
    @FXML TextField nLeftTextField ;
    @FXML Label nLeftLabel ;
    @FXML TextField nRightTextField ;
    @FXML Label nRightLabel ;
    @FXML TextField orderTextField ;
    @FXML Label orderLabel ;
    @FXML ListView<String> resultListView ;

	@Override
	public void initialize() {
		xDataLabel.setText("");
		yDataLabel.setText("");
		orderLabel.setText("");
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.renderPlot();
        fig.xlabel("");
        fig.ylabel("");
        this.fig = fig ;
        showPlot(fig, matlabPane);
        matlabPane.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<none>");
	}

	@FXML
	public void setOrder(){
		if(!orderTextField.getText().isEmpty()){
			nOrder = Integer.parseInt(orderTextField.getText()) ;
			orderLabel.setText("Order is set to " + nOrder);
		}
		else{
			orderLabel.setText("");
		}
	}

	@FXML
	public void setXData(){
		String st = xDataTextField.getText() ;
		if(simDataBase.variableExists(st)){
			xData = simDataBase.getVariable(st) ;
			xDataLabel.setText("X data is set to '" + xData.getName() + "'");
		}
		if(xData != null && yData != null){
			fig = createPlot(xData, yData) ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	public void setYData(){
		String st = yDataTextField.getText() ;
		if(simDataBase.variableExists(st)){
			yData = simDataBase.getVariable(st) ;
			yDataLabel.setText("Y data is set to '" + yData.getName() + "'");
		}
		if(xData != null && yData != null){
			fig = createPlot(xData, yData) ;
			showPlot(fig, matlabPane);
		}
	}

	@FXML
	public void chooseXData(){
		VariableSelectorModule varModule = new VariableSelectorModule(simDataBase) ;
		varModule.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
				xData = new SimulationVariable(varModule.getController().getVariable().getName(), varModule.getController().getVariable().getAlias(), varModule.getController().getValues()) ;
				xDataLabel.setText("X data is set to '" + xData.getName() + "'");
				if(xData != null && yData != null){
					fig = createPlot(xData, yData) ;
					showPlot(fig, matlabPane);
				}
			}
		});
	}

	@FXML
	public void chooseYData(){
		VariableSelectorModule varModule = new VariableSelectorModule(simDataBase) ;
		varModule.setExitAction(new ActionInterface() {
			@Override
			public void setExitAction() {
				yData = new SimulationVariable(varModule.getController().getVariable().getName(), varModule.getController().getVariable().getAlias(), varModule.getController().getValues()) ;
				yDataLabel.setText("Y data is set to '" + yData.getName() + "'");
				if(xData != null && yData != null){
					fig = createPlot(xData, yData) ;
					showPlot(fig, matlabPane);
				}
			}
		});
	}


	@FXML
	public void calculate(){

		int N = nOrder ;
		int M = xData.getLength()/N ;
		double[] xDataDS = new double[M] ;
		double[] yDataDS =new double[M] ;
		for(int i=0; i<M; i++){
			xDataDS[i] = xData.getValue(i*N) ;
			yDataDS[i] = yData.getValue(i*N) ;
		}
		xDataDownSampled = new SimulationVariable(xData.getName()+"-DS", xData.getAlias()+"-DS", xDataDS) ;
		yDataDownSampled = new SimulationVariable(yData.getName()+"-DS", yData.getAlias()+"-DS", yDataDS) ;
		simDataBase.addNewVariable(xDataDownSampled);
		simDataBase.addNewVariable(yDataDownSampled);
		
		fig = new MatlabChart() ;
		fig.plot(xDataDS, yDataDS, "r", 2f);
		fig.plot(xData.getAllValues(), yData.getAllValues());
		fig.renderPlot();
		fig.xlabel(xData.getAlias());
		fig.ylabel(yData.getAlias());
		showPlot(fig, matlabPane);

		resultListView.getItems().removeAll(resultListView.getItems()) ;
		resultListView.getItems().add("Original Samples = " + xData.getLength()) ;
		resultListView.getItems().add("New Samples = " + M) ;
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

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}


}
