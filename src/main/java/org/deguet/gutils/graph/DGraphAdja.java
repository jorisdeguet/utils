package org.deguet.gutils.graph;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.nuplets.Trio;

import java.util.HashSet;
import java.util.Set;

/**
 * IMMUTABLE Class
 * 
 * This implementation of the DiGraphNoLabel Interface uses adjacency lists.
 * This should be slower than matrix based graphs for most consultations but also
 * exhibits a smaller memory footprint.
 * 
 * @author joris deguet
 *
 * @param <V>
 * @param <E>
 */
public final class DGraphAdja<V,E> implements DGraph<V,E>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * adjacency lists.
	 */
	private final Set<EdgeBucket>[] sets;

	private final V[] vertices;

	@SuppressWarnings("unchecked")
	public DGraphAdja(){
		V[] vert = (V[])new Object[0];
		vertices = vert;
		sets     = new Set[0];
	}

	private DGraphAdja(V[] verts, Set<EdgeBucket>[] sets){
		this.sets = sets;
		this.vertices = verts;
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G addEdge(V a, V b, E tag) {
		if (!this.contains(a)){
			return this.addVertex(a).addEdge(a, b, tag);
		}
		if (!this.contains(b)){
			return this.addVertex(b).addEdge(a, b, tag);
		}
		int index = this.indexFor(a);
		EdgeBucket bucket = new EdgeBucket(b,tag);
		if (sets[index] != null && sets[index].contains(bucket)){
			return (G)this;
		}
		else{
			// Now perform the actual insertion
			Set<EdgeBucket>[] newSets = new Set[sets.length];
			for (int ind = 0 ; ind < sets.length ; ind ++){
				newSets[ind] = sets[ind];
			}
			// Make it a new set
			newSets[index] = (newSets[index]==null?new HashSet<EdgeBucket>():new HashSet<EdgeBucket>(newSets[index]));
			newSets[index].add(bucket);
			return (G)new DGraphAdja<V, E>(vertices,newSets);
		}
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G addVertex(V tag) {
		if (this.contains(tag)){
			return (G)this;
		}
		else{
			int newSize= this.sets.length + 1;
			Set<EdgeBucket>[] newSets = 
				new Set[newSize];
			V[] newVert = (V[])new Object[newSize];
			System.arraycopy(vertices, 0, newVert, 0, vertices.length);
			System.arraycopy(sets, 0, newSets, 0, vertices.length);
			newVert[vertices.length] = tag;
			return (G)new DGraphAdja<V, E>(newVert,newSets);
		}
	}

	public Set<Duo<V, V>> couples() {
		Set<Duo<V,V>> result =
			new HashSet<Duo<V,V>>();
		for (int index = 0 ; index < this.vertices.length ; index++){
			V a = this.vertices[index];
			if (this.sets[index] != null){
				for (EdgeBucket bucket : this.sets[index]){
					result.add(Duo.d(a,bucket.vertex));
				}
			}
		}
		return result;
	}

	public Set<Trio<V, V, E>> triplets() {
		Set<Trio<V, V, E>> result =
			new HashSet<Trio<V, V, E>>();
		for (int index = 0 ; index < this.vertices.length ; index++){
			V a = this.vertices[index];
			if (this.sets[index] != null){
				for (EdgeBucket bucket : this.sets[index]){
					result.add(Trio.t(a,bucket.vertex,bucket.tag));
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G delVertex(V tag) {
		int index = this.indexFor(tag);
		// Contained
		if (index > -1){
			// Remove vertex
			V[] newVerts = (V[]) new Object[this.vertices.length-1];
			System.arraycopy(vertices, 0, newVerts, 0, index);
			System.arraycopy(vertices, index+1, newVerts, index, (vertices.length - (index+1)));

			// Remove edges
			Set<EdgeBucket>[] newSets = new Set[this.vertices.length-1];
			System.arraycopy(sets, 0, newSets, 0, index);
			System.arraycopy(sets, index+1, newSets, index, (vertices.length - (index+1)));
			// Remove edges towards this vertex
			EdgeBucket fake = new EdgeBucket(tag,null);
			for (int ind = 0 ; ind < newVerts.length ; ind++){
				if (newSets[ind] != null && newSets[ind].contains(fake)) {
					newSets[ind] = new HashSet<EdgeBucket>(newSets[ind]);
					newSets[ind].remove(fake);
				}
			}
			return (G)new DGraphAdja<V,E>(newVerts,newSets);
		}
		else{
			return (G)this;
		}
	}

	public Set<Duo<V,E>> labeledPredecessors(V v) {
		try{
			Set<Duo<V,E>> result = new HashSet<Duo<V,E>>();
			//EdgeBucket bucket = new EdgeBucket(v,null);
			for (int index = 0 ; index < sets.length ; index++){
				if (this.sets[index] != null) {
					for (EdgeBucket b : sets[index]){
						if (b.vertex.equals(v)){
							result.add(Duo.d(vertices[index],b.tag));
						}
					}
				}
			}
			return result;
		}
		catch(Throwable t){
			t.printStackTrace();
			//Thread.dumpStack();Thread.currentThread().dumpStack();
			return null;}
	}

	public Set<V> sinks() {
		Set<V> result = new HashSet<V>();
		for (int index = 0 ; index < this.vertices.length ; index++){
			if (sets[index] == null || sets[index].isEmpty()){
				result.add(vertices[index]);
			}
		}
		return result;
	}

	public Set<V> sources() {
		Set<V> result = new HashSet<V>();
		for (int index = 0 ; index < vertices.length ; index++){
			V vertex = vertices[index];
			if (this.labeledPredecessors(vertex).isEmpty()){
				result.add(vertex);
			}
		}
		return result;
	}

	public Set<Duo<V,E>> labeledSuccessors(V v) {
		if (this.contains(v)){
			Set<EdgeBucket> buckets = this.sets[this.indexFor(v)];
			if (buckets != null){
				Set<Duo<V,E>> result = new HashSet<Duo<V,E>>(buckets.size());
				for (EdgeBucket bucket : buckets){
					result.add(Duo.d(bucket.vertex,bucket.tag));
				}
				return result;
			}
			return new HashSet<Duo<V,E>>();
		}
		return new HashSet<Duo<V,E>>();
	}

	// memory question
	public int bucketNumber(){
		int result = 0;
		for (int index = 0 ; index < this.vertices.length ; index++){
			if (sets[index] != null)
				result += sets[index].size();
		}
		return result;
	}

	// memory question
	public int estimateSize(){
		int result = this.vertices.length *2;
		result += bucketNumber()*2;
		return result;
	}

	public Set<V> vertices() {
		HashSet<V> result =
			new HashSet<V>();
		for (V v: this.vertices){
			result.add(v);
		}
		return result;
	}

	private class EdgeBucket{

		public E tag;

		public V vertex;

		public EdgeBucket(V v, E e){
			this.tag = e;
			this.vertex = v;
		}

		@Override
		public int hashCode(){return vertex.hashCode();}

		@Override
		public boolean equals(Object o){
			if (o instanceof DGraphAdja.EdgeBucket){
				EdgeBucket bis = (EdgeBucket) o;
				return bis.vertex.equals(this.vertex);
			}
			return false;
		}

		@Override
		public String toString(){
			return "("+tag+")->"+vertex;
		}
	}

	private int indexFor(V v){
		int hashv= v.hashCode();
		for (int index = 0 ; index < this.vertices.length ; index++){
			V other = this.vertices[index];
			if (other.hashCode() == hashv && other.equals(v)){
				return index;
			}
		}
		return -1;
	}

	public boolean contains(V v) {
		return this.indexFor(v) > -1;
	}

	
	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G replaceVertex(V before, V after) {
		int bef = this.indexFor(before);
		int aft = this.indexFor(after);
		if (bef == -1 || aft != -1){
			return (G)this;
		}
		else{
			int size = vertices.length;
			// Handle vertices
			V[] newV = (V[])new Object[size];
			System.arraycopy(vertices, 0, newV, 0, size);
			newV[bef] = after;
			// Handle edges
			Set<EdgeBucket>[] newS = new Set[size];
			System.arraycopy(sets, 0, newS, 0, size);
			for (int index = 0 ; index < size ; index++){
				if (newS[index] != null){
					for (EdgeBucket bucket : newS[index]){
						if (bucket.vertex.equals(before)){
							newS[index].remove(bucket);
							newS[index].add(new EdgeBucket(after,bucket.tag));
						}
					}
				}
			}
			return (G)new DGraphAdja<V, E>(newV, newS);
		}
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G replaceEdge(V from, V to, E newTag) {
		int index = this.indexFor(from);
		EdgeBucket bucket = new EdgeBucket(to,newTag);
		// Contained in graph
		if (sets[index] != null && sets[index].contains(bucket)){
			Set<EdgeBucket>[] newSets = new Set[sets.length];
			System.arraycopy(sets, 0, newSets, 0, sets.length);
			// Make it a new set
			newSets[index] = (newSets[index]==null?new HashSet<EdgeBucket>():new HashSet<EdgeBucket>(newSets[index]));
			// Play a little bit with equality to remove old one and add new one.
			newSets[index].remove(bucket);
			newSets[index].add(bucket);
			return (G)new DGraphAdja<V, E>(vertices, newSets);
		}
		// Not contained in Graph
		else{
			return (G)this;

		}
	}
	
	@SuppressWarnings("unchecked")
	public E getEdge(V from, V to) {
		int index = this.indexFor(from);
		if (index == -1 ){
			return null;
		}
		for (EdgeBucket bucket : this.sets[index]){
			if (bucket.vertex.equals(to)){
				return bucket.tag;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <G extends DGraph<V, E> > G delEdge(V from, V to) {
		int index = this.indexFor(from);
		EdgeBucket bucket = new EdgeBucket(to,null);
		// Contained in graph
		if (sets[index] != null && sets[index].contains(bucket)){
			Set<EdgeBucket>[] newSets = new Set[sets.length];
			System.arraycopy(sets, 0, newSets, 0, sets.length);
			// Make it a new set
			newSets[index] = (newSets[index]==null?new HashSet<EdgeBucket>():new HashSet<EdgeBucket>(newSets[index]));
			newSets[index].remove(bucket);
			if (newSets[index].isEmpty())
				newSets[index] = null;
			return (G)new DGraphAdja<V, E>(vertices, newSets);
		}
		// Not contained in Graph
		else{
			return (G)this;

		}
	}

	public <G extends DGraph<V, E> > G empty() {
		return (G)new DGraphAdja<V,E>();
	}
	
	public Set<Iterable<V>> pathsFromTo2(Set<V> from, Set<V> to) {
		return DGraphs.pathsFromTo2(this, from, to);
	}
}
