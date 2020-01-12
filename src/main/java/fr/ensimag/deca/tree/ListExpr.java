package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Iterator;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl13
 * @date 01/01/2020
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        // - decompile each expr
        Iterator<AbstractExpr> itrExprs = this.iterator();
        while (itrExprs.hasNext()){
            itrExprs.next().decompile(s);
            // - print ',' between expressions
            if(itrExprs.hasNext()){
                s.print(",");
            }
        }

    }
}
