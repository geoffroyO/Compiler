package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl13
 * @date 01/01/2020
 */
public abstract class AbstractLValue extends AbstractExpr {

	abstract protected void codeGenLValueAddr(DecacCompiler compiler, GPRegister register);
}
