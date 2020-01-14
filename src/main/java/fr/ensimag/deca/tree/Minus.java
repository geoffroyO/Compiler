package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl13
 * @date 01/01/2020
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    protected void codeGenExpr(DecacCompiler compiler, GPRegister register){

        if (compiler.getRegM().hasFreeGPRegister()) {
            GPRegister reg_right_op = compiler.getRegM().findFreeGPRegister();

            this.getLeftOperand().codeGenExpr(compiler, register);
            this.getRightOperand().codeGenExpr(compiler, reg_right_op);

            compiler.addInstruction(new SUB(reg_right_op, register));
            compiler.getRegM().freeRegister(reg_right_op);
        } else{
            GPRegister reg_right_op = Register.getR(compiler.getRegM().getNb_registers());

            this.getLeftOperand().codeGenExpr(compiler, register);

            compiler.addInstruction(new PUSH(reg_right_op));

            this.getRightOperand().codeGenExpr(compiler, reg_right_op);

            compiler.addInstruction(new LOAD(reg_right_op, Register.R0));
            compiler.addInstruction(new POP(reg_right_op));

            compiler.addInstruction(new ADD(Register.R0, register));
        }
    }
}
