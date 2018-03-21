package test;

import java.math.BigInteger;
import java.util.Date;

import main.BigIncrementer;

/**
 * Compares the run times of incrementing with int, {@link BigInteger}, and
 * {@link BigIncrementer}
 *
 * @author Evan Quan
 * @since March 9, 2018
 *
 */
public class BigIncrementerRuntimeTest {
	public static void main(String[] args) {
		test(0);
		test(1);
		test(10);
		test(100);
		test(1000);
		test(10000);
		test(100000);
		test(1000000);
		test(10000000);
		test(100000000);
		test(1000000000);
	}

	/**
	 * Tests incrementing time from 0 to i_count with int, BigInteger, and
	 * BigIncrementor
	 *
	 * @param i_count
	 *            the maximum count that the tests increment to
	 */
	public static void test(long i_count) {
		String start = "0";

		System.out.println("\nCount " + start + " + " + i_count + " = "
				+ (new BigInteger(start)).add(new BigInteger(Long.toString(i_count))));

		@SuppressWarnings("unused")
		int num = 0;
		System.out.print("Start int...");
		long timeElapsed = 0L;
		long startTime = System.currentTimeMillis();
		for (long i = 0; i < i_count; i++) {
			num++;
		}
		timeElapsed = (new Date()).getTime() - startTime;
		System.out.println("END: " + timeElapsed + "ms");

		BigInteger bigInt = new BigInteger(start);
		System.out.print("Start BigInteger...");
		timeElapsed = 0L;
		startTime = System.currentTimeMillis();
		for (long i = 0; i < i_count; i++) {
			bigInt = bigInt.add(new BigInteger("1"));
		}
		timeElapsed = (new Date()).getTime() - startTime;
		System.out.println("END: " + timeElapsed + "ms");

		BigIncrementer incr = new BigIncrementer(start);
		System.out.print("Start BigIncrementer...");
		timeElapsed = 0L;
		startTime = System.currentTimeMillis();
		for (long i = 0; i < i_count; i++) {
			incr.increment();
		}
		timeElapsed = (new Date()).getTime() - startTime;
		System.out.println("END: " + timeElapsed + "ms");
		if (bigInt.toString().equals(Integer.toString(num)) && bigInt.toString().equals(incr.toString())) {
			System.out.println("PASSED");
		} else {
			System.out.println("FAILED");
		}
	}
}
