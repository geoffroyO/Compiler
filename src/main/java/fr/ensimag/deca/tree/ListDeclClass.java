package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 *
 * @author gl13
 * @date 01/01/2020
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        // TODO
//        System.out.println("[ ListDeclClass -> verifyListClass should be implemented later ]");
        // LOG.debug("verify listClass: end");

        // - we verify each class declaration
//        Iterator<AbstractDeclClass> iterDeclClass = this.iterator();
//        while (iterDeclClass.hasNext()){
//            iterDeclClass.next().verifyClass(compiler);
//        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGenListDeclClass(DecacCompiler compiler){
        for (AbstractDeclClass A : getList()) {
            A.codeGenDeclClass(compiler);
        }
    }


}
