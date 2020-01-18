package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Iterator;

public class ListDeclField extends TreeList<AbstractDeclField> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField f : this.getList()){
            f.decompile(s);
            s.println();
        }
    }

    public void verifyListField(DecacCompiler compiler, ClassDefinition memberOf) throws ContextualError {
        Iterator<AbstractDeclField> iterDeclField = this.iterator();
        while (iterDeclField.hasNext()){
            iterDeclField.next().verifyField(compiler, memberOf);
        }

    }

    public void codeGenListDeclField(DecacCompiler compiler){
        for (AbstractDeclField field : getList()){
            field.codeGenDeclField(compiler);
        }
    }
}
