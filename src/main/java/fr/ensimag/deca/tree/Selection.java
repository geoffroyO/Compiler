package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Selection extends AbstractLValue {
	private AbstractExpr instance;
	private AbstractIdentifier field;

	public Selection(AbstractExpr instance, AbstractIdentifier field) {
		this.instance = instance;
		this.field = field;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		ClassType instanceType = instance.verifyExpr(compiler, localEnv, currentClass)
				.asClassType("Contextual error : The left member of 'instance.field' must be the instance of a class", getLocation());
		EnvironmentExp instanceEnv = instanceType.getDefinition().getMembers();
		Type fieldType = field.verifyExpr(compiler, instanceEnv, currentClass);
		if (field.getFieldDefinition().getVisibility() == Visibility.PROTECTED) {
			if (currentClass == null) {
				throw new ContextualError("Contextual error : It is not allowed to access a protected field outside a class",
						getLocation());
			}
			if (!currentClass.getType().isSubClassOf(field.getFieldDefinition().getContainingClass().getType())) {
				throw new ContextualError("Contextual error(3.66) with an expression of type 'instance.field' \n"
						+ "'field is protected and the current class (where instance.field is called) is not a subClass of the class where 'field' is declared",
						this.getLocation());
			}
			if (!(instanceType).isSubClassOf(currentClass.getType())) {
				throw new ContextualError("Contextual error(3.66) with an expression of type 'instance.field' \n"
						+ "'field' is protected and the class of 'instance' is not a subClass of the current class (where instance.field is called)",
						this.getLocation());
			}
		}
		this.setType(fieldType);
		return (this.getType());
	}

	@Override
	public void decompile(IndentPrintStream s) {
		instance.decompile(s);
		s.print(".");
		field.decompile(s);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		instance.prettyPrint(s, prefix, false);
		field.prettyPrint(s, prefix, true);
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
		// - evaluate the expression
		instance.codeGenExpr(compiler, register);
		// - check if object is null
		compiler.referenceErr(register);
		// - return the address in register
		compiler.addInstruction(new LEA(new RegisterOffset(field.getFieldDefinition().getIndex(), register), register));
	}


	/**
	 * Generates assembly code to evaluate the expression.
	 *
	 * @param compiler
	 * @param register
	 * 		Register to store the result of the operation
	 */
	protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
		// - evaluate the expression
		instance.codeGenExpr(compiler, register);
		// - check if object is null
		compiler.referenceErr(register);
		// - load the value in register
		compiler.addInstruction(
				new LOAD(new RegisterOffset(field.getFieldDefinition().getIndex(), register), register));
	}

	/**
	 * Generates assembly code to evaluate and print the expression.
	 *
	 * @param compiler
	 * @param printHex
	 * 		Boolean that considers if the user wants a printx or a simple print(ln)
	 */
	@Override
	protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
		GPRegister addrReg = compiler.getRegM().findFreeGPRegister();
		instance.codeGenExpr(compiler, addrReg);
		// - check if object is null
		compiler.referenceErr(addrReg);
		compiler.addInstruction(
				new LOAD(new RegisterOffset(field.getFieldDefinition().getIndex(), addrReg), Register.R1));
		super.codeGenPrint(compiler, printHex);
		compiler.getRegM().freeRegister(addrReg);
	}
}
