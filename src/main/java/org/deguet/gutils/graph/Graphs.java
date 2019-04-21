package org.deguet.gutils.graph;

import org.deguet.gutils.random.CopiableRandom;

import java.util.Set;

public class Graphs {

	/**
	 * Returns a graph where x and y are the same node 
	 * @param graph
	 * @param x
	 * @param y
	 * @return
	 */
	public static <V> Graph<V> contract(Graph<V> graph, V x, V y){
		Set<V> yneighbors = graph.neighbors(y);
		yneighbors.remove(x);
		Graph<V> result = graph;
		// y disappear from graph.
		result = result.delVertex(y);
		System.out.println("Vertex are "+result.vertices());
		System.out.println(result.toDot("after del vert"));
		// All neighbors of y become neighbors of x.
		for (V neighbor : yneighbors){
			result = result.addEdge(x, neighbor);
		}
		return result;
	}


	public static Graph<Integer> getErdosRenyi(
			Graph<Integer> start,
			double edgeProbability, 
			int size, int seed){
		CopiableRandom rand = new CopiableRandom(seed);
		Graph<Integer> result = start;
		for (int i = 0 ; i < size ; i++){
			result = result.addVertex(i);
			//System.out.println("Erdos completed "+(i*100/size));
			for (int j = i+1 ; j < size ; j++){
				if (rand.nextFloat() < edgeProbability)
					//System.out.println("Erdos tries add an edge between "+i+"  "+j);
					result = result.addEdge(i, j);
			}
		}
		return result;
	}

}
