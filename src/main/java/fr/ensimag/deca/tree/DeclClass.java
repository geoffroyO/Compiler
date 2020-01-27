package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.MethodsTable;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl13
 * @date 01/01/2020
 */
public class DeclClass extends AbstractDeclClass {

	private AbstractIdentifier className;
	private AbstractIdentifier superClass;
	private ListDeclField fields;
	private ListDeclMethod methods;

	// - constructor
	public DeclClass(AbstractIdentifier className, AbstractIdentifier superClass, ListDeclField fields,
			ListDeclMethod methods) {
		this.className = className;
		this.superClass = superClass;
		this.fields = fields;
		this.methods = methods;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("class ");
		s.print(className.getName().getName());
		s.print(" extends ");
		s.print(superClass.getName().getName());
		s.println(" {");
		s.indent();
		fields.decompile(s);
		methods.decompile(s);
		s.unindent();
		s.println("}");
	}

	@Override
	protected void verifyClass(DecacCompiler compiler) throws ContextualError {

		// - verify that super's type exists in env_types
		Type superType = this.superClass.verifyType(compiler);

		// if the super class is not a class
		if (!superType.isClass()) {
			throw new ContextualError("Contextual error : super is not a class", getLocation());
		}

		// - get the super class definition from env_types to create the definition of
		// the son
		ClassDefinition superDefinition = (ClassDefinition) compiler.getEnvTypes().get(this.superClass.getName());
		// - create the type and definition for the son
		ClassType type = new ClassType(className.getName(), this.getLocation(), superDefinition);
		ClassDefinition definition = type.getDefinition();

		// - declare the new class in env_types
		try {
			compiler.getEnvTypes().declare(this.className.getName(), definition);
		} catch (EnvironmentType.DoubleDefException d) {
			throw new ContextualError("Contextual error : Class name was already defined", this.getLocation());
		}

		// - set the type and definition for the new class
		this.className.setType(type);
		this.className.setDefinition(definition);

		// - if the extends class is empty, set the super location to Object's location
		if (superClass.getName().getName() == "Object") {
			superClass.setLocation(superDefinition.getLocation());
		}

	}

	@Override
	protected void verifyClassMembers(DecacCompiler compiler) throws ContextualError {
		ClassDefinition superClass = this.className.getClassDefinition().getSuperClass();
		// Verify the fields declaration
		this.fields.verifyListDeclField(compiler, className.getClassDefinition());
		// Verify the methods declaration
		this.methods.verifyListDeclMethod(compiler, className.getClassDefinition(), superClass);
	}

	@Override
	protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
		// - call a function in (ListDeclMethod) that create an iterator and call
		// "verifyBody" for each method in the class
		this.methods.verifyListMethodBody(compiler, this.className.getClassDefinition());
	}


	/**
	 * Generates assembly code for the first pass of the code generation for the class declaration.
	 *
	 * @param compiler
	 *
	 */
	@Override
	protected void codeGenFpDeclClass(DecacCompiler compiler) {
		// - get max index
		int maxIndex = methods.getMaxIndex();

		compiler.addComment("Code de la table des méthodes de la classe " + className.getName());

		// - save some place in the stack + 1 for object method
		compiler.getRegM().incSP(maxIndex + 1);

		// - set and address to the current class
		DAddr addrClass = new RegisterOffset(compiler.getRegM().getLB(), compiler.getRegM().getBase());
		className.getClassDefinition().setAddrClass(addrClass);

		// - fill the stack, entering the address of the super class
		DAddr addrSuperClass = superClass.getClassDefinition().getAddrClass();
		compiler.addInstruction(new LEA(addrSuperClass, Register.R0));
		compiler.addInstruction(
				new STORE(Register.R0, new RegisterOffset(compiler.getRegM().getLB(), compiler.getRegM().getBase())));
		compiler.getRegM().incLB();

		// - code for the object method
		codeGenFpDeclObjectMethod(compiler);

		MethodsTable tableMethods = className.getClassDefinition().getMT();

		// - code for the other methods
		for (int index = 2; index <= maxIndex; index++) {
			Label labelCodeMethod = tableMethods.getFromMT(index).getLabel();
			compiler.addInstruction(new LOAD(new LabelOperand(new Label("code." + labelCodeMethod)), Register.R0));
			compiler.addInstruction(new STORE(Register.R0,
					new RegisterOffset(compiler.getRegM().getLB() + index - 2, compiler.getRegM().getBase())));
		}

		// - increment LB
		compiler.getRegM().incLB(maxIndex - 1);
	}


	/**
	 * Generates assembly code for the first pass of the code generation for the declaration of the object class method
	 *
	 * @param compiler
	 *
	 */
	private void codeGenFpDeclObjectMethod(DecacCompiler compiler) {
		Label labelCodeMethod = new Label("code.Object.equals");
		compiler.addInstruction(new LOAD(new LabelOperand(labelCodeMethod), Register.R0));
		compiler.addInstruction(
				new STORE(Register.R0, new RegisterOffset(compiler.getRegM().getLB(), compiler.getRegM().getBase())));
		compiler.getRegM().incLB();
	}


	/**
	 * Generates assembly code for the second pass of the code generation for the class declaration.
	 *
	 * @param compiler
	 *
	 */
	protected void codeGenDeclClass(DecacCompiler compiler) {
		// - initialization of the constructor
		compiler.addComment("Initialisation des champs de la classe de " + className.getName());
		compiler.addLabel(new Label("init." + className.getName()));

		if (superClass.getName().toString() != "Object") {
			// - prepare the stack
			compiler.TSTO(3);
			compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
			compiler.addInstruction(new PUSH(Register.R0));

			// - jump to the constructor of the superClass
			compiler.addComment("Initialisation de " + superClass.getName());
			compiler.addInstruction(new BSR(new Label("init." + superClass.getName())));
			compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));

		}

		// - fields declaration
		fields.codeGenListDeclField(compiler);

		// - return
		compiler.addInstruction(new RTS());

		// - code of the methods
		compiler.addComment("Code des méthodes de la classe de " + className.getName());
		methods.codeGenListDeclMethod(compiler);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		this.className.prettyPrint(s, prefix, false);
		this.superClass.prettyPrint(s, prefix, false);
		this.fields.prettyPrint(s, prefix, false);
		this.methods.prettyPrint(s, prefix, false);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		this.className.iter(f);
		this.superClass.iter(f);
		this.methods.iter(f);
		this.fields.iter(f);
	}

}
