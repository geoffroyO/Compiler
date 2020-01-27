package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Single precision, floating-point literal
 *
 * @author gl13
 * @date 01/01/2020
 */
public class FloatLiteral extends AbstractExpr {

	public float getValue() {
		return value;
	}

	private float value;

	public FloatLiteral(float value) {
		Validate.isTrue(!Float.isInfinite(value), "literal values cannot be infinite");
		Validate.isTrue(!Float.isNaN(value), "literal values cannot be NaN");
		this.value = value;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		this.setType(compiler.getEnvTypes().get(compiler.getSymbols().create("float")).getType());
		return this.getType();
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print(java.lang.Float.toHexString(value));
	}

	@Override
	String prettyPrintNode() {
		return "Float (" + getValue() + ")";
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// leaf node => nothing to do
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// leaf node => nothing to do
	}

	/**
	 * Generates assembly code to evaluate the operation.
	 *
	 * @param compiler
	 * @param register
	 * 		We store the value of the float in this register
	 */
	protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
		compiler.addInstruction(new LOAD(new ImmediateFloat(getValue()), register));
	}

	@Override
	protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
		compiler.addInstruction(new LOAD(new ImmediateFloat(this.getValue()), Register.R1));
		super.codeGenPrint(compiler, printHex);
	}
}
