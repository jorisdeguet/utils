package org.deguet.gutils.graph;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.pairing.Pairing;
import org.deguet.gutils.pairing.PairingHopcroft;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class implements an undirected graph with labels on its edges.
 * 
 * This is based on giving one index to every vertex and on computing edge indices through a pairing function.
 * 
 * The pairing function PairingHopcroft is described in this class javadoc.
 * 
 * @author joris
 *
 * @param <V>
 * @param <E>
 */
public final class GraphTagTiny<V,E> implements GraphTag<V,E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6040888678218480426L;

	private final Map<Integer,E> edges;
	
	private final long size;
	
	private final V[] vertices;
	
	private final Pairing pair = new PairingHopcroft();
	
	@SuppressWarnings("unchecked")
	public GraphTagTiny(){
		this.edges = new HashMap<Integer,E>();
		this.size = 0;
		this.vertices = (V[]) new Object[0];
	}
	
	public GraphTagTiny(Map<Integer,E> newEdges, long newSize, V[] newVert){
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
	
	

	public boolean contains(V vertex) {
		return this.vertexFor(vertex) != -1;
	}

	public Set<V> vertices() {
		Set<V> result = new HashSet<V>();
		for (V v : vertices){
			result.add(v);
		}
		return result;
	}

	public Set<V> neighbors(V v) {
		Set<V> result = new HashSet<V>();
		int indexV = this.vertexFor(v);
		if (indexV != -1){
			for (int i = indexV ; i < size ; i++){
				int index = pair.compose(Duo.d((long) i,(long) indexV)).intValue();
				if (edges.containsKey(index)){
					//System.out.println("Detect neighbor of "+v+" is "+vertices[i]);
					result.add(vertices[i]);
				}
			}
		}
		return result;
	}
	
	public Set<Duo<V,V>> couples(){
		Set<Duo<V,V>> result = new HashSet<Duo<V,V>>();
		for(int i = 0 ; i < size ; i++){
			for(int j = i ; j < size ; j++){
				int index = pair.compose(Duo.d((long) j,(long) i)).intValue();
				if (edges.containsKey(index)){
					result.add(Duo.d(vertices[j],vertices[i]));
				}
			}
		}
		return result;
	}	
	
	public String toDot(String arg){
		StringBuilder sb = new StringBuilder();
		sb.append("graph prout \n{\n");
		for (V tag : vertices){
			sb.append("\n         \""+tag+"\"   [label=\""+tag+"@"+this.vertexFor(tag)+"\"] ");
		}
		for(Duo<V,V> couple : couples()){
			sb.append("\n      \""+couple.get1()+"\" -- \""+couple.get2()+"\"");
		}
		sb.append("\n}\n");
		sb.append("edges  \n===============\n"+edges.toString()+"\n===============");
		return sb.toString();
	}

	public Set<Duo<V, E>> labelledNeighbors(V v) {
		int indexV = this.vertexFor(v);
		Set<Duo<V,E>> result = new HashSet<Duo<V,E>>();
		for (int vertex = indexV ; vertex < vertices.length ; vertex++){
			int edgeIndex = this.pair.compose(Duo.d((long) vertex,(long) indexV)).intValue();
			if (edges.containsKey(edgeIndex)){
				result.add(Duo.d(vertices[vertex],edges.get(edgeIndex)));
			}
		}
		for (int vertex = 0 ; vertex < indexV ; vertex++){
			int edgeIndex = this.pair.compose(Duo.d((long) indexV,(long) vertex)).intValue();
			if (edges.containsKey(edgeIndex)){
				result.add(Duo.d(vertices[vertex],edges.get(edgeIndex)));
			}
		}
		return result;
	}

	public GraphTagTiny<V, E> replaceEdge(V from, V to, E newTag) {
		int indexA = this.vertexFor(from);
		int indexB = this.vertexFor(to);
		int lit = indexA>indexB?indexB:indexA;
		int big = indexA<indexB?indexB:indexA;
		int edgeIndex = pair.compose(Duo.d((long) big, (long) lit)).intValue();
		if (indexA == -1 || indexB == -1 || !edges.containsKey(edgeIndex)){
			return this;
		}
		else{
			Map<Integer,E> newEdges = new HashMap<Integer,E>(edges);
			newEdges.remove(edgeIndex);
			newEdges.put(edgeIndex, newTag);
			return new GraphTagTiny<V,E>(newEdges,size,vertices);
		}
	}


	public GraphTagTiny<V, E> restrictTo(Set<V> kept) {
		GraphTagTiny<V,E> result = this;
		for (V vert : vertices){
			if (!kept.contains(vert)){
				result = result.delVertex(vert);
			}
		}
		return result;
	}

	public <G extends GraphTag<V,E>> G delEdge(V a, V b) {
		int indexA = this.vertexFor(a);
		int indexB = this.vertexFor(b);
		if (indexA == -1 || indexB == -1){
			return (G)this;
		}
		else{
			int small = Math.min(indexA, indexB);
			int big   = Math.max(indexA, indexB);
			int index = pair.compose(Duo.d((long) big,(long) small)).intValue();
			Map<Integer,E> newEdges = new HashMap<Integer,E>(edges);
			newEdges.remove(index);
			return (G)new GraphTagTiny<V,E>(newEdges,size,vertices);
		}
	}

	public <G extends GraphTag<V,E>> G addEdge(V a, V b, E t) {
		int indexA = this.vertexFor(a);
		if (indexA == -1){
			return this.addVertex(a).addEdge(a, b, t);
		}
		int indexB = this.vertexFor(b);
		if (indexB == -1){
			return this.addVertex(b).addEdge(a, b, t);
		}
		int small = (indexA>indexB?indexB:indexA);
		int big   = (indexA<indexB?indexB:indexA);
		Map<Integer,E> newEdges = new HashMap<Integer,E>(edges);
		int index = pair.compose(Duo.d((long) big, (long) small)).intValue();
		newEdges.put(index,t);
		return (G) new GraphTagTiny<V,E>(newEdges, size, vertices);
	}

	public <G extends GraphTag<V,E>> G addVertex(V tag) {
		int index = this.vertexFor(tag);
		if (index != -1){
			return (G)this;
		}
		else{
			long newSize = size + 1;
			V[] newVert = (V[]) new Object[(int) newSize];
			System.arraycopy(vertices, 0, newVert, 0, vertices.length);
			newVert[(int) size] = tag;
			return (G)new GraphTagTiny<V,E>(edges,newSize,newVert);
		}
	}

	public <G extends GraphTag<V,E>> G addVertices(Set<V> verts) {
		// TODO Auto-generated method stub
		return null;
	}

	public <G extends GraphTag<V,E>> G replaceVertex(V before, V after) {
		int indexBef = this.vertexFor(before);
		if (indexBef == -1){
			return (G)this;
		}
		else{
			V[] newVert = (V[]) new Object[vertices.length];
			System.arraycopy(vertices, 0, newVert, 0, vertices.length);
			newVert[indexBef] = after;
			return (G)new GraphTagTiny<V,E>(edges,size,newVert);
		}
	}

	public <G extends GraphTag<V,E>> G delVertex(V tag) {
		int index = this.vertexFor(tag);
		if (index == -1){
			return (G)this;
		}
		else{
			long newSize = size - 1;
			V[] newVert = (V[]) new Object[(int)newSize];
			System.arraycopy(vertices, 0, newVert, 0, index);
			System.arraycopy(vertices, index+1, newVert, index, vertices.length-index-1);
			Map<Integer,E> newEdges = new HashMap<Integer,E>();
			for (Integer edgeIndex : edges.keySet()){
				Duo<Long,Long> couple = pair.decompose((long) edgeIndex);
				int indexA = couple.get1().intValue();
				int indexB = couple.get2().intValue();
				if (indexA != index && indexB != index){
					indexA = indexA>index?indexA-1:indexA;
					indexB = indexB>index?indexB-1:indexB;
					int newIndex = pair.compose(Duo.d((long) indexA,(long) indexB)).intValue();
					edges.put(newIndex, edges.get(edgeIndex));
				}
			}
			return (G)new GraphTagTiny<V,E>(newEdges,newSize,newVert);
		}
	}

	public <G extends Graph<V>> G addEdge(V a, V b) {
		return this.addEdge(a, b, null);
	}
	
}
