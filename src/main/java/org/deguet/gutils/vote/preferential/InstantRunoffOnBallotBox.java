package org.deguet.gutils.vote.preferential;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InstantRunoffOnBallotBox {

	private PreferentialBallot bbox;
	
	public InstantRunoffOnBallotBox(PreferentialBallot bb){
		this.bbox = bb;
	}
	
	public boolean hasMajority(){
		Map<String, Double> simples = bbox.nonPreferentialResults();
		double total = bbox.totalVotes();
		System.out.println("Majority " + total);
		for (String t : simples.keySet()){
			if (simples.get(t) > total / 2) return true;
		}
		return false;
	}
	
	// compute the worst candidates based on first choices to eliminate them
	public Set<String> worst(PreferentialBallot bb){
		Set<String> result = new HashSet<>();
		Double worst = Double.POSITIVE_INFINITY;
		Map<String, Double> simples = bb.nonPreferentialResults();
		for (String t : simples.keySet()){
			if (t == null) continue;
			if (simples.get(t) < worst) {
				result.clear();
				result.add(t);
				worst = simples.get(t);
			} else if(simples.get(t).equals(worst)){
				result.add(t);
			}
		}
		return result;
	}
	
	public PreferentialVote results(){
		// we systematically remove the 
		int rank = bbox.candidates().size();
		PreferentialBallot iterate = bbox;
		PreferentialVote result  = new PreferentialVote();
		while(iterate.candidates().size() > 0){
			// find the last candidates
			Set<String> worsts = this.worst(iterate);
			// add the at a bad rank
			for (String t: worsts){
				result = result.addAtRank(iterate.candidates().size()+1, t);
			}
			
			iterate = iterate.without((String[]) worsts.toArray(new String[worsts.size()]));
			//System.out.println("==============================");
			//System.out.println(iterate.stringMatrix(10));
		}
		
		return result;
	}
	
}
