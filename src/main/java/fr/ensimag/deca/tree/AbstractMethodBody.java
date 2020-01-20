package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;

public abstract class AbstractMethodBody extends Tree {

    protected abstract void codeGenMethodBody(DecacCompiler compiler);

}
