package org.deguet.gutils.graph;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import org.deguet.gutils.nuplets.Duo;
import org.junit.Test;

public class TestPerformance {

	@Test
	public void perf(){
		int size = 10;
		DGraphMatrix<Integer,Integer>   m = new DGraphMatrix<Integer,Integer>();
		DGraphAdja<Integer,Integer>     a = new DGraphAdja<Integer,Integer>();
		DGraphNaive<Integer,Integer> r = new DGraphNaive<Integer,Integer>();
		DGraphTiny<Integer,Integer>  t = new DGraphTiny<Integer,Integer>();
		
		long timeTiny		= buildComplete(t,size).get1();
		long timeMatrix		= buildComplete(m,size).get1();
		long timeAdja		= buildComplete(a,size).get1();
		long timeRela		= buildComplete(r,size).get1();
		
		System.out.println("temps pour tiny " + timeTiny);
		System.out.println("temps pour matr " + timeMatrix);
		System.out.println("temps pour adja " + timeAdja);
		System.out.println("temps pour rela " + timeRela);
	}
	
	
	public static Duo<Long,DGraph<Integer,Integer>> buildComplete(
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
	
	
	
}
