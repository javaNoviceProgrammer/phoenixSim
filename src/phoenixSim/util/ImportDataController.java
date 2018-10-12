package phoenixSim.util;

import java.io.File;
import java.util.ArrayList;

import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import flanagan.io.FileInput;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import mathLib.util.MathUtils;
import phoenixSim.modules.DatabaseModule;
import phoenixSim.modules.PlotterModule;

public class ImportDataController {

	@FXML MenuBar menuBar ;
	@FXML SpreadsheetView table ;

	String[][] data = null ;

	SimulationDataBase simDataBase = new SimulationDataBase() ;

	public void setSimDataBase(SimulationDataBase simDataBase){
		this.simDataBase = simDataBase ;
	}

	public SimulationDataBase getSimDataBase(){
		return simDataBase ;
	}

	@FXML
	public void initialize(){

		initializeGrid() ;
	}

	private void initializeGrid(){
		 //I create an empty grid
		 int rowCount = 100;
	     int columnCount = 1;
	     GridBase grid = new GridBase(rowCount, columnCount);

	     ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
	     for (int row = 0; row < grid.getRowCount(); ++row) {
	         final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
	         for (int column = 0; column < grid.getColumnCount(); ++column) {
	             list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,""));
	         }
	         rows.add(list);
	     }
	      grid.setRows(rows);
	     table.setGrid(grid);
	}

	// this method for creating grid and showing data inside it
	private void createGrid(int numRows, int numColumns, String[][] data){
		 //I create a sample grid
		 int rowCount = numRows ;
	     int columnCount = numColumns ;
	     GridBase grid = new GridBase(rowCount, columnCount);

	     ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
	     for (int row = 0; row < grid.getRowCount(); ++row) {
	         final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
	         for (int column = 0; column < grid.getColumnCount(); ++column) {
	             list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,data[row][column]));
	         }
	         rows.add(list);
	     }
	     grid.setRows(rows);
	     table.setGrid(grid);
	}

	@FXML
	public void chooseFilePressed(){

		// first run the file chooser
		FileChooserFX fc = new FileChooserFX() ;
		fc.setExtension("txt");
		fc.openFile();
		String fullPath = fc.getFilePath() ;

		// now read the rows of data (word by word)
		createTable(fullPath);
	}

	private void createTable(String filePath){
		FileInput fin = new FileInput(filePath) ;
		int numRows = fin.numberOfLines() ;
		ArrayList<String> rows = new ArrayList<>() ;
		for(int i=0; i<numRows; i++){
			String line = fin.readLine() ;
			if(line.isEmpty()){
				continue ;
			}
			else{
				rows.add(line) ;
			}
		}
		fin.close();
		System.gc();
		// now find words in each row and populate the table
		numRows = rows.size() ;
		String str = rows.get(0).replaceAll("[!?,;]", "");
		String[] words = str.split("\\s+");
		int numColumns = words.length ;
		if(isNumeric(words[0]) || isNumeric(words[0]+"")){
			numRows++ ;
			data = new String[numRows][numColumns] ;
			for(int j=0; j<numColumns; j++){
				data[0][j] = "VarName" + (j+1) ;
			}
			for(int i=0; i<rows.size(); i++){
				str = rows.get(i).replaceAll("[!?,;]", "");
				words = str.split("\\s+");
				for(int j=0; j<numColumns; j++){
					data[i+1][j] = words[j] ;
				}
			}
			createGrid(numRows, numColumns, data);
		}
		else{
			data = new String[numRows][numColumns] ;
			for(int j=0; j<numColumns; j++){
				data[0][j] = words[j] ;
			}
			for(int i=1; i<rows.size(); i++){
				str = rows.get(i).replaceAll("[!?,;]", "");
				words = str.split("\\s+");
				for(int j=0; j<numColumns; j++){
					data[i][j] = words[j] ;
				}
			}
			createGrid(numRows, numColumns, data);
		}
	}

	private boolean isNumeric(String s) {
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}

	@FXML
	public void deletePressed(){
		table.deleteSelectedCells();
	}

	@FXML
	public void addColumnsPressed(){

	}

	@FXML
	public void addRowsPressed(){

	}

	@FXML
	public void saveToDatabasePressed(){
		// first read the grid and make sure data matrix is updated with the changes from user
		if (data!=null){
			int numRows = table.getGrid().getRowCount() ;
			int numColumns = table.getGrid().getColumnCount() ;
		     for (int i=0; i<numRows; ++i) {
		         for (int j = 0; j < numColumns; ++j) {
		             data[i][j] = table.getGrid().getRows().get(i).get(j).getText() ;
		         }
		     }
			// then create simulation variables for each column
			for(int j=0; j<numColumns; j++){
				double[] values = getColumnData(j) ;
				SimulationVariable simVar = new SimulationVariable(data[0][j], values) ;
				simDataBase.addNewVariable(simVar);
			}
		}
		else{
			int tempRows = table.getGrid().getRowCount() ;
			int numRows = 0, numColumns = 1 ;
		    for (int i=0; i<tempRows; ++i) {
		    	 String entry = table.getGrid().getRows().get(i).get(0).getText() ;
		    	 if(!entry.isEmpty()){numRows++;}
		     }
		     data = new String[numRows][numColumns] ;
		     for (int i=0; i<numRows; ++i) {
		    	 String entry = table.getGrid().getRows().get(i).get(0).getText() ;
		    	 if(!entry.isEmpty()){data[i][0] = table.getGrid().getRows().get(i).get(0).getText() ;}
		     }

			// then create simulation variables for each column
			double[] values = getColumnData(0) ;
			SimulationVariable simVar = new SimulationVariable(data[0][0], values) ;
			simDataBase.addNewVariable(simVar);
			data = null ;
		}
	}

	private double[] getColumnData(int ColumnNumber){
		int numRows = data.length ;
		double[] values = {} ;
		for(int i=1; i<numRows; i++){
			String st = data[i][ColumnNumber] ;
			if(!st.isEmpty()){
//				values = MoreMath.Arrays.append(values, Double.parseDouble(data[i][ColumnNumber])) ;
				values = MathUtils.Arrays.append(values, MathUtils.evaluate(data[i][ColumnNumber])) ;
			}
		}
		return values ;
	}

	@FXML
	public void databasePressed(){
		DatabaseModule db = new DatabaseModule(simDataBase) ;
		db.refreshTable(simDataBase);
	}

	@FXML
	public void plotterPressed(){
		new PlotterModule(simDataBase) ;
	}

	@FXML
	public void closePressed(){
		table.getScene().getWindow().hide();
	}

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
            String filePath = null;
            for (File file:db.getFiles()) {
                filePath = file.getAbsolutePath();
                createTable(filePath);
            }
        }
        event.setDropCompleted(success);
        event.consume();
	}

	// for test
//	public static void main(String[] args){
//		String s = "-2_3.1" ;
//		ImportDataController im = new ImportDataController() ;
//		System.out.println(im.isNumeric(s));
//	}

}
