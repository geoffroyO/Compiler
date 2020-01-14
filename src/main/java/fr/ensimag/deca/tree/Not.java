package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // - verify operand is boolean
        Type opType;
        try {
            opType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e){
            throw e;
        }
        // - // - verify condition if its type is boolean
        if (!opType.isBoolean()) {
            throw new ContextualError("Unary Minus Operand should be int or float", getLocation());
        }
        this.setType(opType);
        return opType;
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
}
