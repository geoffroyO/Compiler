package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl13
 * @date 01/01/2020
 */
public class ListExpr extends TreeList<AbstractExpr> {

	@Override
	public void decompile(IndentPrintStream s) {
		// - decompile each expr
		Iterator<AbstractExpr> itrExprs = this.iterator();
		while (itrExprs.hasNext()) {
			itrExprs.next().decompile(s);
			// - print ',' between expressions
			if (itrExprs.hasNext()) {
				s.print(", ");
			}
		}

	}
}
