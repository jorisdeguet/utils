package org.deguet.gutils.nuplets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Duo<A,B> implements Comparable<Duo<A,B>>,Serializable,Tuple{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final A one;
	
	private final B two;

	private Integer hash;
	
	private Duo(
			final A o,
			final B t){
		this.one = o;
		this.two = t;
	}
	
	public A get1() {
		return one;
	}

	public B get2() {
		return two;
	}
	
	@Override
	public int hashCode(){
		if (hash == null) this.hash = this.one.hashCode()*this.two.hashCode();
		return hash;
	}
	
	@Override
	public boolean equals(Object o){
		if (o == this) return true;
		//return this.toString().equals(o.toString());
		if (o instanceof Duo){
			Duo<A,B> bis = (Duo<A,B>) o;
			if (bis.one == this.one && bis.two == this.two) return true;
			//System.out.println("Found equals");
			return bis.get1().equals(this.get1()) && bis.get2().equals(this.get2());
		}
		else{
			return false;
		}
	}
	
	public Duo<B,A> revert(){
		return new Duo<B,A>(this.get2(), this.get1());
	}
	
	@Override
	public String toString(){
		return "{" + this.one + "|" + this.two +"}";
	}

	public int compareTo(Duo<A,B> o) {
		if (o instanceof Duo){
			Duo<A,B> bis = (Duo<A,B>) o;
			if (one.toString().equals(bis.one.toString())){
				return two.toString().compareTo(bis.two.toString());
			}
			else{
				return one.toString().compareTo(bis.one.toString());
			}
		}
		return 0;
	}
	
	public static <E> List<Duo<E,E>>  coupleList(List<E> elements){
		List<Duo<E,E>> result = new ArrayList<Duo<E,E>>();
		for (int i = 0 ; i < elements.size() ; i++){
			for (int j = i+1 ; j < elements.size() ; j++){
				result.add(new Duo<E,E>(elements.get(i),elements.get(j)));
			}
		}
		return result;
	}
	
	public static <E> List<Duo<E,E>>  coupleList(E[] elements){
		List<Duo<E,E>> result = new ArrayList<Duo<E,E>>();
		for (int i = 0 ; i < elements.length ; i++){
			for (int j = i+1 ; j < elements.length ; j++){
				result.add(new Duo<E,E>(elements[i],elements[j]));
			}
		}
		return result;
	}
	
	public static <X,Y> Duo<X,Y>  d(X x,Y y){
		return new Duo<X,Y>(x,y);
	}
	
}
