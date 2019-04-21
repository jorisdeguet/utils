package org.deguet.gutils.permutation;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;


public class TestPermut {


	/**
	 * Tested from 0 to LONG.MAXVALUE -1
	 */
	@Test
	public void testFactBase(){
		FactorialBase base = new FactorialBase();
		Random r = new Random(3456789);
		for (long i = 0L ; i< 10000 ; i++){
			//System.out.println("=========================================================   ");
			long n = Math.abs(r.nextLong());
			Long[] arr = base.to(n);
			Long[] arr2 = base.to2(n);
			//System.out.println("arr  is    : "+FactorialBase.pp(arr));
			long recov = base.from(arr);
			long recov2 = base.from(arr2);
			//System.out.println("recov is   : "+recov);
			boolean equal = (recov2==n);
			//System.out.println(" Equality test  "+equal +(equal?"":"  "+recov+" != "+n));
			assertTrue(equal);
			assertTrue(recov == recov2);
		}
	}

	@Test
	public void testKnuthRandom(){
		Random r = new Random(3456789);
		for (int i = 0 ; i < 4 ; i++){
			Permutation p = KnuthGenerator.gen(r, 20);
			//System.out.println(p);
			//System.out.println("cycles "+p.cycles());
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLengthException(){
		Random r = new Random(3456789);
		Permutation p = KnuthGenerator.gen(r, 20);
		Assert.assertEquals(p.equals("bli"), false);
		p.permutArray(new Integer[]{1}, Integer.class);
	}


	/**
	 * Tested for k until 11.
	 * Then it grows very fast.
	 */
	@Test
	public void testLehmer(){
		int k = 9;
		LehmerCode code = new LehmerCode(k);
		long bound = fact(k);
		for (long n = 0L ; n< bound ; n=n+1L){
			//System.out.println("=========================================================   ");
			//System.out.println("n     is    : "+n);
			//System.out.println("bound is    : "+bound);
			Permutation p = code.longToPermut(n);
			//System.out.println("permut  is    : "+p);
			long recov = code.permutToLong(p);
			//System.out.println("recov   is   : "+recov);
			//System.out.println(" Equality test  "+equal +(equal?"":"  "+recov+" != "+n));
			assertTrue(recov == n);
		}
	}




	private long fact(long n){
		long result = 1;
		for (long factor = 2 ; factor <= n ; factor++){
			result *= factor;
		}
		return result;
	}

	public static String string(Object[] array){
		StringBuilder sb = new StringBuilder();
		for(Object o : array){
			sb.append(o+" ");
		}
		return sb.toString();
	}


	@Test
	public void testPermutGeneration(){
		PermutationGenerator gen = new PermutationGenerator(6);
		List<String> letters = new ArrayList<String>();
		letters.add("a");
		letters.add("b");
		letters.add("c");
		letters.add("d");
		letters.add("e");
		letters.add("f");
		//String[] array = new String[]{"a","b","c","d","e","f"};
		int count = 0 ;
		for (Permutation perm : gen){
			count++;
			long percentage = ((gen.getRemain().longValue()*100)/gen.getSize().longValue());
			//			System.out.println("Permut is "+perm + "    percentage is "+ percentage + 
			//					" applied " + perm.permutList(letters, String.class) + 
			//					" lehmer " +  (new LehmerCode(6)).permutToLong(perm));
		}
		Assert.assertEquals("il y en a  " , count, fact(6));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testPermutGenerationException(){
		PermutationGenerator gen = new PermutationGenerator(1);
	}

	@Test
	public void testPermutInverseArray(){
		PermutationGenerator gen = new PermutationGenerator(6);
		String[] array = new String[]{"a","b","c","d","e","f"};
		int count = 0 ;
		for (Permutation perm : gen){
			count++;
			Permutation inverse = perm.inverse();
			String[] a = perm.permutArray(array, String.class);
			String[] aa = inverse.permutArray(a, String.class);
			//System.out.println("original "+ Arrays.toString(array)+"  inv " + Arrays.toString(a)+"  back  " +Arrays.toString(aa));
			Assert.assertArrayEquals("Permut puis inverse  " , aa, array);
			String[] b = inverse.permutArray(array, String.class);
			String[] bb = perm.permutArray(b, String.class);
			Assert.assertArrayEquals("Permut puis inverse  " , bb, array);

		}
		Assert.assertEquals("il y en a  " , count, fact(6));
	}

	@Test
	public void testPermutInverseList(){
		PermutationGenerator gen = new PermutationGenerator(6);
		List<String> list = new ArrayList<String>();
		for (String s : new String[]{"a","b","c","d","e","f"}){
			list.add(s);
		}
		for (Permutation perm : gen){
			Permutation inverse = perm.inverse();
			List<String> a = perm.permutList(list, String.class);
			List<String> aa = inverse.permutList(a, String.class);
			//System.out.println("original "+ Arrays.toString(array)+"  inv " + Arrays.toString(a)+"  back  " +Arrays.toString(aa));
			Assert.assertEquals("Permut puis inverse  " , aa, list);
			List<String> b = inverse.permutList(list, String.class);
			List<String> bb = perm.permutList(b, String.class);
			Assert.assertEquals("Permut puis inverse  " , bb, list);

		}
	}

	@Test
	public void testPermutHashEquals(){
		PermutationGenerator gen = new PermutationGenerator(6);
		List<Permutation> list = new ArrayList<Permutation>();
		Set<Permutation> set = new HashSet<Permutation>();
		for (Permutation perm : gen){
			set.add(perm);
			list.add(perm);
		}
		PermutationGenerator gen2 = new PermutationGenerator(6);
		for (Permutation perm : gen2){
			set.add(perm);
			list.add(perm);
		}
		Assert.assertEquals("Permut puis inverse  " , set.size()*2, list.size());
	}

	@Test
	public void testCompose(){
		Permutation a = (new LehmerCode(7)).longToPermut(56L);
		Permutation b = (new LehmerCode(7)).longToPermut(76L);
		Permutation ab = a.compose(b);
		Permutation ba = a.composeLeft(b);
		Integer[] array = new Integer[]{1,2,3,4,5,6,7};
		Integer[] afterA = a.permutArray(array, Integer.class);
		//System.out.println("After  A " + Arrays.toString(afterA));
		Integer[] afterB = b.permutArray(array, Integer.class);
		//System.out.println("After  B " + Arrays.toString(afterB));

		Integer[] afterBoA = ab.permutArray(array, Integer.class);
		//System.out.println("After BoA" + Arrays.toString(afterBoA));

		Integer[] afterAoB = ba.permutArray(array, Integer.class);
		//System.out.println("After AoB" + Arrays.toString(afterAoB));

		Integer[] afterAB = a.permutArray(afterB, Integer.class);
		//System.out.println("After A&B" + Arrays.toString(afterAB));

		Integer[] afterBA = b.permutArray(afterA, Integer.class);
		//System.out.println("After B&A" + Arrays.toString(afterBA));
		Assert.assertArrayEquals(afterBA, afterBoA);
		Assert.assertArrayEquals(afterAB, afterAoB);

	}

	@Test
	public void testCycles(){
		Permutation a = (new LehmerCode(7)).longToPermut(2000L);
		System.out.println("Permutation "+a);
		Set<List<Integer>> cycles = a.cycles();
		System.out.println(cycles + "  " + a);
	}

	@Test
	public void testLehmer11(){
		LehmerCode code = new LehmerCode(20);
		long a = 12L;
		Permutation permut = code.longToPermut(a);
		//System.out.println(permut);
		//System.out.println("____________________________________________");
		long result = code.permutToLong(permut);
		Assert.assertEquals(a, result);
	}
	
	
}
