package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

	public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
		super(leftOperand, rightOperand);
	}

	@Override
	public abstract Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError;
}
