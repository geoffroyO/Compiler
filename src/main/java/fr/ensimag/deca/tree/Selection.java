package fr.ensimag.deca.tree;


import java.io.PrintStream;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class Selection extends AbstractLValue{
    private AbstractExpr instance;
    private AbstractIdentifier field;

    public Selection(AbstractExpr instance, AbstractIdentifier field)
    {
        this.instance = instance;
        this.field = field;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {    	
        ClassType instanceType = instance.verifyExpr(compiler, localEnv, currentClass).asClassType("The left member of 'instance.field' must be the instance of a class", getLocation());    
        EnvironmentExp instanceEnv = instanceType.getDefinition().getMembers();
        Type fieldType = field.verifyExpr(compiler, instanceEnv, currentClass);  
        if (field.getFieldDefinition().getVisibility() ==  Visibility.PROTECTED) {
        	if (currentClass == null) {
        		throw new ContextualError("It is not allowed to access a protected field outside a class", getLocation());
        	}   	
        	if (! currentClass.getType().isSubClassOf(field.getFieldDefinition().getContainingClass().getType())) {
        		throw new ContextualError("3.66 : Contextual error with an expression of type 'instance.field' \n"
        				+ "'field is protected and the current class (where instance.field is called) is not a subClass of the class where 'field' is declared", this.getLocation());
        	}
        	if (!(instanceType).isSubClassOf(currentClass.getType())) {
        		throw new ContextualError("3.66 : Contextual error with an expression of type 'instance.field' \n"
        				+ "'field' is protected and the class of 'instance' is not a subClass of the current class (where instance.field is called)", this.getLocation());
        	}
        }        
        this.setType(fieldType);  
        return(this.getType());
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


    protected void codeGenExpr(DecacCompiler compiler, GPRegister register){
        instance.codeGenExpr(compiler, register);
        compiler.addInstruction( new LEA(new RegisterOffset(field.getFieldDefinition().getIndex(), register), register));
        if (field.getType().isClass()) {
            compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register));
        }
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
        GPRegister addrReg = compiler.getRegM().findFreeGPRegister();
        instance.codeGenExpr(compiler, addrReg);
        compiler.addInstruction(new LOAD(new RegisterOffset(field.getFieldDefinition().getIndex(), addrReg), Register.R1));

        if (field.getFieldDefinition().getType().isInt()){
            compiler.addInstruction(new WINT());
        }

        if (this.getType().isFloat()){
            if (printHex) {
                compiler.addInstruction(new WFLOATX());
            } else {
                compiler.addInstruction(new WFLOAT());
            }
        }

        compiler.getRegM().freeRegister(addrReg);
    }
}
