package phoenixSim.modules;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mathLib.plot.MatlabChart;
import phoenixSim.util.ExportToMatlabController;
import phoenixSim.util.SimulationVariable;

public class ExportToMatlabModule {
	
	MatlabChart fig ;
	ExportToMatlabController controller ;
	
	public ExportToMatlabModule(MatlabChart fig) {
		this.fig = fig ;
		loadGUI();
		setExportData() ;
	}
	
	private void loadGUI() {
        Stage window = new Stage() ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/phoenixSim/fxmls/exportToMatlab.fxml")) ;
        Parent root;
		try {
			root = loader.load();
	        Scene scene = new Scene(root) ;
	        window.setScene(scene);
	        window.setTitle("Configure Export to MATLAB");
	        window.getIcons().add(new Image(getClass().getResourceAsStream("/phoenixSim/extras/Matlab_Logo.png"))) ;
	        window.setResizable(false);
	        window.show();
	        
	        controller = loader.getController() ;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void setExportData() {
		int M = fig.getChart().getXYPlot().getDataset().getItemCount(0) ;
        double[] x = new double[M] ;
        double[] y = new double[M] ;
        
        for(int i=0; i<M; i++){
        	x[i] = fig.getChart().getXYPlot().getDataset(0).getXValue(0, i) ;
        	y[i] = fig.getChart().getXYPlot().getDataset(0).getYValue(0, i) ;
        }
        SimulationVariable xVar = new SimulationVariable(fig.getXLabel(), x) ;
        SimulationVariable yVar = new SimulationVariable(fig.getYLabel(), y) ;
        controller.setVariables(xVar, yVar);
	}

}
