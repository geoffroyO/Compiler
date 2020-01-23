package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.io.PrintStream;

public class DeclParam extends AbstractDeclParam {

    private AbstractIdentifier type;
    private AbstractIdentifier paramName;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier paramName) {
        this.type = type;
        this.paramName = paramName;
    }

    @Override
    protected Type verifyDeclParam(DecacCompiler compiler, EnvironmentExp localEnv)
            throws ContextualError {

        Type paramType;
        try {
            paramType = this.type.verifyType(compiler);

            if (paramType.isVoid()){
                throw new ContextualError("parameter can't be (void)", getLocation());
            }

            ParamDefinition paramDefinition = new ParamDefinition(paramType, this.getLocation());

            // - declare parameter to local environment
            localEnv.declare(this.paramName.getName(), paramDefinition);

            // - set type and definition
            this.paramName.setType(paramType);
            this.paramName.setDefinition(paramDefinition);

        } catch (EnvironmentExp.DoubleDefException d) {
            throw new ContextualError("parameter already defined",this.paramName.getLocation());
        }

        return paramType;

    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(type.getName().getName() + " " + paramName.getName().getName());
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.type.prettyPrint(s, prefix, false);
        this.paramName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
        this.paramName.iter(f);
    }

    @Override
    protected void codeGenDeclParam(DecacCompiler compiler, int indexLb) {
        // - we set the addr of the parameter in the definition of the expression
        paramName.getExpDefinition().setOperand(new RegisterOffset(indexLb, Register.LB));
    }
}
