package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.deca.DecacCompiler;


/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public abstract Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError;

    abstract protected void codeGenLoopOp(DecacCompiler compiler, Label label, GPRegister left, GPRegister right);
    
    protected void codeGenCond(DecacCompiler compiler, Label label){
        if (compiler.getRegM().hasFreeGPRegister()) {
            GPRegister left = compiler.getRegM().findFreeGPRegister();
            GPRegister right = compiler.getRegM().findFreeGPRegister();
            this.getLeftOperand().codeGenExpr(compiler, left);
            this.getRightOperand().codeGenExpr(compiler, right);       
            codeGenLoopOp(compiler, label, left, right);
            compiler.getRegM().freeRegister(left);
            compiler.getRegM().freeRegister(right);
        }
        else {
            // TODO push et pop
        }
    }
}
