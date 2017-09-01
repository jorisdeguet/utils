package org.deguet.gutils.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fuzzy Search looks for all fuzzy (within a certain edition distance) occurences of a word.
 * This is adapted from the Edition Distance class, however:
 * - we do not keep the complete Levenstein Matrix as it would be too big
 * - therefore, we are not able to make the link between the occurence and its degenerated ocurrnence.
 * 
 * 
 * TODO implment Bitap http://en.wikipedia.org/wiki/Bitap_algorithm
 * 
 * 
 * @author joris
 *
 */
public class FuzzySearch{
	
	// do not have to keep all the cells
	// the last column is enough as it contains the actual edition distances.
	final int[] distances;

	final CharSequence content,pattern;


	/** 
	 * The longer one is aa, we look for occurrences of bb
	 * @param aa
	 * @param bb
	 */
	public FuzzySearch(CharSequence cont, CharSequence patt){
		content = cont; 
		pattern = patt;
		distances = new int[content.length()+1];
		fillArray(distances,content,pattern);
	}

	/**
	 * The actual computing of the distances.
	 * Then results are in the dists array.
	 * @param dists
	 * @param a
	 * @param b
	 */
	static void fillArray(int[] dists, CharSequence a, CharSequence b){
		int[] current = new int[b.length()+1];
		int[] previous = new int[b.length()+1];
		// this is the "no penalty for insertion before" trick that helps computing the number of errors.
		for (int j = 0; j <= b.length(); j++) {current[j] = j;previous[j] = j;}
		for (int i = 1; i <= a.length(); i++){
			//System.out.println(Arrays.toString(current));
			for (int j = 1; j <= b.length(); j++){
				int leftPlusInsert  = current[j - 1] + 1;
				int abovePlusDelete = previous[j] + 1;
				int diagPlusReplace = previous[j - 1]+ ((a.charAt(i-1) == b.charAt(j-1)) ? 0: 1);//-1 in charAt is just because we shifted indexes between string and the array
				current[j] = EditionDistance.minimum(abovePlusDelete,leftPlusInsert,diagPlusReplace);
			}
			dists[i] = current[b.length()];
			System.arraycopy(current, 0, previous, 0, current.length);
		}
		System.out.println(Arrays.toString(current));
		System.out.println(Arrays.toString(dists));
	}
	
	/**
	 * Returns the list of indexes where we can find a string that is approximately the pattern.
	 * @param numberOfErrors
	 * @return
	 */
	public List<Integer> results(int numberOfErrors){
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 1; i <= content.length(); i++){
			if (distances[i] <= numberOfErrors){
				result.add(i-pattern.length());
			}
		}
		return result;
	}
	
}
