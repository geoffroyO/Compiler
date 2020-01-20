package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclMethod  extends Tree {

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the method declaration is OK and Initialize it.
     */
    protected abstract void verifyDeclMethod(DecacCompiler compiler, ClassDefinition current, ClassDefinition superClass)
            throws ContextualError, EnvironmentExp.DoubleDefException;

    protected abstract void verifyMethodBody(DecacCompiler compiler, ClassDefinition current) throws ContextualError;

    /**
     * Generate assembly code for the instruction.
     *
     * @param compiler
     */
    protected abstract void codeGenFpDeclMethod(DecacCompiler compiler);

    protected abstract void codeGenDeclMethod(DecacCompiler compiler);

}
