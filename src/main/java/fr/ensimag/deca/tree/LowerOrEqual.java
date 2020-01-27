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

	@Override
	protected void codeGenOp(DecacCompiler compiler, GPRegister reg, GPRegister regResult) {
		compiler.addInstruction(new CMP(reg, regResult));
		compiler.addInstruction(new SLE(regResult));
	}
}
