package org.deguet.gutils.vote.preferential;

import org.deguet.gutils.graph.DGraph;
import org.deguet.gutils.graph.DGraphMatrix;
import org.deguet.gutils.nuplets.Duo;
import org.deguet.gutils.nuplets.Trio;

import java.util.*;

/**
 * Compiles ballot without keeping all of them.
 * Count the same ballot to have a compact representation.
 * @author joris
 *
 */
public class PreferentialBallot  {
	
	private final Map<PreferentialVote,Long> votes = new HashMap<>();
	
	// cached graph
	private DGraph<String,Long> pairwise;
	
	public void add(PreferentialVote vote, Long qty){
		if (votes.containsKey(vote)){
			votes.put(vote, votes.get(vote)+qty);
		}else{
			votes.put(vote, qty);
		}
		pairwise = null;			// invalidate the cache
	}
	
	public void add(PreferentialVote vote){
		add(vote,1L);
	}
	
	public PreferentialBallot  without(String... candidates){
		PreferentialBallot  result = new PreferentialBallot ();
		for (PreferentialVote  v : votes.keySet()){
			long number = votes.get(v);
			PreferentialVote  without = v.without(candidates);
			//System.out.println(without.toCondense() +"   " + v);
			result.add(without, number);
		}
		return result;
	}
	
	/**
	 * compute results as if only the first vote was taken into account.
	 * if the rankedvote contains a tie, we split into 1/n where n the number of tied candidates
	 * @return
	 */
	public Map<String,Double> nonPreferentialResults(){
		double total = 0.0;
		Map<String, Double> result = new HashMap<String,Double>();
		for (String t : candidates()){
			result.put(t, 0.0);
		}
		for (PreferentialVote  vote : votes.keySet()){
			Set<String>  firsts = vote.asListOfSet().get(0);
			for (String first : firsts){
				double part = 1.0*votes.get(vote)/firsts.size();
				result.put(first, result.get(first) + part);
				total += part;
			}
		}
		//result.put(null, total);
		System.out.println("Total " + total);
		return result;
	}
	
	/**
	 * returns the set of undisputedWinners
	 * @return
	 */
	public Set<String>  undisputedWinners() {
		return this.computePairwise().sources();
	}
	
	
	public Set<String>  candidates(){
		Set<String>  result = new HashSet<>();
		for (PreferentialVote  vote : votes.keySet()){
			result.addAll(vote.candidates());
		}
		return result;
	}
	
	/**
	 * Computes the pairwise graph for these ballots
	 * @return
	 */
	public DGraph<String,Long> computePairwise(){
		if (pairwise != null) return pairwise;
		DGraph<String,Long> res = new DGraphMatrix<>();
		Set<String>  cands = this.candidates();
		for (String a : cands){
			res = res.addVertex(a);
			for (String b : cands){
				if (!a.equals(b)){
					long score = 0;
					// go through all votes for this pair of candidates
					for (PreferentialVote  vote: votes.keySet()){
						int s = vote.score(a, b);
						//System.out.println(vote + "  gives  "+s );
						if (s > 0) score+=s*votes.get(vote); 
					}
					res = res.addEdge(a, b, score);
					//System.out.println("============================= Score for "+cands[i]+" against "+cands[j]+"  = " +score);
				}
			}
		}
		pairwise = deleteSmallerEdge(res);
		return pairwise;
	}

	public static DGraph<String, Trio<Long, Long, Long>> merge(DGraph<String, Trio<Long, Long, Long>> a, DGraph<String, Trio<Long, Long, Long>> b){
		Set<String> vertices = a.vertices();
		vertices.addAll(b.vertices());
		DGraph<String, Trio<Long, Long, Long>> result = new DGraphMatrix<>();
		for (String x: vertices) {
			for (String y : vertices) {
				if (!x.equals(y)){
					Trio<Long, Long, Long> aa = a.getEdge(x, y);
					Trio<Long, Long, Long> bb = b.getEdge(x, y);
					System.out.println(x + " " + y + " " + aa + "   " + bb);
					Trio<Long, Long, Long> newValue = Trio.t(
							aa.get1() + bb.get1(),
							aa.get2() + bb.get2(),
							aa.get3() + bb.get3()
					);
					result = result.addEdge(x, y, newValue);
				}
			}
		}
		return result;
	}

	public static DGraph<String,Long> pairwiseFromDetailed(DGraph<String,Trio<Long,Long,Long>> detailed) {
		DGraph<String,Long> res = new DGraphMatrix<>();
		for (Duo<String, String> edge : detailed.couples()) {
			Trio<Long, Long, Long> d = detailed.getEdge(edge.get1(),edge.get2());
			res = res.addEdge(edge.get1(), edge.get2(), d.get1() - d.get3());
			//System.out.println("============================= Score for "+ed]+" against "+cands[j]+"  = " +score);
		}
		res = deleteSmallerEdge(res);
		return res;
	}

	public DGraph<String,Trio<Long,Long,Long>> computeDetailedPairwise(){
		DGraph<String,Trio<Long,Long,Long>> res = new DGraphMatrix<>();
		Set<String>  cands = this.candidates();
		for (String a : cands){
			res = res.addVertex(a);
			for (String b : cands){
				if (!a.equals(b)){
					Long first = 0L;
					Long second = 0L;
					Long ties = 0L;
					//Long total = 0L;
					// go through all votes for this pair of candidates
					for (PreferentialVote  vote: votes.keySet()){
						int s = vote.score(a, b);
						long count = votes.get(vote);
						//System.out.println(vote + "  gives  "+s );
						//total +=count;
						if (s > 0) { first+=count; }
						else if (s < 0) { second+=count; }
						else if (s == 0) { ties+=count; }
					}
					//System.out.println(a + " " + b+"    >>    "+total+ " = " + first + " + " + second + " + " + ties );
					Trio<Long,Long,Long> tag = Trio.t(first, ties, second);
					res = res.addEdge(a, b, tag);
				}
			}
		}
		return res;
	}
	
	public static DGraph<String, Long> deleteSmallerEdge(DGraph<String, Long> g) {
		String[] vs = g.vertices().toArray(new String[g.vertices().size()]);
		for (int i = 0 ; i < vs.length ; i++){
			for (int j = i+1 ; j < vs.length ; j++){
				String a = vs[i];
				String b = vs[j];
				Long ab = g.getEdge(a, b);
				Long ba = g.getEdge(b, a);
				if (ab != null && ba != null){
					if (ab == ba){
						g = g.delEdge(b, a).delEdge(a, b);
					}
					if (ab>ba){
						g = g.delEdge(b, a);
					}
					else{
						g = g.delEdge(a, b);
					}
				}
			}
		}
		return g;
	}

	public SortedMap<Integer,Long> distributionFor(String candidate){
		SortedMap<Integer, Long> res = new TreeMap<>();
		for (PreferentialVote v : votes.keySet()){
			Integer rank = v.safeRankFor(candidate);
			if (!res.containsKey(rank)) res.put(rank, 0L);
			res.put(rank, votes.get(v)+res.get(rank));
		}
		return res;
	}

	public Long countHowManyTimesBest(String candidate){
		Long result = 0L;
		for (PreferentialVote v : votes.keySet()){
			if(v.isBest(candidate)){
				result += votes.get(v);
			}
		}
		return result;
	}

	public Long countHowManyTimesWorst(String candidate){
		Long result = 0L;
		for (PreferentialVote v : votes.keySet()){
			if(v.isWorst(candidate)){
				result += votes.get(v);
			}
		}
		return result;
	}


	public String toString(){
		return this.stringMatrix(10);
	}
	
	/**
	 * Gives a String representation of the pairwise graph
	 * @param columnWidth
	 * @return
	 */
	public String stringMatrix(int columnWidth) {
		return Condorcet.stringMatrix(computePairwise(), columnWidth);
	}

	public Long totalVotes() {
		Long result = 0L;
		for (PreferentialVote v : votes.keySet()){
			result += votes.get(v);
		}
		return result;
	}
}
