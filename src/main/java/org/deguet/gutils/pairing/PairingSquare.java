package org.deguet.gutils.pairing;

import org.deguet.gutils.nuplets.Duo;

/**
 * Reflechir a un square pairing qui permette d'adresser facilement les cases d'une matrice:
 * 
 *   ... 12
 * 8 7 6 11
 * 3 2 5 10
 * 0 1 4  9
 * 
 * Two main cases:
 * - z is a square its coordinate are (sqrt(z),0)
 * - z is not a square, then it is stricty between a^2 and (a+1)^2
 *   you climb until you reach a^2 + a, then go left.
 * 
 * element that are a^2+a are on the diagonal.
 * 
 * @author joris
 *
 */
public class PairingSquare implements Pairing {

	public Long compose(Duo<Long,Long> couple) {
		long x = couple.get1();
		long y = couple.get2();
		long a = Math.max(x, y);
		if (a == x){
			return a*a + y;
		}
		else{
			return a*a + a + (a-x);
		}
	}

	public Duo<Long,Long> decompose(Long single) {
		long root = (long) Math.floor(Math.sqrt(single));
		if (single.longValue() == root*root){
			return Duo.d(root,0L);
		}
		else{
			long a2a = (root+1)*root;
			if (single.longValue() == a2a){
				return Duo.d(root,root);
			}
			else{
				if (single.longValue() < (root+1)*root){
					return Duo.d(root, single - root*root);
				}
				else{
					return Duo.d(root-(single-a2a), root);
				}
			}
		}
	}

	public long[][] fill(long until){
		Pairing pairing = new PairingSquare();
		int root = (int) Math.floor(Math.sqrt(until));
		long[][] result = new long[root+1][root+1];
		for(long l = 0 ; l <= until ; l++){
			Duo<Long,Long> couple = pairing.decompose(l);
			long x = couple.get1();
			long y = couple.get2();
			result[(int) x][(int) y] = l;
		}
		return result;
	}
	
	public String showSquare(long[][] square){
		StringBuilder sb = new StringBuilder();
		for (int x = 0 ; x < square.length ; x++ ){
			for (int y = 0 ; y < square.length ; y++ ){
				sb.append(String.format("|  %1$-8s ", square[x][y]));
			}
			sb.append("|\n");
		}
		return sb.toString();
	}
	
}
