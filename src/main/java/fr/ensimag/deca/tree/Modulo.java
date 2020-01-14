package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        // - declare 2 types
        Type leftOpType;
        Type rightOpType;

        // - verify and get both operands types
        try {
            leftOpType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            rightOpType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e){
            throw e;
        }

        // - Operands must be numeric numbers
        if((leftOpType.isInt() || leftOpType.isFloat()) && (rightOpType.isInt() || rightOpType.isFloat())){
            if (leftOpType.isFloat()){
                this.setType(leftOpType);
                return leftOpType;
            }else{
                this.setType(rightOpType);
                return rightOpType;
            }
        } else {
            throw new ContextualError("Operands must be numeric numbers [Modulo]", getLocation());
        }


    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

    protected void codeGenExpr(DecacCompiler compiler, GPRegister register){

        if (compiler.getRegM().hasFreeGPRegister()) {
            GPRegister reg_right_op = compiler.getRegM().findFreeGPRegister();

            this.getLeftOperand().codeGenExpr(compiler, register);
            this.getRightOperand().codeGenExpr(compiler, reg_right_op);

            compiler.addInstruction(new REM(reg_right_op, register));
            compiler.getRegM().freeRegister(reg_right_op);
        } else {
            GPRegister reg_right_op = Register.getR(compiler.getRegM().getNb_registers());

            this.getLeftOperand().codeGenExpr(compiler, register);

            compiler.addInstruction(new PUSH(reg_right_op));

            this.getRightOperand().codeGenExpr(compiler, reg_right_op);

            compiler.addInstruction(new LOAD(reg_right_op, Register.R0));
            compiler.addInstruction(new POP(reg_right_op));

            compiler.addInstruction(new REM(Register.R0, register));
        }
    }
}
