package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SHR;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class And extends AbstractOpBool {

	public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
		super(leftOperand, rightOperand);
	}

	@Override
	protected String getOperatorName() {
		return "&&";
	}

	@Override
	protected void codeGenOp(DecacCompiler compiler, GPRegister reg, GPRegister regResult) {
		compiler.addInstruction(new ADD(reg, regResult));
		compiler.addInstruction((new SHR(regResult)));
	}

	@Override
	protected void codeGenBoolLazy(DecacCompiler compiler, Label label, GPRegister result) {
		compiler.addInstruction(new CMP(new ImmediateInteger(0), result));

		// - we jump to false if the comparison before is true
		compiler.addInstruction(new BEQ(label));
	}
}
