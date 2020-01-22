package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import org.apache.commons.lang.Validate;
import org.mockito.exceptions.misusing.CannotVerifyStubOnlyMock;

import java.io.PrintStream;

public class Return extends AbstractInst {

    private AbstractExpr expr;

    // - get expr
    public AbstractExpr getExpr() {
        return expr;
    }

    // - Constructor
    public Return(AbstractExpr expr) {
        Validate.notNull(expr);
        this.expr = expr;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {

        // - verify if return is void
        if(returnType.isVoid()){
            throw new ContextualError("Return type can't be \"void\" (3.24)", getLocation());
        }

        Type type;
        try {
            this.expr = this.expr.verifyRValue(compiler, localEnv, currentClass, returnType);
            type = this.expr.getType();
        } catch (ContextualError e){
            throw e;
        }

        if (!type.sameType(returnType)){
            throw new ContextualError("Current return type doesn't match original function type", this.expr.getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        expr.codeGenExpr(compiler, Register.R0); // on retourne le résultat dans R0
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
        expr.prettyPrint(s, prefix,true);
    }
}
