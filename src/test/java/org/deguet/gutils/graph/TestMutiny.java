package org.deguet.gutils.graph;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.deguet.gutils.graph.mutable.MuTiny;

public class TestMutiny{	
	
	@Test
	public void testSimple(){
		MuTiny<Integer,Integer> graph = new MuTiny<Integer,Integer>();
		graph.addEdge(1, 2, 3);
		System.out.println(graph.toDot("test"));
	} 

}
