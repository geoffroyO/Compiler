package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

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
        // - create or get the boolean type
        Type type = new BooleanType((compiler.getSymbols().create("boolean")));
        // - set this terminal's type to boolean
        setType(type);
        return type;
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


    protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {

        if (this.getValue()) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(1), register));
        } else{
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));
        }
    }

//    @Override
//    protected void codeGenPrint(DecacCompiler compiler) {
//        if (this.getValue()) {
//        	 compiler.addInstruction(new WSTR(new ImmediateString("True")));
//        } else{
//        	 compiler.addInstruction(new WSTR(new ImmediateString("False")));
//        }
//    }

    protected void codeGenWhileCond(DecacCompiler compiler){
        // TODO
    }
}
