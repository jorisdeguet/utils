package org.deguet.gutils.bit;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.pairing.PairingSquare;

/**
 * IMMUTABLE.
 * A class for square bit matrices. 
 * 
 * @author joris
 *
 */
public final class BitSquare {

	private final PairingSquare pair  = new PairingSquare();

	private final BitLine bits;

	private final int size;

	public BitSquare(){
		this.bits = new BitLine();
		this.size = 0;
	}

	private BitSquare(BitLine l, int s) {
		this.size = s;
		this.bits = l;
		System.out.println("size line " +l.getSize()+"  square " +s);
	}

	public boolean isCon(int a, int b){
		if (a < 0 || b < 0 || a >= size || b >= size){
			throw new IllegalArgumentException("problem with dimensions "+a+" "+b+"  for "+size);
		}
		long bitIndex = 
				pair.compose(Duo.d((long)a,(long)b));
		return bits.isOn(bitIndex);
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < size ; i++){
			for(int j = 0 ; j < size ; j++){
				if (this.isCon(i, j)){sb.append("*");}
				else                {sb.append(".");}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public BitSquare disconnect(int a, int b) {
		if (a < 0 || b < 0 || a >= size || b >= size){
			throw new IllegalArgumentException("problem with dimensions "+a+" "+b+"  for "+size);
		}
		// Build a copy of matrix
		// Change the element at the couple position
		long bitIndex = pair.compose(Duo.d((long)a, (long)b));
		BitLine newLine = bits.set(bitIndex, false);
		return new BitSquare(newLine,size);
	}

	public BitSquare connect(int a, int b) {
		if (a < 0 || b < 0 || a >= size || b >= size){
			throw new IllegalArgumentException("problem with dimensions "+a+" "+b+"  for "+size);
		}
		// Change the element at the couple position
		long bitIndex = 
				pair.compose(Duo.d((long) a,(long) b));
		BitLine newLine = bits.set(bitIndex, true);
		return new BitSquare(newLine,size);
	}

	public BitSquare incrementSize() {
		// compute new size and then new necessary length of matrix
		return this.incrementSize(1);
	}

	/**
	 * Can increase or decrease the size of the bit square
	 * @param n
	 * @return
	 */
	public BitSquare incrementSize(int n) {
		// compute new size and then new necessary length of matrix
		int newSize = size + n;
		int newMatrixLength = newSize*newSize;
		BitLine newLine = bits.augmentSize(newMatrixLength);
		// Change the element at the couple position
		return new BitSquare(newLine,newSize);
	}

	
	public long getSize(){
		return size;
	}

	@Override
	public int hashCode() {
		return bits.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BitSquare other = (BitSquare) obj;
		if (bits == null) {
			if (other.bits != null)
				return false;
		} else if (!bits.equals(other.bits))
			return false;
		return true;
	}
}
