package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;

public class InstanceOf extends AbstractExpr{
    private AbstractExpr instance;
    private AbstractIdentifier className;
    private boolean value;

    public InstanceOf(AbstractExpr instance,AbstractIdentifier className ) {
        this.instance = instance;
        this.className = className;
        this.value = false;
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
    	Type instanceType = instance.verifyExpr(compiler, localEnv, currentClass);
    	Type classNameType = className.verifyType(compiler);
        this.setType(compiler.getEnvTypes().get(compiler.getSymbols().create("boolean")).getType());
        
        // Null is not a classType
        if (instanceType.isNull()) {
        	this.value = false;
        } else if (instanceType.isClass() && classNameType.isClass()) {
        	 this.value = instanceType.asClassType("Error TODO", this.getLocation()).isSubClassOf(classNameType.asClassType("Error TODO", this.getLocation()));
        } else {
        	throw new ContextualError("Contextual error, instanceof must be called with an instance and a class", getLocation());
        }
        return(this.getType());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        instance.decompile(s);
        s.print(" instanceof ");
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

    @Override
    protected void codeGenExpr(DecacCompiler compiler, GPRegister register){
        // TODO
    }
}
