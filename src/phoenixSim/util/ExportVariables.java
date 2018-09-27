package phoenixSim.util;

import java.util.ArrayList;
import People.Meisam.GUI.PhoenixSim.ModulesLibrary.ExportCompleted.ExportCompleted;
import People.Meisam.GUI.Utilities.SimulationDataBase;
import People.Meisam.GUI.Utilities.SimulationVariable;
import People.Meisam.GUI.Utilities.ExportPlot.JavaFXFileChooser.FileChooserFX;
import flanagan.io.FileOutput;

public class ExportVariables {
	
	ArrayList<SimulationVariable> variables = new ArrayList<>() ;
	int numRows, numColumns ;
	
	public ExportVariables(SimulationVariable... vars){
		for(int i=0; i<vars.length; i++){
			variables.add(vars[i]) ;
		}
	}
	
	public ExportVariables(){
		
	}
	
	public ExportVariables(SimulationDataBase simDataBase){
		variables = simDataBase.getVariableList() ;
	}
	
	public void addVariables(SimulationVariable... vars){
		for(int i=0; i<vars.length; i++){
			variables.add(vars[i]) ;
		}
	}
	
	public void export(){
		FileChooserFX fc = new FileChooserFX() ;
		fc.setExtension("txt");
		fc.saveFile();
		String fullPath = fc.getFilePath() ;
		
		numColumns = variables.size() ;
		numRows = 0 ;
		for(int i=0; i<variables.size(); i++){
			if(numRows < variables.get(i).getLength()){
				numRows = variables.get(i).getLength() ;
			}
		}
		numRows++ ; // firs row is for name and other rows are for values
		
		FileOutput fout = new FileOutput(fullPath, "w") ;
		for(int i=0; i<numRows; i++){
			fout.println(getRowData(i));
		}
		fout.close();
		System.gc();
		new ExportCompleted() ;
	}

	// fix this problem: if the length of variables is not the same, an error occurs...
	private String getRowData(int i){
		String row = "" ;
		if(i==0){
			for(int j=0; j<numColumns; j++){
				row += variables.get(j).getName()+"\t" ;
			}
		}
		else{
//			for(int j=0; j<numColumns; j++){
//				row += variables.get(j).getValue(i-1)+"    " ;
//			}
			for(int j=0; j<numColumns; j++){
				if(i<=variables.get(j).getSize()){
					row += variables.get(j).getValue(i-1)+"\t" ;
				}
				else{
					row += "\t" ;
				}
				
			}
		}

		return row ;
	}

}
