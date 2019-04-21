package org.deguet.gutils.graph;


import org.deguet.gutils.graph.mutable.MuDja;
import org.junit.Test;

public class TestMuDja{	
	
	@Test
	public void testSimple(){
		MuDja<Integer,Integer> graph = new MuDja<Integer,Integer>();
		graph.addEdge(1, 2, 3);
		System.out.println(graph.toDot("test"));
	} 

}
