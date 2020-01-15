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

        } else if (leftOpType.isFloat() && rightOpType.isInt()){
            // - convert right operand to float
            this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftOpType));

            // - if left operand is int and right operand is float
        } else if (leftOpType.isInt() && rightOpType.isFloat()) {
            // - convert left operand to float
            this.setLeftOperand(this.getLeftOperand().verifyRValue(compiler, localEnv, currentClass, rightOpType));

        } else {
            // no need to throw an error since we are sure that both operands
            // are the same type.
        }

        Type type = new BooleanType(compiler.getSymbols().create("boolean"));
        this.setType(type);
        return type;
    }

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
}


