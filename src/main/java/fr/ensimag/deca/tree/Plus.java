package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * @author gl13
 * @date 01/01/2020
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }

    protected void codeGenExpr(DecacCompiler compiler, GPRegister register){
        // TODO factoriser le code?
        if (compiler.getRegM().hasFreeGPRegister()) {
            GPRegister reg_left_op = compiler.getRegM().findFreeGPRegister();

            this.getLeftOperand().codeGenExpr(compiler, reg_left_op);
            this.getRightOperand().codeGenExpr(compiler, register);



            compiler.addInstruction(new ADD(regLeftOp, register));

            compiler.getRegM().freeRegister(regLeftOp);


        } else {
            GPRegister reg_left_op = Register.getR(compiler.getRegM().getNb_registers());

            this.getRightOperand().codeGenExpr(compiler, register);

            compiler.addInstruction(new PUSH(reg_left_op));

            this.getLeftOperand().codeGenExpr(compiler, reg_left_op);

            compiler.addInstruction(new LOAD(reg_left_op, Register.R0));
            compiler.addInstruction(new POP(reg_left_op));

            compiler.addInstruction(new ADD(Register.R0, register));
        }
    }
}
