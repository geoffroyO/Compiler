package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Deca Identifier
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Identifier extends AbstractIdentifier {

	@Override
	protected void checkDecoration() {
		if (getDefinition() == null) {
			throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
		}
	}

	@Override
	public Definition getDefinition() {
		return definition;
	}

	/**
	 * Like {@link #getDefinition()}, but works only if the definition is a
	 * ClassDefinition.
	 * 
	 * This method essentially performs a cast, but throws an explicit exception
	 * when the cast fails.
	 * 
	 * @throws DecacInternalError
	 *             if the definition is not a class definition.
	 */
	@Override
	public ClassDefinition getClassDefinition() {
		try {
			return (ClassDefinition) definition;
		} catch (ClassCastException e) {
			throw new DecacInternalError(
					"Identifier " + getName() + " is not a class identifier, you can't call getClassDefinition on it");
		}
	}

	/**
	 * Like {@link #getDefinition()}, but works only if the definition is a
	 * MethodDefinition.
	 * 
	 * This method essentially performs a cast, but throws an explicit exception
	 * when the cast fails.
	 * 
	 * @throws DecacInternalError
	 *             if the definition is not a method definition.
	 */
	@Override
	public MethodDefinition getMethodDefinition() {
		try {
			return (MethodDefinition) definition;
		} catch (ClassCastException e) {
			throw new DecacInternalError("Identifier " + getName()
					+ " is not a method identifier, you can't call getMethodDefinition on it");
		}
	}

	/**
	 * Like {@link #getDefinition()}, but works only if the definition is a
	 * FieldDefinition.
	 * 
	 * This method essentially performs a cast, but throws an explicit exception
	 * when the cast fails.
	 * 
	 * @throws DecacInternalError
	 *             if the definition is not a field definition.
	 */
	@Override
	public FieldDefinition getFieldDefinition() {
		try {
			return (FieldDefinition) definition;
		} catch (ClassCastException e) {
			throw new DecacInternalError(
					"Identifier " + getName() + " is not a field identifier, you can't call getFieldDefinition on it");
		}
	}

	/**
	 * Like {@link #getDefinition()}, but works only if the definition is a
	 * VariableDefinition.
	 * 
	 * This method essentially performs a cast, but throws an explicit exception
	 * when the cast fails.
	 * 
	 * @throws DecacInternalError
	 *             if the definition is not a field definition.
	 */
	@Override
	public VariableDefinition getVariableDefinition() {
		try {
			return (VariableDefinition) definition;
		} catch (ClassCastException e) {
			throw new DecacInternalError("Identifier " + getName()
					+ " is not a variable identifier, you can't call getVariableDefinition on it");
		}
	}

	/**
	 * Like {@link #getDefinition()}, but works only if the definition is a
	 * ExpDefinition.
	 * 
	 * This method essentially performs a cast, but throws an explicit exception
	 * when the cast fails.
	 * 
	 * @throws DecacInternalError
	 *             if the definition is not a field definition.
	 */
	@Override
	public ExpDefinition getExpDefinition() {
		try {
			return (ExpDefinition) definition;
		} catch (ClassCastException e) {
			throw new DecacInternalError(
					"Identifier " + getName() + " is not a Exp identifier, you can't call getExpDefinition on it");
		}
	}

	@Override
	public void setDefinition(Definition definition) {
		this.definition = definition;
	}

	@Override
	public Symbol getName() {
		return name;
	}

	private Symbol name;

	public Identifier(Symbol name) {
		Validate.notNull(name);
		this.name = name;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// - if the Identifier is not declared in env_types
		if (localEnv.get(this.getName()) == null) {
			throw new ContextualError("identifier not defined", this.getLocation());
		}

		// get and set the definition and type for the current Identifier
		this.setDefinition(localEnv.get(name));
		Type type = localEnv.get(this.getName()).getType();
		this.setType(type);
		return type;
	}

	/**
	 * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
	 * 
	 * @param compiler
	 *            contains "env_types" attribute
	 */
	@Override
	public Type verifyType(DecacCompiler compiler) throws ContextualError {

		// - Verify that the type exists in envTypes
		if (compiler.getEnvTypes().get(this.getName()) == null) {
			throw new ContextualError("Type not defined", this.getLocation());
		}
		// - set definition for Identifier
		this.setDefinition(compiler.getEnvTypes().get(this.name));

		// - set type for Identifier
		Type type = compiler.getEnvTypes().get(this.getName()).getType();
		this.setType(type);

		return type;
	}

	private Definition definition;

	@Override
	protected void iterChildren(TreeFunction f) {
		// leaf node => nothing to do
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// leaf node => nothing to do
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print(name.toString());
	}

	@Override
	String prettyPrintNode() {
		return "Identifier (" + getName() + ")";
	}

	@Override
	protected void prettyPrintType(PrintStream s, String prefix) {
		Definition d = getDefinition();
		if (d != null) {
			s.print(prefix);
			s.print("definition: ");
			s.print(d);
			s.println();
		}
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
		DAddr addr = this.getVariableDefinition().getOperand();
		compiler.addInstruction(new LOAD(addr, Register.R1));
		super.codeGenPrint(compiler, printHex);
	}


	/**
	 * Generates assembly code to get the address of and Lvalue.
	 *
	 * @param compiler
	 * @param register
	 * 		The address is stored in the register
	 */
	@Override
	protected void codeGenLValueAddr(DecacCompiler compiler, GPRegister register) {
		if (getExpDefinition().isField()) {
			compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), register));
			compiler.addInstruction(new LEA(new RegisterOffset(getFieldDefinition().getIndex(), register), register));
		} else {
			DAddr addr = getExpDefinition().getOperand();
			compiler.addInstruction(new LEA(addr, register));
		}
	}

	/**
	 * Generates assembly code to evaluate the expression.
	 *
	 * @param compiler
	 * @param register
	 * 		The result is stored in register
	 */
	@Override
	protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
		if (getExpDefinition().isField()) {
			compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), register));
			compiler.addInstruction(new LOAD(new RegisterOffset(getFieldDefinition().getIndex(), register), register));
		} else {
			DAddr addr = getExpDefinition().getOperand();
			compiler.addInstruction(new LOAD(addr, register));
		}
	}
}
