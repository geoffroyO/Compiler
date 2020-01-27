package fr.ensimag.deca.context;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.DeclClass;
import fr.ensimag.deca.tree.ListDeclField;
import fr.ensimag.deca.tree.ListDeclMethod;

/**
 * Test for the DeclClass node using mockito, using @Mock and @Before annotations.
 *
 * @author Ensimag
 * @date 01/01/2020
 */
public class TestDeclClass {

	@Mock
	AbstractIdentifier className;

	@Mock
	AbstractIdentifier superClass;

	@Mock
	Symbol testSymbol;

	@Mock
	ListDeclField fields;

	@Mock
	ListDeclMethod methods;

	DecacCompiler compiler;

	Type superType;

	public class DeclClassVisible extends DeclClass {

		public DeclClassVisible(AbstractIdentifier className, AbstractIdentifier superClass, ListDeclField fields,
				ListDeclMethod methods) {
			super(className, superClass, fields, methods);
		}

		@Override
		public void verifyClass(DecacCompiler compiler) throws ContextualError {
			super.verifyClass(compiler);
		}
	}

	@Before
	public void setup() throws ContextualError {
		MockitoAnnotations.initMocks(this);
		compiler = new DecacCompiler(null, null);
		superType = new IntType(null);
		when(superClass.verifyType(compiler)).thenReturn(superType);
		when(superClass.getName()).thenReturn(testSymbol);
		when(superClass.getName().getName()).thenReturn("testClass");

	}

	@Test(expected = ContextualError.class)
	public void testError() throws ContextualError {
		DeclClassVisible DeclM = new DeclClassVisible(className, superClass, fields, methods);
		DeclM.verifyClass(compiler);
	}
}