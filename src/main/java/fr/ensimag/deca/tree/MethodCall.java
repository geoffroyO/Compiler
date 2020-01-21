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

public class MethodCall extends AbstractExpr{

        private AbstractExpr instance ;
        private AbstractIdentifier nameMethod;
        private ListExpr params;

    public MethodCall(AbstractExpr instance, AbstractIdentifier nameMethod, ListExpr params){
        // instance can be null if match method(params)
        this.instance = instance;
        this.nameMethod = nameMethod;
        this.params = params;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return null;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        instance.decompile(s);
        s.print(".");
        nameMethod.decompile();
        s.print("(");
        params.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        instance.prettyPrint(s, prefix, false);
        nameMethod.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }


    protected void codeGenExpr(DecacCompiler compiler) {
        int nbParams = params.size();
        int index = 0;
        compiler.addInstruction(new TSTO(new ImmediateInteger(nbParams)));
        compiler.addInstruction(new BOV(new Label("stack_overflow")));
        compiler.addInstruction(new ADDSP(new ImmediateInteger(nbParams)));

        GPRegister register = compiler.getRegM().findFreeGPRegister();
        instance.codeGenExpr(compiler, register);
        compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register));
        compiler.addInstruction(new STORE(register, new RegisterOffset(index, Register.SP)));
        index--;

        for (AbstractExpr expr : params.getList()) {
            expr.codeGenExpr(compiler, register);
            compiler.addInstruction(new STORE(register, new RegisterOffset(index, Register.SP)));
            index--;
        }

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), register), "On récupère le paramètre implicite");
        compiler.addInstruction(new CMP(new NullOperand(), register));
        compiler.addInstruction(new BEQ(new Label("deferencement.null")));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register), "on récupère l'adresse de la table des méthodes");
        compiler.addInstruction(new BSR(new RegisterOffset(nameMethod.getMethodDefinition().getIndex(), register)), "on saute à la méthode");
        compiler.addInstruction(new SUBSP(new ImmediateInteger(nbParams)));
    }
}
