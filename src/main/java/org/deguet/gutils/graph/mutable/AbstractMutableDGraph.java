package org.deguet.gutils.graph.mutable;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.nuplets.Trio;

import java.util.*;

public abstract class AbstractMutableDGraph<V,E> implements  MutableDGraph<V,E> {

	public String toDot(String name){
		if (name.equals("graph") || name.equals("digraph"))
			throw new IllegalArgumentException(" graph and digraph are reserved keyword in dot format");
		StringBuilder sb = new StringBuilder("digraph "+name+" \n{\n");
		for (V vert : vertices()){
			sb.append("\n    \""+vert+"\"");
		}
		for (Trio<V,V,E> triple : triplets()){
			sb.append("\n    \""+triple.get1()+"\" -> \""+triple.get2()+"\" [label=\""+triple.get3()+"\"]");
		}
		sb.append("\n}\n");
		return sb.toString();
	}
	
	public String toString(){return toDot("test");}

	public final Set<Iterable<V>> pathsFromTo2(Set<V> from, Set<V> to){
		HashSet<Iterable<V>> res = new HashSet<Iterable<V>>();
		List<ArrayList<V>> cur = new ArrayList<ArrayList<V>>();
		Map<V,Set<Duo<V,E>>> predecessors = this.predecessors();
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

	
	public void restrictTo(Set<V> kept){
		// might be faster to restart from scratch
		Set<Trio<V,V,E>> edges = this.triplets();
		this.empty();
		for (Trio<V,V,E> edge : edges){
			if (kept.contains(edge.get1()) && kept.contains(edge.get2())){
				this.addEdge(edge.get1(), edge.get2(), edge.get3());
			}
		}
	}

	public Map<V, Set<Duo<V, E>>> predecessors() {
		Map<V, Set<Duo<V, E>>> result = new HashMap<V, Set<Duo<V, E>>>();
		for (V v : vertices()){
			result.put(v, this.labeledPredecessors(v));
		}
		return result;
	}


	public void addEdges(Set<Trio<V, V, E>> edges) {
		for (Trio<V,V,E> edge : edges){
			this.addEdge(edge.get1(), edge.get2(), edge.get3());
		}
	}
	
}
