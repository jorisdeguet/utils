package org.deguet.gutils.permutation;

import java.util.ArrayList;
import java.util.List;

/**
 * The factorial base for integer representation.
 * 
 * In this base their is no bound on the digits like in decimal where digit are in 0..9.
 * 
 * A number is decomposed as Sum i * i!  where each digit must be inferior or equal to i the position.
 * 
 * @author joris
 *
 */
public class FactorialBase {

	/*
	 * Equivalent of to(), one of them might be faster
	 */
	public Long[] to2(long number){
		List<Long> storing = new ArrayList<Long>();
		long j = 1;
		while(number != 0){
			storing.add(number % j);
			number /= j;
			j++;
		}
		if (storing.isEmpty()){
			storing.add(0L);
		}
		return storing.toArray(new Long[storing.size()]);
	}

	/**
	 * Converts a long value into its factorial code.
	 */
	public Long[] to(long number){
		// a first run to catch the first factorial the number is under
		// In general we need to go to BigInteger.
		long current = number;
		int upper = 1;
		// Max value for longs is under 23!.
		Long[] factorials = new Long[23];
		// First find the first factorial that is above the number.
		long factorial = 1;
		factorials[0] = 0L;
		factorials[1] = 1L;
		// While the factorial is under the number and we are under the max val for Long
		while(number >=factorial && upper <21){
			upper++;
			factorial *= upper;
			factorials[upper] = factorial;
			//System.out.println(upper + "  "+pp(factorials));
		}
		//System.out.println("Upper is found to be "+upper+" because "+factorial+" > "+number);
		// Then remove large chunks for decreasing factorials to deduce digits
		int index = upper;
		Long[] result = new Long[upper];
		while (index != 1){
			index--;
			long digit = (current/factorials[index]);
			//System.out.println("Found that "+factorial+" fits in "+current+" "+digit+" times");
			current -= digit*factorials[index];
			if (digit < 0){
				throw new IllegalArgumentException();
			}
			result[index] = digit;
		}
		result[0] = 0L;
		// always put  a 0 at the end
		return result;
	}

	/**
	 * Converts a factorial code (exponents for given positions) into its long value.
	 */
	public long from(Long[] digits){
		long result = 0;
		long factor = 1;
		if (digits[0] != 0){
			throw new IllegalArgumentException();
		}
		for (int index = 1 ; index < digits.length; index++){
			if (digits[index] > index){
				throw new IllegalArgumentException();
			}
			factor *= index;
			result += digits[index]*factor;
		}
		return result;
	}

	/*
	 * Returns a simple representation of a factorial code
	 */
	public static String toString(Long[] a){
		String result = "{";
		for (Long e : a){
			result +=" "+e;
		}
		return result+"}";
	}

}
