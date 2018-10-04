package phoenixSim.util;

import java.io.IOException;

import People.Meisam.GUI.Plotters.MatlabPlot.MatlabChart;
import People.Meisam.GUI.Utilities.ExportPlot.ExportToMATLAB.ExportToMatlabController;
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import phoenixSim.builder.AbstractController;
import phoenixSim.modules.DatabaseModule;
import phoenixSim.modules.ImportDataModule;
import phoenixSim.modules.PlotterModule;

public class PlotterController extends AbstractController {

	@FXML ToggleButton holdPlotButton ;
	@FXML Button addPlotButton ;
	@FXML ComboBox<String> xAxisComboBox ;
	@FXML ComboBox<String> yAxisComboBox ;
	@FXML ColorPicker pointColorPicker ;
	@FXML ColorPicker lineColorPicker ;
	@FXML AnchorPane plotPane ;
	@FXML AnchorPane variablesPane ;
	@FXML MenuItem export_to_Matlab_menu ;

	int oldWidth, newWidth, oldHeight, newHeight ;
	SwingNode swingNode = new SwingNode() ;

	MatlabChart fig = new MatlabChart() ;

	String xVariable, yVariable ;

    double[] xData ;
    double[] yData ;

    boolean holdFig = false ;

	//**********************creating some simulation data and database ***************************************
//    // step 1: create simulation variables
    double[] x = MoreMath.linspace(-20, 20, 1000) ;
    SimulationVariable varX = new SimulationVariable("X var", x) ;

    double[] y = MoreMath.Arrays.Functions.sin(x) ;
    SimulationVariable varY = new SimulationVariable("Y var", y) ;

    double[] z = MoreMath.Arrays.Functions.asinh(x) ;
    SimulationVariable varZ = new SimulationVariable("Z var", z) ;

//    // step 2: create simulation data base
    SimulationDataBase simDataBase = new SimulationDataBase(varX, varY, varZ) ;
  //**************************************************************

//	SimulationDataBase simDataBase = new SimulationDataBase() ;

	public void setDataBase(SimulationDataBase simDataBase){
		this.simDataBase = simDataBase ;
	}

	@FXML
	public void initialize(){
		if(!plotPane.getChildren().isEmpty()){
			plotPane.getChildren().removeAll(plotPane.getChildren()) ;
		}
        plotPane.getChildren().add(swingNode) ;

		holdFig = false ;
		holdPlotButton.setSelected(false);
		xAxisComboBox.getItems().removeAll(xAxisComboBox.getItems()) ;
		xAxisComboBox.getItems().addAll(simDataBase.getAllNames()) ;
		yAxisComboBox.getItems().removeAll(yAxisComboBox.getItems()) ;
		yAxisComboBox.getItems().addAll(simDataBase.getAllNames()) ;
		initializeColorPickers() ;
		oldWidth = (int) plotPane.getWidth() ;
		oldHeight = (int) plotPane.getHeight() ;
		plotPane.layoutBoundsProperty().addListener((v, oldValue, newValue) -> {
	    	plotPane.getChildren().remove(fig.getChartSwingNode()) ;
	        oldWidth = (int) oldValue.getWidth() ;
	        oldHeight = (int) oldValue.getHeight() ;
	        newWidth = (int) newValue.getWidth() ;
	        newHeight = (int) newValue.getHeight() ;
	        showPlot();
		});
		// initializing the plot
        double[] x = {} ;
        double[] y = {} ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        showPlot();
	}

	private void clearAll(){
		xAxisComboBox.getItems().removeAll(xAxisComboBox.getItems()) ;
		yAxisComboBox.getItems().removeAll(yAxisComboBox.getItems()) ;
		initializeColorPickers() ;
		fig = new MatlabChart() ;
        double[] x = {} ;
        double[] y = {} ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        showPlot();
        holdPlotButton.setSelected(false);
	}

	private void initializeColorPickers(){
		pointColorPicker.setValue(Color.RED);
		lineColorPicker.setValue(Color.BLUE);
	}

    private void showPlot(){
		swingNode.setContent(fig.getChartPanel(newWidth, newHeight));
		swingNode.resize(newWidth, newHeight);
		plotPane.setPrefSize((double) newWidth, (double) newHeight);
    }

	private void updatePlot(){
		if(!holdFig){
			fig = new MatlabChart() ; // creating new plot
			fig.plot(xData, yData);
			fig.RenderPlot();
			fig.xlabel(simDataBase.getVariable(xVariable).getAlias());
			fig.ylabel(simDataBase.getVariable(yVariable).getAlias());

			initializeColorPickers();
			showPlot() ;
		}
//		else{
//			fig.plot(xData, yData);
//			fig.RenderPlot();
//			fig.xlabel(simDataBase.getVariable(xVariable).getAlias());
//			fig.ylabel(simDataBase.getVariable(yVariable).getAlias());
//		}


	}

	private void addPlot(){
		fig.plot(xData, yData);
		fig.RenderPlot();
		fig.xlabel(simDataBase.getVariable(xVariable).getAlias());
		fig.ylabel(simDataBase.getVariable(yVariable).getAlias());
		showPlot() ;
	}

	@FXML
	public void saveAsJPEG(){
		fig.saveAsJPEG(640, 480);
	}

	@FXML
	public void saveAsPNG(){
		fig.saveAsPNG(640, 480);
	}

	@FXML
	public void saveAsSVG(){
		fig.saveAsSVG(640, 480);
	}

	@FXML
	public void xAxisVariablePressed(ActionEvent event){
		xVariable = xAxisComboBox.getSelectionModel().getSelectedItem() ;
		xData = simDataBase.getVariableValues(xVariable) ;
		updatePlot() ;
	}

	@FXML
	public void yAxisVariablePressed(ActionEvent event){
		yVariable = yAxisComboBox.getSelectionModel().getSelectedItem() ;
		yData = simDataBase.getVariableValues(yVariable) ;
		updatePlot() ;
	}

	@FXML
	public void pointColorPressed(ActionEvent event){

	}

	@FXML
	public void lineColorPressed(ActionEvent event){
		int M = fig.getNumberOfFigures() ;
		fig.setFigColor(M-1, lineColorPicker.getValue());
	}

	@FXML
	public void holdPlotButtonPressed(ActionEvent event){
		holdFig = !(holdFig) ; // each time button is pressed we change the state of the boolean
		if(holdFig){
			holdPlotButton.setTextFill(Color.RED);
			addPlotButton.setDisable(false);
		}
		else{
			holdPlotButton.setTextFill(Color.BLACK);
			addPlotButton.setDisable(true);
			updatePlot();
		}
	}

	@FXML
	private void addPlotPressed(){
		if(holdFig){
			fig.plot(xData, yData);
			fig.RenderPlot();
			fig.xlabel(simDataBase.getVariable(xVariable).getAlias());
			fig.ylabel(simDataBase.getVariable(yVariable).getAlias());
		}
		else{
			fig = new MatlabChart() ; // creating new plot
			fig.plot(xData, yData);
			fig.RenderPlot();
			fig.xlabel(simDataBase.getVariable(xVariable).getAlias());
			fig.ylabel(simDataBase.getVariable(yVariable).getAlias());
		}
		initializeColorPickers();
		showPlot() ;
	}

	@FXML
	public void exportToMATLAB(ActionEvent event) throws IOException{
		// need to load the export to Matlab GUI
		FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/ExportPlot/ExportToMATLAB/exportToMatlab.fxml")) ;
		WindowBuilder exportToMatlab = new WindowBuilder(loader) ;
		Image icon = new Image("/People/Meisam/GUI/Utilities/ExportPlot/ExportToMATLAB/Extras/MatlabIcons/Matlab_Logo.png") ;
		exportToMatlab.setIcon(icon);
		exportToMatlab.build("Configure Export to MATLAB", false);
		ExportToMatlabController controller = loader.getController() ;
		controller.setVariables(simDataBase.getVariable(xVariable), simDataBase.getVariable(yVariable));
		controller.setColors(lineColorPicker.getValue(), pointColorPicker.getValue());
		controller.initialize();

	}

	@SuppressWarnings("unused")
	private int[] color_to_RGB255(Color color){
        int blue = (int) (255*color.getBlue()) ;
        int red = (int) (255*color.getRed()) ;
        int green = (int) (255*color.getGreen()) ;
        return new int[] {red, green, blue} ;
	}

	@FXML
	public void newMenuPressed() throws IOException{
		new PlotterModule(simDataBase) ;
	}

	@FXML
	public void databasePressed() throws IOException{
		DatabaseModule db = new DatabaseModule(simDataBase) ;
		db.refreshTable(simDataBase);
		db.getController().getClearAllButton().pressedProperty().addListener((v, ov, nv) -> {
			clearAll();
		});

	}

	@FXML
	private void importPressed(){
		ImportDataModule mod = new ImportDataModule(simDataBase) ;
		mod.getStage().setOnCloseRequest(e -> {
			for(SimulationVariable x : mod.getController().getSimDataBase().getAllVariables()){
				simDataBase.addNewVariable(x);
			}
			initialize() ;
		});

	}

	@FXML
	private void popupPlotPressed(){
		fig.run();
	}

	@FXML
	private void abs_y_pressed(){
		double[] yDataCopy = yData.clone() ; // first make a copy (clone not copy...)
		yData = MoreMath.Arrays.Functions.abs(yData) ;
		addPlot();
		yData = yDataCopy.clone() ; // now return yData to its original state
	}

	@FXML
	private void sqrt_y_pressed(){
		double[] yDataCopy = yData.clone() ; // first make a copy
		int M = yData.length ;
		for(int i=0; i<M; i++){
			if(yData[i] >= 0) {yData[i] = Math.sqrt(yData[i]) ;}
			else {yData[i] = Double.NaN ;}
		}
		addPlot();
		yData = yDataCopy.clone() ; // now return yData to its original state
	}

	@FXML
	private void y_square_pressed(){
		double[] yDataCopy = yData.clone() ; // first make a copy (clone not copy...)
		yData = MoreMath.Arrays.Functions.pow(yData, 2) ;
		addPlot();
		yData = yDataCopy.clone() ; // now return yData to its original state
	}

	@FXML
	private void y_cube_pressed(){
		double[] yDataCopy = yData.clone() ; // first make a copy
		yData = MoreMath.Arrays.Functions.pow(yData, 3) ;
		addPlot();
		yData = yDataCopy.clone() ; // now return yData to its original state
	}

	@FXML
	private void sin_y_pressed(){
		double[] yDataCopy = yData.clone() ; // first make a copy
		yData = MoreMath.Arrays.Functions.sin(yData) ;
		addPlot();
		yData = yDataCopy.clone() ; // now return yData to its original state
	}

	@FXML
	private void cos_y_pressed(){
		double[] yDataCopy = yData.clone() ; // first make a copy
		yData = MoreMath.Arrays.Functions.cos(yData) ;
		addPlot();
		yData = yDataCopy.clone() ; // now return yData to its original state
	}

	@FXML
	private void tan_y_pressed(){
		double[] yDataCopy = yData.clone() ; // first make a copy
		yData = MoreMath.Arrays.Functions.tan(yData) ;
		addPlot();
		yData = yDataCopy.clone() ; // now return yData to its original state
	}

	@FXML
	private void cot_y_pressed(){
		double[] yDataCopy = yData.clone() ; // first make a copy
		yData = MoreMath.Arrays.Functions.cot(yData) ;
		addPlot();
		yData = yDataCopy.clone() ; // now return yData to its original state
	}

	@FXML
	public void closePlotterPressed(){
		variablesPane.getScene().getWindow().hide();
	}

	@FXML
	public void exportToFile(){
		fig.exportToFile(simDataBase);
	}

	@FXML
	public void to_dB_Pressed(){
		double[] yDataCopy = yData.clone() ; // first make a copy
		yData = MoreMath.Arrays.Conversions.todB(yData) ;
		addPlot();
		yData = yDataCopy.clone() ; // now return yData to its original state
	}

	@FXML
	public void from_dB_Pressed(){
		double[] yDataCopy = yData.clone() ; // first make a copy
		yData = MoreMath.Arrays.Conversions.fromdB(yData) ;
		addPlot();
		yData = yDataCopy.clone() ; // now return yData to its original state
	}

	   @FXML
	    public void xAxis_to_Log(){
	    	fig.setXAxis_to_Log();
	    }

	    @FXML
	    public void xAxis_to_Linear(){
	    	fig.setXAxis_to_Linear();
	    }

	    @FXML
	    public void yAxis_to_Log(){
	    	fig.setYAxis_to_Log();
	    }

	    @FXML
	    public void yAxis_to_Linear(){
	    	fig.setYAxis_to_Linear();
	    }

	    @FXML
	    public void setMarkerON(){
	    	fig.markerON();
	    }

	    @FXML
	    public void setMarkerOFF(){
	    	fig.markerOFF();
	    }


	    @FXML TextField lineWidthTextField ;

	    @FXML
	    public void setLineWidth(){
	    	String st = lineWidthTextField.getText() ;
	    	int numPlots = fig.getNumberOfFigures() ;
	    	for(int i=0; i<numPlots; i++){
	    		fig.setFigLineWidth(i, Float.parseFloat(st));
	    	}
	    }

	    @FXML TextField fontSizeTextField ;

	    @FXML
	    public void setFontSize(){
	    	String st = fontSizeTextField.getText() ;
	    	fig.setFontSize((int) MoreMath.evaluate(st));
	    }

}
