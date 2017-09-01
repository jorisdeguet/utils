package org.deguet.gutils.pairing;

import org.deguet.gutils.nuplets.Duo;

public abstract class OrderedPairing implements Pairing{

	public Long compose(Duo<Long,Long> couple) {
		if (couple.get1() < couple.get2()){
			throw new IllegalArgumentException("first argument must be greater than the second one");
		}
		return composeOrdered(couple);
	}
	
	public abstract Long composeOrdered(Duo<Long,Long> couple);
	
	public abstract Duo<Long,Long> decompose(Long single);
	
	
}
