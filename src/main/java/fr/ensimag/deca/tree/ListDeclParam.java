package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Iterator;

public class ListDeclParam extends TreeList<AbstractDeclParam> {

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
    }

    public void verifyListParam(DecacCompiler compiler, ClassDefinition memberOf) throws ContextualError {
        Iterator<AbstractDeclParam> iterDeclParam = this.iterator();
        while (iterDeclParam.hasNext()){
            iterDeclParam.next().verifyParam(compiler);
        }
    }

    public void codeGenListDeclParam(DecacCompiler compiler) {
        for (AbstractDeclParam declParam : getList()) {
            declParam.codeGenDeclParam(compiler);
        }
    }
}
