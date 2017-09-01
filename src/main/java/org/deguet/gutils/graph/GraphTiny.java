package org.deguet.gutils.graph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.deguet.gutils.bit.BitTriangle;
import org.deguet.gutils.nuplets.Duo;

/**
 * Immutable Class.
 * 
 * This is the main implementation of an undirected graph.
 * 
 * 
 * Implementation makes use of bit level storing for edges which makes it really tiny and almost the only
 * solution to deal with very large graphs. 
 * 
 * This implementation uses some kind of boolean matrix to store edges information. Then the object size depends only
 * on the number of vertices. 
 * 
 * @author joris deguet
 *
 * @param <V>
 */
public final class GraphTiny<V> implements Graph<V> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final BitTriangle edges;
	
	private final long size;
	
	private final V[] vertices;
	
	@SuppressWarnings("unchecked")
	public GraphTiny(){
		this.edges = new BitTriangle();
		this.size = 0;
		this.vertices = (V[]) new Object[0];
	}
	
	private GraphTiny(BitTriangle newEdges, long newSize, V[] newVert){
		this.edges = newEdges;
		this.size = newSize;
		this.vertices = newVert;
	}
	
	// Returns the index of v -1 if absent.
	private int vertexFor(V v){
		int hash = v.hashCode();
		for (int index = 0 ; index < vertices.length ; index++){
			V vert = vertices[index];
			if (vert.hashCode() == hash && vert.equals(v))
				return index;
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	public <G extends Graph<V>> G delEdge(V a, V b) {
		int indexA = this.vertexFor(a);
		int indexB = this.vertexFor(b);
		if (indexA == -1 || indexB == -1){
			return (G) this;
		}
		else{
			int small = Math.min(indexA, indexB);
			int big   = Math.max(indexA, indexB);
			BitTriangle newEdges = edges.disconnect(big, small);
			return (G) new GraphTiny<V>(newEdges,size,vertices);
		}
	}

	@SuppressWarnings("unchecked")
	public <G extends Graph<V>> G addVertex(V tag) {
		int index = this.vertexFor(tag);
		if (index != -1){
			return (G) this;
		}
		else{
			long newSize = size + 1;
			// Might be necessary to augment the size of edges.
			BitTriangle newEdges = edges.incrementSize(1);
			V[] newVert = (V[]) new Object[(int) newSize];
			System.arraycopy(vertices, 0, newVert, 0, vertices.length);
			newVert[(int) size] = tag;
			return (G) new GraphTiny<V>(newEdges,newSize,newVert);
		}
	}

	@SuppressWarnings("unchecked")
	public <G extends Graph<V>> G addVertices(Set<V> verts) {
		G result = (G) this;
		for (V v : verts)
			result = result.addVertex(v);
		return result;
	}

	@SuppressWarnings("unchecked")
	public <G extends Graph<V>> G replaceVertex(V before, V after) {
		int indexBef = this.vertexFor(before);
		if (indexBef == -1){
			return (G) this;
		}
		else{
			V[] newVert = (V[]) new Object[vertices.length];
			System.arraycopy(vertices, 0, newVert, 0, vertices.length);
			newVert[indexBef] = after;
			return (G) new GraphTiny<V>(edges,size,newVert);
		}
	}

	@SuppressWarnings("unchecked")
	public <G extends Graph<V>> G delVertex(V tag) {
		int index = this.vertexFor(tag);
		if (index == -1){
			return (G)this;
		}
		else{
			GraphTiny<V> res = new GraphTiny<V>();
			for (Duo<V,V> edge : this.couples()){
				if (edge.get1().equals(tag) || edge.get2().equals(tag)){
					continue;
				}
				else{
					res =res.addEdge(edge.get1(), edge.get2());
				}
			}
			return (G) res;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <G extends Graph<V>> G addEdge(V a, V b) {
		int indexA = this.vertexFor(a);
		if (indexA == -1){
			return this.addVertex(a).addEdge(a, b);
		}
		int indexB = this.vertexFor(b);
		if (indexB == -1){
			return this.addVertex(b).addEdge(a, b);
		}
		int small = (indexA>indexB?indexB:indexA);
		int big   = (indexA<indexB?indexB:indexA);
		//System.out.println("((("+edges.numberOfBits());
		BitTriangle newEdges = edges.connect(big,small);
		//System.out.println(")))"+newEdges.numberOfBits());
		return (G) new GraphTiny<V>(newEdges, size, vertices);
	}

	public Set<V> vertices() {
		Set<V> result = new HashSet<V>();
		for (V v : vertices){
			result.add(v);
		}
		return result;
	}

	public boolean contains(V vertex) {
		return this.vertexFor(vertex) != -1;
	}

	public String toDot(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("graph "+name+" \n{\n");
		for (V tag : vertices){
			sb.append("\n         \""+tag+"\"   [label=\""+tag+"@"+this.vertexFor(tag)+"\"] ");
		}
		for(Duo<V,V> couple : couples()){
			sb.append("\n      \""+couple.get1()+"\" -- \""+couple.get2()+"\"");
		}
		sb.append("\n}\n");
		return sb.toString();
	}

	public Set<Duo<V, V>> couples() {
		Set<Duo<V,V>> result = new HashSet<Duo<V,V>>();
		for(int i = 0 ; i < size ; i++){
			for(int j = i ; j < size ; j++){
				if (edges.isCon(j, i)){
					result.add(Duo.d(vertices[j],vertices[i]));
				}
			}
		}
		return result;
	}
	
	public String toString(){return this.toDot("tinygraph");}
	
	public Set<V> neighbors(V v) {
		Set<V> result = new HashSet<V>();
		int indexV = this.vertexFor(v);
		if (indexV != -1){
			//smaller index
			for (int i = 0 ; i < indexV ; i++){
				if (edges.isCon(indexV, i)){
					result.add(vertices[i]);
				}
			}
			//bigger index
			for (int i = indexV ; i < size ; i++){
				if (edges.isCon(i, indexV)){
					result.add(vertices[i]);
				}
			}
		}
		return result;
	}
	
	public boolean isComplete() {
		for (int a = 0 ; a < vertices.length ; a++){
			for (int b = a+1 ; b < vertices.length ; b++){
				if (!edges.isCon(b, a)){
					return false;
				}
			}
		}
		return true;
	}
	
	
	public <G extends Graph<V>> G restrictTo(Set<V> kept) {
		G result =  (G)this;
		for (V vert : vertices){
			if (!kept.contains(vert)){
				result = result.delVertex(vert);
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(vertices);
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
		GraphTiny other = (GraphTiny) obj;
		if (edges == null) {
			if (other.edges != null)
				return false;
		} else if (!edges.equals(other.edges))
			return false;
		if (size != other.size)
			return false;
		if (!Arrays.equals(vertices, other.vertices))
			return false;
		return true;
	}
}
