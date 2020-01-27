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
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 * Full if/else if/else statement.
 *
 * @author gl13
 * @date 01/01/2020
 */
public class IfThenElse extends AbstractInst {

	private final AbstractExpr condition;
	private final ListInst thenBranch;
	private ListInst elseBranch;

	public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
		Validate.notNull(condition);
		Validate.notNull(thenBranch);
		Validate.notNull(elseBranch);
		this.condition = condition;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	@Override
	protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
			Type returnType) throws ContextualError {
		// - verify the condition and the lists of instructions
		this.condition.verifyCondition(compiler, localEnv, currentClass);
		this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
		this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
	}

	@Override
	protected void codeGenInst(DecacCompiler compiler) {
		Label debut_if = compiler.getLabM().genIfLabel();
		Label fin_if = compiler.getLabM().genEndIfLabel();
		compiler.addLabel(debut_if);

		if (!elseBranch.getList().isEmpty()) {
			Label debut_else = compiler.getLabM().genElseLabel();
			Label fin_else = compiler.getLabM().genEndElseLabel();
			if (compiler.getRegM().hasFreeGPRegister()) {
				GPRegister register = compiler.getRegM().findFreeGPRegister();
				condition.codeGenExpr(compiler, register);
				compiler.addInstruction(new CMP(new ImmediateInteger(1), register));
				compiler.addInstruction(new BNE(debut_else));
				compiler.getRegM().freeRegister(register);
			} else {
				GPRegister register = Register.getR(compiler.getRegM().getNb_registers());
				compiler.addInstruction(new PUSH(register));
				condition.codeGenExpr(compiler, register);
				compiler.addInstruction(new CMP(new ImmediateInteger(1), register));
				compiler.addInstruction(new POP(register));
				compiler.addInstruction(new BNE(debut_else));
			}
			thenBranch.codeGenListInst(compiler);
			compiler.addInstruction(new BRA(fin_else));
			compiler.addLabel(fin_if);
			compiler.addLabel(debut_else);
			elseBranch.codeGenListInst(compiler);
			compiler.addLabel(fin_else);

		} else {
			if (compiler.getRegM().hasFreeGPRegister()) {
				GPRegister register = compiler.getRegM().findFreeGPRegister();
				condition.codeGenExpr(compiler, register);
				compiler.addInstruction(new CMP(new ImmediateInteger(1), register));
				compiler.addInstruction(new BNE(fin_if));
				compiler.getRegM().freeRegister(register);
			} else {
				GPRegister register = Register.getR(compiler.getRegM().getNb_registers());
				compiler.addInstruction(new PUSH(register));
				condition.codeGenExpr(compiler, register);
				compiler.addInstruction(new CMP(new ImmediateInteger(1), register));
				compiler.addInstruction(new POP(register));
				compiler.addInstruction(new BNE(fin_if));
			}
			thenBranch.codeGenListInst(compiler);
			compiler.addLabel(fin_if);
		}
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("if(");
		this.condition.decompile(s);
		s.println("){");
		s.indent();
		this.thenBranch.decompile(s);
		s.unindent();
		s.println("} else {");
		s.indent();
		this.elseBranch.decompile(s);
		s.unindent();
		s.print("}");
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		condition.iter(f);
		thenBranch.iter(f);
		elseBranch.iter(f);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		condition.prettyPrint(s, prefix, false);
		thenBranch.prettyPrint(s, prefix, false);
		elseBranch.prettyPrint(s, prefix, true);
	}
}
