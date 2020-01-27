package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {
	
	private int whileLabelIndex;
	private int ifLabelIndex;
	private int elseLabelIndex;
	private int methodLabelIndex;
	private int opBoolIndex;
	private Label endCurrentLabel;
	
	static private String whileLabel = "while";
	static private String endWhileLabel= "end_while";
	static private String ifLabel = "if";
	static private String endIfLabel = "end_if";
	static private String elseLabel = "else";
	static private String endElseLabel = "end_else";
	static private String methodLabel = "method";
	static private String opBoolLabel = "end_cond";

	
    public LabelManager() {
    	whileLabelIndex = 0;
    	ifLabelIndex = 0;
    	methodLabelIndex = 0;
    	opBoolIndex = 0;
    }
    
    public Label genWhileLabel() {
    	String name = whileLabel + "_" + whileLabelIndex;
    	return new Label(name); 
    }
    
    public Label genIfLabel() {
    	String name = ifLabel + "_" + ifLabelIndex;
    	return new Label(name); 
    }

    public Label genElseLabel() {
    	String name = elseLabel + "_" + elseLabelIndex;
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
    public Label genEndElseLabel() {
    	String name = endElseLabel + "_" + elseLabelIndex;
    	elseLabelIndex++;
    	return(new Label(name));
    }
    public Label genEndOpBoolLabel() {
    	String name =  opBoolLabel + "_" + opBoolIndex;
    	opBoolIndex++;
    	return(new Label(name));
	}

	public void setEndCurrentLabel(String label) {
    	endCurrentLabel = new Label(label);
	}

	public Label getEndCurrentLabel() {
    	return endCurrentLabel;
	}
}
