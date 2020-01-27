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

	@Override
	protected void codeGenOp(DecacCompiler compiler, GPRegister register, GPRegister result) {
		compiler.addInstruction(new ADD(register, result));
	}
}
