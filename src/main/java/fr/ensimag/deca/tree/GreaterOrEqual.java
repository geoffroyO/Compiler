package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SGE;

/**
 * Operator "x >= y"
 * 
 * @author gl13
 * @date 01/01/2020
 */
public class GreaterOrEqual extends AbstractOpIneq {

	public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
		super(leftOperand, rightOperand);
	}

	@Override
	protected String getOperatorName() {
		return ">=";
	}


	/**
	 * Generates assembly code to evaluate the operation.
	 *
	 * @param compiler
	 * @param reg
	 * 	 	This label is used to do the lazy evaluation of the boolean expression
	 * @param regResult
	 * 		The method stores the result in the register result in the assembly code.
	 *
	 */
	@Override
	protected void codeGenOp(DecacCompiler compiler, GPRegister reg, GPRegister regResult) {
		compiler.addInstruction(new CMP(reg, regResult));
		compiler.addInstruction(new SGE(regResult));
	}
}
