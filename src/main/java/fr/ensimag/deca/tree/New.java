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

    AbstractIdentifier type;

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // - get class type
        Type identType = type.verifyType(compiler);

        // - check if the class is defined
        if (!identType.isClass()) {
            throw new ContextualError("The identifier is not a class", this.type.getLocation());
        }

        this.setType(identType);
        return identType;
    }

    public New(AbstractIdentifier classe) {
        this.type = classe;
    }

    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    	type.prettyPrint(s, prefix, true);
    }

    
    
    
    

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
        // TODO manque héritage
        compiler.addInstruction(new NEW(new ImmediateInteger(type.getClassDefinition().getNumberOfFields() + 1), register), " on crée le tas");
        compiler.addInstruction(new BOV(new Label("heap_overflow")));
        compiler.addInstruction(new LEA(type.getClassDefinition().getAddrClass(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, register)));
        compiler.addInstruction(new PUSH(register));
        compiler.addInstruction(new BSR(new Label("init." + type.getName())));
        compiler.addInstruction(new POP(register));

    }
}
