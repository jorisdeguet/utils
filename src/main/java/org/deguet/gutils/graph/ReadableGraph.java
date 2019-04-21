package org.deguet.gutils.graph;

import org.deguet.gutils.nuplets.Duo;

import java.util.Set;

public interface ReadableGraph<V> {

	/**
	 * Returns the set of vertices for that graph.
	 */
	public Set<V> vertices();
	
	public Set<V> neighbors(V v);
	
	/**
	 * Returns true if one the graph vertices is equal to the parameter v.
	 * @param vertex
	 * @return true of the graph contains this object as a vertex
	 */
	public boolean contains(V vertex);
	
	/**
	 * Builds a dot (graphviz) representation of the graph. 
	 * This is based on toString methods of both edge and vertex.
	 * @return graphviz graph representation
	 */
	public String toDot(String name);
	
	/**
	 * Returns the set of edges in the graph
	 * @return
	 */
	public Set<Duo<V,V>> couples();
	
}
