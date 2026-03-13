package at.jku.se.calculator.operators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

/**
 * This test class performs tests for the {@link MultiplyOperation} class.
 */
public class TestMultiplyOperation {

	private MultiplyOperation multiply;

	@Before
	public void setup() {
		multiply = new MultiplyOperation();
	}

	/**
	 * Tests that 4 * 3 = 12.
	 */
	@Test
	public void testCalculateStandardProduct() {
		String result = multiply.calculate("4*3");
		assertEquals(12, Integer.parseInt(result));
	}

	/**
	 * Tests that 0 * 9 = 0.
	 */
	@Test
	public void testCalculateMultiplyByZero() {
		String result = multiply.calculate("0*9");
		assertEquals(0, Integer.parseInt(result));
	}

	/**
	 * Tests that an invalid first operand throws an {@link IllegalArgumentException}.
	 */
	@Test
	public void testCalculateExceptionFirstOperand() {
		assertThrows(IllegalArgumentException.class, () -> multiply.calculate("xyz*3"));
	}

	/**
	 * Tests that an invalid second operand throws an {@link IllegalArgumentException}.
	 */
	@Test
	public void testCalculateExceptionSecondOperand() {
		assertThrows(IllegalArgumentException.class, () -> multiply.calculate("4*abc"));
	}

	/**
	 * Tests that a malformed input without a '*' sign throws an {@link IllegalArgumentException}.
	 */
	@Test
	public void testCalculateMalformedInput() {
		assertThrows(IllegalArgumentException.class, () -> multiply.calculate("4+3"));
	}

}
