package org.deguet.gutils.graph;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.deguet.gutils.graph.mutable.MuDja;

public class TestMuDja{	
	
	@Test
	public void testSimple(){
		MuDja<Integer,Integer> graph = new MuDja<Integer,Integer>();
		graph.addEdge(1, 2, 3);
		System.out.println(graph.toDot("test"));
	} 

}
