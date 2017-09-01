package org.deguet.gutils.graph;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import org.deguet.gutils.nuplets.Duo;

public class TestDiGraph{	
	
	// complete
	private static Duo<Long,DGraph<Integer,Integer>> buildComplete(
			DGraph<Integer,Integer> base, int n){
		ThreadMXBean timer = ManagementFactory.getThreadMXBean();
		DGraph<Integer,Integer> graph = base;
		long adjat = 0;
		adjat -= timer.getCurrentThreadUserTime();
		for (int i = 0 ; i < n ; i++){
			for (int j = 0 ; j < n ; j++){
				graph = graph.addEdge(i,j,0);
			}
		}
		adjat += timer.getCurrentThreadUserTime();
		return Duo.d(adjat,graph);
	}

	// Line
	private static Duo<Long,DGraph<Integer,Integer>> buildLine(
			DGraph<Integer,Integer> base, int n){
		ThreadMXBean timer = ManagementFactory.getThreadMXBean();
		DGraph<Integer,Integer> graph = base;
		long adjat = 0;
		adjat -= timer.getCurrentThreadUserTime();
		for (int i = 0 ; i < n-1 ; i++){
			graph = graph.addEdge(i,(i+1),0);
		}
		adjat += timer.getCurrentThreadUserTime();
		return Duo.d(adjat,graph);
	}


	// Random
	private static Duo<Long,DGraph<Integer,Integer>> buildRandom(
			DGraph<Integer,Integer> base, 
			int seed, int n, double d){
		ThreadMXBean timer = ManagementFactory.getThreadMXBean();
		DGraph<Integer,Integer> graph = base;
		Random rand = new Random(seed);
		long adjat = 0;
		adjat -= timer.getCurrentThreadUserTime();
		for (int i = 0 ; i < n ; i++){
			for (int j = 0 ; j < n ; j++){
				graph =  (DGraph<Integer, Integer>) graph.addVertex(i).addVertex(j);
				if (rand.nextFloat() < d)
					graph = graph.addEdge(i,j,0);
			}
		}
		adjat += timer.getCurrentThreadUserTime();
		return Duo.d(adjat,graph);
	}

	// Cycle
	private static Duo<Long,DGraph<Integer,Integer>> buildCycle(final DGraph<Integer,Integer> base,final int n){
		ThreadMXBean timer = ManagementFactory.getThreadMXBean();
		DGraph<Integer,Integer> graph = base;
		long adjat = 0;
		adjat -= timer.getCurrentThreadUserTime();
		for (int i = 0 ; i < n-1 ; i++){
			graph = graph.addEdge(i,(i+1),0);
		}
		graph = graph.addEdge((n-1), 0, 0);
		adjat += timer.getCurrentThreadUserTime();
		return Duo.d(adjat,graph);
	}

	// Test method have no parameter
	@Test
	public void testLine (){
		DGraphMatrix<Integer,Integer>   m = new DGraphMatrix<Integer,Integer>();
		DGraphAdja<Integer,Integer>     a = new DGraphAdja<Integer,Integer>();
		DGraphNaive<Integer,Integer> r = new DGraphNaive<Integer,Integer>();
		DGraphTiny<Integer,Integer>  t = new DGraphTiny<Integer,Integer>();
		a = (DGraphAdja<Integer,Integer>)     buildLine(a, 10).get2();
		m = (DGraphMatrix<Integer,Integer>)   buildLine(m, 10).get2();
		r = (DGraphNaive<Integer,Integer>) buildLine(r, 10).get2();
		t = (DGraphTiny<Integer,Integer>)  buildLine(t, 10).get2();
		test4("Lines ",m,r,a,t);
	}

	@Test
	public void testRandom (){
		for (int pourcent = 0 ; pourcent <= 100 ; pourcent+=20){
			//System.out.println("Test for proba "+pourcent);
			for (int seed = 0 ; seed < 20 ; seed++){
				for (int size = 0 ; size < 10 ; size++){
					double proba = pourcent*1.0/100.0;
					DGraph<Integer,Integer> a = 
						buildRandom(new DGraphAdja<Integer,Integer>(), seed,size,proba).get2();
					DGraph<Integer,Integer> m = 
						buildRandom(new DGraphMatrix<Integer,Integer>(), seed,size,proba).get2();
					DGraph<Integer,Integer> r = 
						buildRandom(new DGraphNaive<Integer,Integer>(), seed,size,proba).get2();
					DGraph<Integer,Integer> t = 
						buildRandom(new DGraphTiny<Integer,Integer>(), seed,size,proba).get2();
					assertTrue(a.vertices().size() == size);
					assertTrue(m.vertices().size() == size);
					assertTrue(r.vertices().size() == size);
					assertTrue(t.vertices().size() == size);
					assertEquals("Egaux ", a.vertices(),m.vertices());
					assertEquals("Egaux ", r.vertices(),m.vertices());
					assertEquals("Egaux ", r.vertices(),t.vertices());
					assertEquals("Egaux ", a.vertices(),t.vertices());
					test4("Random ",m,r,a,t);
				}
			}
		}
	}

	@Test
	public void testEdgeReplacement (){
		int seed = 123;
		int size = 20;
		double proba = 0.1;
		DGraph<Integer,Integer> a = 
			buildRandom(new DGraphAdja<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer> m = 
			buildRandom(new DGraphMatrix<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer> r = 
			buildRandom(new DGraphNaive<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer> t = 
			buildRandom(new DGraphTiny<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer> abis = a.replaceEdge(11, 9, 1234567);
		DGraph<Integer,Integer> mbis = m.replaceEdge(11, 9, 1234567);
		DGraph<Integer,Integer> rbis = r.replaceEdge(11, 9, 1234567);
		DGraph<Integer,Integer> tbis = t.replaceEdge(11, 9, 1234567);
		//System.out.println("Edge Replacement Test\n"+DirectedGraph.toDot("prout"));
		test4("Replaced ",mbis,rbis,abis,tbis);
	}

	@Test
	public void testVertexReplacement (){
		int seed = 123;
		int size = 20;
		double proba = 0.1;
		DGraph<Integer,Integer>     a = 
			buildRandom(new DGraphAdja<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer>   m = 
			buildRandom(new DGraphMatrix<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer> r = 
			buildRandom(new DGraphNaive<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer> t = 
			buildRandom(new DGraphTiny<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer> abis = (DGraph<Integer, Integer>) a.replaceVertex(11, 1234567);
		DGraph<Integer,Integer> mbis = (DGraph<Integer, Integer>) m.replaceVertex(11, 1234567);
		DGraph<Integer,Integer> rbis = (DGraph<Integer, Integer>) r.replaceVertex(11, 1234567);
		DGraph<Integer,Integer> tbis = (DGraph<Integer, Integer>) t.replaceVertex(11, 1234567);
		System.out.println("Vertex Replacement Test ");
		test4("Replaced ",mbis,rbis,abis,tbis);
	}

	@Test
	public void testVertexDeletion (){
		int seed = 123;
		int size = 20;
		double proba = 0.1;
		DGraph<Integer,Integer>     a = 
			buildRandom(new DGraphAdja<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer>   m = 
			buildRandom(new DGraphMatrix<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer> r = 
			buildRandom(new DGraphNaive<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer> t = 
			buildRandom(new DGraphTiny<Integer,Integer>(), seed,size,proba).get2();
		DGraph<Integer,Integer> abis = (DGraph<Integer, Integer>) a.delVertex(size/2);
		DGraph<Integer,Integer> mbis = (DGraph<Integer, Integer>) m.delVertex(size/2);
		DGraph<Integer,Integer> rbis = (DGraph<Integer, Integer>) r.delVertex(size/2);
		DGraph<Integer,Integer> tbis = (DGraph<Integer, Integer>) t.delVertex(size/2);
		System.out.println("Vertex Deletion Test");
		test4("Replaced ",mbis,rbis,abis,tbis);
	}

	// Test if edges for vertex are deleted by adding it afterwards.
	// This way we check.
	@Test
	public void testVertexDeletionAdvanced (){
		int size = 40;
		DGraph<Integer,Integer>     a = 
			buildComplete(new DGraphAdja<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer>   m = 
			buildComplete(new DGraphMatrix<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> r = 
			buildComplete(new DGraphNaive<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> t = 
			buildComplete(new DGraphTiny<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> abis = (DGraph<Integer, Integer>) a.delVertex(size/2);
		DGraph<Integer,Integer> mbis = (DGraph<Integer, Integer>) m.delVertex(size/2);
		DGraph<Integer,Integer> rbis = (DGraph<Integer, Integer>) r.delVertex(size/2);
		DGraph<Integer,Integer> tbis = (DGraph<Integer, Integer>) t.delVertex(size/2);
		System.out.println("Vertex Deletion Test");
		test4("delete ",mbis,rbis,abis,tbis);
		DGraph<Integer,Integer> ater = (DGraph<Integer, Integer>) abis.addVertex(size/2);
		DGraph<Integer,Integer> mter = (DGraph<Integer, Integer>) mbis.addVertex(size/2);
		DGraph<Integer,Integer> rter = (DGraph<Integer, Integer>) rbis.addVertex(size/2);
		DGraph<Integer,Integer> tter = (DGraph<Integer, Integer>) tbis.addVertex(size/2);
		test4("restors ",mter,rter,ater,tter);
	}

	@Test
	public void testEdgeDeletion (){
		int size = 10;
		DGraph<Integer,Integer> a = buildLine(new DGraphAdja<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> m = buildLine(new DGraphMatrix<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> r = buildLine(new DGraphNaive<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> t = buildLine(new DGraphTiny<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> abis = (DGraph<Integer, Integer>) a.delEdge(1,2);
		DGraph<Integer,Integer> mbis = (DGraph<Integer, Integer>) m.delEdge(1,2);
		DGraph<Integer,Integer> rbis = (DGraph<Integer, Integer>) r.delEdge(1,2);
		DGraph<Integer,Integer> tbis = (DGraph<Integer, Integer>) t.delEdge(1,2);
		//System.out.println("Delete Edge Test \n"+abis.toDot("del")+"\nfrom\n"+a.toDot("original"));
		test4("Edge Delete ",mbis,rbis,abis,tbis);
	}

	@Test
	public void testALot (){
		int size = 10;
		DGraph<Integer,Integer> a = buildLine(new DGraphAdja<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> m = buildLine(new DGraphMatrix<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> r = buildLine(new DGraphNaive<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> t = buildLine(new DGraphTiny<Integer,Integer>(), size).get2();
		a = (DGraph<Integer, Integer>) a.delEdge(1,2).delVertex(1).addVertex(1).addEdge(1, 2, 6).addEdge(6, 9, 12).delEdge(6, 7).delEdge(6, 7);
		m = (DGraph<Integer, Integer>) m.delEdge(1,2).delVertex(1).addVertex(1).addEdge(1, 2, 6).addEdge(6, 9, 12).delEdge(6, 7).delEdge(6, 7);
		r = (DGraph<Integer, Integer>) r.delEdge(1,2).delVertex(1).addVertex(1).addEdge(1, 2, 6).addEdge(6, 9, 12).delEdge(6, 7).delEdge(6, 7);
		t = (DGraph<Integer, Integer>) t.delEdge(1,2).delVertex(1).addVertex(1).addEdge(1, 2, 6).addEdge(6, 9, 12).delEdge(6, 7).delEdge(6, 7);
		//System.out.println("A Lot "+a.toDot("ALot"));
		test4("Edge Delete ",m,r,a,t);
	}

	public static void main (String[] args){
		// Used to test what went wrong
		int size = 4;
		DGraph<Integer,Integer> m = buildLine(new DGraphMatrix<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> t = buildLine(new DGraphTiny<Integer,Integer>(), size).get2();
		DGraph<Integer,Integer> mbis = (DGraph<Integer, Integer>) m.delEdge(1,2);
		DGraph<Integer,Integer> tbis = (DGraph<Integer, Integer>) t.delEdge(1,2);
		System.out.println(m.couples());
		System.out.println(t.couples());
		//System.out.println(mbis.couples());
		//System.out.println(tbis.couples());
		myAssertTrue("Sinks equality  ", nullOrEquals(t.sinks(),m.sinks()));
	}


	@SuppressWarnings("unchecked")
	public static void test4(
			String prefix, 
			DGraph m, 
			DGraph r, 
			DGraph a,
			DGraph t){
		test2(prefix,m,r);
		test2(prefix,m,a);
		test2(prefix,m,t);
	}

	@SuppressWarnings("unchecked")
	public static void test2(String prefix, DGraph m, DGraph r){
		//System.out.println("##############Test for "+prefix+"###############");
		// Test on vertices
		myAssertTrue(prefix+"Vertex equality  ", r.vertices().equals(m.vertices()));
		// Test on successors of 0
		myAssertTrue(prefix+"Successors equality  ", r.labeledSuccessors(0).equals(m.labeledSuccessors(0)));
		// Test on couples
		myAssertTrue(prefix+"Couples equality  ", r.couples().equals(m.couples()));
		// Test on triples
		myAssertTrue(prefix+"Triples equality ", r.triplets().equals(m.triplets()));
		// Test on sources
		myAssertTrue(prefix+"Sources equality  ", r.sources().equals(m.sources()));
		// Test on sinks
		myAssertTrue(prefix+"Sinks equality  ", nullOrEquals(r.sinks(),m.sinks()));
		// Test on recursivity
		myAssertTrue(prefix+"Recursivity equality  ", DGraphs.isCyclic(r) == DGraphs.isCyclic(m));
		// Test on topological Sort
//		if (!nullOrEquals(r.topoSort(),m.topoSort())){
//			System.out.println(m.toDot("first"));
//			System.out.println(r.toDot("second"));
//		}
		myAssertTrue(prefix+"TopoSort equality Rela Matr ", nullOrEquals(DGraphs.topoSort(r),DGraphs.topoSort(m)));
	}

	private static void myAssertTrue(String message, boolean expression){
		if (!expression){
			System.out.println("   !!!!     "+message);
		}
		assertTrue(message,expression);
	}

	private static boolean nullOrEquals(Object a,Object b){
		boolean result =  (a==null && b==null) || a.equals(b);
		if (!result){
			System.out.print("nullOrEquals is false for \n-> "+a+"\n-> "+b);
		}
		return result;
	}

	private void showTopo(final DGraph<Integer,Integer> graph){
		for (Set<Integer> layer: DGraphs.topoSort(graph)){
			for (Integer elt : layer){
				System.out.print(elt+ " ");
			}
			System.out.println("-----------");
		}
	}

}
