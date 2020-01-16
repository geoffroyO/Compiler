package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
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
    
    protected void codeGenInst(DecacCompiler compiler, Label label){
        if (compiler.getRegM().hasMultFreeGPRegister(2)) {
            GPRegister left = compiler.getRegM().findFreeGPRegister();
            GPRegister right = compiler.getRegM().findFreeGPRegister();
            getLeftOperand().codeGenExpr(compiler, left);
            getRightOperand().codeGenExpr(compiler, right);
            codeGenLoopOp(compiler, label, left, right);
            compiler.getRegM().freeRegister(left);
            compiler.getRegM().freeRegister(right);
        }
    }
}
