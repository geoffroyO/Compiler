package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {

    final private AbstractIdentifier type;
    final private AbstractIdentifier name;
    final private ListDeclParam listDeclParam;
    final private AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam listDeclParam, AbstractMethodBody body){
        this.type = type;
        this.name = name;
        this.listDeclParam = listDeclParam;
        this.body = body;

    }

    @Override
    protected void verifyMethod(DecacCompiler compiler, ClassDefinition memberOf) throws ContextualError {
        // TODO
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO
    }

    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler) {
        // TODO
    }

}
