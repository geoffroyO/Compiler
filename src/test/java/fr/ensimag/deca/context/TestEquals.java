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

import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.Divide;
import fr.ensimag.deca.tree.Equals;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.Modulo;
import fr.ensimag.deca.tree.This;
/**
 * Test for the This node using mockito, using @Mock and @Before annotations.
 *
 * @author Ensimag
 * @date 01/01/2020
 */


public class TestEquals {
	
    final Type INT = new IntType(null);
    final Type FLOAT = new FloatType(null);
    final ConvFloat CONVFLOAT = new ConvFloat(new IntLiteral(4));	
	
	@Mock
	AbstractExpr floatexpr1;
	
	@Mock
	AbstractExpr floatexpr2;
	
	@Mock
	AbstractExpr intexpr1;
	
	@Mock
	AbstractExpr intexpr2;
	
	DecacCompiler compiler;


    @Before
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intexpr2.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(floatexpr1.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
        when(floatexpr2.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
   
        when(intexpr1.verifyRValue(compiler, null, null, FLOAT)).thenReturn(CONVFLOAT);
        when(intexpr2.verifyRValue(compiler, null, null, FLOAT)).thenReturn(CONVFLOAT);
    }
    @Test
    public void testIntInt() throws ContextualError {
        Equals eq = new Equals(intexpr1, intexpr2);
        // check the result
        assertTrue(eq.verifyExpr(compiler, null, null).isBoolean());
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError {
    	Equals eq = new Equals(intexpr1, floatexpr1);
        eq.verifyExpr(compiler, null, null);
        assertTrue(eq.getLeftOperand() instanceof ConvFloat);
        assertFalse(eq.getRightOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr1).verifyExpr(compiler, null, null);
    }
    
    @Test
    public void testFloatInt() throws ContextualError {
    	Equals eq = new Equals(floatexpr1, intexpr1);
        eq.verifyExpr(compiler, null, null);
        assertFalse(eq.getLeftOperand() instanceof ConvFloat);
        assertTrue(eq.getRightOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr1).verifyExpr(compiler, null, null);
    }  
}