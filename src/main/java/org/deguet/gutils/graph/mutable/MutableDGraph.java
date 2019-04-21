package org.deguet.gutils.graph.mutable;

import org.deguet.gutils.graph.ReadableDGraph;
import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.nuplets.Trio;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface MutableDGraph<V,E> extends Serializable, ReadableDGraph<V,E> {

	/**
	 * Deletes the edge between the two vertices.
	 * @param a
	 * @param b
	 */
	public void delEdge(V a, V b);

	/**
	 * Adds an edge with the specified edge label
	 * @param a
	 * @param b
	 * @param e
	 */
	public void addEdge(V a, V b, E e);

	/**
	 * Replaces the label on the edge between these two vertices
	 * @param from
	 * @param to
	 * @param newTag
	 */
	public void replaceEdge(V from, V to, E newTag);

	public void addVertex(V v);

	public void delVertex(V v);

	public void replaceVertex(V before, V after);

	public void empty();

	public String toDot(String graphName);

	//public Set<Iterable<V>> pathsFromTo2(Set<V> starters,Set<V> finishers);

	public void restrictTo(Set<V> toKeep);

	public Map<V, Set<Duo<V, E>>> predecessors();

	public void addEdges(Set<Trio<V, V, E>> edges);

}
