package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    protected void codeGenExpr(DecacCompiler compiler, GPRegister register){

        if (compiler.regM.hasFreeGPRegister()) {
            GPRegister reg_left_op = compiler.regM.findFreeGPRegister();

            this.getLeftOperand().codeGenExpr(compiler, reg_left_op);
            this.getRightOperand().codeGenExpr(compiler, register);

            compiler.addInstruction(new MUL(reg_left_op, register));

            compiler.regM.freeRegister(reg_left_op);
        } else {
            GPRegister reg_left_op = Register.getR(compiler.regM.getNb_registers());

            this.getRightOperand().codeGenExpr(compiler, register);

            compiler.addInstruction(new PUSH(reg_left_op));

            this.getLeftOperand().codeGenExpr(compiler, reg_left_op);

            compiler.addInstruction(new LOAD(reg_left_op, Register.R0));
            compiler.addInstruction(new POP(reg_left_op));

            compiler.addInstruction(new MUL(Register.R0, register));

        }
    }

    protected void codeGenWhileCond(DecacCompiler compiler, Label label){
        // TODO push et pop
        if (compiler.regM.hasFreeGPRegister()) {
            GPRegister reg_left_op = compiler.regM.findFreeGPRegister();
            GPRegister reg_right_op = compiler.regM.findFreeGPRegister();

            this.getLeftOperand().codeGenExpr(compiler, reg_left_op);
            this.getRightOperand().codeGenExpr(compiler, reg_right_op);

            compiler.addInstruction(new MUL(reg_left_op, reg_right_op));

            compiler.addInstruction(new CMP(new ImmediateInteger(1), reg_right_op));
            compiler.addInstruction(new BEQ(label));

            compiler.regM.freeRegister(reg_left_op);
            compiler.regM.freeRegister(reg_right_op);
        }
    }

}
