package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class New extends AbstractExpr{

    AbstractIdentifier className;

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // - get class type
        Type identType;
        try {
            identType = className.verifyType(compiler);
        } catch (ContextualError e) {
            throw e;
        }
        // - check if the class is defined
        if (!identType.isClass()) {
            throw new ContextualError("The identifier is not a class", this.className.getLocation());
        }

        this.setType(identType);
        return identType;
    }

    public New(AbstractIdentifier classe) {
        this.className = classe;
    }

    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    String prettyPrintNode() {
        return "new " + className.getName();

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
        // TODO manque héritage
        compiler.addInstruction(new NEW(new ImmediateInteger(className.getClassDefinition().getNumberOfFields() + 1), register), " on crée le tas");
        compiler.addInstruction(new BOV(new Label("heap_overflow")));
        compiler.addInstruction(new LEA(className.getClassDefinition().getAddrClass(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, register)));
        compiler.addInstruction(new PUSH(register));
        compiler.addInstruction(new BSR(new Label("init." + className.getName())));
        compiler.addInstruction(new POP(register));

    }
}
