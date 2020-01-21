package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class Selection extends AbstractLValue{
    private AbstractLValue instance;
    private AbstractIdentifier field;

    public Selection(AbstractLValue instance, AbstractIdentifier field)
    {
        this.instance = instance;
        this.field = field;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type tInstance = instance.verifyExpr(compiler, localEnv, currentClass);
        Type tField = field.verifyExpr(compiler, localEnv, currentClass);
        
        this.setType(tField);
      
        return(tField);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        instance.decompile(s);
        s.print(".");
        field.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        instance.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
