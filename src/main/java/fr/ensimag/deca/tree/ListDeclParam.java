package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclParam extends TreeList<AbstractDeclParam> {

	@Override
	public void decompile(IndentPrintStream s) {
		int c = 0;
		int size = this.getList().size();

		for (AbstractDeclParam p : this.getList()) {
			p.decompile(s);
			if (c != size - 1) {
				s.print(", ");
			}
			c += 1;
		}
	}

	public Signature verifyListDeclParam(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
		Signature signature = new Signature();
		Iterator<AbstractDeclParam> iterDeclParam = this.iterator();
		while (iterDeclParam.hasNext()) {
			Type paramType = iterDeclParam.next().verifyDeclParam(compiler, localEnv);
			signature.add(paramType);
		}
		return signature;
	}

	public void codeGenListDeclParam(DecacCompiler compiler) {
		int indexLb = -3;
		for (AbstractDeclParam declParam : getList()) {
			declParam.codeGenDeclParam(compiler, indexLb);
			indexLb--;
		}
	}
}
