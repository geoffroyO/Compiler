package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;

public class Return extends AbstractInst {

	private AbstractExpr expr;

	public AbstractExpr getExpr() {
		return expr;
	}

	public Return(AbstractExpr expr) {
		Validate.notNull(expr);
		this.expr = expr;
	}

	@Override
	protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
			Type returnType) throws ContextualError {
		// - verify if return is void
		if (returnType.isVoid()) {
			throw new ContextualError("Contextual error : Return type can't be \"void\" (3.24)", getLocation());
		}
		Type type = this.expr.verifyExpr(compiler, localEnv, currentClass);
		if (!type.sameType(returnType)) {
			throw new ContextualError("Contextual error : Current return type doesn't match original function type",
					this.expr.getLocation());
		}
		compiler.setContainsReturn(true);
	}

	@Override
	protected void codeGenInst(DecacCompiler compiler) {
		expr.codeGenExpr(compiler, Register.R0); // on retourne le résultat dans R0
		compiler.addInstruction(new BRA(compiler.getLabM().getEndCurrentLabel()));
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// -
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("return ");
		expr.decompile(s);
		s.print(";");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		expr.prettyPrint(s, prefix, true);
	}
}
