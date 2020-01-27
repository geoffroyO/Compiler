package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Modulo extends AbstractOpArith {

	public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
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

		// - Operands must be numeric numbers
		if ((leftOpType.isInt()) && (rightOpType.isInt())) {
			this.setType(rightOpType);
			return rightOpType;
		} else {
			throw new ContextualError("Operands must be int [Modulo] int", getLocation());
		}
	}

	@Override
	protected String getOperatorName() {
		return "%";
	}

	@Override
	protected void codeGenOp(DecacCompiler compiler, GPRegister register, GPRegister result) {
		compiler.addInstruction(new REM(register, result));
	}
}
