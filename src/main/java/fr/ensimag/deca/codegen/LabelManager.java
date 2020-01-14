package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {
	
	private int whileLabelIndex;
	private int ifLabelIndex;
	private int methodLabelIndex;
	
	static private String whileLabel = "while";
	static private String endWhileLabel= "end_while";
	static private String ifLabel = "if";
	static private String endIfLabel = "end_if";;
	static private String methodLabel = "method";
	
    public LabelManager() {
    	whileLabelIndex = 0;
    	ifLabelIndex = 0;
    	methodLabelIndex = 0;
    }
    
    public Label genWhileLabel() {
    	String name = whileLabel + "_" + whileLabelIndex;
    	return new Label(name); 
    }
    
    public Label genIfLabel() {
    	String name = ifLabel + "_" + ifLabelIndex;
    	return new Label(name); 
    }
    
    public Label genEndWhileLabel() {
    	String name = endWhileLabel + "_" + whileLabelIndex;
    	whileLabelIndex++;
    	return(new Label(name));
    }
   
    public Label genEndIfLabel() {
    	String name = endIfLabel + "_" + ifLabelIndex;
    	ifLabelIndex++;
    	return(new Label(name));    	
    }
}
