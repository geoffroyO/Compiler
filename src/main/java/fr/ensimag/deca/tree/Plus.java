package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
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
            GPRegister regLeftOp = compiler.getRegM().findFreeGPRegister();

            this.getLeftOperand().codeGenExpr(compiler, regLeftOp);
            this.getRightOperand().codeGenExpr(compiler, register);
            
            if (!this.getLeftOperand().getType().sameType(this.getLeftOperand().getType()) ) {
            	if (getLeftOperand().getType().isInt()) {		
            		//TODO push pop
            		GPRegister newRegLeftOp = compiler.getRegM().findFreeGPRegister();
            		
            		compiler.addInstruction(new FLOAT(regLeftOp, newRegLeftOp));
            		compiler.addInstruction(new ADD(newRegLeftOp, register));
            		compiler.getRegM().freeRegister(newRegLeftOp);
            	}
            	else {           		
            		//TODO push pop
            		GPRegister newRegister = compiler.getRegM().findFreeGPRegister();
            		
            		compiler.addInstruction(new FLOAT(register, newRegister));
            		compiler.addInstruction(new ADD(regLeftOp, newRegister));
            		compiler.getRegM().freeRegister(newRegister);            		
            	}
            }            
            compiler.addInstruction(new ADD(regLeftOp, register));
            compiler.getRegM().freeRegister(regLeftOp);
        } else {
            GPRegister regLeftOp = Register.getR(compiler.getRegM().getNb_registers());

            this.getRightOperand().codeGenExpr(compiler, register);

            compiler.addInstruction(new PUSH(regLeftOp));

            this.getLeftOperand().codeGenExpr(compiler, regLeftOp);

            compiler.addInstruction(new LOAD(regLeftOp, Register.R0));
            compiler.addInstruction(new POP(regLeftOp));

            compiler.addInstruction(new ADD(Register.R0, register));
        }
        
    }
}
