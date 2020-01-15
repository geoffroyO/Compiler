package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

/**
 * @author gl13
 * @date 01/01/2020
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        Type type;
        try {
            // - verify that type exists in envtypes (verifyType available in Identifier.java)
            type = this.type.verifyType(compiler);
            // - create definition for the variable using the type and location
            VariableDefinition definition = new VariableDefinition(type, this.type.getLocation());
            this.varName.setDefinition(definition);
            // - add the variable to the local environment (Symbol -> definition)
            localEnv.declare(this.varName.getName(), definition);

        } catch (ContextualError e) {
            throw e;
        } catch (EnvironmentExp.DoubleDefException doubleDefinition) {
            throw new ContextualError("variable already defined", this.getLocation());
        }

        if (type.isVoid()) {
            throw new ContextualError("variable can't be void", this.getLocation());
        }

        // verify initialization for the declared variable
        try {
            this.initialization.verifyInitialization(compiler, type, localEnv, currentClass);
        } catch (ContextualError e) {
            throw e;
        }

    }


    @Override
    public void decompile(IndentPrintStream s) {
        // -
        s.print(this.type.getName().getName() + " " + this.varName.getName().getName());
        s.print(initialization.decompile());
        s.println(";");


    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    public void codeGenDeclVar(DecacCompiler compiler) {
        GPRegister register = compiler.getRegM().findFreeGPRegister();
        this.initialization.codeGenInit(compiler, register, varName.getVariableDefinition().getType());


        compiler.getRegM().incrGB();
        this.varName.getVariableDefinition().setOperand(new RegisterOffset(compiler.getRegM().getGB(), Register.GB));
        this.initialization.codeGenStInit(compiler, register);
        compiler.getRegM().freeRegister(register);
    }
}
