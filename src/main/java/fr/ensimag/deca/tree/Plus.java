package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;

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
        GPRegister reg_left_op = compiler.regM.findFreeGPRegister();

        this.getLeftOperand().codeGenExpr(compiler, reg_left_op);
        this.getRightOperand().codeGenExpr(compiler, register);

        compiler.addInstruction(new ADD(reg_left_op, register));
        compiler.regM.freeRegister(reg_left_op);
    }
}
