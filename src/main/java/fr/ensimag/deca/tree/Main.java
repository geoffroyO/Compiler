package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;

/**
 * @author gl13
 * @date 01/01/2020
 */
public class Main extends AbstractMain {
	
	private static final Logger LOG = Logger.getLogger(Main.class);
	private ListDeclVar declVariables;
	private ListInst insts;

	public Main(ListDeclVar declVariables, ListInst insts) {
		Validate.notNull(declVariables);
		Validate.notNull(insts);
		this.declVariables = declVariables;
		this.insts = insts;
	}

	@Override
	protected void verifyMain(DecacCompiler compiler) throws ContextualError {
		LOG.debug("verify Main: start");
		// - create empty environment
		EnvironmentExp env = new EnvironmentExp(null);
		// - check declaration of variables
		this.declVariables.verifyListDeclVariable(compiler, env, null);

		// - check the list of instructions
		VoidType returnType = new VoidType(compiler.getSymbols().create("void"));
		this.insts.verifyListInst(compiler, env, null, returnType); // (3.4)
		LOG.debug("verify Main: end");
	}

	@Override
	protected void codeGenMain(DecacCompiler compiler) {
		compiler.addComment("Beginning of main instructions:");
		// - save the place needed in the stack
		compiler.TSTO(declVariables.size());
		compiler.addInstruction(new ADDSP(declVariables.size()));
		declVariables.codeGenDeclVar(compiler);
		// - instructions
		insts.codeGenListInst(compiler);
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.println("{");
		s.indent();
		declVariables.decompile(s);
		insts.decompile(s);
		s.unindent();
		s.println("}");
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		declVariables.iter(f);
		insts.iter(f);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		declVariables.prettyPrint(s, prefix, false);
		insts.prettyPrint(s, prefix, true);
	}
}
