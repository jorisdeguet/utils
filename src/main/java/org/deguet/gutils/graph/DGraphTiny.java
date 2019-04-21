package org.deguet.gutils.graph;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.nuplets.Trio;
import org.deguet.gutils.pairing.Pairing;
import org.deguet.gutils.pairing.PairingConcat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * Immutable Class
 * 
 * Implements a directed labelled graph in a tiny fashion. 
 * 
 * The size of the object depends on both the numbers of vertices and edges.
 * This makes it adapted for sparse graphs.
 * 
 * @author joris
 *
 * @param <V>
 * @param <E>
 */
public class DGraphTiny<V,E> implements DGraph<V,E>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final V[] vertices;

	private final Map<Long,E> edges;

	private final Pairing pair =
		//new PairingSquare();
	    new PairingConcat();

	@SuppressWarnings("unchecked")
	public DGraphTiny(){
		vertices = (V[]) new Object[0];
		edges = new HashMap<Long, E>();
	}

	private DGraphTiny(final V[] newVert, final Map<Long,E> newEdges){
		vertices = newVert;
		edges = newEdges;
		//System.out.println("Tiny constructor " + edges+ "  " +vertices);
	}

	public <G extends DGraph<V, E> > G addEdge(V a, V b, E tag) {
		int indexA = this.vertexFor(a);
		int indexB = this.vertexFor(b);
		if (indexA == -1 ){
			return this.addVertex(a).addEdge(a, b, tag);
		}
		if (indexB == -1){
			return this.addVertex(b).addEdge(a, b, tag);
		}
		else{
			long edgeIndex = pair.compose(Duo.d((long)indexA,(long)indexB));
			if (!edges.containsKey(edgeIndex)){
				Map<Long,E> newEdges = new HashMap<Long,E>(edges);
				newEdges.remove(edgeIndex);
				newEdges.put(edgeIndex, tag);
				//System.out.println("Tiny addEdge " + edgeIndex+ " from " +indexA+" "+indexB);
				return (G)new DGraphTiny<V, E>(vertices,newEdges);
			}
			else{
				return (G)this;
			}
		}
	}

	public Set<Duo<V, E>> labeledPredecessors(V v) {
		long index = vertexFor(v);
		Set<Duo<V,E>> result = new HashSet<Duo<V,E>>();
		for (long i = 0 ; i < vertices.length ; i++){
			long edgeIndex = pair.compose(Duo.d(i,index));
			if (edges.containsKey(edgeIndex))
				result.add(Duo.d(vertices[(int)i],edges.get(edgeIndex)));
		}
		return result;
	}

	public Set<Duo<V, E>> labeledSuccessors(V v) {
		long index = vertexFor(v);
		Set<Duo<V,E>> result = new HashSet<Duo<V,E>>();
		for (long i = 0 ; i < vertices.length ; i++){
			long edgeIndex = pair.compose(Duo.d(index,i));
			if (edges.containsKey(edgeIndex)){
				result.add(Duo.d(vertices[(int)i],edges.get(edgeIndex)));
			}
		}
		return result;
	}

	public <G extends DGraph<V, E> > G replaceEdge(V from, V to, E newTag) {
		int indexA = this.vertexFor(from);
		int indexB = this.vertexFor(to);
		if (indexA == -1 || indexB == -1){
			return (G)this;
		}
		else{
			long edgeIndex = pair.compose(Duo.d((long)indexA,(long)indexB));
			if (edges.containsKey(edgeIndex)){
				Map<Long,E> newEdges = new HashMap<Long,E>(edges);
				newEdges.remove(edgeIndex);
				newEdges.put(edgeIndex, newTag);
				return (G)new DGraphTiny<V,E>(vertices,newEdges);
			}
			else{
				return (G)this;
			}
		}
	}

	public Set<Trio<V, V, E>> triplets() {
		Set<Trio<V,V,E>> result = new HashSet<Trio<V,V,E>>();
		for (Map.Entry<Long, E> entry : edges.entrySet()){
			Duo<Long,Long> indices = this.pair.decompose(entry.getKey().longValue());
			result.add(
					Trio.t(
							vertices[indices.get1().intValue()] ,
							vertices[indices.get2().intValue()],
							entry.getValue()));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G addVertex(V tag) {
		int indexV = this.vertexFor(tag);
		if (indexV != -1){
			return (G)this;
		}
		else{
			V[] newVert = (V[])new Object[vertices.length+1];
			System.arraycopy(vertices, 0, newVert, 0, vertices.length);
			newVert[vertices.length] = tag;
			return (G)new DGraphTiny<V,E>(newVert, edges);
		}
	}

	public Set<Duo<V, V>> couples() {
		Set<Duo<V,V>> result = new HashSet<Duo<V,V>>();
		for (Map.Entry<Long, E> entry : edges.entrySet()){
			Duo<Long,Long> indices = this.pair.decompose(entry.getKey().longValue());
			result.add(
					Duo.d(
							vertices[indices.get1().intValue()], 
							vertices[indices.get2().intValue()]
					)
			);
		}
		return result;
	}

	public <G extends DGraph<V, E> > G delEdge(V a, V b) {
		long indexA = this.vertexFor(a);
		long indexB = this.vertexFor(b);
		if (indexA == -1 || indexB == -1){
			return (G)this;
		}
		else{
			long edgeIndex = pair.compose(Duo.d(indexA,indexB));
			Map<Long,E> newEdges = new HashMap<Long,E>(edges);
			newEdges.remove(edgeIndex);
			return (G)new DGraphTiny<V,E>(vertices,newEdges);
		}
	}
	
	public E getEdge(V a, V b) {
		long indexA = this.vertexFor(a);
		long indexB = this.vertexFor(b);
		if (indexA == -1 || indexB == -1){
			return null;
		}
		else{
			long edgeIndex = pair.compose(Duo.d(indexA,indexB));
			return edges.get(edgeIndex);
		}
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G delVertex(V tag) {
		int indexV = this.vertexFor(tag);
		if (indexV == -1){
			return (G)this;
		}
		else{
			V[] newVert = (V[]) new Object[vertices.length-1];
			System.arraycopy(vertices, 0, newVert, 0, indexV);
			System.arraycopy(vertices, indexV+1, newVert, indexV, vertices.length-indexV-1);
			Map<Long,E> newEdges = new HashMap<Long,E>();
			for (Long edgeIndex : edges.keySet()){
				Duo<Long,Long> coords = pair.decompose(edgeIndex);
				long a = coords.get1();
				long b = coords.get2();
				if ( a!=indexV && b != indexV){
					// this is to keep
					a = a>indexV?a-1:a;
					b = b>indexV?b-1:b;
					long newEdgeIndex = pair.compose(Duo.d(a,b));
					newEdges.put(newEdgeIndex, edges.get(edgeIndex));
				}
			}
			return (G)new DGraphTiny<V,E>(newVert,newEdges);
		}
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G replaceVertex(V before,
			V after) {
		int indexV = this.vertexFor(before);
		if (indexV == -1){
			return (G)this;
		}
		else{
			V[] newVert = (V[]) new Object[vertices.length];
			System.arraycopy(vertices, 0, newVert, 0, vertices.length);
			newVert[indexV] = after;
			return (G)new DGraphTiny<V,E>(newVert,edges);
		}
	}

	public Set<V> sinks() {
		Set<V> result = new HashSet<V>();
		for (int i = 0 ; i < vertices.length ; i++){
			if (this.labeledSuccessors(vertices[i]).isEmpty())
				result.add(vertices[i]);
		}
		return result;
	}

	public Set<V> sources() {
		Set<V> result = new HashSet<V>();
		for (int i = 0 ; i < vertices.length ; i++){
			if (this.labeledPredecessors(vertices[i]).isEmpty())
				result.add(vertices[i]);
		}
		return result;
	}

	public Set<V> vertices() {
		Set<V> result = new HashSet<V>();
		for (V v : vertices){
			result.add(v);
		}
		return result;
	}

	private int vertexFor(V v){
		int hash = v.hashCode();
		for (int i = 0; i < vertices.length ; i++){
			V vert = vertices[i];
			if (vert.hashCode() == hash && vert.equals(v))
				return i;
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G addEdges(Set<Trio<V,V,E>> e){
		Set<V> vertset = new HashSet<V>();
		//vertset.addAll(this.vertices());
		for (Trio<V,V,E> edge : e){
			vertset.add(edge.get1());
			vertset.add(edge.get2());
		}
		vertset.removeAll(this.vertices());
		Map<V,Integer> rev = new HashMap<V,Integer>();
		// try to guarantee that old indexes will remain.
		V[] verts = (V[]) new Object[vertset.size()+this.vertices.length];
		System.arraycopy(this.vertices, 0, verts, 0, this.vertices.length);
		int index = this.vertices.length;
		for (V elt : vertset){
			verts[index] = elt;
			rev.put(elt,index);
			index++;
		}
		Map<Long,E> edgs = new HashMap<Long,E>();
		edgs.putAll(this.edges);  // can recover all precedent edges as elements indexes are the same, the pair is the same 
		for (Trio<V,V,E> edge : e){      // add the new edges
			long a = (rev.containsKey(edge.get1())?rev.get(edge.get1()):this.vertexFor(edge.get1()));
			long b = (rev.containsKey(edge.get2())?rev.get(edge.get2()):this.vertexFor(edge.get2()));
			long newEdgeIndex = pair.compose(Duo.d(a,b));
			edgs.put(newEdgeIndex, edge.get3());
		}
		return (G)new DGraphTiny<V,E>(verts,edgs);
	}

	//////////////////////////////////// Opt stuff	

	public Map<V,Set<Duo<V,E>>> predecessors(){
		Map<V,Set<Duo<V,E>>> result = 
			new HashMap<V,Set<Duo<V,E>>>();
		for (V v: this.vertices){
			result.put(v, new HashSet<Duo<V,E>>());
		}
		for (Entry<Long, E> entry : edges.entrySet()){
			Duo<Long,Long> coords = pair.decompose(entry.getKey());
			Set<Duo<V,E>> set = result.get(vertices[coords.get2().intValue()]);
			set.add(Duo.d(vertices[coords.get1().intValue()],entry.getValue()));
		}
		return result;
	}

	public <G extends DGraph<V, E> > G empty() {
		return (G)new DGraphTiny<V,E>();
	}

	public boolean contains(V v) {
		return this.vertices().contains(v);
	}

	public Set<Iterable<V>> pathsFromTo2(Set<V> from, Set<V> to) {
		return DGraphs.pathsFromTo2(this, from, to);
	}

}
