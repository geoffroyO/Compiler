package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Absence of initialization (e.g. "int x;" as opposed to "int x = 42;").
 *
 * @author gl13
 * @date 01/01/2020
 */
public class NoInitialization extends AbstractInitialization {

	@Override
	protected void verifyInitialization(DecacCompiler compiler, Type t, EnvironmentExp localEnv,
			ClassDefinition currentClass) throws ContextualError {
		// - Nothing to implement here!
		// - just removed the exception!
	}

	/**
	 * Node contains no real information, nothing to check.
	 */
	@Override
	protected void checkLocation() {
		// nothing
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// nothing
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
	 * Generate assembly code for the code generation of the initialization
	 *
	 * @param compiler
	 * @param register
	 * 		register to store the value
	 * @param type
	 * 		Type of the argument
	 */
	@Override
	protected void codeGenInit(DecacCompiler compiler, GPRegister register, AbstractIdentifier type) {
		// - cf documentation for the no initialization case
		if (type.getType().isInt()) {
			compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));
		} else if (type.getType().isFloat()) {
			compiler.addInstruction(new LOAD(new ImmediateFloat(0), register));
		} else {
			compiler.addInstruction(new LOAD(new NullOperand(), register));
		}
	}
}
