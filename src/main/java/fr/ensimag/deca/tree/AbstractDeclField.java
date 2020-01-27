package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractDeclField extends Tree {
	/**
	 * Pass 2 of [SyntaxeContextuelle]. Verify that the field declaration is OK and
	 * Initialize it.
	 */
	protected abstract void verifyDeclField(DecacCompiler compiler, ClassDefinition memberOf) throws ContextualError;

	/**
	 * Generate assembly code for the declaration of fields.
	 *
	 * @param compiler
	 */
	protected abstract void codeGenDeclField(DecacCompiler compiler);
}
