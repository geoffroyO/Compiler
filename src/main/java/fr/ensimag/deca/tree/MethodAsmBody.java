package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class MethodAsmBody extends AbstractMethodBody{
    private String multiLineStream;
    private Location location;

    public MethodAsmBody(String multiLineStream, Location location) {
        this.multiLineStream = multiLineStream;
        this.location = location;
    }

    @Override
    protected void verifyBody(DecacCompiler compiler, ClassDefinition currentClass, EnvironmentExp localEnv, Type returnType) {
        // - Nothing to verify
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO
    }
}
