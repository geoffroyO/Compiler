package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class DeclParam extends AbstractDeclParam {

    private AbstractIdentifier type;
    private AbstractIdentifier paramName;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier paramName) {
        this.type = type;
        this.paramName = paramName;
    }

    @Override
    protected void verifyParam(DecacCompiler compiler) throws ContextualError {
        // TODO
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(type.getName().getName() + " " + paramName.getName().getName());
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.type.prettyPrintChildren(s, prefix);
        this.paramName.prettyPrintChildren(s, prefix);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
        this.paramName.iter(f);
    }

    @Override
    protected void codeGenDeclParam(DecacCompiler compiler) {
        // TODO
    }
}
