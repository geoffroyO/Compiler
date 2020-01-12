package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl13
 * @date 01/01/2020
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        int v = this.size();
        int c = 1;

        for (AbstractExpr i : getList()) {
            i.decompile(s);

            if (c != v){
                // If not the last parameter, add comma
                s.print(", ");
            }

            c += 1;
        }

       // throw new UnsupportedOperationException("Not yet implemented");
    }
}
