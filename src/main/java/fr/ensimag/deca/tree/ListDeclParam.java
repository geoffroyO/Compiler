package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Iterator;

public class ListDeclParam extends TreeList<AbstractDeclParam> {

    @Override
    public void decompile(IndentPrintStream s) {
        int c = 0;
        int size = this.getList().size();

        for (AbstractDeclParam p : this.getList()) {
            p.decompile(s);
            if (c != size -1){
                s.print(", ");
            }
            c += 1;
        }
    }

    public Signature verifyListDeclParam(DecacCompiler compiler, EnvironmentExp localEnv)
            throws ContextualError {
        Signature signature = new Signature();
        Iterator<AbstractDeclParam> iterDeclParam = this.iterator();
        while (iterDeclParam.hasNext()){
            try {
                Type paramType = iterDeclParam.next().verifyDeclParam(compiler, localEnv);
                signature.add(paramType);
            } catch (ContextualError e) {
                throw e;
            }
        }
        return signature;
    }

    public void codeGenListDeclParam(DecacCompiler compiler) {
        for (AbstractDeclParam declParam : getList()) {
            declParam.codeGenDeclParam(compiler);
        }
    }
}
