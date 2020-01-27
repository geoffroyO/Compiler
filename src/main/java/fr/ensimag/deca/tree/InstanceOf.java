package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

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
    	Type instanceType = instance.verifyExpr(compiler, localEnv, currentClass);
    	Type classNameType = className.verifyType(compiler);
        this.setType(compiler.getEnvTypes().get(compiler.getSymbols().create("boolean")).getType());
        
        // Test if the elements can be compare
        if (!(instanceType.isNull() || (instanceType.isClass() && classNameType.isClass()))) {
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

        // - generate the value of the expression
        instance.codeGenExpr(compiler, register);

        if (instance.getType().isInt() && className.getType().isInt()) {

            compiler.addInstruction(new LOAD(new ImmediateInteger(1), register));

        } else if (instance.getType().isFloat() && className.getType().isFloat()) {

            compiler.addInstruction(new LOAD(new ImmediateInteger(1), register));

        } else if (instance.getType().isNull()) {

            compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));

        } else {

            // - get the address of the class to cast in the stack
            DAddr addr = className.getClassDefinition().getAddrClass();

            // - code of the function instance of
            compiler.addInstruction(new LEA(addr, Register.R1));
            compiler.addInstruction(new PUSH(register));
            compiler.addInstruction(new BSR(new Label("instance_of")));
            compiler.addInstruction(new POP(register));

            // - load the result in register
            compiler.addInstruction(new LOAD(Register.R0, register));

        }
    }
}
