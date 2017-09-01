package org.deguet.gutils.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Helps represent a set of positive Integer with one using the uniqueness of prime decomposition.
 * 	4 ::: [0, 2]			as 1^0 * 2^2
 *	3 ::: [0, 0, 1]			as 1^0 * 2^0 * 3^1
 *	2 ::: [0, 1]
 *	1 ::: [1]
 *	0 ::: []
 *	each number in the array corresponds to the exponent of the corresponding prime number
 *
 * @author joris
 *
 */
public class PrimeBase {

	public int[] to(long source){
		//System.out.println("====================================== "+source);
		if (source == 0) return new int[]{};
		if (source == 1) return new int[]{1};
		long n = source;
	    List<Integer> factors = new ArrayList<Integer>();
	    for (int i = 2; i <= n; i++) {
	      while (n % i == 0) {
	        factors.add(i);
	        n = n / i;
	      }
	    }
	    //System.out.println("List " + factors);
	    int maxFactor = Collections.max(factors).intValue();
	    int size  = primeIndex(maxFactor);
	    //System.out.println("size " + size);
	    int[] result = new int[size];
	    for (int f = 0 ; f < size ; f++){
	    	int prime = nthPrime(f+1);
	    	//System.out.println("Prime is "+prime);
	    	result[f] = Collections.frequency(factors, prime);
	    }
	    return result;
	}

	private int primeIndex(int prime){
		if (!isPrime(prime)) throw new IllegalArgumentException();
		if (prime == 1) return 1;
		if (prime == 2) return 2;
		int i = 0 ;
		while(true){
			int p = nthPrime(i);
			if (prime == p) return i;
			i++;
		}
	}
	
	/**
	 * Converts a prime base code (exponents for given positions) into its long value.
	 */
	public long from(int[] digits){
		if (digits.length == 0 ) return 0;
		if (Arrays.equals(digits, new int[]{1})) return 1;
		long result = 1;
		for (int i = 0; i < digits.length; i++) {
			int prime = nthPrime(i+1);
			int expon = (int)digits[i];
			//System.out.println("Res " + result +"  prime " + prime+"  exp " +expon);
			result *= Math.pow(prime, expon);
		}
		return result;
	}
	
	// Count number of set bits in an int
    private static int popCount(int n) {
        n -= (n >>> 1) & 0x55555555;
        n = ((n >>> 2) & 0x33333333) + (n & 0x33333333);
        n = ((n >> 4) & 0x0F0F0F0F) + (n & 0x0F0F0F0F);
        return (n * 0x01010101) >> 24;
    }

    private int nthPrime(int n){
    	if (n==1) return 1;
    	if (n==2) return 2;
    	return nthprime(n-1);
    }
    
    /**
     * https://bitbucket.org/dafis/javaprimes
     * @param n
     * @return
     */
    private int nthprime(int n) {
        if (n < 2) return 2;
        if (n == 2) return 3;
        if (n == 3) return 5;
        int limit, root, count = 2;
        limit = (int)(n*(Math.log(n) + Math.log(Math.log(n)))) + 3;
        root = (int)Math.sqrt(limit);
        switch(limit%6) {
            case 0:
                limit = 2*(limit/6) - 1;
                break;
            case 5:
                limit = 2*(limit/6) + 1;
                break;
            default:
                limit = 2*(limit/6);
        }
        switch(root%6) {
            case 0:
                root = 2*(root/6) - 1;
                break;
            case 5:
                root = 2*(root/6) + 1;
                break;
            default:
                root = 2*(root/6);
        }
        int dim = (limit+31) >> 5;
        int[] sieve = new int[dim];
        for(int i = 0; i < root; ++i) {
            if ((sieve[i >> 5] & (1 << (i&31))) == 0) {
                int start, s1, s2;
                if ((i & 1) == 1) {
                    start = i*(3*i+8)+4;
                    s1 = 4*i+5;
                    s2 = 2*i+3;
                } else {
                    start = i*(3*i+10)+7;
                    s1 = 2*i+3;
                    s2 = 4*i+7;
                }
                for(int j = start; j < limit; j += s2) {
                    sieve[j >> 5] |= 1 << (j&31);
                    j += s1;
                    if (j >= limit) break;
                    sieve[j >> 5] |= 1 << (j&31);
                }
            }
        }
        int i;
        for(i = 0; count < n; ++i) {
            count += popCount(~sieve[i]);
        }
        --i;
        int mask = ~sieve[i];
        int p;
        for(p = 31; count >= n; --p) {
            count -= (mask >> p) & 1;
        }
        return 3*(p+(i<<5))+7+(p&1);
    }
    
    private static final int[] TEST_BASE = { 2, 7, 61 };
    private static final int NUM_BASES = 3;
    private static boolean isPrime(int n) {
        if ((n & 1) == 0) return n == 2;
        if (n % 3 == 0) return n == 3;
        int step = 4, m;
        boolean onlyTD;
        if (n < 40000) {
            m = (int)Math.sqrt(n) + 1;
            onlyTD = true;
        } else {
            m = 100;
            onlyTD = false;
        }
        for(int i = 5; i < m; step = 6-step, i += step) {
            if (n % i == 0) {
                return false;
            }
        }
        if (onlyTD) {
            return true;
        }
        long md = n, n1 = n-1, exp = n-1;
        int s = 0;
        do {
            ++s;
            exp >>= 1;
        }while((exp & 1) == 0);
        // now n-1 = 2^s * exp
        for(int i = 0; i < NUM_BASES; ++i) {
            long r = modPow(TEST_BASE[i],exp,md);
            if (r == 1) continue;
            int j;
            for(j = s; j > 0; --j){
                if (r == n1) break;
                r = (r * r) % md;
            }
            if (j == 0) return false;
        }
        return true;
    }

    private static long modPow(long base, long exponent, long modulus) {
        if (exponent == 0) return 1;
        if (exponent < 0) throw new IllegalArgumentException("Can't handle negative exponents");
        if (modulus < 0) modulus = -modulus;
        base %= modulus;
        if (base < 0) base += modulus;
        long aux = 1;
        while(exponent > 1) {
            if ((exponent & 1) == 1) {
                aux = (aux * base) % modulus;
            }
            base = (base * base) % modulus;
            exponent >>= 1;
        }
        return (aux * base) % modulus;
    }
    
    
}
