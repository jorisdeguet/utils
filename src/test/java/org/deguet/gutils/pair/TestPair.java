package org.deguet.gutils.pair;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.pairing.*;
import org.junit.Assert;
import org.junit.Test;

public class TestPair {

	@Test
	public void TestCantor(){
		Pairing pairing = new PairingCantor();
		testPairing(pairing, 0, 10);
	}

	@Test
	public void TestSquare(){
		Pairing pairing = new PairingSquare();
		testPairing(pairing,0, 100);
	}

	@Test
	public void TestBitInterleave(){
		Pairing pairing = new PairingBitInterleave();
		testPairing(pairing,0, 100);
	}

	@Test
	public void TestConcat(){
		Pairing pairing = new PairingConcat();
		testPairing(pairing,10000, 10200);
	}

	@Test
	public void TestHopcroft(){
		Pairing pairing = new PairingHopcroft();
		testPairing(pairing,1, 100);
		/*
		 * At some point the first element of the couple is smaller than the second one
		 * This should not happen, must be because of an arithmetic error.
		 * TODO investigate
		 */
	}

	private static void printGrid(Long[][] grid){
		for (int x = 0 ; x < grid[0].length ; x++ ){
			for (int y = 0 ; y < grid[0].length ; y++ ){
				System.out.print("|| "+grid[x][y]+" ");
			}
			System.out.println(" ||");
		}
	}


	private static void testPairing(Pairing pair, long from,long to){
		//printGrid(fillGrid(10, pair));
		for (long l1 = from ; l1 < to ; l1++){
			for (long l2 = from ; l2 < to ; l2++){
				Duo<Long,Long> one = Duo.d(l1,l2);
				Long two = pair.compose(one);
				//System.out.println(two +" is compose for "+l1+" and "+l2);
				Duo<Long,Long> thr = pair.decompose(two);
				Assert.assertEquals("pairs equal ", one, thr);
			}
		}


		for (long l = from ; l < to ; l = l + 10L){
			long one = l;
			//System.out.println("one is    " +one);
			Duo<Long,Long> two = pair.decompose(l);
			//System.out.println("two is    " +two);
			//System.out.println("pair is   " +pair);
			long thr = pair.compose(two);
			//System.out.println("three is  " +thr);
			Duo<Long,Long> fou = pair.decompose(thr);
			if (fou == null)
				System.out.println("fou is null " +fou);
			if (l % 10 == 0){
				//System.out.println("Max value for Long "+Long.MAX_VALUE);
				//System.out.println(" check point at    " +l);
			}
			if (one != thr || !two.equals(fou)){
				System.out.println("----------------------------------------");
				System.out.println("Long 1  is "+one);
				System.out.println("Couple1 is "+two);
				System.out.println("Long 2  is "+thr);
				System.out.println("Couple2 is "+fou);
			}
			Assert.assertEquals(one, thr);
			Assert.assertEquals(two, fou);
		}
	}

	private static void testOrderedPairing(OrderedPairing pair, long from, long to){
		//printGrid(fillGrid(10, pair));
		for (long l1 = from ; l1 < to ; l1++){
			for (long l2 = from ; l2 <= l1 ; l2++){
				Duo<Long,Long> one = Duo.d(l1,l2);
				//System.out.println(one +" is compose for "+l1+" and "+l2);
				Long two = pair.compose(one);
				System.out.println(two +" is compose for "+l1+" and "+l2);
				Duo<Long,Long> thr = pair.decompose(two);
				Assert.assertEquals("pair is recovered ", one, thr);
			}
		}
	}

	@Test
	public  void testSquareFilling(){
		PairingSquare pairing = new PairingSquare();
		System.out.println(pairing.showSquare(pairing.fill(25)));
	}
	
	
	public static void main (String[] args){
		Pairing pairing;
		pairing = new PairingHopcroft();
		for (long i = 0 ; i < 10 ; i++){
			System.out.println(i+" - "+pairing.decompose(i));
		}

	}

}
