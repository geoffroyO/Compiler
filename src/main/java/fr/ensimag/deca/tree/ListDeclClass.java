package fr.ensimag.deca.tree;

import java.util.Iterator;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

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
		// - we verify each class declaration
		Iterator<AbstractDeclClass> iterDeclClass = this.iterator();
		while (iterDeclClass.hasNext()) {
			iterDeclClass.next().verifyClass(compiler);
		}
		verifyListClassMembers(compiler);
		LOG.debug("verify listClass: end");
	}

	/**
	 * Pass 2 of [SyntaxeContextuelle]
	 */
	public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
		LOG.debug("verify listClassMembers: start");
		// - we verify each class members (fields and methods)
		Iterator<AbstractDeclClass> iterDeclClass = this.iterator();
		while (iterDeclClass.hasNext()) {
			iterDeclClass.next().verifyClassMembers(compiler);
		}
		verifyListClassBody(compiler);
		LOG.debug("verify listClassMembers: end");
	}

	/**
	 * Pass 3 of [SyntaxeContextuelle]
	 */
	public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
		LOG.debug("verify verifyListClassBody: start");
		// - we verify each class methods body
		Iterator<AbstractDeclClass> iterDeclClass = this.iterator();
		while (iterDeclClass.hasNext()) {
			iterDeclClass.next().verifyClassBody(compiler);
		}
		LOG.debug("verify verifyListClassBody: end");
	}

	public void codeGenListFpDeclClass(DecacCompiler compiler) {
		for (AbstractDeclClass A : getList()) {
			A.codeGenFpDeclClass(compiler);
		}
	}

	public void codeGenListDeclClass(DecacCompiler compiler) {
		for (AbstractDeclClass A : getList()) {
			A.codeGenDeclClass(compiler);
		}
	}

}
