package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class This extends AbstractLValue {

	public This() {
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		if (currentClass == null) {
			throw new ContextualError("Contextual error : 'this' must be used inside a class", this.getLocation());
		}
		Type type = currentClass.getType();
		this.setType(type);
		return this.getType();
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("this");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
	}

	@Override
	protected void iterChildren(TreeFunction f) {

	}

	/**
	 * Generates assembly code to get the address of the Lvalue.
	 *
	 * @param compiler
	 * @param register
	 * 		Register to store the address of the Lvalue
	 */
	@Override
	protected void codeGenLValueAddr(DecacCompiler compiler, GPRegister register) {
		compiler.addInstruction(new LEA(new RegisterOffset(-2, Register.LB), register));
	}

	/**
	 * Generates assembly code to evaluate the expression.
	 *
	 * @param compiler
	 * @param register
	 * 		Register to store the result of the operation
	 */
	@Override
	public void codeGenExpr(DecacCompiler compiler, GPRegister register) {
		compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), register));
	}
}
