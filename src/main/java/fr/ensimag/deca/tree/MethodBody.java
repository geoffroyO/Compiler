package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
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
    protected void verifyBody(DecacCompiler compiler, ClassDefinition current, EnvironmentExp localEnv, Type returnType)
            throws ContextualError {
        try {
            this.ListDeclVar.verifyListDeclVariable(compiler, localEnv, current);
            this.ListInst.verifyListInst(compiler, localEnv, current, returnType);
        } catch (ContextualError e) {
            throw e;
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.ListDeclVar.prettyPrint(s, prefix, false);
        this.ListInst.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.ListDeclVar.iter(f);
        this.ListInst.iter(f);
    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {
        
        ListDeclVar.codeGenDeclVar(compiler);
        ListInst.codeGenListInst(compiler);
    }
}
