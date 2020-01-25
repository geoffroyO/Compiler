package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
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
            } else if (leftOpType.isInt() && rightOpType.isInt()) {
                // - convert left operand to float
                Type floatType = new FloatType(compiler.getSymbols().create("float"));
                this.setLeftOperand(this.getLeftOperand().verifyRValue(compiler, localEnv, currentClass, floatType));
                this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, floatType));
                // - set current type to rightOpType (= float)
                this.setType(floatType);
                return floatType;
            }
        }
        this.setType(leftOpType);
        return leftOpType;
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }
    
	@Override
	protected void codeGenOp(DecacCompiler compiler, GPRegister register, GPRegister result) {
		if (!compiler.getCompilerOptions().isNoCheck()) {
	        compiler.addInstruction(new CMP(new ImmediateFloat( (float) 0), register));
	        compiler.addInstruction(new BEQ(new Label("Zero_division")));			
		}
		compiler.addInstruction(new DIV(register, result));
	}
}
