package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Equals extends AbstractOpExactCmp {

	public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
		super(leftOperand, rightOperand);
	}

	@Override
	protected String getOperatorName() {
		return "==";
	}

	/**
	 * Generates assembly code to evaluate the operation
	 *
	 * @param compiler
	 * @param regResult
	 * 		The method stores the result in the register result in the assembly code.
	 * @param reg
	 * 		This register is a temporary register used to stock some result
	 */
	@Override
	protected void codeGenOp(DecacCompiler compiler, GPRegister reg, GPRegister regResult) {
		compiler.addInstruction(new CMP(regResult, reg));
		compiler.addInstruction(new SEQ(regResult));
	}
}
