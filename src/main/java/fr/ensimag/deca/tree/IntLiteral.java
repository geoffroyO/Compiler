package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Integer literal
 *
 * @author gl13
 * @date 01/01/2020
 */
public class IntLiteral extends AbstractExpr {
	public int getValue() {
		return value;
	}

	private int value;

	public IntLiteral(int value) {
		this.value = value;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// - create or get the int type
		setType(compiler.getEnvTypes().get(compiler.getSymbols().create("int")).getType());
		return getType();
	}

	@Override
	String prettyPrintNode() {
		return "Int (" + getValue() + ")";
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print(Integer.toString(value));
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
	 * Generates assembly code to evaluate the expression.
	 *
	 * @param compiler
	 *
	 * @param register
	 * 		The result is stored in the register.
	 */
	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
		compiler.addInstruction(new LOAD(new ImmediateInteger(getValue()), register));
	}


	/**
	 * Generates assembly code to evaluate and print the expression.
	 *
	 * @param compiler
	 * @param printHex
	 * 		Boolean that considers if the user wants a printx or a simple print(ln)
	 */
	@Override
	protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
		compiler.addInstruction(new LOAD(new ImmediateInteger(this.getValue()), Register.R1));
		super.codeGenPrint(compiler, printHex);
	}
}
