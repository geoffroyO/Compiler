package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

//import fr.ensimag.deca.tree.AbstractExpr;
//import fr.ensimag.deca.tree.ConvFloat;
//import fr.ensimag.deca.tree.Divide;
//import fr.ensimag.deca.tree.This;
/**
 * Test for the This node using mockito, using @Mock and @Before annotations.
 *
 * @author Ensimag
 * @date 01/01/2020
 */


public class TestEquals {
	
//	public class ThisVisible extends This {	
//		public void setType(Type t) {
//			super.setType(t);
//		}
//	}
	
//	@Mock
//	AbstractExpr expr1;
//	
//	@Mock
//	AbstractExpr expr2;
//	
//	DecacCompiler compiler;
//
//
//    @Before
//    public void setup() throws ContextualError {
////        MockitoAnnotations.initMocks(this);
////        compiler = new DecacCompiler(null, null);
////        when(expr1.)
////        when(th.getType()).thenReturn(new IntType(compiler.getSymbols().create("A")));
////        when(th.verifyExpr(compiler, null, null)).thenCallRealMethod();
////        when(th.verifyExpr(compiler, null, def)).thenCallRealMethod();
//
//       
////        when(th.setType(any(Type.class))).thenCallRealMethod();
//
//    }
//
////    @Test(expected = ContextualError.class)
//    public void testError() throws ContextualError {
//        // check the result
////        th.verifyExpr(compiler, null, null);
//    }
//    
//    @Test
//    public void testType() throws ContextualError {
//        // check the result
//        assertTrue(th.verifyExpr(compiler, null, def) instanceof Type);
//    }
}