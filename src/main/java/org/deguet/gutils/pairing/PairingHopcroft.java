package org.deguet.gutils.pairing;

import org.deguet.gutils.nuplets.Duo;

/**
 * comes from http://mathworld.wolfram.com/PairingFunction.html
 * @author joris
 *
 */
public class PairingHopcroft implements Pairing {

	public Long compose(Duo<Long, Long> couple) {
		long i = couple.get1();
		long j = couple.get2();
		long result = delta(i+j-2) + i;
		//System.out.println("Duo to compose is "+couple+"  ->  "+ result);
		return result;
	}

	private long delta(long x){
		return x*(x+1)/2;
	}
	
	public Duo<Long,Long> decompose(Long h) {
		long c = (long) Math.floor(Math.sqrt(2*h) - 0.5);
		long i = h-delta(c);
		long j = c-i+2;
		return Duo.d(i,j);
	}

}
