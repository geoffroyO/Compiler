package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Null extends AbstractExpr {

	public Null() {
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		Type type = new NullType(compiler.getSymbols().create("null"));
		this.setType(type);
		return getType();
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("null");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// -
		s.print(prefix);
		s.println();
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// leaf node
	}

}
