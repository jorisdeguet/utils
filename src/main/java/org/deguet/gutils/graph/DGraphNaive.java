package org.deguet.gutils.graph;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.nuplets.Trio;

import java.util.HashSet;
import java.util.Set;


/**
 * IMMUTABLE Class
 * This class provides one implementation of the DiGraphNoLabel interface.
 * The implementation is based on a set of vertices and a set of edges.
 * 
 * @author j. deguet
 *
 * @param <V> the class of object that are on the vertices
 * @param <E> the class of object that are on the edges
 */
public final class DGraphNaive<V,E> implements DGraph<V,E>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Set<V> vertices;
	
	private final Set<Edge> edges;
	
	public DGraphNaive(){
		this.vertices = new HashSet<V>();
		this.edges = new HashSet<Edge>();
	}
	
	private DGraphNaive(Set<V> vert, Set<Edge> edges){
		this.vertices = new HashSet<V>(vert);
		this.edges = new HashSet<Edge>(edges);
	}
	
	public <G extends DGraph<V, E> > G addVertex(V object){
		V already = vertexFor(object);
		if (already == null){
			Set<V> newVert = new HashSet<V>(vertices);
			newVert.add(object);
			return (G)new DGraphNaive<V,E>(newVert,edges);
		}
		else{
			return (G)this;
		}
	}

	private V vertexFor(V o){
		return (this.vertices.contains(o)?o:null);
	}
	
	public <G extends DGraph<V, E> > G addEdge(V from, V to, E tag){
		V a = vertexFor(from);
		V b = vertexFor(to);
		if (a == null)
			return this.addVertex(from).addEdge(from, to, tag);
		if (b == null)
			return this.addVertex(to).addEdge(from, to, tag);
		Edge edge = new Edge(from,tag,to);
		if (this.edges.contains(edge)){
			return (G)this;
		}
		else{
			Set<Edge> newEdges = new HashSet<Edge>(edges);
			newEdges.add(edge);
			return (G)new DGraphNaive<V,E>(vertices,newEdges);
		}
	}
	
	public Set<V> vertices(){
		HashSet<V> result = new HashSet<V>();
		for (V wrap : this.vertices){
			result.add(wrap);
		}
		return result;
	}
	
	public boolean contains(V v) {
		return this.vertexFor(v) != null;
	}

	public Set<Duo<V, V>> couples() {
		Set<Duo<V,V>> result = new HashSet<Duo<V,V>>();
		for (Edge edge : this.edges){
			result.add(Duo.d(edge.a,edge.b));
		}
		return result;
	}

	public Set<Trio<V,V,E>> triplets(){
		Set<Trio<V,V,E>> result = new HashSet<Trio<V,V,E>>();
		for (Edge edge : this.edges){
			result.add(Trio.t(edge.a,edge.b,edge.tag));
		}
		return result;
	}

	public <G extends DGraph<V, E> > G delVertex(V tag) {
		V vertex = this.vertexFor(tag);
		if (vertex == null){
			return (G)this;
		}
		else{
			Set<V> newVert = new HashSet<V>(vertices);
			Set<Edge> newEdge = new HashSet<Edge>(edges);
			newVert.remove(vertex);
			for (Edge edge : this.edges){
				if (edge.a.equals(tag) || edge.b.equals(tag))
					newEdge.remove(edge);
			}
			return (G)new DGraphNaive<V,E>(newVert,newEdge);
		}
	}

	public Set<Duo<V,E>> labeledPredecessors(V v) {
		Set<Duo<V,E>> result = new HashSet<Duo<V,E>>();
		for (Edge edge : this.edges){
			if (edge.b.equals(v)){
				result.add(Duo.d(edge.a,edge.tag));
			}
		}
		return result;
	}

	public Set<V> sinks() {
		Set<V> result = new HashSet<V>();
		for (V vertex : this.vertices()){
			boolean last = true;
			for (Edge edge : this.edges){
				if (edge.a.equals(vertex)){
					last = false;
				}
			}
			if (last){
				result.add(vertex);
			}
		}
		return result;
	}

	public Set<V> sources() {
		Set<V> result = new HashSet<V>();
		for (V vertex : this.vertices()){
			boolean first = true;
			for (Edge edge : this.edges){
				if (edge.b.equals(vertex)){
					first = false;
				}
			}
			if (first){
				result.add(vertex);
			}
		}
		return result;
	}

	public Set<Duo<V,E>> labeledSuccessors(V v) {
		Set<Duo<V,E>> result = new HashSet<Duo<V,E>>();
		for (Edge edge : this.edges){
			if (edge.a.equals(v)){
				result.add(Duo.d(edge.b,edge.tag));
			}
		}
		return result;
	}
	
	class Edge {
		
		final V a;
		
		final E tag;
		
		final V b;
		
		public Edge(V aa, E t, V bb){
			this.a = aa;
			this.tag = t;
			this.b = bb;
		}
		
		@Override
		public int hashCode(){
			return this.a.hashCode();
		}
		
		
		@Override
		public boolean equals(Object o){
			if (o instanceof DGraphNaive.Edge){
				Edge bis = (Edge) o;
				return this.a.equals(bis.a) && this.b.equals(bis.b);
			}
			return false;
		}
	}

	public <G extends DGraph<V, E> > G replaceVertex(V before, V after) {
		V vert = vertexFor(before);
		V afte = vertexFor(after);
		if (vert == null || afte !=null){
			return (G)this;
		}
		else{
			HashSet<V> newV = new HashSet<V>(vertices);
			newV.remove(vert);
			V newVertex = after;
			newV.add(newVertex);
			HashSet<Edge> newE = new HashSet<Edge>();
			for (Edge edge : edges){
				if (edge.a.equals(before)) 
					newE.add(new Edge(after,edge.tag,edge.b));
				else if (edge.b.equals(before))
					newE.add(new Edge(edge.a,edge.tag,after));
				else
					newE.add(edge);
			}
			return (G)new DGraphNaive<V,E>(newV,newE);
		}
	}

	public <G extends DGraph<V, E> > G replaceEdge(V from, V to, E newTag) {
		Edge edge = new Edge(from,newTag,to);
		if (this.edges.contains(edge)){
			Set<Edge> newEdges = new HashSet<Edge>(edges);
			newEdges.remove(edge);
			newEdges.add(edge);
			return (G)new DGraphNaive<V,E>(vertices,newEdges);
		}
		else{
			return (G)this;
		}
	}

	public <G extends DGraph<V, E> > G delEdge(V from, V to) {
		Edge edge = new Edge(from,null,to);
		if (this.edges.contains(edge)){
			Set<Edge> newEdges = new HashSet<Edge>(edges);
			newEdges.remove(edge);
			return (G)new DGraphNaive<V,E>(vertices,newEdges);
		}
		else{
			return (G)this;
		}
	}
	
	public E getEdge(V from, V to) {
		Edge edge = new Edge(from,null,to);
		if (this.edges.contains(edge)){
			for (Edge old: edges){
				if (old.hashCode() == edge.hashCode() && old.equals(edge))
					return old.tag;
			}
		}
		return null;
	}

	public <G extends DGraph<V, E> > G empty() {
		return (G)new DGraphNaive<V,E>();
	}
	
	public Set<Iterable<V>> pathsFromTo2(Set<V> from, Set<V> to) {
		return DGraphs.pathsFromTo2(this, from, to);
	}
	
}