package org.deguet.gutils.bit;

import org.junit.Assert;
import org.junit.Test;

public class TestBitTriangle {

	@Test
	public void testSwitchOn(){
		BitTriangle t = new BitTriangle();
		t = t.incrementSize(10);
		t = t.connect(4, 1);
		t = t.connect(0, 0);
		t = t.connect(7, 2);
		//t = t.connect(2, 7);
		t = t.connect(9, 9);
		Assert.assertTrue(t.isCon(7, 2));
	}
	
	@Test
	public void testHowManyOn(){
		BitTriangle t = new BitTriangle();
		t = t.incrementSize(10);
		t = t.connect(4, 1);
		t = t.connect(0, 0);
		t = t.connect(7, 2);
		t = t.connect(9, 9);
		Assert.assertEquals(t.numberOfBits(),4);
	}
	
	@Test
	public void testHashEquals(){
		BitTriangle t = new BitTriangle();
		BitTriangle t1 = new BitTriangle();
		BitTriangle t2 = new BitTriangle();
		t1 = t.incrementSize(10);
		t2 = t.incrementSize(10);
		t1 = t1.connect(4, 1);
		t2 = t2.connect(4, 1);
		t1 = t1.connect(0, 0);
		t2 = t2.connect(0, 0);
		t1 = t1.connect(7, 2);
		t2 = t2.connect(7, 2);
		t1 = t1.connect(9, 9);
		t2 = t2.connect(9, 9);
		Assert.assertEquals(t1.hashCode(),t2.hashCode());
		Assert.assertTrue(t1.equals(t2));
	}

	
	@Test
	public void testSwitchOnOff(){
		BitTriangle t = new BitTriangle();
		t = t.incrementSize(10);
		t = t.connect(4, 1);
		Assert.assertTrue(t.isCon(4, 1));
		t = t.disconnect(4, 1);
		Assert.assertTrue(!t.isCon(4, 1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSwitchExceptionIsCon(){
		BitTriangle t = new BitTriangle();
		t = t.incrementSize(10);
		t = t.connect(4, 1);
		t = t.connect(0, 0);
		t = t.connect(7, 2);
		
		t = t.connect(9, 9);
		t.isCon(2, 7);
		System.out.println(t);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSwitchOnexception(){
		BitTriangle t = new BitTriangle();
		t = t.incrementSize(10);
		t = t.connect(4, 1);
		t = t.connect(0, 0);
		//t = t.connect(7, 2);
		t = t.connect(2, 7);		// first index always greater than second one
		t = t.connect(9, 9);
		System.out.println(t);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSwitchOnTooLarge(){
		BitTriangle t = new BitTriangle();
		t = t.incrementSize(10);
		t = t.connect(4, 1);
		t = t.connect(0, 0);
		t = t.connect(10, 9);
		System.out.println(t);
	}
	
}
