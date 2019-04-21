package org.deguet.gutils.nuplets;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.TreeSet;

public class TestCouple {

	@Test
	public void testRevertAndEquals(){
		Random r = new Random(342);
		TreeSet<Duo<Integer,Integer>> sorted = new TreeSet<Duo<Integer,Integer>>();
		for (int i = 0 ; i < 100 ; i++){
			Duo<Integer,Integer> c = Duo.d(r.nextInt(100),r.nextInt(100));
			sorted.add(c);
			Duo<Integer,Integer> revert = c.revert();
			sorted.add(revert);
			Duo<Integer,Integer> recvrd = revert.revert();
			sorted.add(recvrd);
			Assert.assertEquals(recvrd.hashCode(), c.hashCode());
			Assert.assertEquals(recvrd, c);
		}
	}
	
	@Test
	public void testD(){
		Duo<Integer,String> duo = Duo.d(19, "trio");
		System.out.println("Duo is "+ duo);
	}
	
}
