package org.deguet.gutils.pairing;

import org.deguet.gutils.nuplets.Duo;

/**
 * Inspired by Steven Pigeon in his dissertation
 * Only works on 32 bits source to go in a 64 bits result.
 * 
 * It works by interleaving bits of two integers in one and doing the converse operations backwards.
 * 
 * @author joris
 *
 */
public class PairingBitInterleave implements Pairing{

	public Long compose(Duo<Long,Long> couple) {
		long first  = couple.get1();
		long second = couple.get2();
		long result = 0L;
		for (int i = 31 ; i >= 0 ; i--){
			long firstbit = first & (1L << i);
			result = result | (firstbit << i);
			//result = result << 1;
			long secondbit = second & (1L << i);
			result = result | (secondbit<<(i+1));
			//result = result << 1;
		}
		return result;
	}

	public Duo<Long,Long> decompose(Long single) {
		long bits = single;
		long first = 0;
		long second = 0;
		int i = 0;
		// we go along the 32 bits of the two decomposed but we stop if we reach a 0 value
		while (bits != 0 && i<32 ){
			long bitfirst = bits & 1L;
			bits = bits>>1;
			long bitsecond = bits & 1L;
			bits = bits>>1;
			first = (int) (first | (bitfirst << i));
			second = (int) (second | (bitsecond << i));
			i++;
		}
		return Duo.d(first,second);
	}
}
