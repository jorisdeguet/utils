package org.deguet.gutils.pairing;

import org.deguet.gutils.nuplets.Duo;

/**
 * Pairing by Cantor.
 * 
 * Argument for the inverse function is found in :
 * http://en.wikipedia.org/wiki/Cantor_pairing_function#Inverting_the_Cantor_pairing_function
 * 
 * @author joris
 *
 */
public class PairingCantor implements Pairing {

	public Duo<Long,Long> decompose(Long single){
		long w = (long) Math.floor(  ( Math.sqrt(8*single+1)-1 )/2  );
		long t = w*(w+1)/2;
		long y = (single - t);
		long x = (w - y);
		return Duo.d(x,y);
	}
	
	/**
	 * This is based on Cantor's polynomial that is a bijection from N to N X N.
	 * g(x,y)=((x+y)^2+x+3y)/2
	 * @param couple
	 * @return
	 */
	public Long compose(Duo<Long,Long> couple){
		long x = couple.get1();
		long y = couple.get2();
		long sum = x+y;
		return sum*(sum+1)/2 + y;
	}
	
}
