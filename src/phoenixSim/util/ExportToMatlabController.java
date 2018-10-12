package phoenixSim.util;

import java.io.IOException;

import flanagan.io.FileOutput;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import mathLib.util.MathUtils;
import phoenixSim.builder.AbstractController;
import phoenixSim.modules.ExportCompleted;

public class ExportToMatlabController extends AbstractController {

	// items in plot style tab
	@FXML Button exportButton ;
	@FXML ComboBox<String> lineStyle ;
	@FXML ComboBox<String> markerStyle ;
	@FXML TextField plotTitle ;
	@FXML TextField lineWidth ;
	@FXML TextField markerSize ;
	@FXML CheckBox plotBox ;
	@FXML CheckBox gridON ;
	@FXML CheckBox holdON ;
	@FXML ColorPicker lineColorPicker ;
	@FXML ColorPicker markerColorPicker ;
	// items in X Axis style tab
	@FXML TextField xAxisLabel ;
	@FXML TextField xAxisMin ;
	@FXML TextField xAxisMax ;
	@FXML RadioButton xAxisScaleLinear ;
	@FXML RadioButton xAxisScaleLog ;
	@FXML CheckBox xAxisAuto ;
	@FXML CheckBox xAxisGrid ;
	@FXML CheckBox xMinorGrid ;
	@FXML RadioButton xAxisPositionBottom ;
	@FXML RadioButton xAxisPositionTop ;
	@FXML RadioButton xAxisDirectionNormal ;
	@FXML RadioButton xAxisDirectionReverse ;
	@FXML ComboBox<String> xAxisFontSize ;
	// items in Y Axis style tab
	@FXML TextField yAxisLabel ;
	@FXML TextField yAxisMin ;
	@FXML TextField yAxisMax ;
	@FXML RadioButton yAxisScaleLinear ;
	@FXML RadioButton yAxisScaleLog ;
	@FXML CheckBox yAxisAuto ;
	@FXML CheckBox yAxisGrid ;
	@FXML CheckBox yMinorGrid ;
	@FXML RadioButton yAxisPositionLeft ;
	@FXML RadioButton yAxisPositionRight ;
	@FXML RadioButton yAxisDirectionNormal ;
	@FXML RadioButton yAxisDirectionReverse ;
	@FXML ComboBox<String> yAxisFontSize ;
	// items in Legend tab
	@FXML ComboBox<String> legendFontSize ;
	@FXML ToggleGroup showLegend ;
	@FXML ToggleGroup legendOrientation ;
	@FXML ToggleGroup legendPosition ;
	@FXML RadioButton legendEnabled ;
	@FXML RadioButton legendDisabled ;
	@FXML TextField legendTitle ;
	@FXML RadioButton northEast ;
	@FXML RadioButton north ;
	@FXML RadioButton northWest ;
	@FXML RadioButton west ;
	@FXML RadioButton east ;
	@FXML RadioButton southEast ;
	@FXML RadioButton south ;
	@FXML RadioButton southWest ;
	@FXML RadioButton legendHorizontal ;
	@FXML RadioButton legendVertical ;

//	int[] lineColor = null ;
//	int[] markerColor = null ;
//	Color line_color = null ;
//	Color marker_color = null ;

	Color line_color = Color.BLUE ;
	Color marker_color = Color.RED ;

//	SimulationVariable xVar, yVar ;

//	Test SimulationVariable xVar, yVar ;

    double[] x = MathUtils.linspace(-10, 10, 100) ;
    SimulationVariable xVar = new SimulationVariable("X var", "variable X (Hz)", x) ;

    double[] y = MathUtils.Arrays.Functions.sin(x) ;
    SimulationVariable yVar = new SimulationVariable("Y var", "variable Y (dB)", y) ;

	public void setVariables(SimulationVariable xVar, SimulationVariable yVar){
		this.xVar = xVar ;
		this.yVar = yVar ;
	}

//	public void setColors(int[] lineColor, int[] markerColor){
//		// RGB between 0 , 255 values
//		this.lineColor = lineColor ;
//		this.markerColor = markerColor ;
//	}

	public void setColors(Color line_color, Color marker_color){
		this.line_color = line_color ;
		this.marker_color = marker_color ;
//		lineColor = color_to_RGB255(line_color) ;
//		markerColor = color_to_RGB255(marker_color) ;
	}

	@FXML
	public void initialize(){
		//*** plot style initialize**************
		lineStyle.getItems().clear();
		lineStyle.getItems().add("none") ;
		lineStyle.getItems().add("-") ;
		lineStyle.getItems().add("--") ;
		lineStyle.getItems().add(":") ;
		lineStyle.getItems().add("-.") ;

		lineStyle.getSelectionModel().select(1);

		markerStyle.getItems().clear();
		markerStyle.getItems().add("none") ;
		markerStyle.getItems().add("+") ;
		markerStyle.getItems().add("o") ;
		markerStyle.getItems().add("*") ;
		markerStyle.getItems().add(".") ;
		markerStyle.getItems().add("x") ;
		markerStyle.getItems().add("square") ;
		markerStyle.getItems().add("diamond") ;
		markerStyle.getItems().add("v") ;
		markerStyle.getItems().add("^") ;
		markerStyle.getItems().add(">") ;
		markerStyle.getItems().add("<") ;
		markerStyle.getItems().add("pentagram") ;
		markerStyle.getItems().add("hexagram") ;

		markerStyle.getSelectionModel().select(0);

		initializeColorPickers() ;

		gridON.selectedProperty().addListener(e -> {
			if(gridON.isSelected()){
				xAxisGrid.setSelected(true);
				xAxisGrid.setDisable(true);
				yAxisGrid.setSelected(true);
				yAxisGrid.setDisable(true);
			}
			else{
				xAxisGrid.setSelected(false);
				xAxisGrid.setDisable(false);
				yAxisGrid.setSelected(false);
				yAxisGrid.setDisable(false);
			}
		});
		//*** x-Axis style initialize**************
		initializeFontSize(xAxisFontSize);
		xAxisAuto.selectedProperty().addListener(e -> {
			if(xAxisAuto.isSelected()){
				xAxisMin.setDisable(true);
				xAxisMax.setDisable(true);
			}
			else{
				xAxisMin.setDisable(false);
				xAxisMax.setDisable(false);
			}
		});
		//*** y-Axis style initialize**************
		initializeFontSize(yAxisFontSize);
		yAxisAuto.selectedProperty().addListener(e -> {
			if(yAxisAuto.isSelected()){
				yAxisMin.setDisable(true);
				yAxisMax.setDisable(true);
			}
			else{
				yAxisMin.setDisable(false);
				yAxisMax.setDisable(false);
			}
		});
		//*** Legend style initialize**************
		initializeFontSize(legendFontSize);

	}

	private void initializeFontSize(ComboBox<String> fontBox){
		double[] fontSize = MathUtils.linspace(10, 60, 5d) ;
		for(double x : fontSize){
			fontBox.getItems().add(x + "") ;
		}
	}

	private void initializeColorPickers(){
		lineColorPicker.setValue(line_color);
		markerColorPicker.setValue(marker_color);
	}

//	private int[] color_to_RGB255(Color color){
//        int blue = (int) (255*color.getBlue()) ;
//        int red = (int) (255*color.getRed()) ;
//        int green = (int) (255*color.getGreen()) ;
//        return new int[] {red, green, blue} ;
//	}

	@FXML
	public void lineColorPressed(){
		line_color = lineColorPicker.getValue() ;
	}

	@FXML
	public void markerColorPressed(){
		marker_color = markerColorPicker.getValue() ;
	}

	@FXML
	public void exportButtonPressed() throws IOException{
		try {
			// creating dialog box for setting path and name
			FileChooserFX fc = new FileChooserFX() ;
			fc.setExtension("m");
			fc.saveFile();
			String fullPath = fc.getFilePath() ;

			FileOutput fout = new FileOutput(fullPath, "w") ;
			getGenericCode(fout);
			fout.println(getPlotStyleCode());
			fout.println(getColorsCode());
			fout.println(getXAxisStyleCode());
			fout.println(getYAxisStyleCode());
			fout.println(getLegendCode());
			fout.close();
			System.gc();
			new ExportCompleted() ;

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void getGenericCode(FileOutput fout){
		// creating plot and plot handler from x and y values
		fout.print("x = [");
		for(int i=0; i<xVar.getLength(); i++){
			fout.print(xVar.getValue(i) + " ");
		}
		fout.print("];\n");
		fout.print("y = [");
		for(int i=0; i<yVar.getLength(); i++){
			fout.print(yVar.getValue(i) + " ");
		}
		fout.print("];\n");
		fout.println("ph = plot(x, y) ;");
		fout.println("ax = gca ;");
	}

	private String[] getPlotStyleCode(){
		String[] code = {} ;
		// setting plot box configuration
		String st0 ;
		if(plotBox.isSelected()){
			st0 = "ax.Box = 'on' ;" ;
		}
		else{
			st0 = "ax.Box = 'off' ;" ;
		}
		// setting plot title
		String st1 ;
		if(plotTitle.getText().isEmpty()){
			st1 = "ax.Title.String = " + "'" + "" + "'" + ";" ;
		}
		else{
			st1 = "ax.Title.String = " + "'" + plotTitle.getText() + "'" + ";" ;
		}
		// setting line style
		String st2 = "ph.LineStyle = " + "'" + lineStyle.getSelectionModel().getSelectedItem() + "'" + ";" ;
		// setting line width
		String st3 ;
		if(lineWidth.getText().isEmpty()){
			st3 = "ph.LineWidth = " + 1 + ";" ;
		}
		else{
			st3 = "ph.LineWidth = " + lineWidth.getText() + ";" ;
		}
		// setting marker style
		String st4 = "ph.Marker = " + "'" + markerStyle.getSelectionModel().getSelectedItem() + "'" + ";" ;
		// setting marker size
		String st5 ;
		if(markerSize.getText().isEmpty()){
			st5 = "ph.MarkerSize = " + 5 + ";" ;
		}
		else{
			st5 = "ph.MarkerSize = " + markerSize.getText() + ";" ;
		}
		// setting grid ON configuration
		String st6 ;
		if(gridON.isSelected()){
			st6 = "grid on ;" ;
		}
		else{
			st6 = "grid off ;" ;
		}
		String st7 ;
		if(gridON.isSelected()){
			st7 = "hold on ;" ;
		}
		else{
			st7 = "hold off ;" ;
		}
		code = MathUtils.Arrays.concat(code, new String[] {st0, st1, st2, st3, st4, st5, st6, st7}) ;
		return code ;
	}

	private String[] getColorsCode(){
		String[] code = {} ;
		String st0 = "ph.Color = [" + line_color.getRed() + " " + line_color.getGreen() + " " + line_color.getBlue() + "] ;" ;
		String st1 = "ph.MarkerEdgeColor = [" + marker_color.getRed() + " " + marker_color.getGreen() + " " + marker_color.getBlue() + "] ;" ;
		code = MathUtils.Arrays.concat(code, new String[] {st0, st1}) ;
		return code ;
	}

	private String[] getXAxisStyleCode(){
		String[] code = {} ;
		String st0 ;
		if(xAxisLabel.getText().isEmpty()){
//			st0 = "ax.XLabel.String = " + "'" + xVar.getName() + "'" + ";" ;
			st0 = "ax.XLabel.String = " + "'" + xVar.getAlias() + "'" + ";" ;
		}
		else{
			st0 = "ax.XLabel.String = " + "'" + xAxisLabel.getText() + "'" + ";" ;
		}
		String st1 ;
		if(xAxisAuto.isSelected()){
			st1 = "ax.XLimMode = 'auto' ;" ;
		}
		else{
			String s1 ;
			if(xAxisMin.getText().isEmpty()){
				s1 = MathUtils.Arrays.FindMinimum.getValue(xVar.getAllValuesRemoveNaNs()) + "" ;
			}
			else{
				s1 = xAxisMin.getText() ;
			}
			String s2 ;
			if(xAxisMax.getText().isEmpty()){
				s2 = MathUtils.Arrays.FindMaximum.getValue(xVar.getAllValuesRemoveNaNs()) + "" ;
			}
			else{
				s2 = xAxisMax.getText() ;
			}
			st1 = "ax.XLim = [" + s1 + " " + s2 + "] ;" ;
		}
		String st2 ;
		if(xAxisGrid.isSelected()){
			st2 = "ax.XGrid = 'on' ;" ;
		}
		else{
			st2 = "ax.XGrid = 'off' ;" ;
		}
		String st3 ;
		if(xAxisScaleLinear.isSelected()){
			st3 = "ax.XScale = 'linear' ;" ;
		}
		else if(xAxisScaleLog.isSelected()){
			st3 = "ax.XScale = 'Log' ;" ;
		}
		else{
			st3 = "ax.XScale = 'linear' ;" ; // this is default
		}
		String st4 ;
		if(xMinorGrid.isSelected()){
			st4 = "ax.XMinorGrid = 'on' ;" ;
		}
		else{
			st4 = "ax.XMinorGrid = 'off' ;" ;
		}
		String st5 ;
		if(xAxisPositionBottom.isSelected()){
			st5 = "ax.XAxisLocation = 'bottom' ;" ;
		}
		else if(xAxisPositionTop.isSelected()){
			st5 = "ax.XAxisLocation = 'top' ;" ;
		}
		else{
			st5 = "ax.XAxisLocation = 'bottom' ;" ; // this is default
		}
		String st6 ;
		if(xAxisDirectionNormal.isSelected()){
			st6 = "ax.XDir = 'normal' ;" ;
		}
		else if(xAxisDirectionReverse.isSelected()){
			st6 = "ax.XDir = 'reverse' ;" ;
		}
		else{
			st6 = "ax.XDir = 'normal' ;" ; // this is default
		}
		String st7 ;
		if(xAxisFontSize.getSelectionModel().isEmpty()){
			st7 = "ax.XLabel.FontSize = 11 ;" ;
		}
		else{
			st7 = "ax.XLabel.FontSize =" + xAxisFontSize.getSelectionModel().getSelectedItem() + " ;" ;
		}

		code = MathUtils.Arrays.concat(code, new String[] {st0, st1, st2, st3, st4, st5, st6, st7}) ;
		return code ;
	}

	private String[] getYAxisStyleCode(){
		String[] code = {} ;
		String st0 ;
		if(yAxisLabel.getText().isEmpty()){
//			st0 = "ax.YLabel.String = " + "'" + yVar.getName() + "'" + ";" ;
			st0 = "ax.YLabel.String = " + "'" + yVar.getAlias() + "'" + ";" ;
		}
		else{
			st0 = "ax.YLabel.String = " + "'" + yAxisLabel.getText() + "'" + ";" ;
		}
		String st1 ;
		if(yAxisAuto.isSelected()){
			st1 = "ax.YLimMode = 'auto' ; " ;
		}
		else{
			String s1 ;
			if(yAxisMin.getText().isEmpty()){
				s1 = MathUtils.Arrays.FindMinimum.getValue(yVar.getAllValuesRemoveNaNs()) + "" ;
			}
			else{
				s1 = yAxisMin.getText() ;
			}
			String s2 ;
			if(yAxisMax.getText().isEmpty()){
				s2 = MathUtils.Arrays.FindMaximum.getValue(yVar.getAllValuesRemoveNaNs()) + "" ;
			}
			else{
				s2 = yAxisMax.getText() ;
			}
			st1 = "ax.YLim = [" + s1 + " " + s2 + "] ;" ;
		}
		String st2 ;
		if(yAxisGrid.isSelected()){
			st2 = "ax.YGrid = 'on' ;" ;
		}
		else{
			st2 = "ax.YGrid = 'off' ;" ;
		}
		String st3 ;
		if(yAxisScaleLinear.isSelected()){
			st3 = "ax.YScale = 'linear' ;" ;
		}
		else if(yAxisScaleLog.isSelected()){
			st3 = "ax.YScale = 'Log' ;" ;
		}
		else{
			st3 = "ax.YScale = 'linear' ;" ; // this is default
		}
		String st4 ;
		if(yMinorGrid.isSelected()){
			st4 = "ax.YMinorGrid = 'on' ;" ;
		}
		else{
			st4 = "ax.YMinorGrid = 'off' ;" ;
		}
		String st5 ;
		if(yAxisPositionLeft.isSelected()){
			st5 = "ax.YAxisLocation = 'left' ;" ;
		}
		else if(yAxisPositionRight.isSelected()){
			st5 = "ax.YAxisLocation = 'right' ;" ;
		}
		else{
			st5 = "ax.YAxisLocation = 'bottom' ;" ; // this is default
		}
		String st6 ;
		if(yAxisDirectionNormal.isSelected()){
			st6 = "ax.YDir = 'normal' ;" ;
		}
		else if(yAxisDirectionReverse.isSelected()){
			st6 = "ax.YDir = 'reverse' ;" ;
		}
		else{
			st6 = "ax.YDir = 'normal' ;" ; // this is default
		}
		String st7 ;
		if(yAxisFontSize.getSelectionModel().isEmpty()){
			st7 = "ax.YLabel.FontSize = 11 ;" ;
		}
		else{
			st7 = "ax.YLabel.FontSize =" + yAxisFontSize.getSelectionModel().getSelectedItem() + " ;" ;
		}

		code = MathUtils.Arrays.concat(code, new String[] {st0, st1, st2, st3, st4, st5, st6, st7}) ;
		return code ;
	}

	private String[] getLegendCode(){
		String[] code = {} ;
		if(legendDisabled.isSelected()){
			String st0 = "legend('off') ;" ;
			code = MathUtils.Arrays.concat(code, new String[]{st0}) ;
		}
		else if(legendEnabled.isSelected()){
			String st0 = "legend({'" + getLegendTtile() + "'}, 'Orientation', '" + getLegendOrientation() +
					"', 'Location', '" + getLegendPosition() + "', 'FontSize', " + legendFontSize.getSelectionModel().getSelectedItem() + ") ;" ;
			code = MathUtils.Arrays.concat(code, new String[]{st0}) ;
		}

		return code ;
	}

	private String getLegendOrientation(){
		if(legendVertical.isSelected()){
			return "vertical" ;
		}
		else{
			return "horizontal" ;
		}
	}

	private String getLegendTtile(){
		if(legendTitle.getText().isEmpty()){
			return "XYseries" ;
		}
		else{
			return legendTitle.getText() ;
		}
	}

	private String getLegendPosition(){
		if(legendPosition.getSelectedToggle().equals(northEast)){
			return "northeast" ;
		}
		else if(legendPosition.getSelectedToggle().equals(north)){
			return "north" ;
		}
		else if(legendPosition.getSelectedToggle().equals(northWest)){
			return "northwest" ;
		}
		else if(legendPosition.getSelectedToggle().equals(west)){
			return "west" ;
		}
		else if(legendPosition.getSelectedToggle().equals(east)){
			return "east" ;
		}
		else if(legendPosition.getSelectedToggle().equals(southEast)){
			return "southeast" ;
		}
		else if(legendPosition.getSelectedToggle().equals(south)){
			return "south" ;
		}
		else{
			return "southWest" ;
		}
	}

	@FXML
	private void disableLegendOptions(){
		legendTitle.setDisable(true);
		legendVertical.setDisable(true);
		legendHorizontal.setDisable(true);
		northEast.setDisable(true);
		north.setDisable(true);
		northWest.setDisable(true);
		east.setDisable(true);
		west.setDisable(true);
		southEast.setDisable(true);
		south.setDisable(true);
		southWest.setDisable(true);
		legendFontSize.setDisable(true);
	}

	@FXML
	private void enableLegendOptions(){
		legendTitle.setDisable(false);
		legendVertical.setDisable(false);
		legendHorizontal.setDisable(false);
		northEast.setDisable(false);
		north.setDisable(false);
		northWest.setDisable(false);
		east.setDisable(false);
		west.setDisable(false);
		southEast.setDisable(false);
		south.setDisable(false);
		southWest.setDisable(false);
		legendFontSize.setDisable(false);
	}






}
