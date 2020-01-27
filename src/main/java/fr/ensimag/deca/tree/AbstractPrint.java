package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.Iterator;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Print statement (print, println, ...).
 *
 * @author gl13
 * @date 01/01/2020
 */
public abstract class AbstractPrint extends AbstractInst {

	private boolean printHex;
	private ListExpr arguments = new ListExpr();

	abstract String getSuffix();

	public AbstractPrint(boolean printHex, ListExpr arguments) {
		Validate.notNull(arguments);
		this.arguments = arguments;
		this.printHex = printHex;
	}

	public ListExpr getArguments() {
		return arguments;
	}

	@Override
	protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
			Type returnType) throws ContextualError {

		// - create iterator for the list of exps 'arguments'
		Iterator<AbstractExpr> itrExpr = this.arguments.iterator();

		Type type;
		while (itrExpr.hasNext()) {
		
			// - verify each expr in println(expr expr*)
			type = itrExpr.next().verifyExpr(compiler, localEnv, currentClass);

			// - (3.31)
			// - verify (type) variable : should be only int, float or string
			if (!type.isInt() && !type.isFloat() && !type.isString()) {
				throw new ContextualError("Contextual error : only integers, floats and strings can be printed", this.getLocation());
			}
		}
	}

	@Override
	protected void codeGenInst(DecacCompiler compiler) {
		for (AbstractExpr a : getArguments().getList()) {
			a.codeGenPrint(compiler, printHex);
		}
	}

	private boolean getPrintHex() {
		return printHex;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// -
		s.print("print" + getSuffix());
		if (this.printHex) {
			s.print("x");
		}
		s.print("(");
		s.indent();
		this.arguments.decompile(s);
		s.unindent();
		s.println(");");
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		arguments.iter(f);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		arguments.prettyPrint(s, prefix, true);
	}

}
