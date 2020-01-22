package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SHR;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // - declare 2 types
        Type leftOpType;
        Type rightOpType;

        // - verify and get both operands types
        try {
            leftOpType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            rightOpType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e){
            throw e;
        }

        // - verify if both types are booleans
        if (!leftOpType.isBoolean() && !rightOpType.isBoolean()) {
            throw new ContextualError("Operands should be booleans", getLocation());
        }
        this.setType(leftOpType);
        return leftOpType;
    }


    protected abstract void codeGenBoolLazy(DecacCompiler compiler, Label label, GPRegister result);

    protected void codeGenExpr(DecacCompiler compiler, GPRegister result){
        // - boolean label for the begining of the expression
        Label label = compiler.getLabM().genEndOpBoolLabel();

        if (compiler.getRegM().hasFreeGPRegister()) {
            GPRegister right = compiler.getRegM().findFreeGPRegister();

            // - the value of the left operand is in the register result
            getLeftOperand().codeGenExpr(compiler, result);

            // - lazy evalutation of boolean expression
            codeGenBoolLazy(compiler, label, result);

            // - result of the right operand in the register right
            getRightOperand().codeGenExpr(compiler, right);

            // - proceed to the final operation
            codeGenOp(compiler, right, result);

            // - free the register
            compiler.getRegM().freeRegister(right);
        } else {
            GPRegister right = Register.getR(compiler.getRegM().getNb_registers());
            getLeftOperand().codeGenExpr(compiler, result);
            codeGenBoolLazy(compiler, label, result);

            // - push the right register at the top of the stack
            compiler.addInstruction(new PUSH(right));

            // - the result of the expression is in the register right
            getRightOperand().codeGenExpr(compiler, right);

            // -  load the result in R0
            compiler.addInstruction(new LOAD(right, Register.R0));

            // - backup of the register
            compiler.addInstruction(new POP(right));

            // - proceed to the final evaluation
            codeGenOp(compiler, Register.R0, result);
        }

        // - end label - where we jump if we have the result before the end of the evaluation
        compiler.addLabel(label);
    }


}
