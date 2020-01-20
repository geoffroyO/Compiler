package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclMethod m: this.getList())
        {
            m.decompile(s);
            s.println();
        }
    }

    public void codeGenListFpDeclMethod(DecacCompiler compiler){
        for (AbstractDeclMethod method : getList()){
            method.codeGenFpDeclMethod(compiler);
        }
    }

    public void codeGenListDeclMethod(DecacCompiler compiler){
        for (AbstractDeclMethod method : getList()){
            method.codeGenDeclMethod(compiler);
        }
    }
}
