package at.jku.se.calculator.operators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

/**
 * This test class performs tests for the {@link SubtractOperation} class.
 */
public class TestSubtractOperation {

	private SubtractOperation subtract;

	@Before
	public void setup() {
		subtract = new SubtractOperation();
	}

	/**
	 * Tests that subtracting a smaller number from a larger one yields a positive result.
	 */
	@Test
	public void testCalculatePositiveResult() {
		String result = subtract.calculate("8-3");
		assertEquals(5, Integer.parseInt(result));
	}

	/**
	 * Tests that subtracting a larger number from a smaller one yields a negative result.
	 */
	@Test
	public void testCalculateNegativeResult() {
		String result = subtract.calculate("3-8");
		assertEquals(-5, Integer.parseInt(result));
	}

	/**
	 * Tests that an invalid first operand throws an {@link IllegalArgumentException}.
	 */
	@Test
	public void testCalculateExceptionFirstOperand() {
		assertThrows(IllegalArgumentException.class, () -> subtract.calculate("xyz-3"));
	}

	/**
	 * Tests that an invalid second operand throws an {@link IllegalArgumentException}.
	 */
	@Test
	public void testCalculateExceptionSecondOperand() {
		assertThrows(IllegalArgumentException.class, () -> subtract.calculate("3-abc"));
	}

	/**
	 * Tests that a malformed input without a '-' sign throws an {@link IllegalArgumentException}.
	 */
	@Test
	public void testCalculateMalformedInput() {
		assertThrows(IllegalArgumentException.class, () -> subtract.calculate("3+3"));
	}

}
