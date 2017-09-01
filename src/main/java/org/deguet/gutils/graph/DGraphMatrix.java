package org.deguet.gutils.graph;

import java.util.HashSet;
import java.util.Set;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.nuplets.Trio;

/**
 * The attempt to make MatrixGraph with simple arrays.
 * @author joris deguet
 *
 */
public final class DGraphMatrix<V,E> implements DGraph<V,E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The set of vertices on which the graph relation will live.
	 */
	private final V[] vertices;

	/**
	 * This contains the relation of the graph.
	 */
	private final E[][] edges;

	/**
	 * Builds an empty graph
	 */
	@SuppressWarnings("unchecked")
	public DGraphMatrix(){
		this(
				(V[])new Object[0],
				(E[][])new Object[0][0]);
	}

	private DGraphMatrix(V[] vert,E[][] edg){
		this.vertices = vert;
		this.edges = edg;
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G addVertex(V tag){
		// if the vertex is here return the graph.
		if (this.vertexFor(tag) != -1){
			return (G)this;
		}
		// we have a new Vertex to create
		int oldsize = this.vertices.length;
		int newsize = this.vertices.length+1;
		V[] newvertices = (V[])new Object[newsize];
		E[][] newedges = (E[][])new Object[newsize][newsize];
		// copy existing stuff
		System.arraycopy(vertices, 0, newvertices, 0, oldsize);
		for (int a = 0 ; a<this.vertices.length ; a++){
			System.arraycopy(this.edges[a], 0, newedges[a], 0, oldsize);
		}
		newvertices[newsize-1] = tag;
		return (G)new DGraphMatrix<V,E>(newvertices,newedges);
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G addEdge(V a, V b, E tag){
		int verta = this.vertexFor(a);
		// First make sure that we have the necessary vertices
		if (verta == -1){
			return this.addVertex(a).addEdge(a, b, tag);
		}
		int vertb = this.vertexFor(b);
		if (vertb == -1){
			return this.addVertex(b).addEdge(a, b, tag);
		}
		if (this.edges[verta][vertb] == null){
			int size = this.vertices.length;
			E[][] newedges = (E[][]) new Object[size][size];
			for (int index = 0 ; index < size ; index++){
				System.arraycopy(edges[index], 0, newedges[index], 0, size);
			}
			newedges[verta][vertb] = tag;
			return (G)new DGraphMatrix<V,E>(this.vertices,newedges);
		}
		else{
			return (G)this;
		}
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G delVertex(V tag){
		int vpos = this.vertexFor(tag);
		int newSize = this.vertices.length-1;
		V[] newVert = (V[])new Object[newSize];
		E[][] newEdg = (E[][])new Object[newSize][newSize];
		for (int index = 0 ; index < newSize ; index++){
			int oldIndex = (index<vpos?index:index+1);
			newVert[index] = this.vertices[oldIndex];
		}
		for (int a = 0 ; a < newSize ; a++){
			for (int b = 0 ; b < newSize ; b++){
				int olda = (a<vpos?a:a+1);
				int oldb = (b<vpos?b:b+1);
				newEdg[a][b] = this.edges[olda][oldb];
			}
		}
		return (G)new DGraphMatrix<V,E>(newVert,newEdg);
	}

	public Set<V> vertices(){
		HashSet<V> result = new HashSet<V>();
		for (int index = 0 ; index < this.vertices.length ; index++){
			result.add(vertices[index]);
		}
		return result;
	}

	public Set<V> sinks(){
		HashSet<V> result = new HashSet<V>();
		for (V vert : this.vertices()){
			if (this.labeledSuccessors(vert).isEmpty()){
				result.add(vert);
			}
		}
		return result;
	}

	public Set<Duo<V,E>> labeledPredecessors(V v){
		HashSet<Duo<V,E>> result = new HashSet<Duo<V,E>>();
		int vpos = this.vertexFor(v);
		for (int index = 0; index < this.vertices.length; index++){
			if (this.edges[index][vpos] != null){
				result.add(
						Duo.d(
								this.vertices[index],
								this.edges[index][vpos])
						);
			}
		}
		return result;
	}

	public Set<Duo<V,E>> labeledSuccessors(V v){
		HashSet<Duo<V,E>> result = new HashSet<Duo<V,E>>();
		int vpos = this.vertexFor(v);
		for (int index = 0; index < this.vertices.length; index++){
			if (this.edges[vpos][index] != null){
				result.add(
						Duo.d(
								this.vertices[index],
								this.edges[vpos][index])
						);
			}
		}
		return result;
	}

	private int vertexFor(V v){
		int hashv = v.hashCode();
		for (int index = 0; index < this.vertices.length ; index++){
			V other =  this.vertices[index];
			if (other.hashCode() == hashv && other.equals(v)){
				return index;
			}
		}
		return -1;
	}

	public Set<Duo<V,V>> couples() {
		HashSet<Duo<V,V>> result = new HashSet<Duo<V,V>>();
		for (int a = 0 ; a<this.vertices.length ; a++){
			V verta = this.vertices[a];
			for (int b = 0 ; b < this.vertices.length ; b ++){
				V vertb = this.vertices[b];
				if (this.edges[a][b] != null){
					result.add(Duo.d(verta,vertb));
				}
			}
		}
		return result;
	}

	public Set<Trio<V,V,E>> triplets() {
		HashSet<Trio<V,V,E>> result = new HashSet<Trio<V,V,E>>();
		for (int a = 0 ; a<this.vertices.length ; a++){
			V verta = this.vertices[a];
			for (int b = 0 ; b < this.vertices.length ; b ++){
				V vertb = this.vertices[b];
				if (this.edges[a][b] != null){
					result.add(Trio.t(verta,vertb,this.edges[a][b]));
				}
			}
		}
		return result;
	}

	public boolean contains(V v) {
		return this.vertexFor(v) > -1;
	}

	public Set<V> sources() {
		HashSet<V> result = new HashSet<V>();
		for (V vert : this.vertices()){
			if (this.labeledPredecessors(vert).isEmpty()){
				result.add(vert);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G replaceVertex(V before, V after) {
		int ind = vertexFor(before);
		int aft = vertexFor(after);
		if (ind == -1 || aft != -1){return (G)this;}
		else{
			int size = this.vertices.length;
			V[] newvert = (V[])new Object[size];
			System.arraycopy(vertices, 0, newvert, 0, size);
			newvert[ind] = after;
			DGraphMatrix<V,E> res = new DGraphMatrix<V,E>(newvert,edges);
			return (G)res;
		}
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G replaceEdge(V from, V to, E newTag) {
		int indFrom = this.vertexFor(from);
		int indTo   = this.vertexFor(to);
		int size = this.vertices.length;
		E[][] newedges = (E[][])new Object[size][size];
		for (int index = 0 ; index < size ; index++){
			System.arraycopy(edges[index], 0, newedges[index], 0, size);
		}
		newedges[indFrom][indTo] = newTag;
		return (G)new DGraphMatrix<V, E>(vertices,newedges);
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G delEdge(V from, V to) {
		int indFrom = this.vertexFor(from);
		int indTo   = this.vertexFor(to);
		if (indFrom == -1 || indTo == -1){
			return (G)this;
		}
		else{
			int size = this.vertices.length;
			E[][] newedges = (E[][])new Object[size][size];
			for (int index = 0 ; index < size ; index++){
				System.arraycopy(edges[index], 0, newedges[index], 0, size);
			}
			newedges[indFrom][indTo] = null;
			return (G)new DGraphMatrix<V,E>(vertices,newedges);
		}
	}

	@SuppressWarnings("unchecked")
	public E getEdge(V from, V to) {
		int indFrom = this.vertexFor(from);
		int indTo   = this.vertexFor(to);
		if (indFrom == -1 || indTo == -1){
			return null;
		}
		return edges[indFrom][indTo];
	}

	public <G extends DGraph<V, E> > G  empty() {
		return (G)new DGraphMatrix<V,E>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.vertices().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DGraphMatrix other = (DGraphMatrix) obj;
		if (!this.vertices().equals(other.vertices()))
			return false;
		if (!this.triplets().equals(other.triplets()))
			return false;
		return true;
	}

	public Set<Iterable<V>> pathsFromTo2(Set<V> from, Set<V> to) {
		return DGraphs.pathsFromTo2(this, from, to);
	}
	

}
