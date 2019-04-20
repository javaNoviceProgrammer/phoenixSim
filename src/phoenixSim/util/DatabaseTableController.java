package phoenixSim.util;

import java.io.File;
import java.io.IOException;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import mathLib.util.MathUtils;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import phoenixSim.builder.AbstractController;
import phoenixSim.modules.ImportDataModule;

/**
 * Created by meisam on 7/7/17.
 */
public class DatabaseTableController extends AbstractController {

    @FXML TableView<SimulationVariable> paramTable ;
    @FXML Button paramCloseButton ;
    @FXML Button clearAllButton ;
    @FXML Button refreshButton ;
    @FXML Button loadButton ;
    @FXML TextField functionField, nameField, aliasField ;
    @FXML public Tab tab ;

    public SimpleBooleanProperty loadCompleted = new SimpleBooleanProperty(false) ;

//    SimulationDataBase dataBase ;
//
//    //**********************creating some simulation data and database ***************************************
    // step 1: create simulation variables
    double[] x = MathUtils.linspace(-20, 20, 5) ;
    SimulationVariable varX = new SimulationVariable("varX", "variable x", x) ;

    double[] y = MathUtils.Arrays.Functions.sin(x) ;
    SimulationVariable varY = new SimulationVariable("varY", "variable y", y) ;

    double[] z = MathUtils.linspace(-10, 10, 3) ;
    SimulationVariable varZ = new SimulationVariable("varZ", "variable z", z) ;

    // step 2: create simulation data base
    SimulationDataBase dataBase = new SimulationDataBase(varX, varY, varZ) ;
    //**************************************************************

    public void setDataBase(SimulationDataBase dataBase){
        this.dataBase = dataBase ;
    }

    public SimulationDataBase getDataBase(){
        return dataBase ;
    }

    @FXML
    public void initialize(){
    	loadCompleted = new SimpleBooleanProperty(false) ;
    }


    @FXML
    public void clearDataBaseAndTable(){
    	if(paramTable.getSelectionModel().getSelectedItems().isEmpty()){
            paramTable.getItems().clear();
            dataBase.clear();
    	}
    	else{
        	for(SimulationVariable var : paramTable.getSelectionModel().getSelectedItems()){
        		dataBase.removeVariable(var);
        	}
        	refreshTable(dataBase);
    	}

    }

    @SuppressWarnings("unchecked")
	public void updateParamTable(){
        ObservableList<SimulationVariable> list = FXCollections.observableArrayList() ;
        for(SimulationVariable x : dataBase.getVariableList()){
            list.add(x) ;
        }
        TableColumn<SimulationVariable, String> nameColumn = new TableColumn<SimulationVariable, String>("Name") ;
        nameColumn.setCellValueFactory(new PropertyValueFactory<SimulationVariable, String>("name"));

        TableColumn<SimulationVariable, String> aliasColumn = new TableColumn<SimulationVariable, String>("Alias") ;
        aliasColumn.setCellValueFactory(new PropertyValueFactory<SimulationVariable, String>("alias"));

        TableColumn<SimulationVariable, String> sizeColumn = new TableColumn<SimulationVariable, String>("Size") ;
        sizeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SimulationVariable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SimulationVariable, String> param) {
                return new SimpleStringProperty(param.getValue().getArraySize());
            }
        }) ;

        TableColumn<SimulationVariable, String> typeColumn = new TableColumn<SimulationVariable, String>("Type") ;
        typeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SimulationVariable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SimulationVariable, String> param) {
                return new SimpleStringProperty(param.getValue().getType()) ;
            }
        });

        TableColumn<SimulationVariable, String> valuesColumn = new TableColumn<SimulationVariable, String>("Values") ;
        valuesColumn.setCellValueFactory(new PropertyValueFactory<SimulationVariable, String>("variableList"));

        paramTable.getColumns().addAll(nameColumn, aliasColumn, sizeColumn, typeColumn, valuesColumn) ;
        paramTable.setItems(list);
        // choosing multiple rows
        paramTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    public Button getCloseButton(){
        return paramCloseButton ;
    }

    public Button getRefreshButton(){
    	return refreshButton ;
    }

    // saving database to the file
    @FXML
    public void seveToFile() throws IOException{
    	dataBase.saveToFile();
    }

    // loading database from file
    @FXML
    public void laodFromFile(){
    	dataBase.loadFromFile();
    	if(dataBase.dataBaseChanged()){
        	paramTable.getColumns().clear();
        	updateParamTable();
    	}

    }

    public void refreshTable(SimulationDataBase simDataBase){
    	paramTable.getColumns().clear();
    	this.setDataBase(simDataBase);
    	updateParamTable();
    }

    public Button getLoadButton(){
    	return loadButton ;
    }

    public Button getClearAllButton(){
    	return clearAllButton ;
    }

    @FXML
    public void exportPressed(){
    	ObservableList<SimulationVariable> list = paramTable.getSelectionModel().getSelectedItems() ;
    	if(list.isEmpty()){
    		(new ExportVariables(dataBase)).export();
    	}
    	else{
    		SimulationDataBase selectedVars = new SimulationDataBase() ;
    		for(SimulationVariable x : list){
    			selectedVars.addNewVariable(x);
    		}
    		(new ExportVariables(selectedVars)).export();
    	}
    }

    @FXML
    public void importPressed(){
    	new ImportDataModule(dataBase) ;
    }

    @FXML
    public void applyFunctionPressed(){
    	for(SimulationVariable var : paramTable.getSelectionModel().getSelectedItems()){
    		applyFunctionToVariable(var);
    	}
    	refreshTable(dataBase);
    }

    private void applyFunctionToVariable(SimulationVariable selectedVar){
    	String st = functionField.getText() ;
    	Expression func = new ExpressionBuilder(st).variable("x").build() ; // assume that variable is "x"
    	double[] funcValues = new double[selectedVar.getLength()] ;
    	for(int i=0; i<funcValues.length; i++){
    		funcValues[i] = func.setVariable("x", selectedVar.getValue(i)).evaluate() ;
    	}
    	String funcName = st.replaceAll("x", selectedVar.getName()) ;
    	String funcAlias = st.replaceAll("x", selectedVar.getAlias()) ;
    	SimulationVariable funcVariable = new SimulationVariable(funcName, funcAlias, funcValues) ;
    	dataBase.addNewVariable(funcVariable);
    }
    
    @FXML
    private void applyNamePressed() {
    	String st = nameField.getText() ;
    	SimulationVariable var = paramTable.getSelectionModel().getSelectedItem() ;
    	dataBase.getVariableFromAlias(var.getAlias()).setName(st);
    	refreshTable(dataBase);
    	nameField.clear();
    }
    
    @FXML
    private void applyAliasPressed() {
    	String st = aliasField.getText() ;
    	SimulationVariable var = paramTable.getSelectionModel().getSelectedItem() ;
    	dataBase.getVariable(var.getName()).setAlias(st);
    	refreshTable(dataBase);
    	aliasField.clear();
    }

    
    // implementing drag and drop
    
	@FXML
	private void setOnDragOver(DragEvent event){
		Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
	}

	@FXML
	private void setOnDragDropped(DragEvent event){
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            File file = db.getFiles().get(0) ;
        	if(checkExtension(file, "dbsim")){
        		dataBase.loadFromFile(file);
        		paramTable.getColumns().clear();
        		updateParamTable();
        	}
        }
        event.setDropCompleted(success);
        event.consume();
	}
	
	private boolean checkExtension(File file, String ext){
		String filePath = file.getAbsolutePath() ;
		String[] st = filePath.trim().split("\\.") ;
		int M = st.length ;
		String fileExt = st[M-1] ;
		if(fileExt.equals(ext)){
			return true ;
		}
		else{
			return false ;
		}
	}

}
