package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // - verify operand is boolean
        Type opType;
        try {
            opType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e){
            throw e;
        }
        // - // - verify condition if its type is boolean
        if (!opType.isBoolean()) {
            throw new ContextualError("Unary Minus Operand should be int or float", getLocation());
        }
        this.setType(opType);
        return opType;
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }

    protected void codeGenExpr(DecacCompiler compiler, GPRegister register){

        this.getOperand().codeGenExpr(compiler, register);

        compiler.addInstruction(new ADD(new ImmediateInteger(1), register));
        compiler.addInstruction(new REM(new ImmediateInteger(2), register));

    }

    protected void codeGenInst(DecacCompiler compiler, Label label){
        // TODO push et pop
        if (compiler.getRegM().hasFreeGPRegister()) {
            GPRegister register = compiler.getRegM().findFreeGPRegister();
            this.getOperand().codeGenExpr(compiler, register);
            compiler.addInstruction(new ADD(new ImmediateInteger(1), register));
            compiler.addInstruction(new REM(new ImmediateInteger(2), register));
            compiler.addInstruction(new CMP(new ImmediateInteger(1),register));
            compiler.addInstruction(new BNE(label));
            compiler.getRegM().freeRegister(register);
        }
    }
}
