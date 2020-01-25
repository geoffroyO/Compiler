package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingDeque;

public class Cast extends  AbstractExpr{
    private AbstractIdentifier type;
    private AbstractExpr nameVar;

    public Cast (AbstractIdentifier type, AbstractExpr nameVar){
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
        s.print(")");
        s.print("(");
        nameVar.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        nameVar.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }


    @Override
    protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {

        // - evaluation of the expression and value stored in register
        nameVar.codeGenExpr(compiler, register);


        if (nameVar.getType().isInt() && type.getType().isFloat()) {

            // - casting integer --> float
            compiler.addInstruction(new FLOAT(register, register), "Casting integer --> float");

        } else if (nameVar.getType().isFloat() && type.getType().isInt()){

            // - casting float --> integer
            compiler.addInstruction(new INT(register, register), "casting float --> integer");

        } else {

            // - get the address of the class to cast in the stack
            DAddr addr = type.getClassDefinition().getAddrClass();

            // - label to jump in if cast possible
            Label castPossible = new Label("end_cast_possible");


            // - code of the cast
            compiler.addInstruction(new LEA(addr, Register.R1));
            compiler.addInstruction(new PUSH(register));
            compiler.addInstruction(new BSR(new Label("instance_of")));
            compiler.addInstruction(new POP(register));

            // - if cast possible
            compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.R1));
            compiler.addInstruction(new CMP(Register.R0, Register.R1));
            compiler.addInstruction(new BEQ(castPossible));

            // - cast impossible
            compiler.addInstruction(new WSTR(new ImmediateString("cast " + nameVar.getType() + " --> " + type.getType() + " impossible")));
            compiler.addInstruction(new ERROR());

            // - label to stop the execution with an error to put in decac compiler
            compiler.addLabel(castPossible);

            // - store this address in R0
            compiler.addInstruction(new LEA(addr, Register.R0));

            // - change the base of the heap
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, register)), "casting " + nameVar.getType() + " --> " + type.getType());
        }
    }


}
