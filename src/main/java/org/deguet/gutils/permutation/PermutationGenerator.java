package org.deguet.gutils.permutation;

import java.math.BigInteger;
import java.util.Iterator;

/**
 * Generates all permutations of n elements given as 
 * 
 * This code is inspired by a web page:
 * 
 * By what I want precisely is a lehmer code : map from k to a n-permut where k < n!
 * 
 * 
 * @author joris
 *
 */
public class PermutationGenerator implements Iterable<Permutation>, Iterator<Permutation>{

	// the current working array
	private int[] current;
	
	// the number of permutation remaining
	private BigInteger remain;
	
	// the size of this permutation space
	private BigInteger size;

	/**
	 * Builds a permutation generator of permutations of size n.
	 * For a value of 9, you can expect to grab all permutations
	 * Over it will take a long time.
	 * Would need RANDOM GENERATOR.
	 * @param n
	 */
	public PermutationGenerator (int n) {
		if (n <= 1) {
			throw new IllegalArgumentException ("Please give a size of permutation greater than 1");
		}
		current = new int[n];
		size = getFactorial (n);
		reset ();
	}

	/**
	 * Resets the generator to regebin from beginning.
	 */
	public void reset () {
		for (int i = 0; i < current.length; i++) {
			current[i] = i;
		}
		remain = new BigInteger (size.toByteArray());
	}

	/**
	 * The number remaining.
	 * @return
	 */
	public BigInteger getRemain () {
		return remain;
	}

	/**
	 * The size of permutation space.
	 * This is n!
	 * @return
	 */
	public BigInteger getSize () {
		return size;
	}

	/**
	 * Compute factorial.
	 * @param n
	 * @return
	 */
	private BigInteger getFactorial (int n) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--) {
			fact = fact.multiply (new BigInteger (Integer.toString (i)));
		}
		return fact;
	}

	
	private int[] getNext () {

		if (remain.equals (size)) {
			remain = remain.subtract (BigInteger.ONE);
			return current;
		}

		int temp;

		// Find largest index j with a[j] < a[j+1]

		int j = current.length - 2;
		while (current[j] > current[j+1]) {
			j--;
		}

		// Find index k such that a[k] is smallest integer
		// greater than a[j] to the right of a[j]

		int k = current.length - 1;
		while (current[j] > current[k]) {
			k--;
		}

		// Interchange a[j] and a[k]

		temp = current[k];
		current[k] = current[j];
		current[j] = temp;

		// Put tail end of permutation after jth position in increasing order

		int r = current.length - 1;
		int s = j + 1;

		while (r > s) {
			temp = current[s];
			current[s] = current[r];
			current[r] = temp;
			r--;
			s++;
		}

		remain = remain.subtract (BigInteger.ONE);
		return current;

	}

	public boolean hasNext() {
		return remain.compareTo(BigInteger.ZERO) == 1;
	}

	public Permutation next() {
		int[] next = this.getNext();
		return new Permutation(next);
	}

	/**
	 * Remover does nothing
	 */
	public void remove() {}

	public Iterator<Permutation> iterator() {
		return this;
	}

}
