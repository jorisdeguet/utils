package org.deguet.gutils.list;

import java.util.Collection;
import java.util.Iterator;

/**
 * Immutable generic list based on the idea of LISP lists.
 * @author joris
 *
 * @param <E>
 */
public class CarCon<E> implements Iterable<E>{

	// the first element
	final E car;

	// the tail that contains other elements
	final CarCon<E> con;

	Integer cacheSize;
	
	//E[] cacheElts;

	// the empty list 
	public CarCon(){
		this.car = null;
		this.con = null;
	}

	public CarCon(E car){
		this.car = car;
		this.con = nil();
	}

	public CarCon(E car,CarCon<E> con){
		this.car = car;
		this.con = con;
	}

	public CarCon<E> addLeft(E first){
		return new CarCon<E>(first, this);
	}

	public boolean isEmpty(){
		return car==null && con==null;
	}
	
	@Override
	public int hashCode(){
		return this.car.hashCode();
	}
	
	public int size(){
		if (cacheSize == null){
			if (con == null && car == null) cacheSize = 0;
			else if (con == null) cacheSize = 1;
			else cacheSize = 1 + con.size();
		}
		return cacheSize;
	}

	@Override
	public boolean equals(Object o){
		if (o == this) return true;
		if (o instanceof CarCon){
			CarCon<E> bis = (CarCon<E>) o;
			if (car == null) return bis.car == null;
			if (con == null) return (bis.con == null && car.equals(bis.car));
			return this.car.equals(bis.car)	&& this.con.equals(bis.con);
		}
		return false;
	}

	public boolean contains(Object elt){
		int hashy = elt.hashCode();
		for (E e : this){
			if (e.hashCode() == hashy && e.equals(elt)) return true;
		}
		return false;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("[");
		for (E elt : this){
			sb.append(elt+" ");
		}
		sb.append("]");
		return sb.toString();
	}

	public Iterator<E> iterator() {
		return new RecIterator<E>(this);
	}

	private class RecIterator<E> implements Iterator<E>{

		CarCon<E> cc;
		public RecIterator(CarCon<E> cc){
			this.cc = cc;
		}

		public boolean hasNext() {
			return cc.car!=null;
		}

		public E next() {
			E res = cc.car;
			cc = cc.con;
			return res;
		}

		public void remove() {
		}

	}

	public E getFirst() {
		return this.car;
	}

	public E getLast() {
		if (con == null){
			return this.car;
		}
		else {
			return con.getLast();
		}
	}

	public Object[] toArray() {
		Object[] res = new Object[this.size()];
		int i = 0;
		for (E e : this){
			res[i] = e;
			i++;
		}
		return res;
	}

	public <T> T[] toArray(T[] a) {
		return null;
	}

	private CarCon<E> nil(){
		return new CarCon<E>();
	}
	
	public CarCon<E> addEnd(E e) {
		if (car == null) return new CarCon<E>(e);
		if (con == null) return new CarCon<E>(this.car, new CarCon<E>(e));
		return new CarCon<E>(this.car, con.addEnd(e));
	}
	
	public CarCon<E> addStart(E e) {
		return addLeft(e);
	}
	
	public CarCon<E> add(int index, E e) {
		if (index > this.size()) throw new IllegalArgumentException(index+ " larger than "+this.size());
		if (index == 0) return this.addLeft(e);
		return new CarCon<E>(car,con.add(index-1, e));
	}
	
	public CarCon<E> subList(int start, int end){
		//System.out.println("rec call " + this+"  at " +start+"  "+end);
		if (end > this.size()) throw new IllegalArgumentException("end index too far");
		if (start > end) throw new IllegalArgumentException("start must be smaller than end");
		if (end == start) return nil();
		CarCon<E> result = this;
		// avancer pour se placer sur le bon début
		while(start > 0){
			start--;end--;
			result = result.con;
		}
		if (end == result.size()) {
			return result;
		}
		// at this point result point on the right starting index
		return new CarCon<E>(result.car, result.con.subList(start, end-1));
	}
	

	public CarCon<E> remove(E e) {
		if (!this.contains(e)) return this;
		if (e.equals(car)) return con;
		return new CarCon(car,con.remove(e));
	}

	public boolean containsAll(Collection<E> c) {
		for (E e : c){
			if (!this.contains(e))
				return false;
		}
		return true;
	}

	public CarCon<E> addAllStart(Collection<E> c) {
		CarCon<E> result = this;
		for(E e : c){
			result = new CarCon<E>(e,result);
		}
		return result;
	}

	public E get(int index){
		if (index >= this.size()) throw new IllegalArgumentException(index+ " larger than "+this.size());
		CarCon<E> current = this;
		while(index > 0){
			current = current.con;
			index--;
		}
		return current.car;
	}
	
	public CarCon<E> removeAll(Collection<E> c) {
		CarCon<E> result = this;
		for(E e : c){
			result = result.remove(e);
		}
		return result;
	}

	// TODO
	public boolean retainAll(Collection<?> c) {
		throw new IllegalArgumentException("Immutable");
	}

}
