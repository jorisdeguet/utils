package org.deguet.gutils.graph;

import org.deguet.gutils.nuplets.Duo;

import java.io.Serializable;
import java.util.Set;

/**
 * This interface corresponds to directed graphs, graph where edges are arrows.
 * 
 * The second parameter.
 * 
 * @author joris
 *
 * @param <V> the vertex object class
 * @param <G> bound parameter used to emulate some kind of covariance.
 */
public interface DiGraphNoLabel<V> extends Serializable {

	/**
	 * Returns a new graph where the given edge is deleted.
	 * If there is no such edge in graph, returns the graph.
	 * @param a the origin of the edge
	 * @param b the end point of the edge
	 * @return the graph with the edge deleted
	 */
	public <G extends DiGraphNoLabel<V>> G delEdge(V a, V b);
	
	/**
	 * Returns a new graph which is the precedent with the vertex added.
	 * If the vertex is already contained, returns the same graph.
	 * @param tag
	 * @return a new graph with the vertex added
	 */
	public <G extends DiGraphNoLabel<V>> G addVertex(V tag);
	
	public <G extends DiGraphNoLabel<V>> G addVertices(Set<V> verts);
	
	/**
	 * Replace the vertex by another while maintaining edges.
	 * Returns the same graph if "before" is not contained or if "after" is contained.
	 * @param before
	 * @param after
	 * @return a new graph where before is replaced by after
	 */
	public <G extends DiGraphNoLabel<V>> G replaceVertex(V before, V after);
	
	/**
	 * Returns a new graph without the given vertex.
	 * Also removes every edge where the vertex is involved.
	 * @param tag
	 * @return the graph without the specified vertex
	 */
	public <G extends DiGraphNoLabel<V>> G delVertex(V tag);
	
	/**
	 * Returns the set of vertices for that graph.
	 */
	public Set<V> vertices();
	
	/**
	 * Returns if the graph contains a cycle.
	 * @return true if the graph contains a cycle.
	 */
	public boolean cyclic();
	
	
	/**
	 * The set of vertices that point to v. With the edges objects
	 * @param v
	 * @return The set of couple (predecessor/object on edge)
	 */
	//public Set<V> predecessors(V v);
	
	/**
	 * The set of vertices that are pointed by v. With the edges objects.
	 * @param v
	 * @return the set of couples (successor/object on edge)
	 */
	//public Set<V> successors(V v);
	
	/**
	 * Builds a dot (graphviz) representation of the graph. 
	 * This is based on toString methods of both edge and vertex.
	 * @return graphviz graph representation
	 */
	public String toDot(String name);
	
	/**
	 * The set of vertices without in edge.
	 * @return the set of vertices without incoming edge
	 */
	public Set<V> sources();
	
	/**
	 * The set of vertices without out edge.
	 * @return the set of vertices without outgoing edges
	 */
	public Set<V> sinks();
	
	/**
	 * Returns a set made of couples of vertices that are linked by an edge.
	 */
	public Set<Duo<V,V>> couples();
	
}
