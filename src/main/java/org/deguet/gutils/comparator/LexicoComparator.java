package org.deguet.gutils.comparator;

import java.util.Comparator;

/**
 * The lexicographic comparator order.
 * Returns the result of its second comparator if object are equal with respect to the first one.
 * Returns the result of the first otherwise.
 * 
 * Can be composed to obtain a more complex comparator:
 * Comparator third = new LexicoComparator(one, new LexicoComparator(two,three));
 * 
 * with one, two and three being comparator has a lexicographic behavior too.
 * 
 * @author joris deguet
 *
 * @param <S>
 */
public final class LexicoComparator<S> implements Comparator<S>{

	private Comparator<S>[] comps;
	
	public LexicoComparator(Comparator<S>... comps){
		this.comps = comps;
	}
	
	public int compare(S o1, S o2) {
		for (Comparator<S>  c : comps){
			int result = c.compare(o1, o2);
			if (result != 0) return result;
		}
		return 0;
	}

}
