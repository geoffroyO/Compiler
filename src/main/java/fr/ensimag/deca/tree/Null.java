package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;

public class Null extends AbstractExpr {

    public Null() {}

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // - create or get the null type
        Type type = new NullType(compiler.getSymbols().create("null"));
        // - set this terminal's type
        this.setType(type);
        return type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // -
        s.print(prefix);
        s.println();
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        //leaf node
    }

}
