package phoenixSim.util;

import People.Meisam.GUI.PhoenixSim.ModulesLibrary.ExportCompleted.ExportCompleted;
import People.Meisam.GUI.Utilities.ExportPlot.JavaFXFileChooser.FileChooserFX;
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import ch.epfl.general_libraries.utils.SimpleMap;
import flanagan.io.FileInput;
import flanagan.io.FileOutput;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by meisam on 6/25/17.
 *
 * This class is a map of all the simulation variables that have been created during a specific simulation.
 *
 */
public class SimulationDataBase {

	String dataBaseName = "" ;
    private Map<String, SimulationVariable> dataBase  ;
    boolean databaseChanged = false ;

    public SimulationDataBase(){
        // initialize data-base map
        this.dataBase = new SimpleMap<String, SimulationVariable>() ;
    }

    public SimulationDataBase(SimulationVariable... var){
        // initialize data-base map
        this.dataBase = new SimpleMap<String, SimulationVariable>() ;
        // add the arguments to the data base
        for (SimulationVariable x : var){
            dataBase.put(x.getName(), x) ;
        }
    }

    public SimulationDataBase(ArrayList<SimulationVariable> var){
        // initialize data-base map
        this.dataBase = new SimpleMap<String, SimulationVariable>() ;
        // add the arguments to the data base
        for (SimulationVariable x : var){
            dataBase.put(x.getName(), x) ;
        }
    }

    public void clear(){
        dataBase.clear() ;
    }

    public Map<String, SimulationVariable> getDataBase(){
    	return dataBase ;
    }

    public String getDataBaseName(){
    	return dataBaseName ;
    }

    public void setDataBaseName(String name){
    	this.dataBaseName = name ;
    }

    public void addNewVariable(SimulationVariable var){
//    	removeVariable(var);
        dataBase.put(var.getName(), var) ;
    }

    public void addVariableMap(Map<String, String> allParameters){
        for(String x : allParameters.keySet()){
            double value = Double.parseDouble(allParameters.get(x)) ;
            if(dataBase.keySet().contains(x)){
                dataBase.get(x).appendValue(value);
            }
            else{
                dataBase.put(x, new SimulationVariable(x, new double[]{value})) ;
            }
        }
    }

    public ArrayList<SimulationVariable> getVariableList(){
    	ArrayList<SimulationVariable> list = new ArrayList<>() ;
    	list.clear();
    	for(String x : dataBase.keySet()){
    		list.add(dataBase.get(x)) ;
    	}
    	return list ;
    }

    public void removeVariable(SimulationVariable var){
        if(!dataBase.isEmpty() && dataBase.containsKey(var.getName())){
            dataBase.remove(var.getName()) ;
        }
    }

    public void removeVariables(SimulationVariable... var){
    	for(SimulationVariable x : var){
            if(!dataBase.isEmpty() && dataBase.containsKey(x.getName())){
                dataBase.remove(x.getName()) ;
            }
    	}
    }

    public boolean isEmpty(){
    	return dataBase.isEmpty() ;
    }

    public boolean variableExists(String varName){
        return dataBase.containsKey(varName) ;
    }

    public boolean variableExistsFromAlias(String alias){
        ArrayList<SimulationVariable> list = getVariableList() ;
        boolean aliasExists = false ;
        for(SimulationVariable x : list){
            if(x.getAlias() == alias){
                aliasExists = true ;
            }
            else{
                aliasExists = false ;
            }
        }
        return aliasExists ;
    }

    public SimulationVariable getVariable(String varName){
        return dataBase.get(varName) ;
    }

    public SimulationVariable getVariableFromAlias(String alias){
    	SimulationVariable var = null ;
    	ArrayList<SimulationVariable> list = getVariableList() ;
        for(SimulationVariable x : list){
            if(x.getAlias() == alias){
                var = x ;
            }
        }
        return var ;
    }

    public double[] getVariableValues(String varName){
        return getVariable(varName).getAllValues() ;
    }

    public Set<String> getAllNames(){
    	return dataBase.keySet() ;
    }

    public int getDataBaseSize(){
    	return dataBase.size() ;
    }

    public void show(){
        for(String x : dataBase.keySet()){
            dataBase.get(x).showFull();
        }
    }

    public void saveToFile() throws IOException {
		FileChooserFX fc = new FileChooserFX() ;
		fc.setExtension("dbsim");
		fc.saveFile();
		if(fc.getSelectedFile() != null){
			String fullPath = fc.getFilePath() ;
			FileOutput fout = new FileOutput(fullPath, "w") ;
			for(SimulationVariable x :  getVariableList()){
//				fout.println(x.getSaveToFileString());
				x.printToFile(fout);
			}
			fout.close();
			System.gc();
	        new ExportCompleted() ;
		}

    }

    @SuppressWarnings("unused")
	public void loadFromFile(){
    	// next making file chooser
		FileChooserFX fc = new FileChooserFX() ;
		fc.setExtension("dbsim");
		fc.openFile();
		if(fc.getSelectedFile() != null){
	    	// first clearing database
	    	dataBase.clear();
			String fullPath = fc.getFilePath() ;
			String name, alias, size, type ;
			double[] values = {} ;
			// next reading the file
			FileInput fin = new FileInput(fullPath) ;
			int M = fin.numberOfLines() ;
			// 6 is the number of lines for each simulation variable
			int k=0 ;
			while(k < M/6){
				name = fin.readLine() ;
				alias = fin.readLine() ;
				size = fin.readLine() ;
				type = fin.readLine() ;
				values = getValuesFromLine(fin.readLine()) ;
				fin.readLine() ;
//				simDataBase.addNewVariable(new SimulationVariable(name, alias, values));
				dataBase.put(name, new SimulationVariable(name, alias, values)) ;
				k++ ;
			}
			fin.close();
			System.gc();
			databaseChanged = true ;
		}
		else{
			databaseChanged = false ;
		}
    }
    
    @SuppressWarnings("unused")
	public void loadFromFile(File file){
		if(file != null){
	    	// first clearing database
	    	dataBase.clear();
			String fullPath = file.getAbsolutePath() ;
			String name, alias, size, type ;
			double[] values = {} ;
			// next reading the file
			FileInput fin = new FileInput(fullPath) ;
			int M = fin.numberOfLines() ;
			// 6 is the number of lines for each simulation variable
			int k=0 ;
			while(k < M/6){
				name = fin.readLine() ;
				alias = fin.readLine() ;
				size = fin.readLine() ;
				type = fin.readLine() ;
				values = getValuesFromLine(fin.readLine()) ;
				fin.readLine() ;
//				simDataBase.addNewVariable(new SimulationVariable(name, alias, values));
				dataBase.put(name, new SimulationVariable(name, alias, values)) ;
				k++ ;
			}
			fin.close();
			System.gc();
			databaseChanged = true ;
		}
		else{
			databaseChanged = false ;
		}
    }

    public boolean dataBaseChanged(){
    	return databaseChanged ;
    }

    private double[] getValuesFromLine(String valueLine){
    	double[] values = {} ;
    	List<String> allValues = Arrays.asList(valueLine.trim().split("\t")) ;
    	int M = allValues.size() ;
    	for(int i=0; i<M; i++){
    		values = MoreMath.Arrays.append(values, Double.parseDouble((String) allValues.get(i))) ;
    	}
    	return values ;
    }

//    @SuppressWarnings("unused")
//	private String printNames(){
//        String st0 = "" ;
//        for(String x : getAllNames()){
//            st0 = x + "         " ;
//        }
//        return st0 ;
//    }

    public SimulationVariable[] getAllVariables(){
    	int M = getDataBaseSize() ;
    	SimulationVariable[] allVars = new SimulationVariable[M] ;
    	for(int i=0; i<M; i++){
    		allVars[i] = getVariableList().get(i) ;
    	}
    	return allVars ;

    }

    // ************************ for diagnostic purposes *****************
//    public static void main(String[] args){
//        // step 1: create simulation variables
//        double[] x = MoreMath.linspace(-10, 10, 3) ;
//        SimulationVariable varX = new SimulationVariable("X var", x) ;
//
//        double[] y = MoreMath.Arrays.Functions.abs(x) ;
//        SimulationVariable varY = new SimulationVariable("Y var", y) ;
//
//        double[] z = {0 , 1} ;
//        SimulationVariable varZ = new SimulationVariable("Z var", z) ;
//
//        // step 2: create simulation data base
//        SimulationDataBase simData = new SimulationDataBase(varX, varY) ;
//        simData.show() ;
//
////        String st =  "1.2" + "\t" + "-2.3" ;
////        double[] v = simData.getValuesFromLine(st) ;
////        System.out.println(st);
//
////        double[] v = {0 , 2 , 5, 10, 100} ;
////        SimulationVariable varV = new SimulationVariable("V var", v) ;
////        simData.addNewVariable(varV);
////        simData.show();
////
////        simData.clear();
////        simData.show();
////
////        Map<String, String> vars = new HashMap<>() ;
////        vars.put("Z var", 3 +"" ) ;
////        simData.addVariableMap(vars);
////        simData.show();
////        vars.put("Z var", 5 + "") ;
////        simData.addVariableMap(vars);
////        simData.show();
//    }



}
