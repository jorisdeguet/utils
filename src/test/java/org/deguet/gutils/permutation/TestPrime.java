package org.deguet.gutils.permutation;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TestPrime {


	@Test(timeout =10000)
	public  void testCodec(){
		PrimeBase base = new PrimeBase();
		long n = 20;
		System.out.println("=========================================================   ");
		System.out.println("n    is    : "+n);
		int[] arr = base.to(n);
		long recov = base.from(arr);
		System.out.println("recov is   : "+recov);
		Assert.assertEquals(" Equality test  ",recov, n);
		int[] arr2 = base.to(recov);
		Assert.assertArrayEquals("Blop ", arr, arr2);
		for (long ll = 100 ; ll >=0; ll -= 1){
			System.out.println(ll+" ::: "+Arrays.toString(base.to(ll)));
			Assert.assertEquals("should always be equal ",ll, base.from(base.to(ll)));
		}
	}

	// test a combien on monterait si on représente
	@Test
	public void testGradeVote(){
		PrimeBase b = new PrimeBase();
		int[] test = {10, 10, 0, 0, 0, 0, 0, 0, 10};
		System.out.println(b.from(test));
	}

}
