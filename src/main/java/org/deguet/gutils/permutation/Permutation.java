package org.deguet.gutils.permutation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public final class Permutation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// the int array that contains the permutation
	private int[] permut;
	
	public Permutation(int[] content){
		int[] temp = new int[content.length];
		System.arraycopy(content, 0, temp, 0, content.length);
		this.permut = temp;
	}
	
	/**
	 * Returns the inverse permutation of this one.
	 * so sigma(inverse(i)) = i.
	 * @return
	 */
	public Permutation inverse(){
		int[] result = new int[this.permut.length];
		for (int i = 0 ; i < permut.length ; i++){
			//result[i] = permut[permut[i]];
			result[permut[i]] = i;
		}
		return new Permutation(result);
	}
	
	
	/**
	 * Returns a new array that is permuted.
	 * @param <T>
	 * @param source
	 * @return
	 */
	public <T> T[] permutArray(final T[] source, Class<T> c){
		if (permut.length != source.length){
			throw new IllegalArgumentException("Not the same size permut is "+this.permut.length+" array "+source.length);
		}
		T[] result = (T[]) Array.newInstance(c,source.length);
		for (int i = 0 ; i < permut.length ; i++){
			result[permut[i]] = source[i];
		}
		return result;
	}
	
	public <T> List<T> permutList(final List<T> source, Class<T> c){
		T[] temp = (T[]) new Object[source.size()];
		T[] array = permutArray(source.toArray(temp), c);
		List<T> result = new ArrayList<T>();
		for (T elt : array){
			result.add(elt);
		}
		return result;
	}
	
	/**
	 * Returns the composition of this permutation with the provided one.
	 * result(i) = this(factor(i)), result = this o factor
	 * @param factor
	 * @return
	 */
	public Permutation composeLeft(Permutation factor){
		return factor.compose(this);
	}
	
	public Permutation compose(Permutation factor){
		int[] result = new int[this.permut.length];
		for (int i = 0 ; i < permut.length ; i++){
			result[i] = factor.permut[permut[i]];
		}
		return new Permutation(result);
	}
	
	/**
	 * Returns the set of cycles that make this permutation.
	 * @return
	 */
	public Set<List<Integer>> cycles(){
		Set<Integer> indexes = new HashSet<Integer>();
		Set<List<Integer>> result = new HashSet<List<Integer>>();
		for (int  i = 0 ; i < this.permut.length ; i++) indexes.add(i);
		while(!indexes.isEmpty()){
			int first = indexes.iterator().next();
			List<Integer> cycle = new ArrayList<Integer>();
			int current = first;
			do{
				cycle.add(current);
				current = permut[current];
			} while(current != first);
			result.add(cycle);
			indexes.removeAll(cycle);
		}
		
		return result;
	}
	
	@Override
	public int hashCode(){
		return Arrays.hashCode(this.permut);
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Permutation){
			Permutation bis = (Permutation) o;
			return Arrays.equals(bis.permut, this.permut);
		}
		return false;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for (int i = 0 ; i < permut.length ; i++){
			sb.append(i+">"+permut[i]+"  ");
		}
		sb.append("]");
		return sb.toString();
	}

	public int[] getArray() {
		return this.permut;
	}
	
}
