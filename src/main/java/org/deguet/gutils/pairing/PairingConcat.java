package org.deguet.gutils.pairing;

import org.deguet.gutils.nuplets.Duo;

public class PairingConcat implements Pairing{

	public static long mask = Long.parseLong("00000000FFFFFFFF",16);

	private static final boolean debug = false;

	public Long compose(Duo<Long, Long> couple) {
		long l1 = couple.get1();
		long l2 = couple.get2();
		return compose(l1,l2);
	}
	
	public Long compose(Long l1, Long l2) {
		long result = (l1<<32)|l2;
		if (debug) System.out.println(Long.toHexString(l1)+"    "+Long.toHexString(l2)+"  compound   "+Long.toHexString(result));
		return result;
	}

	public Duo<Long, Long> decompose(Long single) {
		long l1 = last32bits(single >> 32);
		long l2 = last32bits(single);
		if (debug) System.out.println("  compound   "+Long.toHexString(single)+"   "+Long.toHexString(l1)+"    "+Long.toHexString(l2));
		return Duo.d(l1,l2);
	}

	private long last32bits(long source){
		return source & mask;
	}
}
