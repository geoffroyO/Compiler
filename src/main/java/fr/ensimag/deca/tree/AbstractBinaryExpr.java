package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import org.apache.commons.lang.Validate;

/**
 * Binary expressions.
 *
 * @author gl13
 * @date 01/01/2020
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

    protected void codeGenOp(DecacCompiler compiler, GPRegister register, GPRegister result) {
    };
    
    protected void codeGenExpr(DecacCompiler compiler, GPRegister result){
        if (compiler.getRegM().hasFreeGPRegister()) {
            GPRegister right = compiler.getRegM().findFreeGPRegister();
            this.getLeftOperand().codeGenExpr(compiler, result);
            this.getRightOperand().codeGenExpr(compiler, right);
            this.codeGenOp(compiler, right, result);
            compiler.getRegM().freeRegister(right);
        } 
        else {
            GPRegister right = Register.getR(compiler.getRegM().getNb_registers());
            this.getLeftOperand().codeGenExpr(compiler, result);
            compiler.addInstruction(new PUSH(right));
            this.getRightOperand().codeGenExpr(compiler, right);
            compiler.addInstruction(new LOAD(right, Register.R0));
            compiler.addInstruction(new POP(right));
            this.codeGenOp(compiler, Register.R0, result);
        }
    }
}
