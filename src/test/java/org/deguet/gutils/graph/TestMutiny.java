package org.deguet.gutils.graph;


import org.deguet.gutils.graph.mutable.MuTiny;
import org.junit.Test;

public class TestMutiny{	
	
	@Test
	public void testSimple(){
		MuTiny<Integer,Integer> graph = new MuTiny<Integer,Integer>();
		graph.addEdge(1, 2, 3);
		System.out.println(graph.toDot("test"));
	} 

}
