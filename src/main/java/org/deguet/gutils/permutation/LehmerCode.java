package org.deguet.gutils.permutation;

import java.util.ArrayList;
import java.util.List;

public class LehmerCode {

	private int size;
	
	private FactorialBase base = new FactorialBase();
	
	public LehmerCode(int size){
		this.size = size;
	}
	
	/**
	 * This makes a permutation form an integer by going through the factorial representation of the integer.
	 * 
	 * Look at the code for the algorithm understanding or the following page for description:
	 * http://en.wikipedia.org/wiki/Factoradic
	 * 
	 * 
	 * @param number
	 * @return
	 */
	public Permutation longToPermut(Long number){
		Long[] facto = base.to(number);
		if (facto.length > size){
			throw new IllegalArgumentException("factorial decomposition of "+number+" is too long "+FactorialBase.toString(facto));
		}
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < size ; i++){
			numbers.add(i);
		}
		int[] permut = new int[size];
		for (int digit = size-1 ; digit >= 0 ; digit--){
			long toPlace = 0;
			// this is a leading 0
			if (digit >= facto.length){
				toPlace = 0;
			}
			else{
				toPlace = facto[digit];
			}
			int index = permut.length-1-digit;
			//System.out.println("Array index is       "+index);
			permut[index] = numbers.get((int) toPlace);
			numbers.remove((int) toPlace);	
		}
		return new Permutation(permut);
	}
	
	/**
	 * The Lehmer code is the array of n ints noted c_i such that:
	 * c_i = card({j>i, sig(i)>sig(j)})
	 * @return
	 */
//	private int[] lehmerCode(){
//		int[] result = new int[this.permut.length];
//		for (int i = 0 ; i < permut.length ; i++){
//			int ci = 0;
//			for (int j = i+1 ; j < permut.length ; j++){
//				if (i < j && permut[i] > permut[j])
//					ci++;
//			}
//			result[i] = ci;
//		}
//		return result;
//	}
	
	
	
	/*
	 * Converts a Permutation objects into its integer value under a long value.
	 */
	public Long permutToLong(Permutation permut){
		List<Long> facto = new ArrayList<Long>();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < size ; i++){
			numbers.add(i);
		}
		int[] perm = permut.getArray();
		boolean leading = true;
		for (int elt: perm){
			int index = numbers.indexOf(elt);
			numbers.remove(index);
			if (!leading || index != 0 ){
				leading = false;
				facto.add(0, (long)index);
			}
		}
		//System.out.println(facto);
		if (facto.isEmpty()){
			facto.add(0L);
		}
		Long[] fact = facto.toArray(new Long[facto.size()]);
		return base.from(fact);
	}
	
	
}
