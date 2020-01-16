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
public class Or extends AbstractOpBool {
	
    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected void codeGenOp(DecacCompiler compiler, GPRegister regResult, GPRegister reg) {
    	GPRegister tmp = compiler.getRegM().findFreeGPRegister();  
    	compiler.addInstruction(new LOAD(regResult, tmp));
        compiler.addInstruction(new ADD(reg, regResult));      
        compiler.addInstruction(new MUL(tmp, reg));
        compiler.addInstruction(new SUB(reg, regResult));   
        compiler.getRegM().freeRegister(tmp);
    }
}
