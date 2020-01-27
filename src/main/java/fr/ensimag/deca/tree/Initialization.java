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

/**
 * @author gl13
 * @date 01/01/2020
 */
public class Initialization extends AbstractInitialization {

	public AbstractExpr getExpression() {
		return expression;
	}

	private AbstractExpr expression;

	public void setExpression(AbstractExpr expression) {
		Validate.notNull(expression);
		this.expression = expression;
	}

	public Initialization(AbstractExpr expression) {
		Validate.notNull(expression);
		this.expression = expression;
	}

	@Override
	protected void verifyInitialization(DecacCompiler compiler, Type t, EnvironmentExp localEnv,
			ClassDefinition currentClass) throws ContextualError {
		// - check the right side of the initialization (that means what's after =)
		this.expression = this.expression.verifyRValue(compiler, localEnv, currentClass, t);
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print(" = ");
		this.expression.decompile(s);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		expression.iter(f);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		expression.prettyPrint(s, prefix, true);
	}

	protected void codeGenInit(DecacCompiler compiler, GPRegister register, AbstractIdentifier type) {

		// - here we only evaluate the value of the expression and put it in register
		expression.codeGenExpr(compiler, register);
	}

}
