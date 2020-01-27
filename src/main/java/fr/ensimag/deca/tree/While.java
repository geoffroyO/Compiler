package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class While extends AbstractInst {
	private AbstractExpr condition;
	private ListInst body;

	public AbstractExpr getCondition() {
		return condition;
	}

	public ListInst getBody() {
		return body;
	}

	public While(AbstractExpr condition, ListInst body) {
		Validate.notNull(condition);
		Validate.notNull(body);
		this.condition = condition;
		this.body = body;
	}

	@Override
	protected void codeGenInst(DecacCompiler compiler) {
		Label debut_while = compiler.getLabM().genWhileLabel();
		Label fin_while = compiler.getLabM().genEndWhileLabel();
		compiler.addLabel(debut_while);
		if (compiler.getRegM().hasFreeGPRegister()) {
			GPRegister register = compiler.getRegM().findFreeGPRegister();
			condition.codeGenExpr(compiler, register);
			compiler.addInstruction(new CMP(new ImmediateInteger(0), register));
			compiler.addInstruction(new BEQ(fin_while));
		} else {
			GPRegister register = Register.getR(compiler.getRegM().getNb_registers());
			compiler.addInstruction(new PUSH(register));
			condition.codeGenExpr(compiler, register);
			compiler.addInstruction(new CMP(new ImmediateInteger(0), register));
			compiler.addInstruction(new BEQ(fin_while));
			compiler.addInstruction(new POP(register));
		}
		body.codeGenListInst(compiler);
		compiler.addInstruction(new BRA(debut_while));
		compiler.addLabel(fin_while);
	}

	@Override
	protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
			Type returnType) throws ContextualError {
		// - verify the condition and the lists of instructions
		this.condition.verifyCondition(compiler, localEnv, currentClass);
		this.body.verifyListInst(compiler, localEnv, currentClass, returnType);
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("while (");
		getCondition().decompile(s);
		s.println(") {");
		s.indent();
		getBody().decompile(s);
		s.unindent();
		s.print("}");
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		condition.iter(f);
		body.iter(f);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		condition.prettyPrint(s, prefix, false);
		body.prettyPrint(s, prefix, true);
	}

}
