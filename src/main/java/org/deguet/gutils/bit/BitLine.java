
package org.deguet.gutils.bit;

import org.deguet.gutils.nuplets.Duo;

import java.util.*;

/**
 * Used to provide an immutable alternative to BitSet in Java.
 * 
 * Wrong index exception are only thrown when accessing
 * 
 * Setting a bit out of the bounds is understood as an implicit resize
 * 
 * ALL OPERATIONS THAT WRITE OR CAN SUFFER FROM WRITES (ITERATOR) ARE HERE
 * 
 * SHOULD BE CHANGED TO SUPPORT 2 MODES
 * 
 * @author joris deguet
 *
 */
public final class BitLine implements Iterable<Boolean>, Comparable<BitLine>{

	private final long[] line;

	private final static long LONGSIZE = 64;

	private final long size;

	public BitLine(){
		this.line = new long[0];
		this.size = 0;
	}

	/**
	 * Creates the immutable instance of BitLine from the provided long array.
	 * @param newMat
	 * @param size2
	 */
	private BitLine(long[] newMat, long size2) {
		this.size = size2;
		this.line = newMat;
	}

	/**
	 * Instantiate a BitLine of the given size.
	 * @param size
	 */
	public BitLine(long size) {
		this.size = size;
		this.line = new long[(int) ((size/LONGSIZE)+1)];
	}

	/**
	 * indicates if the ith bit is on or off
	 */
	public boolean isOn(long i){
		if (i > -1 && i < size){
			long bitIndex = i;
			long wordIndex = bitIndex / LONGSIZE;
			long inWordIndex = bitIndex % LONGSIZE;
			long word = line[(int) wordIndex];
			// Used to be a bug here as I used 1 instead of 1L (this was making it a int)
			// Therefore a cyclic behavior was observed, bit level is touchy
			long mask = (1L << inWordIndex);
			return (mask & word) != 0L;
		}
		throw new IllegalArgumentException(i+" not in [0,"+(size-1)+"]");
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < size ; i++){
			if (this.isOn(i)){sb.append("*");}
			else             {sb.append(".");}
		}
		return sb.toString();
	}

	/**
	 * Simple alias to numberOfBitsOn for compliance with BitSet
	 * @return
	 */
	public long cardinality(){
		return this.numberOfBitsOn();
	}

	public BitLine and(BitLine b){
		BitLine res = this;
		for (int i = 0 ; i < this.getSize() ; i++){
			res = res.set(i,this.isOn(i) && b.isOn(i)); 
		}
		return res;
	}

	public BitLine or(BitLine b){
		BitLine res = this;
		for (int i = 0 ; i < this.getSize() ; i++){
			res = res.set(i,this.isOn(i) || b.isOn(i)); 
		}
		return res;
	}
	
	public BitLine xor(BitLine b){
		BitLine res = this;
		for (int i = 0 ; i < this.getSize() ; i++){
			res = res.set(i,this.isOn(i) ^ b.isOn(i)); 
		}
		return res;
	}
	
	public BitLine not(){
		long[] newMat = new long[this.line.length];
		System.arraycopy(line, 0, newMat, 0, line.length);
		for (int i = 0 ; i < newMat.length-1 ; i++){
			newMat[i] = ~this.line[i]; 
		}
		BitLine res = new BitLine(newMat, size);
		// specific handling for the last long
		for(long i = (newMat.length-1)*LONGSIZE ; i < size ; i++){
			res = res.set(i, !res.isOn(i));
		}
		return res;
	}
	
	public static BitLine zeros(long size){
		return new BitLine(size);
	}
	
	public static BitLine ones(long size){
		return new BitLine(size).not();
	}

	/**
	 * Return a sting hexadecimal representation of the bitline.
	 * 0 indexed bit if the leftmost.
	 * @return
	 */
	public String toHex(){
		StringBuilder sb = new StringBuilder();
		sb.append(size+".16.");
		// this is 4 bits for one hex
		for (int index =  0 ; index < size ; index = index + 4){
			int value = bitVal(index) + bitVal(index+1)*2 + bitVal(index+2)*4 + bitVal(index+3)*8;
			sb.append(Integer.toHexString(value));
		}
		return sb.toString();
	}


	/**
	 * produces a base 8 representation of this bitline
	 * @return
	 */
	public String toOct(){
		StringBuilder sb = new StringBuilder();
		sb.append(size+".8.");
		// this is 4 bits for one hex
		for (int index =  0 ; index < size ; index = index + 3){
			int value = bitVal(index) + bitVal(index+1)*2 + bitVal(index+2)*4;
			sb.append(Integer.toOctalString(value));
		}
		return sb.toString();
	}



	/**
	 * Returns a representation of this number in the corresponding power of 2
	 * Example toBase2(4) represents the number in the 2^4 base meaning 16
	 * @param power
	 * @return
	 */
	public String toBase2(int power){
		StringBuilder sb = new StringBuilder();
		sb.append(size+"."+power+".");
		for (int index =  0 ; index < size ; index = index + power){
			int value = 0;
			for (int comp = 0 ; comp < power; comp++){
				value += bitVal(index+comp) * Math.pow(2,comp);
			}
			sb.append(Integer.toString(value,(int) Math.pow(2, power)));
		}
		return sb.toString();
	}

	/**
	 * Returns the base 2 representation of the bitline
	 * @return
	 */
	public String toBits(){
		StringBuilder sb = new StringBuilder();
		sb.append(size+".2.");
		// this is 4 bits for one hex
		sb.append(this.toRawBits());
		return sb.toString();
	}

	public String toRawBits() {
		StringBuilder sb = new StringBuilder();
		for (int index =  0 ; index < size ; index++){
			sb.append(bitVal(index));
		}
		return sb.toString();
	}

	/**
	 * Does not check index range (return 0 instead for appropriate chunk in conversions), only for private use.
	 * @param index
	 * @return
	 */
	private int bitVal(int index){
		if (index >= size) return 0;
		return this.isOn(index)?1:0;
	}

	public long getSize(){
		return size;
	}


	public static BitLine random(Random r, int size){
		BitLine line = new BitLine(size);
		Long[] toswap = new Long[size/2];
		for (int i = 0 ; i < size/2 ; i++){
			toswap[i] = (long) r.nextInt(size);
		}
		return line.flipBitsIndexes(toswap);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(line);
		result = (int) (prime * result + size);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BitLine other = (BitLine) obj;
		if (!Arrays.equals(line, other.line))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	public BitLine switchBits(boolean bitValue, List<Long> list) {
		Long max = Collections.max(list);
		// create room for the new one
		if (max+1 > size) return this.augmentSize(max+1).switchBits(bitValue, list);
		// Build a copy of matrix
		long[] newMat = new long[this.line.length];
		System.arraycopy(line, 0, newMat, 0, line.length);
		for (long bitIndex : list){
			long wordIndex = bitIndex / 64;
			long inWordIndex = bitIndex % 64;
			long word = newMat[(int) wordIndex];
			long mask = 1L << inWordIndex;
			long newWord = (bitValue?(word | mask):word & (~mask));
			newMat[(int) wordIndex] = newWord;
		}
		return new BitLine(newMat,size);
	}

	public BitLine setBits(boolean bitValue, Long... indexes) {
		return switchBits(bitValue,Arrays.asList(indexes));
	}

	public BitLine flipBitsIndexes(Long[] indexes) {
		Long max = Collections.max(Arrays.asList(indexes));
		// create room for the new one
		if (max+1 > size) return this.augmentSize(max+1).flipBitsIndexes(indexes);
		// Build a copy of matrix
		long[] newMat = new long[this.line.length];
		System.arraycopy(line, 0, newMat, 0, line.length);
		//System.out.println(Arrays.toString(newMat));
		for (long index : indexes){
			long bitIndex = index;
			long wordIndex = bitIndex / LONGSIZE;
			long inWordIndex = bitIndex % LONGSIZE;
			long word = newMat[(int) wordIndex];
			long mask = 1L << inWordIndex;
			long newWord = ((mask & word) == 0L?(word | mask):word & (~mask));
			//System.out.println(Arrays.toString(newMat));
			newMat[(int) wordIndex] = newWord;
		}
		//System.out.println(Arrays.toString(newMat));
		return new BitLine(newMat,size);
	}

	public BitLine flipBits(Long... indexes) {
		return this.flipBitsIndexes(indexes);
	}

	private BitLine setOn(long index){
		return this.setBits(true, index);
	}

	public BitLine reverse(){
		BitLine res = new BitLine(this.size);
		for(int i = 0 ; i < this.size ; i++){
			res = res.set(size-1-i, this.isOn(i));
		}
		return res;
	}

	public BitLine concat(BitLine sequel){
		BitLine result = this;
		for(int i = 0; i < sequel.getSize() ; i++){
			result = result.set(this.size+i, sequel.isOn(i));
		}
		return result;
	}


	public BitLine interleaveWith(BitLine other){
		if (this.getSize() != other.getSize()){
			throw new IllegalArgumentException(
					"Cannot interleave two bitlines if they do not have the same size"
					);
		}
		BitLine res = new BitLine(this.getSize()*2);
		for (int i = 0 ; i < this.getSize() ; i++){
			res = res.set(2*i, this.isOn(i)).set(2*i+1, other.isOn(i));
		}
		return res;
	}


	public Duo<BitLine,BitLine> deinterleave(){
		BitLine one = new BitLine(this.getSize()/2);
		BitLine two = new BitLine(this.getSize()/2);
		for (int i = 0 ; i<this.getSize();i++){
			if (i%2==0) one = one.set(i/2, this.isOn(i));
			else two = two.set(i/2, this.isOn(i));
		}
		return Duo.d(one,two);
	}

	/**
	 * Could be optimized to have only one array allocation
	 * @param bl
	 * @return
	 */
	public BitLine commonPrefixWith(BitLine bl){
		// go through the shortest bitline
		long shortestSize = Math.min(this.getSize(), bl.getSize());
		BitLine prefix = new BitLine();
		int i = 0;
		while(i< shortestSize && bl.isOn(i) == this.isOn(i)){
			prefix = prefix.set(i, this.isOn(i));
			i++;
		}
		return prefix;
	}

	public BitLine incrementSizeOf(int extraBits) {
		// compute new size and then new necessary length of matrix
		long newSize = size + extraBits;
		return this.augmentSize(newSize);
	}

	public BitLine augmentSize(long l) {
		// compute new size and then new necessary length of matrix
		int newLineLength = (int) ((l / LONGSIZE) + 1L);
		// Build a copy of matrix
		long[] newMat = new long[newLineLength];
		System.arraycopy(line, 0, newMat, 0, line.length);
		// Change the element at the couple position
		return new BitLine(newMat,l);
	}

	public BitLine set(long index, boolean val) {
		if (index >= this.size){
			return this.augmentSize(index+1).set(index, val);
		}
		return setBits(val,index);
	}

	public static BitLine fromBase2(String source, int power){
		//System.out.println("from base "+source+" base "+power);
		String[] split = source.split("\\."+power+"\\.");
		//System.out.println(Arrays.toString(split));
		BitLine res = new BitLine(Integer.parseInt(split[0]));
		int index = 0;
		for (Character ch : split[1].toCharArray()){
			Integer value = Integer.parseInt(ch+"", (int) Math.pow(2, power));
			int pow = 1;
			for (int comp = 0 ; comp < power; comp++){
				int bitIndex = index*power + comp;
				if (value/pow % 2 == 1 && bitIndex < res.getSize()) {
					res = res.setOn(bitIndex);
				}
				pow *= 2;
			}
			index++;
		}
		return res;
	}

	public static BitLine fromOct(String oct){
		String[] split = oct.split("\\.8\\.");
		BitLine res = new BitLine(Integer.parseInt(split[0]));
		long index = 0;
		for (Character ch : split[1].toCharArray()){
			Integer value = Integer.parseInt(ch+"", 8);
			if (value % 2 == 1) res = res.setBits(true,index*3);
			if (value/2 % 2 == 1) res = res.setBits(true, index*3+1);
			if (value/4 % 2 == 1) res = res.setBits(true, index*3+2);
			index++;
		}
		return res;
	}

	public Iterator<Boolean> iterator() {
		return new It(this);
	}


	public static BitLine fromBits(String bitline){
		String bits = bitline;
		if (bitline.contains(".2.")){
			String[] split = bitline.split("\\.2\\.");
			bits = split[1];
		}
		BitLine res = new BitLine(bits.length());
		int index = 0;
		for (Character ch : bits.toCharArray()){
			if (ch.equals('1')){
				res = res.setOn(index);
			}
			index++;
		}
		return res;
	}

	public static BitLine fromHex(String hex){
		String[] split = hex.split("\\.16\\.");
		BitLine res = new BitLine(Integer.parseInt(split[0]));
		int index = 0;
		for (Character ch : split[1].toCharArray()){
			Integer value = Integer.parseInt(ch+"", 16);
			if (value % 2 == 1) res = res.setOn(index*4);
			if (value/2 % 2 == 1) res = res.setOn(index*4+1);
			if (value/4 % 2 == 1) res = res.setOn(index*4+2);
			if (value/8 % 2 == 1) res = res.setOn(index*4+3);
			index++;
		}
		return res;
	}


	private class It implements Iterator<Boolean>{

		int index;
		BitLine line;
		public It(BitLine line){
			this.line = line;
			this.index = 0;
		}

		public boolean hasNext() {
			return index < line.getSize();
		}

		public Boolean next() {
			return line.isOn(index++);
		}
		// nothing as it is immutable
		public void remove() {}
	}


	public long numberOfBitsOn() {
		long result = 0;
		for (Long l : this.line){
			result += Long.bitCount(l);
		}
		return result;
	}

	/**
	 * uses some kind of alphabetical order on bitlines
	 */
	public int compareTo(BitLine o) {
		long size = Math.min(this.getSize(), o.getSize());
		for (int i = 0 ; i < size ; i++){
			if (this.isOn(i) != o.isOn(i)){
				int a = this.isOn(i)?1:0;
				int b =    o.isOn(i)?1:0;
				return a-b;
			}
		}
		// there one is the prefix of the other.
		if (this.getSize() < o.getSize()) return -1;
		if (this.getSize() > o.getSize()) return 1;
		return 0;
	}

	public static Iterable<BitLine> allLinesForSize(final int size){
		return new Iterable<BitLine>(){
			public Iterator<BitLine> iterator() {
				return new Iterator<BitLine>(){

					BitLine l = new BitLine(size);
					boolean done = false;
					public boolean hasNext() {return !done;}

					public BitLine next() {
						BitLine r = l;
						long index = 0;
						while (index < l.size && r.isOn(index)){
							l = l.flipBits(index);
							index++;
						}
						if (index == l.size) {
							done = true;
							return r;
						}
						l = l.setOn(index);
						return r;
					}
					public void remove() {}
				};
			}
		};		
	}

}
