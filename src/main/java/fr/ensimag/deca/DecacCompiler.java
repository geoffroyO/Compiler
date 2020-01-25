package fr.ensimag.deca;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl13
 * @date 01/01/2020
 */
public class DecacCompiler {
	private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

	/**
	 * Portable newline character.
	 */
	private static final String nl = System.getProperty("line.separator", "\n");
	/**
	 * Manager for registers
	 */

	private RegisterManager regM;

	private LabelManager labM;

	public LabelManager getLabM() {
		return this.labM;
	}

	public RegisterManager getRegM() {
		return this.regM;
	}


	public void setContainsReturn(boolean containsReturn) {
		this.containsReturn = containsReturn;
	}
	public boolean isContainsReturn() {
		return containsReturn;
	}

	// Use to verify if a method contains a return of not
	private boolean containsReturn = false;


	public DecacCompiler(CompilerOptions compilerOptions, File source) {
		super();
		this.compilerOptions = compilerOptions;
		this.source = source;
		this.symbols = new SymbolTable();
		this.envTypes = new EnvironmentType(this.symbols);
		if (compilerOptions != null) {
			this.regM = new RegisterManager(compilerOptions.getRegistersNumber());
		}
		this.labM = new LabelManager();
	}

	/**
	 * // - Symbols table for the entier program
	 */
	private SymbolTable symbols;

	public SymbolTable getSymbols() {
		return symbols;
	}

	/**
	 * // - EnvTypes for the entire program
	 */
	private EnvironmentType envTypes;

	public EnvironmentType getEnvTypes() {
		return envTypes;
	}

	/**
	 * Source file associated with this compiler instance.
	 */
	public File getSource() {
		return source;
	}

	/**
	 * Compilation options (e.g. when to stop compilation, number of registers to
	 * use, ...).
	 */
	public CompilerOptions getCompilerOptions() {
		return compilerOptions;
	}

	/**
	 * @see fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
	 */
	public void add(AbstractLine line) {

		if (isInBloc){
			blocProgram.add(line);
		} else {
			program.add(line);
		}
	}

	/**
	 * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
	 */
	public void addComment(String comment) {
		if (isInBloc) {
			blocProgram.addComment(comment);
		} else {
			program.addComment(comment);
		}
	}

	/**
	 * @see fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
	 */
	public void addLabel(Label label) {
		if (isInBloc){
			blocProgram.addLabel(label);
		} else {
			program.addLabel(label);
		}
	}

	/**
	 * @see fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
	 */
	public void addInstruction(Instruction instruction) {
		if (isInBloc) {
			blocProgram.addInstruction(instruction);
		} else {
			program.addInstruction(instruction);
		}
	}

	/**
	 * @see fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
	 *      java.lang.String)
	 */
	public void addInstruction(Instruction instruction, String comment) {
		if (isInBloc) {
			blocProgram.addInstruction(instruction, comment);
		} else {
			program.addInstruction(instruction, comment);
		}
	}

	public void addFirst(Instruction instruction, String comment) {
		if (isInBloc) {
			blocProgram.addFirst(instruction, comment);
		} else {
			program.addFirst(instruction, comment);
		}
	}

	public void addFirst(Instruction instruction) {
		if (isInBloc) {
			blocProgram.addFirst(instruction);
		} else {
			program.addFirst(instruction);
		}
	}

	public void addFirstComment(String comment) {
		if (isInBloc) {
			blocProgram.addFirstComment(comment);
		} else {
			program.addFirstComment(comment);
		}
	}

	public void addFirstTSTO(int n) {
		if (!getCompilerOptions().isNoCheck()) {
			if (n > 0) {
				addFirst(new BOV(new Label("stack_overflow")));
				addFirst(new TSTO(new ImmediateInteger(n)));
				addFirstComment("Test Stack_overflow");
			}
		}
	}

	public void beginBloc(){
		isInBloc = true;
		regM.setSP();
		regM.setLocalVariable();
		regM.setRegisterToSave();
		blocProgram = new IMAProgram();
	}

	public void endBloc(){
		program.append(blocProgram);
		isInBloc = false;
	}

	public void delBloc() {
		isInBloc = false;
	}

	/**
	 * @see fr.ensimag.ima.pseudocode.IMAProgram#display()
	 */
	public String displayIMAProgram() {
		return program.display();
	}

	public void TSTO(int n) {
		if (!getCompilerOptions().isNoCheck()) {
			if (n > 0) {
				addComment("Test Stack_overflow");
				addInstruction(new TSTO(new ImmediateInteger(n)));
				addInstruction(new BOV(new Label("stack_overflow")));
			}		
		}
	}

	public void referenceErr(GPRegister register) {
		if (!getCompilerOptions().isNoCheck()) {		
			addInstruction(new CMP(new NullOperand(), register));
			addInstruction(new BEQ(new Label("dereferencement.null")));
		}
	}

	private final CompilerOptions compilerOptions;
	private final File source;
	/**
	 * The main program. Every instruction generated will eventually end up here.
	 */
	private final IMAProgram program = new IMAProgram();
	private IMAProgram blocProgram = new IMAProgram();
	private boolean isInBloc = false;

	/**
	 * Run the compiler (parse source file, generate code)
	 *
	 * @return true on error
	 */
	public boolean compile() {
		String sourceFile = source.getAbsolutePath();
		String destFile = sourceFile.substring(0, sourceFile.lastIndexOf('.'));
		destFile += ".ass";
		// A FAIRE: calculer le nom du fichier .ass à partir du nom du
		// A FAIRE: fichier .deca.
		PrintStream err = System.err;
		PrintStream out = System.out;
		LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
		try {
			return doCompile(sourceFile, destFile, out, err);
		} catch (LocationException e) {
			e.display(err);
			return true;
		} catch (DecacFatalError e) {
			err.println(e.getMessage());
			return true;
		} catch (StackOverflowError e) {
			LOG.debug("stack overflow", e);
			err.println("Stack overflow while compiling file " + sourceFile + ".");
			return true;
		} catch (Exception e) {
			LOG.fatal("Exception raised while compiling file " + sourceFile + ":", e);
			err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
			return true;
		} catch (AssertionError e) {
			LOG.fatal("Assertion failed while compiling file " + sourceFile + ":", e);
			err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
			return true;
		}
	}

	/**
	 * Internal function that does the job of compiling (i.e. calling lexer,
	 * verification and code generation).
	 *
	 * @param sourceName
	 *            name of the source (deca) file
	 * @param destName
	 *            name of the destination (assembly) file
	 * @param out
	 *            stream to use for standard output (output of decac -p)
	 * @param err
	 *            stream to use to display compilation errors
	 *
	 * @return true on error
	 */
	private boolean doCompile(String sourceName, String destName, PrintStream out, PrintStream err)
			throws DecacFatalError, LocationException {
		AbstractProgram prog = doLexingAndParsing(sourceName, err);

		if (prog == null) {
			LOG.info("Parsing failed");
			return true;
		}
		assert (prog.checkAllLocations());

		// - if '-p' option is used, stop after parsing stage and print the tree
		if (getCompilerOptions().getParse()) {
			System.out.print(prog.decompile());
			return false;
		}

		prog.verifyProgram(this);
		assert (prog.checkAllDecorations());

		// if '-v' option used, stop after verification
		if (getCompilerOptions().isVerification()) {
			return false;
		}

		addComment("start main program");
		prog.codeGenProgram(this);

		addComment("end main program");
		LOG.debug("Generated assembly code:" + nl + program.display());
		LOG.info("Output file assembly file is: " + destName);

		FileOutputStream fstream = null;
		try {
			fstream = new FileOutputStream(destName);
		} catch (FileNotFoundException e) {
			throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
		}

		LOG.info("Writing assembler file ...");

		program.display(new PrintStream(fstream));
		LOG.info("Compilation of " + sourceName + " successful.");
		return false;
	}

	/**
	 * Build and call the lexer and parser to build the primitive abstract syntax
	 * tree.
	 *
	 * @param sourceName
	 *            Name of the file to parse
	 * @param err
	 *            Stream to send error messages to
	 * @return the abstract syntax tree
	 * @throws DecacFatalError
	 *             When an error prevented opening the source file
	 * @throws DecacInternalError
	 *             When an inconsistency was detected in the compiler.
	 * @throws LocationException
	 *             When a compilation error (incorrect program) occurs.
	 */
	protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
			throws DecacFatalError, DecacInternalError {
		DecaLexer lex;
		try {
			lex = new DecaLexer(CharStreams.fromFileName(sourceName));
		} catch (IOException ex) {
			throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
		}
		lex.setDecacCompiler(this);
		CommonTokenStream tokens = new CommonTokenStream(lex);
		DecaParser parser = new DecaParser(tokens);
		parser.setDecacCompiler(this);
		return parser.parseProgramAndManageErrors(err);
	}

}
