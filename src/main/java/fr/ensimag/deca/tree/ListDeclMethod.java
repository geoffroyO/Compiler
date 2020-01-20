package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Iterator;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclMethod m: this.getList())
        {
            m.decompile(s);
            s.println();
        }
    }

    public void verifyListMethod(DecacCompiler compiler, ClassDefinition memberOf) throws ContextualError {
        Iterator<AbstractDeclMethod> iterDeclMethod = this.iterator();
        while (iterDeclMethod.hasNext()){
            iterDeclMethod.next().verifyDeclMethod(compiler, memberOf);
        }

    }

    public int getMaxIndex() {
        int maxIndex = 0;
        for (AbstractDeclMethod method : getList()) {
            int index = method.getDeclMethodIndex();
            if (index > maxIndex) { maxIndex = index; }
        }
        return maxIndex;
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
