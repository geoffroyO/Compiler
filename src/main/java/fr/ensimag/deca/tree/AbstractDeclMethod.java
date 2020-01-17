package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractDeclMethod  extends Tree {

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the method declaration is OK and Initialize it.
     */
    protected abstract void verifyDeclMethod(DecacCompiler compiler, ClassDefinition memberOf)
            throws ContextualError;

    /**
     * Generate assembly code for the instruction.
     *
     * @param compiler
     */
    protected abstract void codeGenDeclMethod(DecacCompiler compiler);}
