package org.deguet.gutils.graph;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.nuplets.Trio;

import java.util.Set;

/**
 * Readable DGraph
 * @author joris
 *
 */
public interface ReadableDGraph<V,E>  {

	public E  getEdge(V a, V b);
	
	public Set<Duo<V,E>> labeledPredecessors(V v);
	
	public Set<Duo<V,E>> labeledSuccessors(V v);
	
	public Set<Duo<V, V>> couples();
	
	public Set<V> vertices();
	
	public Set<V> sinks();
	
	public Set<V> sources();
	
	public Set<Trio<V, V, E>> triplets();
	
	public boolean contains(V v);

	public Set<Iterable<V>> pathsFromTo2(Set<V> starters, Set<V> finishers);
	
}
