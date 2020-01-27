package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class DeclField extends AbstractDeclField {
	final private AbstractIdentifier type;
	final private AbstractIdentifier fieldName;
	final private AbstractInitialization initialization;
	final private Visibility visibility;

	public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier fieldName,
			AbstractInitialization initialization) {
		Validate.notNull(type);
		Validate.notNull(fieldName);
		Validate.notNull(initialization);
		this.visibility = visibility;
		this.type = type;
		this.fieldName = fieldName;
		this.initialization = initialization;
	}

	@Override
	protected void verifyDeclField(DecacCompiler compiler, ClassDefinition memberOf) throws ContextualError {
		Type type;
		EnvironmentExp envExpSuper = memberOf.getMembers();
		try {
			// - verify that type exists in envtypes (verifyType available in
			// Identifier.java)
			type = this.type.verifyType(compiler);

			// - create definition for the field using the type and location
			memberOf.incNumberOfFields(); // Add a new field
			FieldDefinition definition = new FieldDefinition(type, this.type.getLocation(), this.visibility, memberOf,
					memberOf.getNumberOfFields());

			this.fieldName.setDefinition(definition);

			// - add the variable to the local environment (Symbol -> definition)
			envExpSuper.declare(this.fieldName.getName(), definition);
		} catch (EnvironmentExp.DoubleDefException doubleDefinition) {
			throw new ContextualError("Field already defined in this class", this.getLocation());
		}
		if (type.isVoid()) {
			throw new ContextualError("Field can't be void", this.getLocation());
		}
		// verify initialization for the declared variable
		this.initialization.verifyInitialization(compiler, type, envExpSuper, memberOf);
	}

	@Override
	public void decompile(IndentPrintStream s) {
		if (this.visibility == Visibility.PROTECTED) {
			s.print("protected ");
		}
		s.print(this.type.getName().getName() + " " + this.fieldName.getName().getName());
		s.print(initialization.decompile());
		s.print(";");
	}

	@Override
	/**
	 * Generate the name of the node and add the visibility
	 * 
	 * @return String that contains visibility and the name DeclField
	 */
	String prettyPrintNode() {
		return ("[visibility = " + visibility.name() + "] " + this.getClass().getSimpleName());
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		type.prettyPrint(s, prefix, false);
		fieldName.prettyPrint(s, prefix, false);
		initialization.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		type.iter(f);
		fieldName.iter(f);
		initialization.iter(f);
	}

	@Override
	protected void codeGenDeclField(DecacCompiler compiler) {
		compiler.addComment("initialisation de " + fieldName.getName());
		initialization.codeGenInit(compiler, Register.R0, type);
		compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
		compiler.addInstruction(
				new STORE(Register.R0, new RegisterOffset(fieldName.getFieldDefinition().getIndex(), Register.R1)));
	}
}
