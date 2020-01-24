package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl13
 * @date 01/01/2020
 */
public abstract class AbstractLValue extends AbstractExpr {

    abstract protected void codeGenLValueAddr(DecacCompiler compiler, GPRegister register);
}
