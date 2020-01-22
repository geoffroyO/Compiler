package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {

    final private AbstractIdentifier type;
    final private AbstractIdentifier name;
    final private ListDeclParam listDeclParam;
    final private AbstractMethodBody body;

    public int getDeclMethodIndex() { return name.getMethodDefinition().getIndex(); }

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam listDeclParam, AbstractMethodBody body){
        this.type = type;
        this.name = name;
        this.listDeclParam = listDeclParam;
        this.body = body;

    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, ClassDefinition current, ClassDefinition superClass)
            throws ContextualError {

        // - verify method type
        Type returnType = this.type.verifyType(compiler);
        Signature signature;
        MethodDefinition newMethodDefinition;

        try {
            // - get the class environment and verify if the method already in the environment
            EnvironmentExp members = current.getMembers();
            ExpDefinition superDefinition = members.get(this.name.getName());

            // - create localEnv and verify the signature of the method
            EnvironmentExp localEnv = new EnvironmentExp(members);
            signature = this.listDeclParam.verifyListDeclParam(compiler, localEnv);


            // - if method already exist in the environment
            if (superDefinition != null && superDefinition.isMethod()){
                int superDefinitionIndex = ((MethodDefinition)superDefinition).getIndex();
                newMethodDefinition = new MethodDefinition(returnType, this.type.getLocation(), signature, superDefinitionIndex);
            } else {
                // - method index
                int methodIndex = current.incNumberOfMethods() + superClass.getNumberOfMethods();
                newMethodDefinition = new MethodDefinition(returnType, this.type.getLocation(), signature, methodIndex);
            }

            // - set Label for the new method
            newMethodDefinition.setLabel(new Label((current.getType().getName().getName()) + "." + this.name.getName().getName()));

            // - declare the new method in the environment and set this
            members.declare(name.getName(), newMethodDefinition);

            // - add the method to methods table (MT)
            current.getMT().putInMT(newMethodDefinition);

            // - set type and definition for this method
            this.name.setType(returnType);
            this.name.setDefinition(newMethodDefinition);

        } catch (ContextualError e) {
            throw e;
        } catch (EnvironmentExp.DoubleDefException d) {
            throw new ContextualError("Method already defined in the class", this.name.getLocation());
        }
        // - verify if there is an @override by checking if the signatures of same methods in different levels
        EnvironmentExp superMembers = current.getSuperClass().getMembers();
        if (superMembers.get(this.name.getName()) != null && superMembers.get(this.name.getName()).isMethod()) {

            MethodDefinition definitionInSuper = (MethodDefinition) superMembers.get(this.name.getName());

            // - compare the 2 methods types and signatures size
            Type type1 = newMethodDefinition.getType();
            Type type2 = definitionInSuper.getType();

            if (type1.isClass() && type2.isClass()){
                if (!((ClassType)type1).isSubClassOf((ClassType)type2)){
                    throw new ContextualError("Return type should be subtype of the super class method",this.getLocation());
                }
            }else if (!newMethodDefinition.getType().sameType(definitionInSuper.getType())){
                throw new ContextualError("Return type should be the same or subtype of the super class method",this.getLocation());
            }
            if (newMethodDefinition.getSignature().size() != definitionInSuper.getSignature().size()) {
                throw new ContextualError("Override method with different signature",this.getLocation());

            } else {
                // - compare arguments in the 2 methods
                for (int i = 0; i < newMethodDefinition.getSignature().size();i++) {
                    if (!newMethodDefinition.getSignature().paramNumber(i).sameType(definitionInSuper.getSignature().paramNumber(i))) {
                        throw new ContextualError("Override method with different signature", this.getLocation());
                    }
                }
            }
        }
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, ClassDefinition current) throws ContextualError{
        // - create a Local Environment Local and add to it current class members and method parameters
        EnvironmentExp methodEnv = new EnvironmentExp(current.getMembers());
        this.listDeclParam.verifyListDeclParam(compiler, methodEnv);

        // - get method return type (void, int, float...)
        Type returnType = this.type.verifyType(compiler);

        // - verify the body of the method (function in MethodBody)
        this.body.verifyBody(compiler, current, methodEnv, returnType);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(type.getName().getName() + " " + name.getName().getName() + '(');
        listDeclParam.decompile(s);
        s.print(") {");
        s.println();
        s.indent();
        body.decompile(s);
        s.unindent();
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
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getRegM().getGB() + name.getMethodDefinition().getIndex() - 1, Register.GB)));
    }

    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler) {
        Label labelCodeMethod = new Label("code" + name.getMethodDefinition().getLabel().toString());
        compiler.addLabel(labelCodeMethod);


        int nbMaxRegister = compiler.getRegM().getNb_registers();
        compiler.addComment("Sauvegarde des registres utilisés");

        for (int k = 2; k < nbMaxRegister; k++) {
            if (!compiler.getRegM().isFreeRegister(Register.getR(k))){
                compiler.addInstruction(new TSTO(new ImmediateInteger(1)));
                compiler.addInstruction(new PUSH( Register.getR(k)));
                compiler.addInstruction(new BOV(new Label("stack_overflow")));
            }
        }
        boolean[] oldFreeRegister = compiler.getRegM().setFreeRegister();

        listDeclParam.codeGenListDeclParam(compiler);
        body.codeGenMethodBody(compiler);

        compiler.addLabel(new Label("fin." + name.getMethodDefinition().getLabel()));
        compiler.addComment("Restauration des registres");
        for (int k = 2; k < nbMaxRegister; k++){
            if (oldFreeRegister[k]){
                if (!compiler.getRegM().isFreeRegister(Register.getR(k))) {
                    compiler.getRegM().freeRegister(Register.getR(k));
                }
            } else {
                compiler.addInstruction(new POP(Register.getR(k)));
                compiler.getRegM().unFreeRegister(Register.getR(k));
            }
        }
        compiler.addInstruction(new RTS());
    }

}
