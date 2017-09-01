package org.deguet.gutils.graph;


import java.util.*;

import org.deguet.gutils.graph.mutable.MuDja;
import org.deguet.gutils.graph.mutable.MutableDGraph;
import org.deguet.gutils.nuplets.Duo;
import org.junit.Assert;
import org.junit.Test;

import org.deguet.gutils.graph.mutable.MuTiny;

public class TestMutable{	
	
	MutableDGraph<Integer,Integer>[] graphs(){
		return new MutableDGraph[]{
			new MuDja<Integer,Integer>(),
			new MuTiny<Integer,Integer>()
		};
	} 
	
	@Test
	public void testSimple(){
		MutableDGraph<Integer,Integer>[] graphs = graphs();
		for (MutableDGraph<Integer,Integer> graph : graphs ){
			Random r = new Random(1329);
			for (int i = 0 ; i < 1000 ; i++){
				int random1 = r.nextInt(20);
				int random2 = r.nextInt(20);
				graph.addEdge(1, random1, r.nextInt(10));
				graph.addEdge(random1, random2, r.nextInt(10));
			}
			System.out.println(graph.toDot(""+graph.getClass()));
		}
		for (Duo<MutableDGraph<Integer,Integer>, MutableDGraph<Integer,Integer>>
			duo: Duo.coupleList(graphs)){
			System.out.println(duo.get1()+" and "+duo.get2());
			Assert.assertEquals("should be the same", duo.get1().triplets(), duo.get2().triplets());
		}
	} 
	

	@Test
	public void testDelVertex(){
		MutableDGraph<Integer,Integer>[] graphs = graphs();
		for (MutableDGraph<Integer,Integer> graph : graphs ){
			Random r = new Random(1329);
			for (int i = 0 ; i < 120 ; i++){
				int random1 = r.nextInt(40);
				int random2 = r.nextInt(40);
				//graph.addEdge(1, random1, r.nextInt(10));
				graph.addEdge(random1, random2, r.nextInt(10));
			}
			System.out.println(graph.triplets());
			for (int i = 0 ; i < 10 ; i++){
				int random1 = r.nextInt(40);
				//System.out.println(random1);
				graph.delVertex(random1);
			}
			System.out.println(graph.triplets());
			System.out.println("\n\n");
		}
		for (Duo<MutableDGraph<Integer,Integer>, MutableDGraph<Integer,Integer>>  
			duo: Duo.coupleList(graphs)){
			System.out.println("A : "+duo.get1().vertices());
			System.out.println("B : "+duo.get2().vertices());
			System.out.println("Edges A : "+duo.get1().triplets());
			System.out.println("Edges B : "+duo.get2().triplets());
			System.out.println(duo.get1().vertices().size()+" verts "+duo.get2().vertices().size());
			System.out.println(duo.get1().triplets().size()+" edges "+duo.get2().triplets().size());
			Assert.assertEquals("should be the same vertices", duo.get1().vertices(), duo.get2().vertices());
			Assert.assertEquals("should be the same edges", duo.get1().triplets(), duo.get2().triplets());
		}
	} 
	
	@Test
	public void testLabelPreds(){
		MutableDGraph<Integer,Integer>[] graphs = graphs();
		for (MutableDGraph<Integer,Integer> graph : graphs ){
			for (int i = 2 ; i < 6 ; i++){
				
				graph.addEdge(1, i, 11);
				graph.addEdge(i-1, i, 22);
			}
		}
		for (Duo<MutableDGraph<Integer,Integer>, MutableDGraph<Integer,Integer>>  
			duo: Duo.coupleList(graphs)){
			System.out.println("A : "+duo.get1().labeledPredecessors(5));
			System.out.println("B : "+duo.get2().labeledPredecessors(5));
			System.out.println("Edges A : "+duo.get1().couples());
			System.out.println("Edges B : "+duo.get2().couples());
			Assert.assertEquals(
					"should be the same paths", 
					duo.get1().labeledPredecessors(5), 
					duo.get2().labeledPredecessors(5));
		}
	} 
	
	@Test
	public void testPreds(){
		MutableDGraph<Integer,Integer>[] graphs = graphs();
		for (MutableDGraph<Integer,Integer> graph : graphs ){
			for (int i = 2 ; i < 6 ; i++){
				
				graph.addEdge(1, i, 11);
				graph.addEdge(i-1, i, 22);
			}
		}
		for (Duo<MutableDGraph<Integer,Integer>, MutableDGraph<Integer,Integer>>  
			duo: Duo.coupleList(graphs)){
			System.out.println("A : "+duo.get1().predecessors());
			System.out.println("B : "+duo.get2().predecessors());
			Assert.assertEquals(
					"should be the same paths", 
					duo.get1().predecessors(), 
					duo.get2().predecessors());
		}
	} 
	
	@Test
	public void testPaths(){
		MutableDGraph<Integer,Integer>[] graphs = graphs();
		for (MutableDGraph<Integer,Integer> graph : graphs ){
			for (int i = 4 ; i < 6 ; i++){
				
				graph.addEdge(1, i, 11);
				graph.addEdge(i, 2, 22);
			}
		}
		for (Duo<MutableDGraph<Integer,Integer>, MutableDGraph<Integer,Integer>>  
			duo: Duo.coupleList(graphs)){
			Set<Integer> starters = new HashSet<Integer>(); starters.add(1);
			Set<Integer> finishers = new HashSet<Integer>(); finishers.add(2);
			System.out.println("A : "+duo.get1().pathsFromTo2(starters, finishers));
			System.out.println("B : "+duo.get2().pathsFromTo2(starters, finishers));
			Assert.assertEquals(
					"should be the same paths", 
					duo.get1().pathsFromTo2(starters, finishers), 
					duo.get2().pathsFromTo2(starters, finishers));
		}
	} 

}
