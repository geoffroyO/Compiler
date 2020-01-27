package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;

/**
 * @author gl13
 * @date 01/01/2020
 */
public class Plus extends AbstractOpArith {
	public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
		super(leftOperand, rightOperand);
	}

	@Override
	protected String getOperatorName() {
		return "+";
	}

	/**
	 * Generate assembly code for a binary expression, store the result in the register passed
	 * in argument.
	 *
	 * @param compiler
	 * @param register
	 * 		Temporary register to store a value.
	 * @param result
	 * 		The result is stored in regResult.
	 */
	@Override
	protected void codeGenOp(DecacCompiler compiler, GPRegister register, GPRegister result) {
		compiler.addInstruction(new ADD(register, result));
	}
}
