package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class Cast extends  AbstractExpr{
    private AbstractIdentifier type;
    private AbstractExpr nameVar;

    public Cast (AbstractIdentifier type, AbstractExpr nameVar)
    {
        this.type = type;
        this.nameVar = nameVar;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

        
    	Type castType = type.verifyType(compiler);
    	
    	

        Type varType = nameVar.verifyExpr(compiler, localEnv, currentClass);

        EnvironmentType env = compiler.getEnvTypes();

        if (varType.isVoid())
        {
            // Can't cast void element
            throw new ContextualError ("Object can't be void", getLocation());
        }

        if (!(this.assignCompatible(env , castType, varType) || this.assignCompatible(env, varType, castType)))
        {
            // not cast_compatible
             throw new ContextualError ("The elements aren't cast compatible.", getLocation());
        }

        this.setType(castType);
        return(this.getType());

    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(") ");
        nameVar.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        nameVar.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
