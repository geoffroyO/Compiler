package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SUB;

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
        GPRegister reg_right_op = compiler.regM.findFreeGPRegister();

        this.getLeftOperand().codeGenExpr(compiler, register);
        this.getRightOperand().codeGenExpr(compiler, reg_right_op);

        compiler.addInstruction(new SUB(reg_right_op, register));
        compiler.regM.freeRegister(reg_right_op);
    }
}
