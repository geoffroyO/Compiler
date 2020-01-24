package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
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

    private void codeGenFPDeclObjectClass(DecacCompiler compiler) {
        ClassDefinition object = (ClassDefinition) compiler.getEnvTypes().get(compiler.getSymbols().create("Object"));
        object.setAddrClass(new RegisterOffset(1, Register.LB));
        compiler.addComment("Code de la table des méthodes de la classe Object");
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getRegM().getGB(), compiler.getRegM().getBase())));
        compiler.getRegM().incrGB();
        compiler.addInstruction(new LOAD(new LabelOperand(new Label("code.Object.equals")), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getRegM().getGB(), Register.LB)));
        compiler.getRegM().incrGB();
        compiler.getRegM().incrSP(2);

    }

    private void codeGenDeclObjectMethod(DecacCompiler compiler) {
        compiler.addComment("Initialisation des champs de la classe de " + "Object");
        compiler.addLabel(new Label("init." + "Object"));
        compiler.addInstruction(new RTS());

        compiler.addComment("Code des méthodes de la classe de " + "Object");
        compiler.addLabel(new Label("code.Object.equals"));
        compiler.addInstruction(new LOAD(new RegisterOffset(-1, Register.LB), Register.R0));
        compiler.addInstruction(new CMP(new RegisterOffset(-2, Register.LB), Register.R0));
        compiler.addInstruction(new BEQ(new Label("True_case")));
        compiler.addInstruction(new BRA(new Label("False_case")));
        compiler.addLabel(new Label("True_case"));
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.R0));
        compiler.addInstruction(new BRA(new Label("return")));
        compiler.addLabel(new Label("False_case"));
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        compiler.addLabel(new Label("return"));
        compiler.addInstruction(new RTS());

    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
 
        compiler.addComment("Class program");

        // - begin at 1(GB)
        compiler.getRegM().incrGB();

        // - begin bloc
        compiler.beginBloc();

        // - declaration of objectClass

        codeGenFPDeclObjectClass(compiler);

        // - first pass for class declaration
        classes.codeGenListFpDeclClass(compiler);

        // - save some place in the stack at the begining of the bloc
        int numberToSave = compiler.getRegM().getSP();
        compiler.addFirst(new ADDSP(new ImmediateInteger(numberToSave)));
        compiler.addFirstTSTO(numberToSave);

        // - end bloc
        compiler.endBloc();


        // - second pass
        compiler.addComment("Début seconde passe");

        compiler.addComment("Main program");
        main.codeGenMain(compiler);

        // - end program
        compiler.addInstruction(new HALT());

        // - write the code for to exit the program with an error
        codeGenErrors(compiler);

        // - write the code for Object.equals
        codeGenDeclObjectMethod(compiler);

        // - write the code for the others methods
        classes.codeGenListDeclClass(compiler);
    }

    private void codeGenOutErrors(DecacCompiler compiler){
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }
    private void codeGenErrors(DecacCompiler compiler) {
        compiler.addLabel(new Label("dereferencement.null"));
        compiler.addInstruction(new WSTR(new ImmediateString("Error: dereferencement.null")));
        codeGenOutErrors(compiler);

        compiler.addLabel(new Label("Float_overflow"));
        compiler.addInstruction(new WSTR(new ImmediateString("Error: Float_overflow")));
        codeGenOutErrors(compiler);

        compiler.addLabel(new Label("stack_overflow"));
        compiler.addInstruction(new WSTR(new ImmediateString("Error: stack_overflowed")));
        codeGenOutErrors(compiler);

        compiler.addLabel(new Label("Zero_division"));
        compiler.addInstruction(new WSTR(new ImmediateString("Error: Zero_division ")));
        codeGenOutErrors(compiler);

        compiler.addLabel(new Label("heap_overflow"));
        compiler.addInstruction(new WSTR(new ImmediateString("Error: heap_overflow ")));
        codeGenOutErrors(compiler);
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
