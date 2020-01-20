package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {

    final private AbstractIdentifier type;
    final private AbstractIdentifier name;
    final private ListDeclParam listDeclParam;
    final private AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam listDeclParam, AbstractMethodBody body){
        this.type = type;
        this.name = name;
        this.listDeclParam = listDeclParam;
        this.body = body;

    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, ClassDefinition memberOf) throws ContextualError {
        // TODO
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(type.getName().getName() + " " + name.getName().getName() + '(');
        listDeclParam.decompile(s);
        s.print(") {");
        s.println();
        body.decompile(s);
        s.println("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.type.prettyPrint(s, prefix, false);
        this.name.prettyPrint(s, prefix, false);
        this.listDeclParam.prettyPrint(s, prefix, false);
        this.body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
        this.name.iter(f);
        this.listDeclParam.iter(f);
        this.body.iter(f);
    }

    @Override
    protected void codeGenFpDeclMethod(DecacCompiler compiler) {
        Label labelCodeMethod = new Label("code" + name.getMethodDefinition().getLabel().toString());
        compiler.addInstruction(new LOAD(new LabelOperand(labelCodeMethod), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getRegM().getGB(), Register.GB)));
        compiler.getRegM().incrGB();
        compiler.getRegM().incrSP();
    }

    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler) {
        Label labelCodeMethod = new Label("code" + name.getMethodDefinition().getLabel().toString());
        compiler.addLabel(labelCodeMethod);
    }

}
