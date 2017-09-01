package org.deguet.gutils.graph;

import org.junit.Assert;
import org.junit.Test;

public class TestGraph {

	@Test
	public void testGraphsContract(){
		Graph<Integer> g = new GraphTiny<Integer>();
		g = buildLine(5,g);
		Assert.assertEquals("nombre d'arete = nombre de sommets ",g.vertices().size(), g.couples().size());
		System.out.println(g.toDot("test"));
		g = Graphs.contract(g, 2, 3);
		System.out.println(g.toDot("test"));
		Assert.assertEquals("nombre d'arete = nombre de sommets ",g.vertices().size(), g.couples().size());
		
	}
	
	@Test
	public void testErdosRenyi(){
		Graph<Integer> a = new GraphTiny<Integer>();
		Graph<Integer> b = new GraphTiny<Integer>();
		
		a = Graphs.getErdosRenyi(a, 0.3, 10, 1234);
		b = Graphs.getErdosRenyi(b, 0.3, 10, 1234);
		System.out.println("=======================================");
		System.out.println(a.vertices());
		System.out.println(b.vertices());
		System.out.println("=======================================");
		System.out.println(a.couples());
		System.out.println(b.couples());
		Assert.assertEquals(a, b);
	}



	private Graph<Integer> buildComplete(int n, Graph<Integer> g){
		for (int i = 0 ; i < n ; i ++){
			for (int j = i+1 ; j < n ; j ++){
				g = g.addEdge(i, j);
			}
		}
		return g;
	}

	private Graph<Integer> buildLine(int n, Graph<Integer> g){
		for (int i = 0 ; i < n ; i ++){
			g = g.addEdge(i, (i+1)%n);
		}
		return g;
	}


}
