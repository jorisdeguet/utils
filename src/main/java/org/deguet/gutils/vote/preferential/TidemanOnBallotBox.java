package org.deguet.gutils.vote.preferential;

import org.deguet.gutils.graph.DGraph;
import org.deguet.gutils.graph.DGraphMatrix;
import org.deguet.gutils.graph.DGraphs;
import org.deguet.gutils.nuplets.Trio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TidemanOnBallotBox {
	
	private static Comparator<Trio<String,String,Long>> comparator = new Comparator<Trio<String,String,Long>>(){
		public int compare(Trio<String,String, Long> o1, Trio<String,String, Long> o2) {
			return o2.get3().compareTo(o1.get3());
		}
	};
	
	public static PreferentialVote resultsFromBallot(PreferentialBallot bbox){
		DGraph<String,Long> pairwise = bbox.computePairwise();
		return resultsFromPairWise(pairwise);
	}

	public static PreferentialVote resultsFromPairWise(DGraph<String,Long> pairwise){
		// get the pairs sorted
		List<Trio<String,String,Long>> sorted = new ArrayList<>();
		sorted.addAll( pairwise.triplets());
		Collections.sort(sorted,comparator);
		//for (Trio<T,T,Long> elt : sorted)System.out.println(elt);
		DGraph<String,Long> acyclic = new DGraphMatrix<>();
		for (String candidate : pairwise.vertices()){
			acyclic = acyclic.addVertex(candidate);
		}
		for (Trio<String,String,Long> elt : sorted){
			DGraph<String,Long> candidate = acyclic.addEdge(elt.get1(), elt.get2(), elt.get3());
			if (!DGraphs.isCyclic(candidate)){
				acyclic = candidate;
			}
		}
		//System.out.println(DGraphs.toDot(acyclic, "a") );
		return PreferentialVote.fromListOfSet(DGraphs.topoSort(acyclic));
	}
}
