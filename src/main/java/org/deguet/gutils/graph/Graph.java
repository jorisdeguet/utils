package org.deguet.gutils.graph;

import java.io.Serializable;
import java.util.Set;

public interface Graph<V> extends Serializable, ReadableGraph<V> {

	/**
	 * Returns a new graph where the given edge is deleted.
	 * If there is no such edge in graph, returns the graph.
	 * @param a the origin of the edge
	 * @param b the end point of the edge
	 * @return the graph with the edge deleted
	 */
	public <G extends Graph<V>> G delEdge(V a, V b);
	
	/**
	 * Returns a new graph where the given edge is added.
	 * If there is such edge in graph, returns the graph.
	 * @param a the origin of the edge
	 * @param b the end point of the edge
	 * @return the graph with the edge added
	 */
	public <G extends Graph<V>> G addEdge(V a, V b);
	
	/**
	 * Returns a new graph which is the precedent with the vertex added.
	 * If the vertex is already contained, returns the same graph.
	 * @param tag
	 * @return a new graph with the vertex added
	 */
	public <G extends Graph<V>> G addVertex(V tag);
	
	public <G extends Graph<V>> G addVertices(Set<V> verts);
	
	/**
	 * Replace the vertex by another while maintaining edges.
	 * Returns the same graph if "before" is not contained or if "after" is contained.
	 * @param before
	 * @param after
	 * @return a new graph where before is replaced by after
	 */
	public <G extends Graph<V>> G replaceVertex(V before, V after);
	
	/**
	 * Returns a new graph without the given vertex.
	 * Also removes every edge where the vertex is involved.
	 * @param tag
	 * @return the graph without the specified vertex
	 */
	public <G extends Graph<V>> G delVertex(V tag);
	
	public <G extends Graph<V>> G restrictTo(Set<V> kept);
	
	
	
}
