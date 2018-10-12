package phoenixSim.util;

import org.controlsfx.control.CheckComboBox;

import PhotonicElements.Utilities.MathLibraries.MoreMath;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;

public class VariableSelectorController {

//	SimulationDataBase simDataBase = new SimulationDataBase() ;

	SimulationVariable xVar = new SimulationVariable("xVar", "Variable X", MoreMath.linspace(-10, 10, 1000)) ;
	SimulationVariable yVar = new SimulationVariable("yVar", "variable Y", MoreMath.Arrays.Functions.sin(xVar.getAllValues())) ;
	SimulationDataBase simDataBase = new SimulationDataBase(xVar, yVar) ;

	double[] values ;
	String variable ;

	public void setSimDataBase(SimulationDataBase simDataBase){
		this.simDataBase = simDataBase ;
	}

	public SimulationDataBase getSimDataBase(){
		return simDataBase ;
	}

	@FXML ComboBox<String> variableComboBox ;
	@FXML CheckComboBox<Double> valuesComboBox ;
	@FXML RadioButton selectValues ;
	@FXML RadioButton selectAllValues ;
	@FXML Button setExitButton ;

	@FXML
	public void initialize(){
		// initialize variable combo box
		variableComboBox.getItems().removeAll(variableComboBox.getItems()) ;
		for(String st : simDataBase.getAllNames()){
			variableComboBox.getItems().add(st) ;
		}
		variableComboBox.getSelectionModel().selectedItemProperty().addListener((v, ov, nv)-> {
			setValuesComboBox();
		});
	}

	@FXML
	private void setVariable(){
		if(!variableComboBox.getItems().isEmpty()){
			variable = variableComboBox.getSelectionModel().getSelectedItem() ;
		}
	}

	@FXML
	private void setValuesComboBox(){
		if(selectValues.isSelected()){
			valuesComboBox.getItems().removeAll(valuesComboBox.getItems()) ;
			for(double x : simDataBase.getVariableValues(variable)){
				valuesComboBox.getItems().add(x) ;
			}
		}
	}

	public double[] getValues(){
		double[] values = new double[0] ;
		if(selectAllValues.isSelected()){
			values = MoreMath.Arrays.concat(values, simDataBase.getVariableValues(variable)) ;
		}
		else if(selectValues.isSelected()){
			for(double x : valuesComboBox.getItems()){
				if(valuesComboBox.getCheckModel().isChecked(x)){
					values = MoreMath.Arrays.append(values, x) ;
				}
			}
		}
		return values ;
	}

	public SimulationVariable getVariable(){
		return simDataBase.getVariable(variable) ;
	}

	public Button getSetExitButton(){
		return setExitButton ;
	}

}
