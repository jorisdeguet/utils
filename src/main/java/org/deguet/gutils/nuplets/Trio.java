package org.deguet.gutils.nuplets;

import java.io.Serializable;

/**
 * This class implements generic triplets.
 * 
 * Each element of a triple has its own class.
 * 
 * Equality of triples is computed according to equality on elements. 
 * 
 * @author joris
 *
 * @param <A>
 * @param <B>
 * @param <C>
 */
public final class Trio<A,B,C> implements Serializable,Tuple {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1230659617097735017L;
	private final A one;
	private final B two;
	private final C three;
	
	private transient Integer hash;
	
	private Trio(
			final A o,
			final B t,
			final C th){
		this.one = o;
		this.two = t;
		this.three = th;
	}
	
	public A get1() {
		return one;
	}

	public B get2() {
		return two;
	}
	
	public C get3() {
		return three;
	}
	
	@Override
	public int hashCode(){
		if (hash == null) this.hash = this.one.hashCode()*this.two.hashCode()+this.three.hashCode();
		return hash;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Trio){
			Trio bis = (Trio) o;
			return 
				bis.get1().equals(this.get1()) && 
				bis.get2().equals(this.get2()) &&
				bis.get3().equals(this.get3());
		}
		else{
			return false;
		}
	}
	
	@Override
	public String toString(){
		return "{" + this.one + "|" + this.two +"|" + this.three +"}";
	}
	
	public static <X,Y,Z> Trio<X,Y,Z>  t(X x,Y y, Z z){
		return new Trio<X,Y,Z>(x,y,z);
	}
	
}
