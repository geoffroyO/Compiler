package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.SGE;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Operator "x >= y"
 * 
 * @author gl13
 * @date 01/01/2020
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }
    protected void codeGenExpr(DecacCompiler compiler, GPRegister register){

        if (compiler.regM.hasFreeGPRegister()) {
            GPRegister reg_left_op = compiler.regM.findFreeGPRegister();

            this.getLeftOperand().codeGenExpr(compiler, reg_left_op);
            this.getRightOperand().codeGenExpr(compiler, register);

            compiler.addInstruction(new CMP(reg_left_op, register));
            compiler.addInstruction(new SGE(register));

            compiler.regM.freeRegister(reg_left_op);
        } else {
            GPRegister reg_left_op = Register.getR(compiler.regM.getNb_registers());

            this.getRightOperand().codeGenExpr(compiler, register);

            compiler.addInstruction(new PUSH(reg_left_op));

            this.getLeftOperand().codeGenExpr(compiler, reg_left_op);

            compiler.addInstruction(new CMP(reg_left_op, Register.R0));
            compiler.addInstruction(new POP(reg_left_op));

            compiler.addInstruction(new CMP(Register.R0, register));
            compiler.addInstruction(new SGE(register));
        }
    }

    protected void codeGenWhileCond(DecacCompiler compiler, Label label){
        if (compiler.regM.hasFreeGPRegister()) {
            GPRegister reg_left_op = compiler.regM.findFreeGPRegister();
            GPRegister reg_right_op = compiler.regM.findFreeGPRegister();

            this.getLeftOperand().codeGenExpr(compiler, reg_left_op);
            this.getRightOperand().codeGenExpr(compiler, reg_right_op);

            compiler.addInstruction(new CMP(reg_left_op, reg_right_op));
            compiler.addInstruction(new BGE(label));

            compiler.regM.freeRegister(reg_left_op);
        }
    }
}
