package org.deguet.gutils.bit;

import junit.framework.Assert;
import org.junit.Test;




public class TestBitSquare 
{



	@Test
	public void testRandomSet()
	{
		BitSquare s = new BitSquare();
		s = s.incrementSize(5);
		s = s.connect(3, 4);
		Assert.assertEquals("Is connected ", s.isCon(3, 4), true);
		Assert.assertEquals("Is not connected ", s.isCon(4, 3), false);
	}

	@Test
	public void testSwitchOnAndResize()
	{
		BitSquare s = new BitSquare();
		s = s.incrementSize(5);
		s = s.connect(2, 3);
		System.out.println(s);
		s = s.incrementSize(-1);
		System.out.println(s);
		Assert.assertEquals("Is connected ", s.isCon(2, 3), true);
		Assert.assertEquals("Is not connected ", s.isCon(3, 2), false);
	}
	
	@Test
	public void testSwitchOnAndDisconnect()
	{
		BitSquare s = new BitSquare();
		s = s.incrementSize(5);
		for (int i = 0 ; i < s.getSize() ; i++){
			for (int j = 0 ; j < s.getSize() ; j++){
				s = s.connect(i, j);
				Assert.assertEquals("Is connected ", s.isCon(i, j), true);
			}
		}
		s = s.disconnect(2, 3);
		System.out.println(s);
		s = s.incrementSize(-1);
		System.out.println(s);
		Assert.assertEquals("Is connected ", s.isCon(2, 3), false);
		Assert.assertEquals("Is not connected ", s.isCon(3, 2), true);
	}
	

}
