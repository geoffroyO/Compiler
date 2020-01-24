package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

public class This extends AbstractLValue{

    public This()
    {

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {	
    	if (currentClass == null) {
            throw new ContextualError("'this' must be used inside a class", this.getLocation());
        }   	
    	
    	Type type = currentClass.getType();
    
        this.setType(type);
        

    	
        return this.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("this");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {        
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    @Override
    protected void codeGenLValueAddr(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new LEA(new RegisterOffset(-2, Register.LB), register));
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler,GPRegister register){
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), register));
    }
}
