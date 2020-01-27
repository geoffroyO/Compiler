package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SGE;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Or extends AbstractOpBool {

	public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
		super(leftOperand, rightOperand);
	}

	@Override
	protected String getOperatorName() {
		return "||";
	}

	@Override
	protected void codeGenOp(DecacCompiler compiler, GPRegister reg, GPRegister regResult) {
		compiler.addInstruction(new ADD(reg, regResult));
		compiler.addInstruction(new CMP(new ImmediateInteger(1), regResult));
		compiler.addInstruction(new SGE(regResult));
	}

	@Override
	protected void codeGenBoolLazy(DecacCompiler compiler, Label label, GPRegister result) {
		compiler.addInstruction(new CMP(new ImmediateInteger(1), result));
		compiler.addInstruction(new BEQ(label));
	}

}
