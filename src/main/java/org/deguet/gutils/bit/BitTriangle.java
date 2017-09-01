package org.deguet.gutils.bit;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.pairing.Pairing;
import org.deguet.gutils.pairing.PairingCantor;

/**
 * IMMUTABLE
 * Represents a bit triangle:
 * 
 * .
 * ..
 * ...
 * ....
 * .....
 * 
 * In this the first coordinate is always greater or equal to the second one.
 * The internal representation if a long[].
 * 
 * @author joris
 *
 */
public final class BitTriangle {

	private Pairing pair  = new PairingCantor();

	private BitLine bits;

	private int size;

	/**
	 * The unique constructor for this class. Non empty instances are obtained by applying the method on it.
	 */
	public BitTriangle(){
		this.bits = new BitLine();
		this.size = 0;
	}

	private BitTriangle(BitLine l, int size2) {
		this.size = size2;
		this.bits = l;
	}

	/**
	 * Tells if this triangle's bit for (a,b) is one.
	 * @param a
	 * @param b
	 * @return true is bit is one, false if bit is 0
	 */
	public boolean isCon(int a, int b){
		if (a < b ) throw new IllegalArgumentException("first index should be greater than second");
		if (!(a < size) || b < 0) throw new IllegalArgumentException("index "+a+","+b+" should be in [0,"+(size-1)+"]");
		long bitIndex = pair.compose(Duo.d((long)a,(long)b));
		return bits.isOn(bitIndex);
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < size ; i++){
			sb.append((i%10)+" ");
			for(int j = 0 ; j < i+1 ; j++){
				if (i>=j && this.isCon(i, j))
				{sb.append("+");}
				else                
				{sb.append("-");}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Sets the bit for indices (a,b) to 0.
	 * @param a
	 * @param b
	 * @return
	 */
	public BitTriangle disconnect(int a, int b) {
		if (a < b ) throw new IllegalArgumentException("first index should be greater than second");
		if (!(a < size) || b < 0) throw new IllegalArgumentException("index "+a+","+b+" should be in [0,"+(size-1)+"]");
		// Build a copy of matrix
		long bitIndex = pair.compose(Duo.d((long)a, (long)b));
		BitLine newLine = bits.set(bitIndex,false);
		return new BitTriangle(newLine,size);
	}

	/**
	 * larger coord always first
	 * @param indexA
	 * @param indexB
	 * @return
	 */
	public BitTriangle connect(int a, int b) {
		// Build a copy of matrix
		if (a < b ) throw new IllegalArgumentException("first index should be greater than second");
		if (!(a < size) || b < 0) throw new IllegalArgumentException("index "+a+","+b+" should be in [0,"+(size-1)+"]");
		long bitIndex = pair.compose(Duo.d((long)a, (long)b));
		BitLine newLine = bits.set(bitIndex,true);
		return new BitTriangle(newLine,size);
	}

	private long bitsSize(long s){
		return s*(s+1)/2;
	}
	
	public long numberOfBits(){
		return bits.numberOfBitsOn();
	}

	public BitTriangle incrementSize(int n) {
		// compute new size and then new necessary length of matrix
		if (n < 0) throw new IllegalArgumentException("n should be positive");
		return changeSize(n);
	}
	
	private BitTriangle changeSize(int n) {
		// compute new size and then new necessary length of matrix
		if (n == 0) return this;
		long newSize = size + n;
		long newMatrixLength = pair.compose(Duo.d(newSize,newSize));
		BitLine newL = bits.augmentSize(newMatrixLength);
		//System.out.println(newMatrixLength+"  for  "+newSize);
		return new BitTriangle(newL,(int)newSize);
	}

	public BitTriangle decrementSize(int n) {
		if (n < 0) throw new IllegalArgumentException("n should be positive");
		return changeSize(-n);
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
		BitTriangle other = (BitTriangle) obj;
		if (bits == null) {
			if (other.bits != null)
				return false;
		} else if (!bits.equals(other.bits))
			return false;
		return true;
	}

}
