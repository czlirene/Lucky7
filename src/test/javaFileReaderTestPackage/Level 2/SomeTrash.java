package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.BigIncrementer;

/**
 * JUnit 4 Test for BigIncrementer class with varying starting values
 *
 * @author Evan Quan
 * @since 12 March 2018
 *
 */
public class BigIncrementerVaryingStartsTest {

	private static BigIncrementer in;
	private static final String MAX_INT = Integer.toString(Integer.MAX_VALUE);
	private static final String MAX_LONG = Long.toString(Long.MAX_VALUE);
	private static final String DECILLION = "1000000000000000000000000000000000";

	/**
	 * Check that incrementer works for 1000000000 increments
	 */
	@Test
	public void test_StartAt_DECILLION_Increment_BILLION() {
		in = new BigIncrementer(DECILLION);
		int count = 1000000000;
		String expected = "1000000000000000000000001000000000";
		for (int i = 0; i < count; i++) {
			in.increment();
		}
		assertEquals(expected, in.toString());
	}

	/**
	 * Check that incrementer works for 1 increment starting at Integer.MAX_VALUE.
	 * This is testing for integer overflow.
	 */
	@Test
	public void test_StartAt_MAX_INT_Increment_ONE() {
		in = new BigIncrementer(MAX_INT);
		String expected = "2147483648";
		in.increment();
		assertEquals(expected, in.toString());
	}

	/**
	 * Check that incrementer works for 1 increment starting at Long.MAX_VALUE. This
	 * is testing for long overflow.
	 */
	@Test
	public void test_StartAt_MAX_LONG_Increment_ONE() {
		in = new BigIncrementer(MAX_LONG);
		String expected = "9223372036854775808";
		in.increment();
		assertEquals(expected, in.toString());
	}

	/**
	 * Check that an invalid starting value throws a NumberFormatException
	 */
	@Test(expected = NumberFormatException.class)
	public void testInvalidStartingValue() {
		in = new BigIncrementer("");
	}

}
