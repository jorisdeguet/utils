package org.deguet.gutils.permutation;

import java.util.Random;

/**
 * http://en.wikipedia.org/wiki/Knuth_shuffle
 * @author joris
 * 
 */

public class KnuthGenerator {

	public static Permutation gen(Random r, int size){
		int[] random = new int[size] ;
		for (int i = 0 ; i < random.length ; i ++) random[i] = i;
		//To shuffle an array a of n elements (indices 0..n-1):
		//for i from n − 1 downto 1 do
		for (int i = random.length-1 ; i > 0 ; i--){
			//  j ← random integer with 0 ≤ j ≤ i
			int j = r.nextInt(i+1);
			//  exchange a[j] and a[i]
			int temp = random[i];
			random[i] = random[j];
			random[j] = temp;
		}
		return new Permutation(random);
	}

}
