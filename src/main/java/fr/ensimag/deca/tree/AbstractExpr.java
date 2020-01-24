package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl13
 * @date 01/01/2020
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {

        // - verify Right value
        Type obtainedType;
        try {
            obtainedType = this.verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e){
            throw e;
        }

        // TODO factorisation "assign compatible"

        // - verify expectedType and obtainedType values types are the same (verify when they are classes)
        if (obtainedType.isClass()) {
            if (!expectedType.isClass()) {
                throw new ContextualError("Types incompatible, expecting a class type on the left side",this.getLocation());
            } else {
                ClassType currentType = obtainedType.asClassType("error TO DO", getLocation());
                // - check if the obtainedType is a child for the expectedType
                if (!currentType.isSubClassOf(expectedType.asClassType("error TO DO", getLocation()))) {
                    throw new ContextualError("incompatible class types",this.getLocation());
                }
            }

        // - if obtainedType is not the same as expectedType
        } else if (!obtainedType.sameType(expectedType)){
            // - if expectedType is float and obtainedType is int
            if (expectedType.isFloat() && obtainedType.isInt()){
                // - create conversion to float
                ConvFloat convToFloat = new ConvFloat(this);
                convToFloat.verifyExpr(compiler, localEnv, currentClass);
                return convToFloat;
            } else {
                throw new ContextualError("Types don't match! ", this.getLocation());
            }
        }

        // - return this by default
        return this;
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {

        // - verify expr contextual syntax
        this.verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // - verify condition
        Type condType;
        try {
            condType = this.verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e){
            throw e;
        }
        // - // - verify condition if its type is boolean
        if (!condType.isBoolean() ) {
            throw new ContextualError("Condition should be a boolean", getLocation());
        }
    }

    /**
     * Check if assign_compatible(env, T1, T2)
     * @param env
     * @param T1
     * @param T2
     * @return
     */
    public boolean assignCompatible(EnvironmentType env, Type T1, Type T2) throws ContextualError
    {
        if (T1.sameType(T2))
        {
            return(true);
        }

        if (T1.isFloat() && T2.isInt())
        {
            // Case 1
            return(true);
        }

        // Case we cast Objects

        if (!(T1.isClass() && T2.isClass())){
            // We don't have 2 objects
            return(false);
        }

        return( env.get(T2.getName()).getType().asClassType("Error to use T2 as classType", getLocation()).isSubClassOf(env.get(T1.getName()).getType().asClassType("Error to use T1 as classType", getLocation())) );
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
        // - distinction of type
        if (getType().isInt()){
            if (printHex) {
                compiler.addInstruction(new FLOAT(Register.R1, Register.R1));
                compiler.addInstruction(new WFLOATX());
            } else {
                compiler.addInstruction(new WINT());
            }
        }
        if (getType().isFloat()){
            if (printHex) {
                compiler.addInstruction(new WFLOATX());
            } else {
                compiler.addInstruction(new WFLOAT());
            }
        }
    }

    protected void codeGenInst(DecacCompiler compiler) {
    }

    protected void codeGenExpr(DecacCompiler compiler, GPRegister register){
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
}
