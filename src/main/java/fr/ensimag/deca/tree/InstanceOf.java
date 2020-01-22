package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class InstanceOf extends AbstractExpr{
    private AbstractExpr instance;
    private AbstractIdentifier className;

    public InstanceOf(AbstractExpr instance,AbstractIdentifier className ) {
        this.instance = instance;
        this.className = className;
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        instance.verifyExpr(compiler, localEnv, currentClass);
        className.verifyExpr(compiler, localEnv, currentClass);

        this.setType( compiler.getEnvTypes().get(compiler.getSymbols().create("boolean")).getType() );
        return(this.getType());

        /*
        if ((instanceType.isNull() || instanceType.isClass()) && (classNameType.isClass()))
        {
           // Case Type x Type -> boolean
            if (instanceType.asClassType("Error TODO", this.getLocation()).isSubClassOf(classNameType.asClassType("Error TODO", this.getLocation())))
            {

            }
        }
        */
    }

    @Override
    public void decompile(IndentPrintStream s) {
        instance.decompile(s);
        s.print(" instanceOf ");
        className.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        instance.prettyPrint(s, prefix, false);
        className.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
