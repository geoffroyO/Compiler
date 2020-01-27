package fr.ensimag.deca.context;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.Divide;
import fr.ensimag.deca.tree.IntLiteral;

/**
 * Test for the Divide node using mockito, using @Mock and @Before annotations.
 *
 * @author Ensimag
 * @date 01/01/2020
 */
public class TestDivide {

	final Type INT = new IntType(null);
	final Type FLOAT = new FloatType(null);
	final ConvFloat CONVFLOAT = new ConvFloat(new IntLiteral(4));

	@Mock
	AbstractExpr intexpr1;
	@Mock
	AbstractExpr intexpr2;
	@Mock
	AbstractExpr floatexpr1;
	@Mock
	AbstractExpr floatexpr2;

	DecacCompiler compiler;

	@Before
	public void setup() throws ContextualError {
		MockitoAnnotations.initMocks(this);
		compiler = new DecacCompiler(null, null);
		when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
		when(intexpr2.verifyExpr(compiler, null, null)).thenReturn(INT);
		when(floatexpr1.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
		when(floatexpr2.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
		when(intexpr1.verifyRValue(eq(compiler), eq(null), eq(null), any(FloatType.class))).thenReturn(CONVFLOAT);
		when(intexpr2.verifyRValue(eq(compiler), eq(null), eq(null), any(FloatType.class))).thenReturn(CONVFLOAT);
	}

	@Test
	public void testIntInt() throws ContextualError {
		Divide t = new Divide(intexpr1, intexpr2);
		// check the result
		assertTrue(t.verifyExpr(compiler, null, null).isFloat());
		// check that the mocks have been called properly.
		verify(intexpr1).verifyExpr(compiler, null, null);
		verify(intexpr2).verifyExpr(compiler, null, null);
	}

	@Test
	public void testIntFloat() throws ContextualError {
		Divide t = new Divide(intexpr1, floatexpr1);
		// check the result
		assertTrue(t.verifyExpr(compiler, null, null).isFloat());
		// ConvFloat should have been inserted on the right side
		assertTrue(t.getLeftOperand() instanceof ConvFloat);
		assertFalse(t.getRightOperand() instanceof ConvFloat);
		// check that the mocks have been called properly.
		verify(intexpr1).verifyExpr(compiler, null, null);
		verify(floatexpr1).verifyExpr(compiler, null, null);
	}

	@Test
	public void testFloatInt() throws ContextualError {
		Divide t = new Divide(floatexpr1, intexpr1);
		// check the result
		assertTrue(t.verifyExpr(compiler, null, null).isFloat());
		// ConvFloat should have been inserted on the right side
		assertTrue(t.getRightOperand() instanceof ConvFloat);
		assertFalse(t.getLeftOperand() instanceof ConvFloat);
		// check that the mocks have been called properly.
		verify(intexpr1).verifyExpr(compiler, null, null);
		verify(floatexpr1).verifyExpr(compiler, null, null);
	}
}