package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractDeclParam extends Tree {
    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the field declaration is OK and Initialize it.
     */
    protected abstract void verifyParam(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Generate assembly code for the instruction.
     *
     * @param compiler
     */
    protected abstract void codeGenDeclParam(DecacCompiler compiler);

}
