package org.deguet.gutils.set;

import org.deguet.gutils.bit.BitLine;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implements a generator type iterable for a powerset.
 * @author joris
 *
 * @param <T>
 */
public class PowerSet<T> implements Iterable<Set<T>>, Iterator<Set<T>> {

	T[] elements;
	
	Iterator<BitLine> iterator;
	
	public PowerSet(Set<T> base){
		elements = (T[]) base.toArray();
		iterator =  BitLine.allLinesForSize(elements.length).iterator();
	}
	
	public Iterator<Set<T>> iterator() {
		return this;
	}

	public boolean hasNext() {
		return iterator.hasNext();
	}

	public Set<T> next() {
		BitLine b = iterator.next();
		Set<T> result = new HashSet<T>();
		for (int i  = 0 ; i < elements.length ; i++){
			if (b.isOn(i)) result.add(elements[i]);
		}
		return result;
	}

	public void remove() {}

	
	
}
