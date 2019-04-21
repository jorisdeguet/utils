package org.deguet.gutils.geohash;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestWeekSchedule {

	@Test
	public void test(){
		Random r = new Random(12345);
		Set<Integer> ints = new HashSet<Integer>();
		for (int i = 0 ; i < 10 ; i++){
			ints.add(r.nextInt(24*2));
		}
		WeekSchedule ws = new WeekSchedule();
		for (Integer i : ints){
			ws = ws.setAt(true, WeekSchedule.Days.Mon	, i);
		}
		for (Integer i : ints){
			Assert.assertTrue("should be open", ws.isOpen(WeekSchedule.Days.Mon, i));
		}
		for (int i = 0 ; i < 48 ; i++){
			if (!ints.contains(i))
				Assert.assertTrue("should not be open", !ws.isOpen(WeekSchedule.Days.Mon, i));
		}

	}

	@Test
	public void show(){
		WeekSchedule ws = WeekSchedule.classical();
		System.out.println(ws);
	}

}
