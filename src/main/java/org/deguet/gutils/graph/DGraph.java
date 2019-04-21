package org.deguet.gutils.graph;

import java.io.Serializable;

public interface DGraph<V, E> extends Serializable, ReadableDGraph<V,E>{

	<G extends DGraph<V, E> > G delEdge(V a, V b);
	
	<G extends DGraph<V, E> > G addEdge(V a, V b, E e);
	
	<G extends DGraph<V, E> > G replaceEdge(V from, V to, E newTag);
	
	<G extends DGraph<V, E> > G addVertex(V v);
	
	<G extends DGraph<V, E> > G delVertex(V v);
	
	<G extends DGraph<V, E> > G replaceVertex(V before, V after);
	
	<G extends DGraph<V, E> > G empty();
	
	
	
}
