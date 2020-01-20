package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl13
 * @date 01/01/2020
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
//        ClassDefinition test = (ClassDefinition)compiler.getEnvTypes().get(compiler.getSymbols().create("Object"));
//        System.out.println("nb of methods");
//        System.out.println(test.getNumberOfMethods());

        LOG.debug("verify program: start");
        try {

            // - Verify the list of classes in the program
            this.classes.verifyListClass(compiler);
            // - Verify the main section in the program
            this.main.verifyMain(compiler);

            // - below comment is just for testing
            /*
            Map<String, Symbol> map = compiler.getSymbols().getMap();
            for (Map.Entry<String, Symbol> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            */

        } catch (ContextualError e) {
            throw e;
        }
         LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        compiler.addComment("Class program");
        compiler.getRegM().incrGB(); //On commence à 1(GB)
        compiler.addComment("Table des méthodes");
        classes.codeGenListFpDeclClass(compiler);
        compiler.addComment("Début seconde passe");
        classes.codeGenListDeclClass(compiler);
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
