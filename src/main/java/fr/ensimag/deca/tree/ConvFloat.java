package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl13
 * @date 01/01/2020
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        // - create or get the float type
        Type type = new FloatType((compiler.getSymbols().create("float")));
        // - set this terminal's type to float
        setType(type);
        return type;
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
