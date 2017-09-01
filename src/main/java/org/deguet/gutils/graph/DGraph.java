package org.deguet.gutils.graph;

import java.io.Serializable;

public interface DGraph<V, E> extends Serializable, ReadableDGraph<V,E>{

	public <G extends DGraph<V, E> > G delEdge(V a, V b);
	
	public <G extends DGraph<V, E> > G addEdge(V a, V b, E e);
	
	public <G extends DGraph<V, E> > G replaceEdge(V from, V to, E newTag);
	
	public <G extends DGraph<V, E> > G addVertex(V v);
	
	public <G extends DGraph<V, E> > G delVertex(V v);
	
	public <G extends DGraph<V, E> > G replaceVertex(V before, V after);
	
	public <G extends DGraph<V, E> > G empty();
	
	
	
}
