package org.deguet.gutils.pairing;

import org.deguet.gutils.nuplets.Duo;

/**
 * A function that maps Long X Long into Long.
 * Looking at space filing curves from the book of Hans Sagan.
 * 
 * @author joris
 *
 */
public interface Pairing {

	public Long compose(Duo<Long,Long> couple);
	
	public Duo<Long,Long> decompose(Long single);
	
}
