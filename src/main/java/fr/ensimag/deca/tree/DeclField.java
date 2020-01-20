package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField {
    final private AbstractIdentifier type;
    final private AbstractIdentifier fieldName;
    final private AbstractInitialization initialization;
    final private Visibility visibility;

    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization initialization){
        Validate.notNull(type);
        Validate.notNull(fieldName);
        Validate.notNull(initialization);

        this.visibility = visibility;
        this.type = type;
        this.fieldName = fieldName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyField(DecacCompiler compiler, ClassDefinition memberOf) throws ContextualError {

        Type type;
        EnvironmentExp envExpSuper = memberOf.getMembers();

        try {
            // - verify that type exists in envtypes (verifyType available in Identifier.java)
            type = this.type.verifyType(compiler);

            // - create definition for the field using the type and location
            int index = 1;
            FieldDefinition definition = new FieldDefinition(type, this.type.getLocation(), this.visibility, memberOf, index);

            this.fieldName.setDefinition(definition);

            // - add the variable to the local environment (Symbol -> definition)
            envExpSuper.declare(this.fieldName.getName(), definition);

        } catch (ContextualError e) {
            throw e;
        } catch (EnvironmentExp.DoubleDefException doubleDefinition) {
            throw new ContextualError("field already defined", this.getLocation());
        }

        if (type.isVoid()) {
            throw new ContextualError("Field can't be void", this.getLocation());
        }

        // verify initialization for the declared variable
        try {
            this.initialization.verifyInitialization(compiler, type, envExpSuper, memberOf);
        } catch (ContextualError e) {
            throw e;
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (this.visibility == Visibility.PROTECTED)
        {
            s.print("protected ");
        }
        s.print(this.type.getName().getName() + " " + this.fieldName.getName().getName());
        s.print(initialization.decompile());
        s.println(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        fieldName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        fieldName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void codeGenDeclField(DecacCompiler compiler) {
        initialization.codeGenInit(compiler, Register.R0, fieldName.getVariableDefinition().getType());
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(1, Register.R1)));
    }
}
