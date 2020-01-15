package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

    protected void codeGenExpr(DecacCompiler compiler, GPRegister register){
    	super.codeGenExpr(compiler, register);
        if (!compiler.getRegM().hasFreeGPRegister()) {       
            compiler.addInstruction(new BOV(new Label("Zero_division")));
        }
    }
    
	@Override
	protected void codeGenOp(DecacCompiler compiler, GPRegister register, GPRegister result) {
		compiler.addInstruction(new DIV(register, result));
	}
}
