package org.deguet.gutils.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.nuplets.Trio;

/**
 * Helpful functions built on top of primitives from the graph
 * @author joris
 *
 */
public class DGraphs {

	public static <V,E> String toDot(ReadableDGraph<V,E> g, String name){
		if (name.equals("graph") || name.equals("digraph"))
			throw new IllegalArgumentException(" graph and digraph are reserved keyword in dot format");
		StringBuilder sb = new StringBuilder("digraph "+name+" \n{\n");
		for (V vert : g.vertices()){
			sb.append("\n    \""+vert+"\"");
		}
		for (Trio<V,V,E> triple : g.triplets()){
			sb.append("\n    \""+triple.get1()+"\" -> \""+triple.get2()+"\" [label=\""+triple.get3()+"\"]");
		}
		sb.append("\n}\n");
		return sb.toString();
	}
	
	
	/**
	 * The list of layer of that graph. null if the graph is recursive.
	 * @return vertices organized by layers in a topological sort.
	 */
	public final static <V> List<Set<V>> topoSort(DiGraphNoLabel<V> graph) {
		final List<Set<V>> layers = new ArrayList<Set<V>>();
		DiGraphNoLabel<V> remain = graph;
		int sizeAfter;
		int sizeBefore;
		do {
			sizeBefore = remain.vertices().size();
			layers.add(remain.sources());
			for(final V source : remain.sources()){
				remain = remain.delVertex(source);
			}
			sizeAfter = remain.vertices().size();
		}
		while(sizeAfter != 0 && sizeAfter != sizeBefore);
		// There is a cycle that remains
		return (sizeAfter != 0?null:layers);
	}

	public static final <V,E> List<Set<V>> topoSort(DGraph<V,E> graph) {
		List<Set<V>> layers = new ArrayList<Set<V>>();
		DGraph<V,E> remain = graph;
		int sizeAfter;
		int sizeBefore;
		do {
			sizeBefore = remain.vertices().size();
			layers.add(remain.sources());
			for(V source : remain.sources()){
				remain = remain.delVertex(source);
			}
			sizeAfter = remain.vertices().size();
		}
		while(sizeAfter != 0 && sizeAfter != sizeBefore);
		// There is a cycle that remains
		return (sizeAfter != 0?null:layers);
	}
	

	/**
	 * Returns one possible linearization of the graph if acyclic.
	 * If you plan to build all possible lineraisations, use topoSort() and linearize yourself.
	 * @return one possible linearisation of the graph.
	 */
	public static final <V> List<V> linearisation(DiGraphNoLabel<V> graph){
		final List<V> result = new ArrayList<V>();
		DiGraphNoLabel<V> remain = graph;
		int sizeAfter;
		int sizeBefore;
		do {
			sizeBefore = remain.vertices().size();
			result.addAll(remain.sources());
			for(final V source : remain.sources()){
				remain = remain.delVertex(source);
			}
			sizeAfter = remain.vertices().size();
		}
		while(sizeAfter != 0 && sizeAfter != sizeBefore);
		// There is a cycle that remains
		return (sizeAfter != 0?null:result);
	}


	/**
	 * This gives a dot (graphviz) representation of the graph.
	 */
	public static <V> String toDot(DiGraphNoLabel<V> graph, final String name){
		final StringBuilder sb = new StringBuilder("digraph "+name+" \n{\n");
		for (final Duo<V,V> triple : graph.couples()){
			sb.append("\n    \""+triple.get1()+"\" -> \""+triple.get2()+"\" ");
		}
		sb.append("\n}\n");
		return sb.toString();
	}
	
	public static <V> Set<V> vertices(DiGraphNoLabel<V> g, final Predicate<V> predicate){
		final Set<V> result = new HashSet<V>();
		for (final V vert : g.vertices()){
			if (predicate.evaluate(vert)) result.add(vert);
		}
		return result;
	}	

	public static <V> DiGraphNoLabel<V> restrictTo(DiGraphNoLabel<V> graph, final Set<V> kept){
		DiGraphNoLabel<V> result = graph;
		for (final V vertex : graph.vertices()){
			if (!kept.contains(vertex)){
				result = result.delVertex(vertex);
			}
		}
		return result;
	}
	
	public static <V> boolean isCyclic(DiGraphNoLabel<V> graph){
		return topoSort(graph)==null;
	}
	
	public static <V,E> boolean isCyclic(DGraph<V,E> g) {
		return topoSort(g)==null;
	}
	
	public static <V,E> DGraph<V,E> addEdges(DGraph<V,E> start, Set<Trio<V,V,E>> edges){
		DGraph<V,E> result = start;
		for (Trio<V,V,E> edge : edges){
			result = result.addEdge(edge.get1(), edge.get2(), edge.get3());
		}
		return result;
	}
	
	private static <V,E> Map<V,Set<Duo<V,E>>> predecessors(ReadableDGraph<V,E> graph){
		Map<V,Set<Duo<V,E>>> result = new HashMap<V,Set<Duo<V,E>>>();
		for (V vert : graph.vertices()){
			result.put(vert,graph.labeledPredecessors(vert));
		}
		return result;
	}
	
	public static final <V, E> Set<Iterable<V>> pathsFromTo2(ReadableDGraph<V,E> graph, Set<V> from, Set<V> to){
		HashSet<Iterable<V>> res = new HashSet<Iterable<V>>();
		List<ArrayList<V>> cur = new ArrayList<ArrayList<V>>();
		Map<V,Set<Duo<V,E>>> predecessors = predecessors(graph);
		for (V t : to){
			ArrayList<V> init = new ArrayList<V>();
			init.add(t);
			cur.add(init);
		}
		while (!cur.isEmpty()){
			List<ArrayList<V>> next = new ArrayList<ArrayList<V>>();
			for(ArrayList<V> current : cur){
				V first = current.get(0);
				Set<Duo<V,E>> preds = predecessors.get(first);
				for (Duo<V,E> pred : preds){
					V toAppend = pred.get1();
					// prevent entering loops
					if (!current.contains(toAppend)){
						// check if in from, add to result
						ArrayList<V> appended = (ArrayList<V>) current.clone();
						appended.add(0, toAppend);
						//System.out.println(appended);
						next.add(appended);
						if (from.contains(toAppend)){
							res.add(appended);
						}
					}
				}
			}
			cur = next;
		}
		return res;
	}

	public static <V,E> DGraph<V,E> restrictTo(DGraph<V,E> start, Set<V> kept){
		// might be faster to restart from scratch
		DGraph<V,E> result = start.empty();
		Set<Trio<V,V,E>> newEdges = new HashSet<Trio<V,V,E>>();
		for (Trio<V,V,E> edge : start.triplets()){
			if (kept.contains(edge.get1()) && kept.contains(edge.get2())){
				newEdges.add(edge);
			}
		}
		return addEdges(result,newEdges);
	}


	public static DGraph randomGraph(int seed, int size) {
		DGraph<Integer,Integer> result = new DGraphMatrix<Integer,Integer>();
		Random r = new Random(seed);
		for(int i = 0 ; i < (size*size)/3 ; i++){
			result = result.addEdge(r.nextInt(size), r.nextInt(size), r.nextInt(size*size));
		}
		return result;
	}
	
}
