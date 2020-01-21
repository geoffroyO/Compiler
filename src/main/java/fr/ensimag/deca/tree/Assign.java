package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type  verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // -
        Type LeftOpType;
        try {
            LeftOpType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            // - check for int to float conversion or for different type
            this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, LeftOpType));
        } catch (ContextualError e) {
            throw e;
        }
        this.setType(LeftOpType);
        return LeftOpType;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

    protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
        DAddr addr = ((AbstractIdentifier) this.getLeftOperand()).getVariableDefinition().getOperand();
        this.getRightOperand().codeGenExpr(compiler, register);
        compiler.addInstruction(new STORE(register, addr));
    }

    protected void codeGenInst(DecacCompiler compiler) {
       // DAddr addr = ((AbstractIdentifier) this.getLeftOperand()).getVariableDefinition().getOperand();
        GPRegister register = compiler.getRegM().findFreeGPRegister();
        this.getLeftOperand().codeGenExpr(compiler, register);
        getRightOperand().codeGenExpr(compiler, register);
        compiler.addInstruction(new STORE(register, addr));
        compiler.getRegM().freeRegister(register);
    }
}
