package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

import java.util.Iterator;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl13
 * @date 01/01/2020
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        // -
        Iterator<AbstractDeclVar> iterDeclVar = this.iterator();
        while (iterDeclVar.hasNext()){
            iterDeclVar.next().decompile(s);
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // -
        Iterator<AbstractDeclVar> iterDeclVar = this.iterator();
        while (iterDeclVar.hasNext()){
            iterDeclVar.next().verifyDeclVar(compiler, localEnv, currentClass);
        }
    }

    public void codeGenDeclVar(DecacCompiler compiler) {

        // - get the size of the list so as to save some place in the stack
        int nb_var = size();

        // - stack overflow test
        compiler.addComment("Test Stack_overflow");
        compiler.addInstruction(new TSTO(new ImmediateInteger(nb_var)));
        compiler.addInstruction(new BOV( new Label("stack_overflow")));

        // - save the place in the stack
        compiler.addInstruction(new ADDSP(nb_var));

        for (AbstractDeclVar i : getList()) {
            i.codeGenDeclVar(compiler);
        }
    }


}
