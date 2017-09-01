package org.deguet.gutils.vote;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.deguet.gutils.graph.DGraph;
import org.deguet.gutils.graph.DGraphMatrix;

/**
 * Compiles ballot without keeping all of them.
 * Count the same ballot to have a compact representation.
 * @author joris
 *
 */
public class PreferentialBallot  {
	
	private final Map<PreferentialVote,Integer> votes = new HashMap<PreferentialVote,Integer>();
	
	// cached graph
	private DGraph<String,Long> pairwise;
	
	public void add(PreferentialVote vote, Integer qty){
		if (votes.containsKey(vote)){
			votes.put(vote, votes.get(vote)+qty);
		}else{
			votes.put(vote, qty);
		}
		pairwise = null;			// invalidate the cache
	}
	
	public void add(PreferentialVote vote){
		add(vote,1);
	}
	
	public PreferentialBallot  without(String... candidates){
		PreferentialBallot  result = new PreferentialBallot ();
		for (PreferentialVote  v : votes.keySet()){
			int number = votes.get(v);
			PreferentialVote  without = v.without(candidates);
			//System.out.println(without.toCondense() +"   " + v);
			result.add(without, number);
		}
		return result;
	}
	
	/**
	 * compute results as if only the first vote was taken into account.
	 * if the rankedvote contains a tie, we split into 1/n where n the nimber of tied candidates
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
		result.put(null, total);
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
			for (String b : cands){
				if (a != b){
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
	
	private static    DGraph<String, Long> deleteSmallerEdge(DGraph<String, Long> g) {
		String[] vs = (String[]) g.vertices().toArray(new String[g.vertices().size()]);
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
	
}
