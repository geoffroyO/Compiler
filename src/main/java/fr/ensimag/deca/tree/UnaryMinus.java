package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl13
 * @date 01/01/2020
 */
public class UnaryMinus extends AbstractUnaryExpr {

	public UnaryMinus(AbstractExpr operand) {
		super(operand);
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// - verify operand is float or int
		Type opType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
		// - verify operand type is float or int
		if (!opType.isInt() && !opType.isFloat()) {
			throw new ContextualError("Unary Minus Operand should be boolean", getLocation());
		}
		this.setType(opType);
		return opType;
	}

	/**
	 * Generates assembly code to evaluate and print the expression.
	 *
	 * @param compiler
	 * @param printHex
	 * 		Boolean that considers if the user wants a printx or a simple print(ln)
	 */
	@Override
	protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
		// - the result of the expression is in R1
		codeGenExpr(compiler, Register.R1);
		super.codeGenPrint(compiler, printHex);
	}

	@Override
	protected String getOperatorName() {
		return "-";
	}


	/**
	 * Generates assembly code to evaluate the expression.
	 *
	 * @param compiler
	 * @param register
	 * 		Register to store the result of the operation
	 */
	protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
		this.getOperand().codeGenExpr(compiler, register);

		if (this.getType().isInt()) {
			compiler.addInstruction(new MUL(new ImmediateInteger(-1), register));
		} else {
			compiler.addInstruction(new MUL(new ImmediateFloat(-1), register));

		}
	}
}
