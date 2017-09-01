package org.deguet.gutils.set;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

public class TestPowerSet {

	@Test
	public void test(){
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 1 ; i <17 ; i++)
			set.add(i);
		int counter = 0;
		for (Set<Integer> part : new PowerSet<Integer>(set)){
			//System.out.println(part);
			counter++;
		}
		Assert.assertEquals("Power of 2 ", counter, (int)Math.pow(2, set.size()));
	}

}
