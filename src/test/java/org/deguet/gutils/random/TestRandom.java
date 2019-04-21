package org.deguet.gutils.random;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class TestRandom {

	@Test
	public void testInt(){
		for (long seed = 0 ; seed < 10000 ; seed++){
			Random rand = new Random(seed);
			CopiableRandom myrand = new CopiableRandom(seed);
			for (int i=1;i<1000;i++){
				// copy constructor
				myrand = new CopiableRandom(myrand);
				assertTrue(rand.nextBoolean()==myrand.nextBoolean());
			}
			myrand = new CopiableRandom(seed);
			rand = new Random(seed);
			for (int i=1;i<1000;i++){
				myrand = new CopiableRandom(myrand);
				int a =  rand.nextInt(1000000);
				int b =myrand.nextInt(1000000);
				assertTrue(a==b);
				//System.out.println("Equals    the two  " + (a==b) +" "+a+" "+b);
			}
		}
	}

	
	@Test
	public void testDouble(){
		for (long seed = 0 ; seed < 10000 ; seed++){
			CopiableRandom myrand = new CopiableRandom(seed);
			Random rand           = new Random(seed);
			for (int i=1;i<1000;i++){
				myrand = new CopiableRandom(myrand);
				double a =  rand.nextDouble();
				double b =myrand.nextDouble();
				assertTrue(a==b);
			}
		}
	}
	
	@Test
	public void testBoolean(){
		for (long seed = 0 ; seed < 10000 ; seed++){
			CopiableRandom myrand = new CopiableRandom(seed);
			Random rand           = new Random(seed);
			for (int i=1;i<1000;i++){
				myrand = new CopiableRandom(myrand);
				boolean a =  rand.nextBoolean();
				boolean b =myrand.nextBoolean();
				assertTrue(a==b);
			}
		}
	}
	
	@Test
	public void testBytes(){
		for (long seed = 0 ; seed < 100 ; seed++){
			CopiableRandom myrand = new CopiableRandom(seed);
			Random rand           = new Random(seed);
			for (int i=1;i<100;i++){
				myrand = new CopiableRandom(myrand);
				byte[] a = new byte[100];
				byte[] b = new byte[100];
				rand.nextBytes(a);
				myrand.nextBytes(b);
				Assert.assertArrayEquals(a, b);
			}
		}
	}
	
	
	@Test
	public void testFloat(){
		for (long seed = 0 ; seed < 10000 ; seed++){
			CopiableRandom myrand = new CopiableRandom(seed);
			Random rand           = new Random(seed);
			for (int i=1;i<1000;i++){
				myrand = new CopiableRandom(myrand);
				float a =  rand.nextFloat();
				float b =myrand.nextFloat();
				assertTrue(a==b);
			}
		}
	}
	
	
	/**
	 * Test the use of a Copiable Random and a copy of this one to guarantee they behave the same.
	 * @param args
	 */
	@Test
	public  void testCopyAndCopyOfCopy(){
		long seed = 1234;
		Random rand = new Random(seed);
		CopiableRandom myrand = new CopiableRandom(seed);
		
		for (int i=1;i<1000000;i++){
			myrand = new CopiableRandom(myrand);
			CopiableRandom other = new CopiableRandom(myrand);
			int a =rand.nextInt(6);
			int b =myrand.nextInt(6);
			int c = other.nextInt(6);
			Assert.assertEquals("Equals    the two  " , a, b);
			Assert.assertEquals("Equals    the two  " , b, c);
		}
	}
	
	
	@Test
	public  void testCopyOfRandom(){
		long seed = 1234;
		Random rand = new Random(seed);
		CopiableRandom myrand = new CopiableRandom(rand);
		
		for (int i=1;i<1000000;i++){
			myrand = new CopiableRandom(myrand);
			CopiableRandom other = new CopiableRandom(myrand);
			int a =rand.nextInt(6);
			int b =myrand.nextInt(6);
			int c = other.nextInt(6);
			Assert.assertEquals("Equals    the two  " , a, b);
			Assert.assertEquals("Equals    the two  " , b, c);
		}
	}
	
}
