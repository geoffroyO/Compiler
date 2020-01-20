package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.util.HashMap;
import java.util.Map;
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
        s.print("class ");
        s.print(className.getName().getName());
        s.print(" extends ");
        s.print(superClass.getName().getName());
        s.println(" {");
        s.indent();
        fields.decompile(s);
        methods.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {

        // - verify that super's type exists in env_types
        Type superType = this.superClass.verifyType(compiler);

        // - FOR DEBBUGING ONLY | DO NOT DELETE THIS | WE BE REMOVED LATER
//        System.out.println("Current class is : " + className.getName().getName());
//        System.out.println("Super class is : " + superClass.getName().getName());
//        System.out.println("Super class definition is : " + compiler.getEnvTypes().get(superType.getName()));

        // if the super class is not a class
        if(!superType.isClass()){
            throw new ContextualError("super is not a class", getLocation());
        }

        // - get the super class definition from env_types to create the definition of the son
        ClassDefinition superDefinition = (ClassDefinition)compiler.getEnvTypes().get(this.superClass.getName());
        // - create the type and definition for the son
        ClassType type = new ClassType(className.getName(), this.getLocation(), superDefinition);
        ClassDefinition definition = type.getDefinition();

        // - declare the new class in env_types
        try {
            compiler.getEnvTypes().declare(this.className.getName(), definition);
        } catch (EnvironmentType.DoubleDefException d){
            throw new ContextualError("Class name was already defined", this.getLocation());
        }

        // - set the type and definition for the new class
        this.className.setType(type);
        this.className.setDefinition(definition);

        // - if the extends class is empty, set the super location to Object's location
        if (superClass.getName().getName() == "Object"){
            superClass.setLocation(superDefinition.getLocation());
        }

    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {

        // Verify the fiels declaration
        this.fields.verifyListDeclField(compiler, className.getClassDefinition());

    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenFpDeclClass(DecacCompiler compiler) {
        if (superClass == null){
            className.getClassDefinition().setAddrClass(new RegisterOffset(1, Register.GB));
            compiler.addComment("Code de la table des méthodes de la classe Object");
            compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getRegM().getGB(), Register.GB)));
            compiler.getRegM().incrSP();
            compiler.getRegM().incrGB();
            compiler.addInstruction(new LOAD(new LabelOperand(new Label("code.Object.equals")), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getRegM().getGB(), Register.GB)));
            compiler.getRegM().incrSP();
            compiler.getRegM().incrGB();

        } else {
            DAddr addrClass = new RegisterOffset(compiler.getRegM().getGB(), Register.GB);
            className.getClassDefinition().setAddrClass(addrClass);
            DAddr addrSuperClass = superClass.getClassDefinition().getAddrClass();
            compiler.addComment("Code de la table des méthodes de la classe " + className.getName());
            compiler.addInstruction(new LEA(addrSuperClass, Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getRegM().getGB(), Register.GB)));
            compiler.getRegM().incrGB();
            compiler.getRegM().incrSP();
            /* A rajouter si la méthode equals n'est pas dans la liste des méthodes
            Label labelCodeMethod = new Label("code.Object.equals");
            compiler.addInstruction(new LOAD(new LabelOperand(labelCodeMethod), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getRegM().getGB(), Register.GB)));
            compiler.getRegM().incrGB();
            compiler.getRegM().incrSP();
             */
            methods.codeGenListFpDeclMethod(compiler);
        }
    }

    protected void codeGenDeclClass(DecacCompiler compiler) {
        compiler.addComment("Initialisation des champs de la classe de " + className.getName());
        compiler.addLabel(new Label("init." + className.getName()));
        fields.codeGenListDeclField(compiler);
        compiler.addInstruction(new RTS());

        compiler.addComment("Code des méthodes de la classe de " + className.getName());
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
