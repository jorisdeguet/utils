package org.deguet.gutils.list;

import java.util.Arrays;
import java.util.List;

import org.junit.*;

public class TestCarCon {

	@Test
	public void testCarCon(){
		CarCon<Integer> test = new CarCon<Integer>();
		CarCon<Integer> test2 = new CarCon<Integer>();
		Assert.assertTrue("Elle est vide ", test.isEmpty());
		
		for (int i = 0 ; i < 10 ; i++){
			test = test.addEnd(i);
			test2 = test2.addEnd(i);
			//System.out.println("CarCon est  " + test);
		}
		Assert.assertEquals("Longueur ", test.size(), 10);
		for (int i = 50 ; i < 60 ; i++){
			test = test.addStart(i);
			test2 = test2.addStart(i);
			//System.out.println("CarCon est  " + test);
		}
		Assert.assertEquals("Longueur ", test.size(), 20);
		Assert.assertEquals("Contenu ", test, test2);
		
		Assert.assertArrayEquals("Contenu ", test.toArray(), test2.toArray());
		
		System.out.println("CarCon est  " + test);
		System.out.println("CarCon en tableau   " + Arrays.toString(test.toArray()));
	}
	
	@Test
	public void testCarConAddRemoveAll(){
		CarCon<String> test = new CarCon<String>();
		Assert.assertTrue("Elle est vide ", test.isEmpty());
		List<String> list = Arrays.asList(new String[]{"a","b","c","d","e"});
		test = test.addAllStart(list);
		System.out.println(test);
		System.out.println("3 ème est " +test.get(2));
		Assert.assertEquals("Longueur ", test.size(), 5);
		test = test.remove("a");
		System.out.println(test);
		System.out.println("3 ème est " +test.get(2));
		Assert.assertEquals("Longueur ", test.size(), 4);
		test = test.remove("a");
		System.out.println(test);
		System.out.println("3 ème est " +test.get(2));
		Assert.assertEquals("Longueur ", test.size(), 4);
		test = test.removeAll(list);
		System.out.println(test);
		Assert.assertEquals("Longueur ", test.size(), 0);
	}
	
	@Test
	public void testCarConAddIndex(){
		CarCon<String> test = new CarCon<String>();
		Assert.assertTrue("Elle est vide ", test.isEmpty());
		test = test.add(0, "a");
		System.out.println(test);
		Assert.assertEquals("Longueur ", test.size(), 1);
		test = test.add(0, "b");
		System.out.println(test);
		Assert.assertEquals("Longueur ", test.size(), 2);
		test = test.add(2, "c");
		System.out.println(test);
		Assert.assertEquals("Longueur ", test.size(), 3);
		test = test.add(2, "d");
		System.out.println(test);
		Assert.assertEquals("Longueur ", test.size(), 4);
		test = test.add(test.size(), "e");
		System.out.println(test);
		Assert.assertEquals("Longueur ", test.size(), 5);
		
		
		//test = test.subList(1, 3);
		System.out.println(test.subList(1, 3));
		System.out.println(test.subList(1, 4));
		System.out.println(test.subList(2, 5));
	}
}
