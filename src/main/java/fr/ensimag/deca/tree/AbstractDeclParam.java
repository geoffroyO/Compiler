package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

public abstract class AbstractDeclParam extends Tree {
	/**
	 * Pass 2 of [SyntaxeContextuelle]. Verify that the field declaration is OK and
	 * Initialize it.
	 */
	protected abstract Type verifyDeclParam(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError;

	/**
	 * Generate assembly code for the instruction.
	 *
	 * @param compiler
	 */
	protected abstract void codeGenDeclParam(DecacCompiler compiler, int indexLb);

}
