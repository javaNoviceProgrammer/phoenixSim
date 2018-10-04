package phoenixSim.util;

import PhotonicElements.Utilities.MathLibraries.MoreMath;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import phoenixSim.builder.AbstractController;

public class DataCollectorController extends AbstractController {

	@FXML ComboBox<String> methodComboBox ;
	@FXML Label stepPointsLabel ;
	@FXML TextField startTextField ;
	@FXML TextField endTextField ;
	@FXML TextField stepPointsTextField ;
	@FXML Button addValueButton ;
	@FXML Button deleteValueButton ;
	@FXML Button setExitButton ;
	@FXML TableColumn<InputData, Double> valueTable ;
	@FXML TableView<InputData> tablePane ;
	@FXML SplitPane splitPane ;

	ObservableList<InputData> data ;

	private double startValue, endValue, stepSize ;
	private int numPoints ;
	private boolean stepSelected ;

	@FXML
	public void initialize(){

		// initialize combo box for method of input
		methodComboBox.getItems().removeAll(methodComboBox.getItems()) ;
		methodComboBox.getItems().addAll("step size", "number of points") ;

	}

	@FXML
	public void methodComboBoxPressed(){
		String selected = methodComboBox.getSelectionModel().getSelectedItem() ;
		if(selected == "step size"){
			stepPointsLabel.setText("step size");
			stepSelected = true ;
		}
		else{
			stepPointsLabel.setText("number of points");
			stepSelected = false ;
		}

	}

	@FXML
	public void addValueButtonPressed(){

		startValue = Double.parseDouble(startTextField.getText()) ;
		endValue = Double.parseDouble(endTextField.getText()) ;

		double[] x ;

		if(stepSelected) {
			stepSize = Double.parseDouble(stepPointsTextField.getText()) ;
			x = MoreMath.linspace(startValue, endValue, stepSize);
		}
		else {
			numPoints = Integer.parseInt(stepPointsTextField.getText()) ;
			x = MoreMath.linspace(startValue, endValue, numPoints);
		}

			setUpTable(x) ;

	}


	private void setUpTable(double[] x){

			data = FXCollections.observableArrayList();

		for (double aX : x) {
			data.add(new InputData("", aX));
		}

			valueTable.setCellValueFactory(new PropertyValueFactory<>("value"));
			tablePane.setItems(data);
			tablePane.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	@FXML
	public void deleteValueButtonPressed(){
		tablePane.getItems().removeAll(tablePane.getSelectionModel().getSelectedItems()) ;
	}

	@FXML
	public void setExitButtonPressed(){
	}

	public double[] getInputData(){
		int M = data.size() ;
		double[] x = new double[M] ;
		for(int i=0; i<M; i++){
			x[i] = data.get(i).getValue() ;
		}
		return x ;
	}

	public double[] getAllValues(){
		int M = data.size() ;
		double[] x = new double[M] ;
		for(int i=0; i<M; i++){
			x[i] = data.get(i).getValue() ;
		}
		return x ;
	}

	public Button getExitButton(){
		return setExitButton ;
	}

}
