package org.deguet.gutils.graph.mutable;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.nuplets.Trio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * MUTABLE Class
 * 
 * This implementation uses adjacency lists.
 * This should be slower than matrix based graphs for most consultations but also
 * exhibits a smaller memory footprint.
 * 
 * @author joris deguet
 *
 * @param <V>
 * @param <E>
 */
public final class MuDja<V,E> extends AbstractMutableDGraph<V,E>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * adjacency sets.
	 */
	private final Map<V,Set<EdgeBucket>> sets;

	public MuDja(){
		sets     = new HashMap<V,Set<EdgeBucket>>();
	}


	public void addEdge(V a, V b, E tag) {
		addVertex(a);
		addVertex(b);
		Set<EdgeBucket> current = sets.get(a);
		EdgeBucket bucket = new EdgeBucket(b,tag);
		if (current != null && current.contains(bucket)){
			return ;
		}
		current.add(bucket);
	}

	@SuppressWarnings("unchecked")
	public void addVertex(V tag) {
		if (this.contains(tag)){
			return ;
		}
		else{
			sets.put(tag, new HashSet<EdgeBucket>());
		}
	}

	public Set<Duo<V, V>> couples() {
		Set<Duo<V,V>> result =
				new HashSet<Duo<V,V>>();
		for (V a : sets.keySet()){
			Set<EdgeBucket> current = sets.get(a);
			if (current != null){
				for (EdgeBucket bucket : current){
					result.add(Duo.d(a,bucket.vertex));
				}
			}
		}
		return result;
	}

	public Set<Trio<V, V, E>> triplets() {
		Set<Trio<V, V, E>> result =
				new HashSet<Trio<V, V, E>>();
		for (V a : sets.keySet()){
			Set<EdgeBucket> current = sets.get(a);
			if (current != null){
				for (EdgeBucket bucket : current){
					result.add(Trio.t(a,bucket.vertex,bucket.tag));
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public void delVertex(V tag) {
		if (!contains(tag)) return;
		// Remove vertex
		sets.remove(tag);

		// Remove edges to this specific vertex
		EdgeBucket fake = new EdgeBucket(tag,null);
		for (V v : sets.keySet()){
			Set<EdgeBucket> current = sets.get(v);
			if (current != null && current.contains(fake)) {
				current.remove(fake);
			}
		}
	}

	public Set<Duo<V,E>> labeledPredecessors(V v) {
		//System.out.println("MuDja label preds for  "+v);
		Set<Duo<V,E>> result = new HashSet<Duo<V,E>>();
		//EdgeBucket bucket = new EdgeBucket(v,null);
		for (V vertex : sets.keySet()){
			//System.out.println("  > looking for "+v+"  in adja of "+ vertex);
			Set<EdgeBucket> current = sets.get(vertex);
			//System.out.println("  > looking for "+v+"  in adja of "+ vertex + "  "+current);
			if (current != null) {
				for (EdgeBucket b : current){
					//System.out.println(b);
					if (b.vertex.equals(v)){
						result.add(Duo.d(vertex,b.tag));
					}
				}
			}
		}
		return result;
	}

	public Set<V> sinks() {
		Set<V> result = new HashSet<V>();
		for (V v : sets.keySet()){
			Set<EdgeBucket> current = sets.get(v);
			if (current == null || current.isEmpty()){result.add(v);}
		}
		return result;
	}

	public Set<V> sources() {
		Set<V> result = new HashSet<V>();
		for (V vertex : sets.keySet()){
			if (this.labeledPredecessors(vertex).isEmpty()){
				result.add(vertex);
			}
		}
		return result;
	}

	public Set<Duo<V,E>> labeledSuccessors(V v) {
		if (this.contains(v)){
			Set<EdgeBucket> buckets = this.sets.get(v);
			if (buckets != null){
				Set<Duo<V,E>> result = new HashSet<Duo<V,E>>(buckets.size());
				for (EdgeBucket bucket : buckets){
					result.add(Duo.d(bucket.vertex,bucket.tag));
				}
				return result;
			}
		}
		return new HashSet<Duo<V,E>>();
	}

	public int bucketNumber(){
		int result = 0;
		for (V key : sets.keySet()){
			if (sets.get(key) != null)
				result += sets.get(key).size();
		}
		return result;
	}

	public Set<V> vertices() {
		return sets.keySet();
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
			if (o instanceof MuDja.EdgeBucket){
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


	public boolean contains(V v) {
		return this.sets.containsKey(v);
	}


	@SuppressWarnings("unchecked")
	public void replaceVertex(V before, V after) {
		if (this.contains(after)) return;
		if (!this.contains(before)) return;

		// change the vertex entry
		Set<EdgeBucket> outgoing = this.sets.get(before);
		this.sets.remove(before);
		this.sets.put(after, outgoing);
		// change in the buckets
		for (V v : this.sets.keySet()){
			Set<EdgeBucket> current = sets.get(v);
			if (current != null){
				for (EdgeBucket bucket : current){
					if (bucket.vertex.equals(before)){
						current.remove(bucket);
						current.add(new EdgeBucket(after,bucket.tag));
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void replaceEdge(V from, V to, E newTag) {
		if (! contains(from)) return;
		EdgeBucket bucket = new EdgeBucket(to,null);
		// Contained in graph
		Set<EdgeBucket> current = sets.get(from);
		if (current != null && current.contains(bucket)){
			// remove the old tag bucket
			current.remove(bucket);
			// adds the new one (trick on equality)
			current.add(bucket);
		}
	}

	@SuppressWarnings("unchecked")
	public E getEdge(V from, V to) {
		for (EdgeBucket bucket : this.sets.get(from)){
			if (bucket.vertex.equals(to)){
				return bucket.tag;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void delEdge(V from, V to) {
		if (! contains(from)) return;
		EdgeBucket bucket = new EdgeBucket(to,null);
		// Contained in graph
		Set<EdgeBucket> current = sets.get(from);
		if (current != null && current.contains(bucket)){
			current.remove(bucket);
		}
	}


	public void empty() {
		sets.clear();
	}


}
