package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
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


    public void verifyListDeclMethod(DecacCompiler compiler, ClassDefinition current, ClassDefinition superClass)
            throws ContextualError {
        Iterator<AbstractDeclMethod> iterDeclMethod = this.iterator();
        while (iterDeclMethod.hasNext()){
                try {
                    iterDeclMethod.next().verifyDeclMethod(compiler, current, superClass);
                } catch (EnvironmentExp.DoubleDefException d) {
                    throw new ContextualError("Method already defined in the class", this.getLocation());
                }
        }
        current.getMT().superUpdateMT(superClass);

        /** DON"T DELETE THIS
         *  it's just for testing, We will remove it later
            System.out.println("################");
            System.out.println(current.getMT());
            System.out.println("################");
         */
    }

    protected void verifyListMethodBody(DecacCompiler compiler, ClassDefinition current) throws ContextualError{
        Iterator<AbstractDeclMethod> it = this.iterator();
        while (it.hasNext()) {
            try {
                it.next().verifyMethodBody(compiler, current);
            } catch (ContextualError e) {
                throw e;
            }
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
