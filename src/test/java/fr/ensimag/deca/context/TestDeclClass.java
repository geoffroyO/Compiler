package fr.ensimag.deca.context;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.DeclClass;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.ListDeclField;
import fr.ensimag.deca.tree.ListDeclMethod;
import fr.ensimag.deca.tree.Plus;

/**
 * Test for the Plus node using mockito, using @Mock and @Before annotations.
 *
 * @author Ensimag
 * @date 01/01/2020
 */
public class TestDeclClass {

//	Type INT = new IntType(null);
//	
//	@Mock
//	AbstractIdentifier className;
//	
//	@Mock
//	AbstractIdentifier superClass;
//	
//	@Mock
//	ListDeclField fields;
//	
//	@Mock
//	ListDeclMethod methods;
//	
//	DecacCompiler compiler;
//
//	public class DeclClassVisible extends DeclClass{
//		
//		public DeclClassVisible(AbstractIdentifier className, AbstractIdentifier superClass,
//                ListDeclField fields, ListDeclMethod methods) {
//			super(className, superClass, fields, methods);
//		}
//		@Override
//		public void verifyClass(DecacCompiler compiler) throws ContextualError {
//			super.verifyClass(compiler);
//		}
//	}
//
//    
//    @Before
//    public void setup() throws ContextualError {
//        MockitoAnnotations.initMocks(this);
//        compiler = new DecacCompiler(null, null); 
//        when(superClass.verifyType(compiler)).thenReturn(INT);
//        
//    }
//
//    @Test
//    public void test() throws ContextualError {
//    	DeclClassVisible DeclM = new DeclClassVisible(className, superClass, fields, methods);
//    	DeclM.verifyClass(compiler);
//       
//    }

}