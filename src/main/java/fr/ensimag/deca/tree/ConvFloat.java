package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl13
 * @date 01/01/2020
 */
public class ConvFloat extends AbstractUnaryExpr {
	public ConvFloat(AbstractExpr operand) {
		super(operand);
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) {
		this.setType(compiler.getEnvTypes().get(compiler.getSymbols().create("float")).getType());
		return getType();
	}

	@Override
	protected String getOperatorName() {
		return "/* conv float */";
	}


	/**
	 * Generates assembly code to evaluate the expression and cast the result to a float.
	 *
	 * @param compiler
	 *
	 * @param register
	 *  The result is stored in register
	 */
	protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
		this.getOperand().codeGenExpr(compiler, register);
		compiler.addInstruction(new LOAD(register, Register.R0));
		compiler.addInstruction(new FLOAT(Register.R0, register));
	}

}
