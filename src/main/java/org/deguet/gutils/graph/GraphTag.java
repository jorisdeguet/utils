package org.deguet.gutils.graph;

import java.util.Set;

import org.deguet.gutils.nuplets.Duo;

public interface GraphTag<V,E> {

	/**
	 * Returns a new graph where the given edge is deleted.
	 * If there is no such edge in graph, returns the graph.
	 * @param a the origin of the edge
	 * @param b the end point of the edge
	 * @return the graph with the edge deleted
	 */
	public <G extends GraphTag<V,E>> G delEdge(V a, V b);
	
	/**
	 * Returns a new graph where the given edge is added.
	 * If there is such edge in graph, returns the graph.
	 * @param a the origin of the edge
	 * @param b the end point of the edge
	 * @return the graph with the edge added
	 */
	public <G extends GraphTag<V,E>> G addEdge(V a, V b, E t);
	
	/**
	 * Returns a new graph which is the precedent with the vertex added.
	 * If the vertex is already contained, returns the same graph.
	 * @param tag
	 * @return a new graph with the vertex added
	 */
	public <G extends GraphTag<V,E>> G addVertex(V tag);
	
	public <G extends GraphTag<V,E>> G addVertices(Set<V> verts);
	
	/**
	 * Replace the vertex by another while maintaining edges.
	 * Returns the same graph if "before" is not contained or if "after" is contained.
	 * @param before
	 * @param after
	 * @return a new graph where before is replaced by after
	 */
	public <G extends GraphTag<V,E>> G replaceVertex(V before, V after);
	
	/**
	 * Returns a new graph without the given vertex.
	 * Also removes every edge where the vertex is involved.
	 * @param tag
	 * @return the graph without the specified vertex
	 */
	public <G extends GraphTag<V,E>> G delVertex(V tag);
	
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
