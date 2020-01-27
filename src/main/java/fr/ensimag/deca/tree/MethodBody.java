package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodBody extends AbstractMethodBody {

	final private ListDeclVar ListDeclVar;
	final private ListInst ListInst;

	public MethodBody(ListDeclVar listDeclVar, ListInst listInst) {
		this.ListDeclVar = listDeclVar;
		this.ListInst = listInst;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		ListDeclVar.decompile(s);
		ListInst.decompile(s);
	}

	@Override
	protected void verifyBody(DecacCompiler compiler, ClassDefinition current, EnvironmentExp localEnv, Type returnType)
			throws ContextualError {
		compiler.setContainsReturn(false);
		this.ListDeclVar.verifyListDeclVariable(compiler, localEnv, current);
		this.ListInst.verifyListInst(compiler, localEnv, current, returnType);

		if (!compiler.isContainsReturn() && !returnType.isVoid()) {
			// Return expected by nothing give
			throw new ContextualError("Contextual error : Return value is expected is this method", this.getLocation());
		}
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		this.ListDeclVar.prettyPrint(s, prefix, false);
		this.ListInst.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		this.ListDeclVar.iter(f);
		this.ListInst.iter(f);
	}

	@Override
	protected void codeGenMethodBody(DecacCompiler compiler) {

		// - declaration of the local variable
		ListDeclVar.codeGenDeclVar(compiler);

		// - this is the number of variable that we will push at the top of the stack
		compiler.getRegM().incLocalVariable(ListDeclVar.size());

		ListInst.codeGenListInst(compiler);

	}
}
