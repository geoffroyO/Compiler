package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;

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
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// - declare 2 types
		Type leftOpType;
		Type rightOpType;

		// - verify and get both operands types
		leftOpType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
		rightOpType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

		// - verify if types are not integer or float
		if ((!leftOpType.isInt() && !leftOpType.isFloat()) || (!rightOpType.isFloat() && !rightOpType.isInt())) {
			throw new ContextualError("Contextual error : Operands for an Arithmetic operation should be int or float", getLocation());
		} else {

			// - if left operand is float and right operand is integer
			if (leftOpType.isFloat() && rightOpType.isInt()) {

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
		// - the result of the expression is in R1
		codeGenExpr(compiler, Register.R1);
		super.codeGenPrint(compiler, printHex);
	}

	protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
		super.codeGenExpr(compiler, register);

		// - error of stack overflow catched
		if (!compiler.getCompilerOptions().isNoCheck()) {
			compiler.addInstruction(new BOV(new Label("Float_overflow")));
		}
	}
}
