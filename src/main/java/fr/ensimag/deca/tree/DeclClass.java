package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl13
 * @date 01/01/2020
 */
public class DeclClass extends AbstractDeclClass {

    private AbstractIdentifier className;
    private AbstractIdentifier superClass;
    private ListDeclField fields;
    private ListDeclMethod methods;

    // - constructor
    public DeclClass(AbstractIdentifier className, AbstractIdentifier superClass,
                    ListDeclField fields, ListDeclMethod methods) {
        this.className = className;
        this.superClass = superClass;
        this.fields = fields;
        this.methods  = methods;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        Type superType = this.superClass.verifyType(compiler);
        if(!superType.isClass()){
            throw new ContextualError("super is not a class", getLocation());
        } else if (compiler.getEnvTypes().get(this.className.getName()) != null){
            throw new ContextualError("class already declared", getLocation());
        }

//        System.out.println("--->>>>> This is a class declaration!!!");

    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenDeclClass(DecacCompiler compiler) {
        /* TODO utile pour l'héritage, CLASS OBJECT
        * int offGb = compiler.getRegM().getGB();
        *
        * int defSupClass = offGb - superClass.getClassDefinition().getNumberOfMethods();
        * compiler.addInstruction(new LEA(new RegisterOffset(defSupClass, Register.GB), Register.R0));
        * compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(offGb, Register.GB)));
        *
        * compiler.getRegM().incrGB();
        * compiler.getRegM().incrSP();
        */
        int offGb = compiler.getRegM().getGB();
        compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB), Register.R0)); //Hérite toujours de object
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(offGb, Register.GB)));
        compiler.getRegM().incrGB();
        compiler.getRegM().incrSP();
        methods.codeGenListDeclMethod(compiler);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.className.prettyPrint(s, prefix, false);
        this.superClass.prettyPrint(s, prefix, false);
        this.fields.prettyPrint(s, prefix, false);
        this.methods.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.className.iter(f);
        this.superClass.iter(f);
        this.methods.iter(f);
        this.fields.iter(f);
    }

}
