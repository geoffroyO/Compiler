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
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }
    
    @Override
    protected void codeGenOp(DecacCompiler compiler, GPRegister reg, GPRegister regResult) {
        compiler.addInstruction(new CMP(regResult, reg));
        compiler.addInstruction(new SEQ(regResult));
    }
  /*
    @Override    
    protected void codeGenLoopOp(DecacCompiler compiler, Label label, GPRegister left, GPRegister right) {
        compiler.addInstruction(new CMP(right, left));
        compiler.addInstruction(new BNE(label));
    }

   */
}
