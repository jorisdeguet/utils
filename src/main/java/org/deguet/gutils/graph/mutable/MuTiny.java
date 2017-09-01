package org.deguet.gutils.graph.mutable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.nuplets.Trio;
import org.deguet.gutils.pairing.Pairing;
import org.deguet.gutils.pairing.PairingConcat;

/**
 * Tiny mutable graphs to accelerate things with parser
 * @author joris
 *
 */
public class MuTiny<V,E>  extends AbstractMutableDGraph<V,E>{

	private static final long serialVersionUID = 1L;

	private final ArrayList<V> vertices;

	private final Map<Long,E> edges;

	private final Pairing pair = new PairingConcat();

	public MuTiny(){
		vertices = new ArrayList<V>();
		edges = new HashMap<Long, E>();
	}

	public void addEdge(V a, V b, E tag) {
		addVertex(a);
		addVertex(b);
		int indexA = this.vertexFor(a);
		int indexB = this.vertexFor(b);
		long edgeIndex = pair.compose(Duo.d((long)indexA,(long)indexB));
		if (!edges.containsKey(edgeIndex)){
			edges.put(edgeIndex, tag);
		}
		else{
			return;
		}

	}

	public Set<Duo<V, E>> labeledPredecessors(V v) {
		long index = vertexFor(v);
		Set<Duo<V,E>> result = new HashSet<Duo<V,E>>();
		for (long i = 0 ; i < vertices.size() ; i++){
			long edgeIndex = pair.compose(Duo.d(i,index));
			if (edges.containsKey(edgeIndex))
				result.add(Duo.d(vertices.get((int)i),edges.get(edgeIndex)));
		}
		return result;
	}

	public Set<Duo<V, E>> labeledSuccessors(V v) {
		long index = vertexFor(v);
		Set<Duo<V,E>> result = new HashSet<Duo<V,E>>();
		for (long i = 0 ; i < vertices.size() ; i++){
			long edgeIndex = pair.compose(Duo.d(index,i));
			if (edges.containsKey(edgeIndex)){
				result.add(Duo.d(vertices.get((int)i),edges.get(edgeIndex)));
			}
		}
		return result;
	}

	public void replaceEdge(V from, V to, E newTag) {
		int indexA = this.vertexFor(from);
		int indexB = this.vertexFor(to);
		if (indexA == -1 || indexB == -1){
			return;
		}
		else{
			long edgeIndex = pair.compose(Duo.d((long)indexA,(long)indexB));
			if (edges.containsKey(edgeIndex)){
				edges.put(edgeIndex, newTag);
			}
			else{
				return;
			}
		}
	}

	public Set<Trio<V, V, E>> triplets() {
		Set<Trio<V,V,E>> result = new HashSet<Trio<V,V,E>>();
		for (Map.Entry<Long, E> entry : edges.entrySet()){
			Duo<Long,Long> indices = this.pair.decompose(entry.getKey().longValue());
			result.add(
					Trio.t(
							vertices.get(indices.get1().intValue()) ,
							vertices.get(indices.get2().intValue()) ,
							entry.getValue()));
		}
		return result;
	}

	public void addVertex(V tag) {
		if (!vertices.contains(tag))
			vertices.add(tag);
	}

	public Set<Duo<V, V>> couples() {
		Set<Duo<V,V>> result = new HashSet<Duo<V,V>>();
		for (Map.Entry<Long, E> entry : edges.entrySet()){
			Duo<Long,Long> indices = this.pair.decompose(entry.getKey().longValue());
			result.add(
					Duo.d(
							vertices.get(indices.get1().intValue()), 
							vertices.get(indices.get2().intValue())
							)
					);
		}
		return result;
	}

	public void delEdge(V a, V b) {
		long indexA = this.vertexFor(a);
		long indexB = this.vertexFor(b);
		if (indexA == -1 || indexB == -1){
			return;
		}
		else{
			long edgeIndex = pair.compose(Duo.d(indexA,indexB));
			edges.remove(edgeIndex);
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

	//	public void delVertex(V tag) {
	//		int indexV = this.vertexFor(tag);
	//		if (indexV == -1){
	//			return ;
	//		}
	//		else{
	//			V[] newVert = (V[]) new Object[vertices.length-1];
	//			System.arraycopy(vertices, 0, newVert, 0, indexV);
	//			System.arraycopy(vertices, indexV+1, newVert, indexV, vertices.length-indexV-1);
	//			Map<Long,E> newEdges = new HashMap<Long,E>();
	//			for (Long edgeIndex : edges.keySet()){
	//				Duo<Long,Long> coords = pair.decompose(edgeIndex);
	//				long a = coords.get1();
	//				long b = coords.get2();
	//				if ( a!=indexV && b != indexV){
	//					// this is to keep
	//					a = a>indexV?a-1:a;
	//					b = b>indexV?b-1:b;
	//					long newEdgeIndex = pair.compose(new Duo<Long,Long>(a,b));
	//					newEdges.put(newEdgeIndex, edges.get(edgeIndex));
	//				}
	//			}
	//		}
	//	}

	public void replaceVertex(V before,
			V after) {
		int indexV = this.vertexFor(before);
		if (indexV == -1){
			return;
		}
		else{
			vertices.set(indexV, after);
		}
	}

	public Set<V> sinks() {
		Set<V> result = new HashSet<V>();
		for (V v : vertices){
			if (this.labeledSuccessors(v).isEmpty())
				result.add(v);
		}
		return result;
	}

	public Set<V> sources() {
		Set<V> result = new HashSet<V>();
		for (V v : vertices){
			if (this.labeledPredecessors(v).isEmpty())
				result.add(v);
		}
		return result;
	}

	public Set<V> vertices() {
		Set<V> result = new HashSet<V>();
		result.addAll(vertices);
		return result;
	}

	private int vertexFor(V v){
		return vertices.indexOf(v);
	}

	public void addEdges(Set<Trio<V,V,E>> e){
		for (Trio<V,V,E> triple : e){
			this.addEdge(triple.get1(), triple.get2(), triple.get3());
		}
	}

	//////////////////////////////////// Opt stuff	

	@Override
	public Map<V,Set<Duo<V,E>>> predecessors(){
		Map<V,Set<Duo<V,E>>> result = 
				new HashMap<V,Set<Duo<V,E>>>();
		for (V v: this.vertices){
			result.put(v, new HashSet<Duo<V,E>>());
		}
		for (Entry<Long, E> entry : edges.entrySet()){
			Duo<Long,Long> coords = pair.decompose(entry.getKey());
			Set<Duo<V,E>> set = result.get(vertices.get(coords.get2().intValue()));
			set.add(Duo.d(vertices.get(coords.get1().intValue()),entry.getValue()));
		}
		return result;
	}

	public void empty() {
		vertices.clear();
		edges.clear();
	}

	public boolean contains(V v) {
		return this.vertices().contains(v);
	}

	
	public void delVertex(V v) {
		int index = vertices.indexOf(v);
		if (index < 0 ) return;
		// all indices should be decreased after the node
		HashMap<Long,E> newEdges = new HashMap<Long,E>();
		for (Long composite : edges.keySet()){
			Duo<Long,Long> couple = this.pair.decompose(composite);
			Long aaa = couple.get1();
			Long bbb = couple.get2();
			if (aaa == index || bbb == index){
			}
			else if (aaa > index || bbb > index){
				E tag = edges.get(composite);
				Long aaaa = aaa>index?aaa-1:aaa;
				Long bbbb = bbb>index?bbb-1:bbb;
				long newComposite = pair.compose(Duo.d(aaaa,bbbb));
				newEdges.put(newComposite, tag);
			}
			else{
				newEdges.put(composite, edges.get(composite));
			}
		}
		edges.clear();
		edges.putAll(newEdges);
		v = vertices.remove(index);
	}

}

