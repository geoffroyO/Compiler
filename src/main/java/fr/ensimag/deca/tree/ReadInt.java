package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RINT;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class ReadInt extends AbstractReadExpr {

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		Type type = new IntType(compiler.getSymbols().create("int"));
		this.setType(type);
		return type;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("readInt()");
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// leaf node => nothing to do
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// leaf node => nothing to do
	}

	protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
		compiler.addInstruction(new RINT());

		compiler.addInstruction(new BOV(new Label("invalid_input")));
		compiler.addInstruction(new LOAD(Register.R1, register));

	}
}
