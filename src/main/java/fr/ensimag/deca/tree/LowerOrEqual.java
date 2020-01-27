package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SLE;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class LowerOrEqual extends AbstractOpIneq {
	public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
		super(leftOperand, rightOperand);
	}

	@Override
	protected String getOperatorName() {
		return "<=";
	}

	/**
	 * Generate assembly code for a binary expression, store the result in the register passed
	 * in argument.
	 *
	 * @param compiler
	 * @param reg
	 * 		Temporary register to store a value.
	 * @param regResult
	 * 		The result is stored in regResult.
	 */
	@Override
	protected void codeGenOp(DecacCompiler compiler, GPRegister reg, GPRegister regResult) {
		compiler.addInstruction(new CMP(reg, regResult));
		compiler.addInstruction(new SLE(regResult));
	}
}
