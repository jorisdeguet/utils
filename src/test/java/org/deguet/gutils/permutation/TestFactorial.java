package org.deguet.gutils.permutation;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TestFactorial {

	
	@Test(timeout =10000)
	public  void testCodec(){
		FactorialBase base = new FactorialBase();
		long n = 20;
		System.out.println("=========================================================   ");
		System.out.println("n    is    : "+n);
		Long[] arr = base.to(n);
		System.out.println("arr  is    : "+FactorialBase.toString(arr));
		Long[] arr2 = base.to2(n);
		System.out.println("arr  is    : "+FactorialBase.toString(arr2));
		long recov = base.from(arr);
		System.out.println("recov is   : "+recov);
		Assert.assertEquals(" Equality test  ",recov, n);
		Assert.assertTrue("Blop ", Arrays.equals(arr, base.to(recov)));
		for (long ll = 0 ; ll < 100000000 ; ll += 1000000){
			//System.out.println("Encoded "+Arrays.toString(base.to(ll)));
			Assert.assertEquals("should always be equal ",ll, base.from(base.to(ll)));
		}
	}
	
}
