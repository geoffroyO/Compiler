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
    protected void codeGenDeclMethod(DecacCompiler compiler) {

        // - TODO find conditions for saving a register

        // - begining of the code for the method
        compiler.addLabel(new Label("code." + name.getMethodDefinition().getLabel()));

        // - declaration of the parameters and set
        listDeclParam.codeGenListDeclParam(compiler);

        // - new bloc so that we can add TSTO, BOV, and ADDSP
        compiler.beginBloc();

        // - generation of the body of the method
        body.codeGenMethodBody(compiler);

        // - END
        compiler.addLabel(new Label("End." + name.getMethodDefinition().getLabel()));

        // - get the number of register to save
        int nbRegistersToSave = compiler.getRegM().getMaxToSave();

        // - we don't take into account R0 and R1
        int max = compiler.getRegM().getNb_registers() - 2;

        // - restoration of the registers to save
        compiler.addComment("Restoring the registers");
        for (int k = 2; k <= nbRegistersToSave + 1; k++) {
            if (k <= max) {
                compiler.addInstruction(new POP(Register.getR(k)));
            }
        }

        // - return
        compiler.addInstruction(new RTS());

        // - save the registers
        for (int k = 2; k <= nbRegistersToSave + 1 ; k++) {
            if (k <= max) {
                compiler.addFirst(new PUSH(Register.getR(k)));
            }
        }
        compiler.addFirstComment("Saving the registers");

        // - add TSTO, ADDSP and BOV
        compiler.addFirst(new ADDSP(new ImmediateInteger(compiler.getRegM().getLocalVariable())));
        compiler.addFirstTSTO(compiler.getRegM().getSP());

        // - END bloc
        compiler.endBloc();
    }

}
