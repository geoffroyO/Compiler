package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr{

        private AbstractExpr instance ;
        private AbstractIdentifier nameMethod;
        private ListExpr params;

    public MethodCall(AbstractExpr instance, AbstractIdentifier nameMethod, ListExpr params){
        // instance can be null if match method(params)
        this.instance = instance;
        this.nameMethod = nameMethod;
        this.params = params;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return null;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        instance.decompile(s);
        s.print(".");
        nameMethod.decompile();
        s.print("(");
        params.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        instance.prettyPrintChildren(s, prefix);
        nameMethod.prettyPrintChildren(s, prefix);
        params.prettyPrintChildren(s, prefix);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
