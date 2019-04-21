package org.deguet.gutils.graph;

import org.deguet.gutils.random.CopiableRandom;
import org.junit.Assert;
import org.junit.Test;


public class TestUndirGraph {
	
	
	
	@Test
	public void testNewTiny(){
		CopiableRandom rand = new CopiableRandom(123456);
		Graph<Integer> tiny  = new GraphTiny<Integer>();
		for (int  i = 0 ; i < 10 ; i++){
			for (int  j = 0 ; j < 10 ; j++){
				if (rand.nextFloat() < 0.5){
					//System.out.println("\n\n\n\nAdd edge from "+i+"  "+ j);
					tiny  = tiny.addEdge(i, j);
					//System.out.println(tiny.toDot(""+i));
				}
			}
		}
		System.out.println(tiny.toDot("new tiny"));
		//System.out.println("naive  "+naive.neighbors(0));
		//System.out.println("tiny   "+tiny.neighbors(0));
	}
	
	@Test
	public void testTiny(){
		Graph<Integer> tiny  = new GraphTiny<Integer>();
		for (int  i = 0 ; i < 10 ; i++){
			tiny  = tiny.addEdge(i, i+1);
		}
		System.out.println(tiny.toDot("new tiny"));
		Assert.assertEquals(11, tiny.vertices().size());
		Assert.assertEquals(10, tiny.couples().size());
		//System.out.println("tiny   "+tiny.neighbors(0));
	}
	
	

}
