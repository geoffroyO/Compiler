package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

	@Override
	public void decompile(IndentPrintStream s) {
		for (AbstractDeclMethod m : this.getList()) {
			m.decompile(s);
			s.println();
		}
	}

	public void verifyListDeclMethod(DecacCompiler compiler, ClassDefinition current, ClassDefinition superClass)
			throws ContextualError {

		Iterator<AbstractDeclMethod> iterDeclMethod = this.iterator();
		while (iterDeclMethod.hasNext()) {
			try {
				iterDeclMethod.next().verifyDeclMethod(compiler, current, superClass);
			} catch (EnvironmentExp.DoubleDefException d) {
				throw new ContextualError("Contextual error : Method already defined in the class", this.getLocation());
			}
		}
		// - update current class number of method (add super class number of methods)
		current.setNumberOfMethods(current.getNumberOfMethods() + current.getSuperClass().getNumberOfMethods());
		// - update current class methods table
		current.getMT().superUpdateMT(superClass);

		// - if option "-mt" is selected, print methods table
		if (compiler.getCompilerOptions().getPrintMethodsTable()) {
			System.out.println("\n" + current.getMT().toString(current));
		}
	}

	protected void verifyListMethodBody(DecacCompiler compiler, ClassDefinition current) throws ContextualError {
		Iterator<AbstractDeclMethod> it = this.iterator();
		while (it.hasNext()) {
			it.next().verifyMethodBody(compiler, current);
		}
	}

	public int getMaxIndex() {
		int maxIndex = 1;
		for (AbstractDeclMethod method : getList()) {
			int index = method.getDeclMethodIndex();
			if (index > maxIndex) {
				maxIndex = index;
			}
		}
		return maxIndex;
	}

	public void codeGenListDeclMethod(DecacCompiler compiler) {
		for (AbstractDeclMethod method : getList()) {
			method.codeGenDeclMethod(compiler);
		}
	}
}
