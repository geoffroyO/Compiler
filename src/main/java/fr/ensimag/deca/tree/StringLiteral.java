package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 * String literal
 *
 * @author gl13
 * @date 01/01/2020
 */
public class StringLiteral extends AbstractStringLiteral {

	@Override
	public String getValue() {
		return value;
	}

	private String value;

	public StringLiteral(String value) {
		Validate.notNull(value);
		this.value = value;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		setType(compiler.getEnvTypes().get(compiler.getSymbols().create("string")).getType());
		return getType();
	}

	@Override
	protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
		compiler.addInstruction(new WSTR(new ImmediateString(value)));
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("\"" + this.getValue() + "\"");
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// leaf node => nothing to do
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// leaf node => nothing to do
	}

	@Override
	String prettyPrintNode() {
		return "StringLiteral (" + value + ")";
	}

}
