package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;


/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.getEnvTypes().get(compiler.getSymbols().create("boolean")).getType());
        return getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }


    /**
     * Generates assembly code to evaluate the expression.
     *
     * @param compiler
     *
     * @param register
     *  The result is stored in register
     */
    protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {

        if (getValue()) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(1), register));
        } else{
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));
        }
    }
}
