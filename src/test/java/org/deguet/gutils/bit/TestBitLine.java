package org.deguet.gutils.bit;

import junit.framework.Assert;
import org.deguet.gutils.nuplets.Duo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;



public class TestBitLine 
{

	@Test
	public void testRandomSwitch()
	{
		Random r = new Random(1234567);
		BitLine a = new BitLine(1000);
		// create a changed line
		for (int i = 0 ; i < 10 ; i++){
			long index = r.nextInt((int)a.getSize());
			boolean bef = a.isOn(index);
			a = a.flipBits(index);
			boolean aft = a.isOn(index);
			Assert.assertEquals("should be the negation if ", !bef,aft);
		}	
	}
	
	@Test
	public void testComparable1()
	{
		TreeSet<BitLine> set = new TreeSet<BitLine>();
		Random r = new Random(1234567);
		// create a changed line
		for (int i = 0 ; i < 100 ; i++){
			BitLine b = BitLine.random(r, 20);
			set.add(b);
		}
		for (BitLine b : set){
			System.out.println(b.toRawBits());
		}
	}
	
	@Test
	public void testComparable2()
	{
		TreeSet<BitLine> set = new TreeSet<BitLine>();
		Random r = new Random(1234567);
		// create a changed line
		for (int i = 0 ; i < 10 ; i++){
			BitLine b = BitLine.random(r, 10+r.nextInt(20));
			set.add(b);
		}
		for (BitLine b : set){
			System.out.println(b.toRawBits());
		}
	}

	@Test
	public void testSwitchOnAndResize()
	{
		Random r = new Random(12567);
		// create a changed line
		for (int i = 0 ; i < 100 ; i++){
			BitLine a = new BitLine();
			int size = r.nextInt(100);
			a = a.set(size, true);
			Assert.assertEquals("should have resized ", size+1 , a.getSize());
			Assert.assertEquals("should be on ", true,a.isOn(size));
		}
	}

	@Test
	public void testSwitchOnAndResize2()
	{
		Random r = new Random(12567);
		// create a changed line
		List<Long> list = new ArrayList<Long>();
		for (int i = 0 ; i < 100000 ; i++){
			int index = r.nextInt(1000);
			list.add((long) index);
		}
		BitLine a = new BitLine();
		a = a.switchBits(true, list);
		r = new Random(12567);
		for (int i = 0 ; i < 100000 ; i++){
			int index = r.nextInt(1000);
			Assert.assertEquals("should be on ", true,a.isOn(index));
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIsOnOutOfBound(){
		BitLine a = new BitLine();
		System.out.println(a.isOn(-19));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIsOnOutOfBound2(){
		BitLine a = new BitLine(100);
		System.out.println(a.isOn(100));
	}

	@Test
	public void testInterleave()
	{
		for (int i = 0 ; i < 100 ; i++){
			BitLine a = randomWithSize(999, i);
			BitLine b = randomWithSize(999, i*17);
			BitLine inter = a.interleaveWith(b);
			Assert.assertEquals("double size of a", inter.getSize(), a.getSize()*2);
			Assert.assertEquals("double size of b", inter.getSize(), a.getSize()*2);
			Duo<BitLine,BitLine> deinter = inter.deinterleave();
			BitLine aa = deinter.get1();
			BitLine bb = deinter.get2();
			Assert.assertEquals("interleave deinterleave is identity", aa, a);
			Assert.assertEquals("interleave deinterleave is identity", bb, b);
		}
	}
	
	@Test
	public void testConcatPrefix()
	{
		Random r= new Random();
		for (int i = 0 ; i < 100 ; i++){
			BitLine a = randomWithSize(r.nextInt(100)+10, i);
			//System.out.println(a);
			BitLine b = randomWithSize(r.nextInt(100)+10, i*17);
			//System.out.println(b);
			BitLine concat = a.concat(b);
			//System.out.println(concat);
			BitLine prefix1 = a.commonPrefixWith(concat);
			BitLine prefix2 = concat.commonPrefixWith(a);
			Assert.assertEquals("size is sum of sizes", concat.getSize(), a.getSize()+b.getSize());
			Assert.assertEquals("a is prefix of concat", prefix1, a);
			Assert.assertEquals("concat is prefix of a", prefix2, a);
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testInterleaveException()
	{
		BitLine a = randomWithSize(99, 1256371);
		BitLine b = randomWithSize(999, 32);
		BitLine inter = a.interleaveWith(b);
	}

	@Test
	public void testIterator()
	{

		BitLine a = randomWithSize(999, 8745934);

		int index = 0;
		for (boolean b : a){
			boolean c = a.isOn(index);
			index++;
			Assert.assertEquals("iterator ", b, c);
		}
		Assert.assertEquals("same number of iterations ", index, a.getSize());
	}


	@Test
	public void testBitStringConversion()
	{
		for (int i = 0 ; i < 100 ; i++){
			BitLine a = randomWithSize(100, i);
			String str = a.toBits();
			BitLine rec = BitLine.fromBits(str);
			Assert.assertEquals("toBits fromBits is identity", rec, a);
		}
	}

	@Test
	public void testHexConversion()
	{
		for (int i = 0 ; i < 100 ; i++){
			BitLine a = randomWithSize(100, i);
			String str = a.toHex();
			BitLine rec = BitLine.fromHex(str);
			Assert.assertEquals("toHex fromHex is identity", rec, a);
		}
	}
	
	@Test
	public void testOctConversion()
	{
		for (int i = 0 ; i < 100 ; i++){
			BitLine a = randomWithSize(100, i);
			//System.out.println("original  " + a);
			String str = a.toOct();
			//System.out.println("oct  " + str);
			BitLine rec = BitLine.fromOct(str);
			//System.out.println("recoverd  " + rec);
			Assert.assertEquals("toOct fromOct is identity", rec, a);
		}
	}
	
	@Test
	public void testBase2Conversion()
	{
		for (int i = 0 ; i < 10 ; i++){
			BitLine a = randomWithSize(100, i);
			String str = a.toBase2(4);
			BitLine rec = BitLine.fromBase2(str,4);
			Assert.assertEquals("tobase2 frombase2 is identity", rec, a);
		}
	}

	@Test
	public void testReverse()
	{
		for (int i = 0 ; i < 100 ; i++){
			BitLine a = randomWithSize(100, i);
			BitLine rev = a.reverse();
			BitLine rec = rev.reverse();
			Assert.assertEquals("Reverse reverse is identity", rec, a);
		}

	}
	
	@Test
	public void testAllLines()
	{
		int size = 7;
		int n = 0;
		for (BitLine l : BitLine.allLinesForSize(size)){
			//System.out.println(l);
			n++;
		}
		Assert.assertEquals("Power of 2 ", n, (int)Math.pow(2, size));
	}
	
	@Test
	public  void testExplicitPrefix(){
		String longbits = "000100111101011111010001";
		String second =   "0001001111111011111010001";
		BitLine bits1 = BitLine.fromBits(longbits);
		BitLine bits2 = BitLine.fromBits(second);
		Assert.assertEquals("Should be identic  ", bits1.commonPrefixWith(bits2),BitLine.fromBits("0001001111"));
		bits1 =bits1.set(23, true);
		String rep = bits1.toBase2(3);
		BitLine rec = BitLine.fromBase2(rep,3);
		Assert.assertEquals("Should be identic  ", bits1,rec);
		//System.out.println(bits1.concat(bits1));
		BitLine b = bits1.interleaveWith(bits1.reverse());
		Duo<BitLine,BitLine> deinter = b.deinterleave();
		Assert.assertEquals("Should be mirrors  ", deinter.get1().reverse(), deinter.get2());
	}
	
	@Test
	public  void testNOT(){
		String a = "000100111101011111010001";
		String b = "111011000010100000101110";
		BitLine aa = BitLine.fromBits(a);
		BitLine bb = BitLine.fromBits(b);
		System.out.println(aa);
		System.out.println(aa.not());
		System.out.println(bb);
		System.out.println(bb.hashCode() +"   " +aa.not().hashCode());
		System.out.println(bb.equals(aa.not()));
		Assert.assertEquals(bb,aa.not());
	}
	
	@Test
	public  void testAND(){
		BitLine a = BitLine.fromBits("0101");
		BitLine b = BitLine.fromBits("0011");
		BitLine c = BitLine.fromBits("0001");
		Assert.assertEquals(a.and(b),c);
	}
	
	@Test
	public  void testOR(){
		BitLine a = BitLine.fromBits("0101");
		BitLine b = BitLine.fromBits("0011");
		BitLine c = BitLine.fromBits("0111");
		Assert.assertEquals(a.or(b),c);
	}
	
	@Test
	public  void testXOR(){
		BitLine a = BitLine.fromBits("0101");
		BitLine b = BitLine.fromBits("0011");
		BitLine c = BitLine.fromBits("0110");
		Assert.assertEquals(a.xor(b),c);
	}
	
	@Test
	public  void testBooleAlgebraOne(){
		Random r = new Random(21312);
		for (int i = 0 ; i < 200 ; i++){
			int size = 2+r.nextInt(200);
			BitLine x = BitLine.random(r, size);
			Assert.assertEquals(x.and(x), x);
			Assert.assertEquals(x.or(x), x);
			Assert.assertEquals(x.xor(x), BitLine.zeros(size));
			
			Assert.assertEquals(x.and(x.not()), BitLine.zeros(size));
			Assert.assertEquals(x.or(x.not()), BitLine.ones(size));
		}
	}
	
	@Test
	public  void testBooleAlgebraWithTwo(){
		Random r = new Random(21312);
		for (int i = 0 ; i < 200 ; i++){
			int size = 2+r.nextInt(200);
			BitLine x = BitLine.random(r, size);
			BitLine y = BitLine.random(r, size);
			Assert.assertEquals(x.and(y), y.and(x));
			Assert.assertEquals(x.or(y), y.or(x));
			Assert.assertEquals(   (x.or(y)).not()  ,   x.not().and(y.not())   );
		}
	}

	private BitLine randomWithSize(int size, int seed){
		Random r = new Random(seed);
		BitLine result = new BitLine(size);
		Long[] indexes = new Long[size];
		for (int i = 0 ; i < size ; i++){
			int index = r.nextInt(size);
			indexes[i] = (long)index;
		}
		return result.flipBits(indexes);
	}
	
	
	
}

