package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<AbstractDeclField> {

	@Override
	public void decompile(IndentPrintStream s) {
		for (AbstractDeclField f : this.getList()) {
			f.decompile(s);
			s.println();
		}
	}


	public void verifyListDeclField(DecacCompiler compiler, ClassDefinition memberOf) throws ContextualError {
		if (memberOf.getSuperClass() != null) {
			memberOf.setNumberOfFields(memberOf.getSuperClass().getNumberOfFields() + memberOf.getNumberOfFields());
		}
		Iterator<AbstractDeclField> iterDeclField = this.iterator();
		while (iterDeclField.hasNext()) {
			iterDeclField.next().verifyDeclField(compiler, memberOf);
		}
	}

	/**
	 *  Generate the assembly code for the list of declField
	 *
	 * @param compiler
	 */
	public void codeGenListDeclField(DecacCompiler compiler) {
		for (AbstractDeclField field : getList()) {
			field.codeGenDeclField(compiler);
		}
	}
}
