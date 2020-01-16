package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl13
 * @date 01/01/2020
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // - declare 2 types
        Type leftOpType;
        Type rightOpType;

        // - verify and get both operands types
        try {
            leftOpType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            rightOpType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e){
            throw e;
        }

        // - verify if types are not int or float
        if ((!leftOpType.isInt() && !leftOpType.isFloat()) || (!rightOpType.isFloat() && !rightOpType.isInt())) {
            throw new ContextualError("Operands should be int or float", getLocation());
        } else {
            // - if left operand is float and right operand is int
            if (leftOpType.isFloat() && rightOpType.isInt()){
                // - convert right operand to float
                this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftOpType));
                // - set current type to leftOpType (= float)
                this.setType(leftOpType);
                return leftOpType;

            // - if left operand is int and right operand is float
            } else if (leftOpType.isInt() && rightOpType.isFloat()) {
                // - convert left operand to float
                this.setLeftOperand(this.getLeftOperand().verifyRValue(compiler, localEnv, currentClass, rightOpType));
                // - set current type to rightOpType (= float)
                this.setType(rightOpType);
                return rightOpType;
            }
        }
        this.setType(leftOpType);
        return leftOpType;
    }  
    
    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
    	GPRegister result = compiler.getRegM().findFreeGPRegister();
    	this.codeGenExpr(compiler, result);
    	
    	 if (this.getType().isInt()){
             compiler.addInstruction(new LOAD(result, Register.R1));
             if (printHex) {
             	compiler.addInstruction(new FLOAT(Register.R1, Register.R1));
                 compiler.addInstruction(new WFLOATX());
             } else {
             	compiler.addInstruction(new WINT());
             }
         }
         if (this.getType().isFloat()){
         	compiler.addInstruction(new LOAD(result, Register.R1));
         	if (printHex) {
         		compiler.addInstruction(new WFLOATX());
         	} else {
         		compiler.addInstruction(new WFLOAT());
         	}
         }   	  	    
    	compiler.getRegM().freeRegister(result);    	
    }
}
