package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class MethodBody extends AbstractMethodBody {

    final private ListDeclVar ListDeclVar;
    final private ListInst ListInst;

    public MethodBody(ListDeclVar listDeclVar, ListInst listInst) {
        this.ListDeclVar = listDeclVar;
        this.ListInst = listInst;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        ListDeclVar.decompile(s);
        ListInst.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO
    }
}
