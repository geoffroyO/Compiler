package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                                    ClassDefinition currentClass) throws ContextualError {

        // - declare 2 types
        Type leftOpType;
        Type rightOpType;

        // - verify and get both operands types
        try {
            leftOpType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            rightOpType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (
                ContextualError e) {
            throw e;
        }

        // - if operands are NOT numeric numbers
        if ((!leftOpType.isInt()&&!leftOpType.isFloat())||(!rightOpType.isInt()&&!rightOpType.isFloat())) {
            throw new ContextualError("Operands must be numeric numbers", getLocation());
        }
        // - if operands types are not the same
        if (!leftOpType.sameType(rightOpType)) {
            throw new ContextualError("Operands should be the same type", getLocation());
        }
        Type type = new BooleanType(compiler.getSymbols().create("boolean"));
        this.setType(type);
        return type;
    }

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
}


