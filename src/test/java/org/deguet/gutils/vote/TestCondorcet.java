package org.deguet.gutils.vote;

import java.util.ArrayList;
import java.util.List;

import org.deguet.gutils.graph.DGraph;
import org.deguet.gutils.graph.DGraphs;
import org.deguet.gutils.random.CopiableRandom;

import org.deguet.gutils.vote.preferential.Condorcet;
import org.deguet.gutils.vote.preferential.PreferentialVote;
import org.junit.Test;

public class TestCondorcet {

	@Test(timeout = 10000)
	public void testCondorcetToString(){
		List<PreferentialVote> list = new ArrayList<>();
		PreferentialVote vote = new PreferentialVote();
		vote.addAtRank(1, "Paul").addAtRank(5,"Peter").addAtRank(5,"Billy");
		for (int i = 0 ; i < 100 ; i++){
			list.add(vote);
		}
		DGraph<String, Long> pairwise = Condorcet.computePairwise(list);
		System.out.println(DGraphs.toDot(pairwise, "bla"));
		String tested = Condorcet.stringMatrix(pairwise, 15);
		System.out.println("Results \n" + tested);
	}
	
	@Test(timeout = 10000)
	public void testCondorcetToString2(){
		List<PreferentialVote> list = new ArrayList<>();
		CopiableRandom r = new CopiableRandom(9875);
		String[] candidates = {"A","B","C","D","E","F","G","H","I","J"};
		for (int i = 0 ; i < 100 ; i++){
			PreferentialVote vote = PreferentialVote.atRandom(r.asRandom(),candidates);
			list.add(vote);
		}
		DGraph<String, Long> pairwise = Condorcet.computePairwise(list);
		System.out.println(DGraphs.toDot(pairwise, "bla"));
		String tested = Condorcet.stringMatrix(pairwise, 7);
		System.out.println("Results \n" + tested);
	}
	
}
