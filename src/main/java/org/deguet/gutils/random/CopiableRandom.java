package org.deguet.gutils.random;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class's aim is to provide a rand gen with a copy constructor.
 * This is useful to have a random generator that gives two offsprings that will behave
 * in the same way.
 * 
 * 
 * 
 * Initially tried to simply extend Random and add a copy constructor but the code uses Unsafe.
 * This was raising a security exception.
 * 
 * This behaves exactly like a Random object as soon as the same seed is provided.
 * 
 * Then I removed All I didn't use and made the seed a simple long.
 * I resynchronized the next() method which is not the case in the original Random class.
 * 
 * All other methods are built upon next(int n).
 * 
 * @author malcolm deguet
 *
 */
public final class CopiableRandom implements Serializable{

	/** use serialVersionUID from JDK 1.1 for interoperability */
	static final long serialVersionUID = 3905348978240129619L;

	/**
	 * The internal state associated with this pseudorandom number generator.
	 * (The specs for the methods in this class describe the ongoing
	 * computation of this value.)
	 *
	 * @serial
	 */
	private long seed;

	private final static long multiplier = 0x5DEECE66DL;
	private final static long addend = 0xBL;
	private final static long mask = (1L << 48) - 1;

	/**
	 * Creates a new random number generator. This constructor sets
	 * the seed of the random number generator to a value very likely
	 * to be distinct from any other invocation of this constructor.
	 */
	public CopiableRandom() { this(++seedUniquifier + System.nanoTime()); }
	private static volatile long seedUniquifier = 8682522807148012L;

	/**
	 * Builds a generator from the given seed.
	 * @param seed
	 */
	public CopiableRandom(long seed) {
		setSeed(seed);
	}

	
	synchronized private void setSeed(long sed) {
		sed = (sed ^ multiplier) & mask;
		this.seed = sed;
	}

	private synchronized int next(int bits) {
		long oldseed = this.seed;
		long nextseed = (oldseed * multiplier + addend) & mask;
		this.seed = nextseed;
		return (int)(nextseed >>> (48 - bits));
	}


	/**
	 * Returns the next array of random bytes.
	 * @param bytes
	 */
	public void nextBytes(byte[] bytes) {
		for (int i = 0, len = bytes.length; i < len; )
			for (int rnd = nextInt(),
					n = Math.min(len - i, Integer.SIZE/Byte.SIZE);
			n-- > 0; rnd >>= Byte.SIZE)
				bytes[i++] = (byte)rnd;
	}

	/**
	 * Returns the next int within this type's bounds.
	 * @return
	 */
	public int nextInt() {
		return next(32);
	}

	/**
	 * Returns the next integer between 0 included and n excluded.
	 * @param n the positive argument.
	 * @return the next integer within bounds
	 */
	public int nextInt(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("n must be positive");

		if ((n & -n) == n)  // i.e., n is a power of 2
			return (int)((n * (long)next(31)) >> 31);

		int bits, val;
		do {
			bits = next(31);
			val = bits % n;
		} while (bits - val + (n-1) < 0);
		return val;
	}

	/**
	 * Returns the next double for this Generator.
	 * Result is x such that 0.0 <= x < 1.0
	 * @return the next double between 0.0 and 1.0
	 */
	public double nextDouble() {
        long l = ((long)(next(26)) << 27) + next(27);
        return l / (double)(1L << 53);
    }

	/**
	 * Returns the next float for this Generator.
	 * Result is x such that 0.0 <= x < 1.0
	 * @return the next float between 0.0 and 1.0
	 */
	public float nextFloat() {
		int i = next(24);
		return i / ((float)(1 << 24));
	}

	/**
	 * The next boolean.
	 * @return the next random boolean.
	 */
	public boolean nextBoolean() {
		return next(1) != 0;
	}

/////////////////////////////// Joris implements a copy constructor:
	
	/**
	 * Makes a copy of this generator that will behave as the original.
	 * Without sharing any data.
	 */
	public CopiableRandom(CopiableRandom model){
		this.seed = model.seed;
	}
	
	public CopiableRandom(Random r){
		
		
		// use reflection to get the seed
		try {
			for (Field f : r.getClass().getDeclaredFields()){
				//System.out.println("f : " + f);
				if (f.getName().equals("seed")){
					// make the seed accessible
					f.setAccessible(true);
					AtomicLong seed = (AtomicLong)f.get(r);
					//System.out.println("SEED                 : " + seed);
					this.seed = seed.longValue();
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public Random asRandom(){
		return new Wrapper(this);
	}
	
	class Wrapper extends Random{
		CopiableRandom source;
		public Wrapper(CopiableRandom c){
			source = c;
		}
		
		protected int next(int bits) {
			return source.next(bits);
		}
	}
}
