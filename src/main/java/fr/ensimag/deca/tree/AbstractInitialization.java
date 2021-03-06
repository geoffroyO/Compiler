package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl13
 * @date 01/01/2020
 */
public abstract class AbstractInitialization extends Tree {

	/**
	 * Implements non-terminal "initialization" of [SyntaxeContextuelle] in pass 3
	 * 
	 * @param compiler
	 *            contains "env_types" attribute
	 * @param t
	 *            corresponds to the "type" attribute
	 * @param localEnv
	 *            corresponds to the "env_exp" attribute
	 * @param currentClass
	 *            corresponds to the "class" attribute (null in the main bloc).
	 */
	protected abstract void verifyInitialization(DecacCompiler compiler, Type t, EnvironmentExp localEnv,
			ClassDefinition currentClass) throws ContextualError;

	/**
	 * Loads the expression into a free register if Initialization
	 * 
	 * @param compiler
	 * @param register
	 * @param type
	 */
	protected abstract void codeGenInit(DecacCompiler compiler, GPRegister register, AbstractIdentifier type);

	/**
	 * Stores the expression into the stack if Initialization
	 * 
	 * @param compiler
	 * @param register
	 */
	protected void codeGenStInit(DecacCompiler compiler, GPRegister register) {

		// - store the result at the top of the stack
		compiler.addInstruction(
				new STORE(register, new RegisterOffset(compiler.getRegM().getLB(), compiler.getRegM().getBase())));
	}

}
