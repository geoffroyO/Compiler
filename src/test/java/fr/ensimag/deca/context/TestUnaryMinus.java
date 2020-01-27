package fr.ensimag.deca.context;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.UnaryMinus;

/**
 * Test for the UnaryMinus node using mockito, using @Mock and @Before annotations.
 *
 * @author Ensimag
 * @date 01/01/2020
 */
public class TestUnaryMinus {

    final Type INT = new IntType(null);
    final Type FLOAT = new FloatType(null);
    
    @Mock
    AbstractExpr intexpr1;

    @Mock
    AbstractExpr floatexpr1;

    DecacCompiler compiler;
    
    @Before
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(floatexpr1.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
    }

    @Test
    public void testInt() throws ContextualError {
        UnaryMinus t = new UnaryMinus(intexpr1);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isInt());
        // check that the mocks have been called properly.
        verify(intexpr1, Mockito.times(1)).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloat() throws ContextualError {
    	UnaryMinus t = new UnaryMinus(floatexpr1);
    	// check the result
        assertTrue(t.verifyExpr(compiler, null, null).isFloat());
        // check that the mocks have been called properly.
        verify(floatexpr1, Mockito.times(1)).verifyExpr(compiler, null, null);
    }
    
    @Test
    public void testGetOp() throws ContextualError {
    	UnaryMinus t = new UnaryMinus(floatexpr1);
    	UnaryMinus sp = spy(t);
    	sp.verifyExpr(compiler, null, null);
    	assert(sp.getOperand() == floatexpr1);
    }
}