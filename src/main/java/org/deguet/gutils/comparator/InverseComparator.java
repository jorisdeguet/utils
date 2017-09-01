package org.deguet.gutils.comparator;

import java.util.Comparator;

/**
 * Implements the opposite behaviour of a base comparator.
 * This will make a comparator answer the opposite of what the original comparator does.
 * 
 * 
 * See also reversOrder in the Collections class.
 * 
 * @author evariste valtrid
 *
 * @param <S>
 */
public final class InverseComparator<S> implements Comparator<S> {

	private Comparator<S> base;
	
	public InverseComparator(Comparator<S> b){
		this.base = b;
	}

	public int compare(S o1, S o2) {
		return -base.compare(o1, o2);
	}
	
}
