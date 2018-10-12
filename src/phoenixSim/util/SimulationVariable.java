package phoenixSim.util;

import java.util.ArrayList;

import flanagan.io.FileOutput;
import mathLib.util.MathUtils;

/**
 * Created by meisam on 6/25/17.
 *
 * This class contains the name and values of each parameter involved in a simulation
 *
 */

public class SimulationVariable {

	public static final String DOUBLE = "Double" ;
	public static final String STRING = "String" ;

    String name ;
    String alias ;
    String type  ;
    String size ;
    double[] variable ;

    // it's a good idea to also create an array list of the data
    ArrayList<Double> variableList ;


    public SimulationVariable(
            String name,
            double[] variable
    ){
        this.name = name ;
        this.alias = name ;
        this.variable = variable ;
        this.variableList = new ArrayList<Double>() ;
        updateVariableList() ;
        this.type = "DOUBLE" ;
        this.size = "1x"+variable.length ;
    }

    public SimulationVariable(
            String name,
            ArrayList<Double> variableList
    ){
        this.name = name ;
        this.alias = name ;
        this.variableList = variableList ;
        updateVariable() ;
        this.type = "DOUBLE" ;
        this.size = "1x" + variableList.size() ;
    }

    // also passing in the alias name

    public SimulationVariable(
            String name,
            String alias,
            double[] variable
    ){
        this.name = name ;
        this.alias = alias ;
        this.variable = variable ;
        this.variableList = new ArrayList<Double>() ;
        updateVariableList() ;
        this.type = "DOUBLE" ;
        this.size = "1x"+variable.length ;
    }

    public SimulationVariable(
            String name,
            String alias,
            ArrayList<Double> variableList
    ){
        this.name = name ;
        this.alias = alias ;
        this.variableList = variableList ;
        updateVariable() ;
        this.type = "DOUBLE" ;
        this.size = "1x" + variableList.size() ;
    }

    // creating variable object with empty data

    public SimulationVariable(
            String name
    ){
        this.name = name ;
        this.alias = name ;
        this.variable = new double[0] ;
        this.variableList = new ArrayList<>() ;
        this.type = "DOUBLE" ;
        this.size = "1x" + variableList.size() ;
    }

    private void updateVariableList(){
    	variableList = new ArrayList<Double>() ;
    	variableList.clear();
        for(double x : variable){
            variableList.add(x) ;
        }
    }

	private void updateVariable(){
        int M = variableList.size() ;
        variable = new double[M] ;
        for(int i=0; i<M; i++){
            variable[i] = variableList.get(i) ;
        }
    }

    public void updateSize(){
	    size = "1x" + variableList.size() ;
    }

    public String getName(){
        return name ;
    }

    public String getAlias(){
    	return alias ;
    }

    public double getValue(int index){
        return variable[index] ;
    }

    public int getLength(){
        return variable.length ;
    }

    public int getSize(){
        return variable.length ;
    }

    public String getArraySize(){
        return size ;
    }

    public double[] getAllValues(){
        return variable ;
    }

    public void setName(String name){
    	this.name = name ;
    }

    public void setAlias(String alias){
    	this.alias = alias ;
    }

    public void setType(String type){
    	this.type = type ;
    }

    public String getType(){
        return type ;
    }

    public ArrayList<Double> getVariableList(){
        return variableList ;
    }

    public void appendValue(double value){
        variable = MathUtils.Arrays.append(variable, value) ;
        updateVariableList() ;
        updateSize();
    }

    public void appendValues(double[] values){
        variable = MathUtils.Arrays.concat(variable, values) ;
        updateVariableList();
        updateSize();
    }

    public void appendInputData(InputData inputData){
        variable = MathUtils.Arrays.append(variable, inputData.getValue()) ;
        updateVariableList() ;
        updateSize();
    }

    public void addValue(double value){
        variable = MathUtils.Arrays.append(variable, value) ;
        updateVariableList() ;
        updateSize();
    }

    public void addValues(double[] values){
        variable = MathUtils.Arrays.concat(variable, values) ;
        updateVariableList();
        updateSize();
    }

    public void addInputData(InputData inputData){
        variable = MathUtils.Arrays.append(variable, inputData.getValue()) ;
        updateVariableList() ;
        updateSize();
    }

    public void clear(){
    	variableList.clear();
    	variable = new double[0] ;
    	updateSize();
    }

    public void removeNaNValues(){
    	ArrayList<Double> vals = new ArrayList<Double>() ;
    	for(double x : variableList){
    		if(Double.isNaN(x)){
    			vals.add(x) ;
    		}
    	}
    	variableList.removeAll(vals) ;
    	updateVariable();
    	updateSize();
    }

    public ArrayList<InputData> getInputDataList(){
    	ArrayList<InputData> list = new ArrayList<>() ;
    	list.clear();
    	for(double x : variable){
    		list.add(new InputData(name, x)) ;
    	}
    	return list ;
    }

    public double[] getAllValuesRemoveNaNs(){
    	ArrayList<Double> NaNVals = new ArrayList<Double>() ;
    	ArrayList<Double> allVals = variableList ;
    	for(double x : variableList){
    		if(Double.isNaN(x)){
    			NaNVals.add(x) ;
    		}
    	}
    	allVals.removeAll(NaNVals) ;
    	double[] vals = new double[allVals.size()] ;
    	for(int i=0; i<allVals.size(); i++){
    		vals[i] = allVals.get(i) ;
    	}
    	return vals ;
    }

    public String toString(){
        int length = getLength() ;
        if(length == 0 ){
            return "[ ]" ;
        }
        else{
            return MathUtils.Arrays.toString(variable) ;
        }

    }

    public String toFullString(){
        int length = getLength() ;
        if(length == 0 ){
            return (name + " = [ ]") ;
        }
        else{
            return (name + " = " + toString())  ;
        }

    }

    public String toFullStringAlias(){
        int length = getLength() ;
        if(length == 0 ){
            return (name + " = [ ]") ;
        }
        else{
            return (alias + " = " + toString())  ;
        }

    }

    public void show(){
    	MathUtils.Arrays.show(variable);
    }

    public void showFull(){
        System.out.println(toFullString());
    }

    public void showFullAlias(){
        System.out.println(toFullStringAlias());
    }

    public String[] toStringArray(){
        int length = getLength() ;
        String[] array = new String[length] ;
        for(int i=0; i<length; i++){
        	array[i] = variable[i] + "" ;
        }
        return array ;
    }


    public String getSaveToFileString(){
    	String st = "" ;
    	st = name + "\n" + alias + "\n" + size + "\n" + type + "\n" ;
    	for(int i=0; i<variable.length-1; i++){
    		st += variable[i] + "\t" ;
    	}
    	st += variable[variable.length-1] ;
    	st += "\n" + "----" ;
    	return st ;
    }

    public void printToFile(FileOutput fout){
    	String st = "" ;
    	st = name + "\n" + alias + "\n" + size + "\n" + type + "\n" ;
    	fout.print(st);
    	for(int i=0; i<variable.length-1; i++){
    		fout.print(variable[i] + "\t");
    	}
    	fout.print(variable[variable.length-1] + "\n");
    	fout.print("----" + "\n");
    }


    // ****************** For diagnostic purposes **************
//    public static void main(String[] args){
//        double[] lambda = MoreMath.linspace(1500, 1600, 10)  ;
//        SimulationVariable var = new SimulationVariable("lambda_(nm)", "wavelength (nm)", lambda) ;
////        var.show() ;
////        var.showFullAlias();
//        var.appendValue(Double.NaN);
////        var.show();
////        System.out.println(var.size);
////
////        var.appendValue(Double.NaN);
////        var.show();
////        System.out.println(var.size);
////        MoreMath.Arrays.show(var.getAllValuesRemoveNaNs());
//
//        System.out.println(var.getSaveToFileString());
//        var.appendValue(Double.NaN);
//        System.out.println(var.getSaveToFileString());
//
//    }

}
