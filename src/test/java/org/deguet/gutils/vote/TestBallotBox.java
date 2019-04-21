package org.deguet.gutils.vote;

import org.deguet.gutils.graph.DGraph;
import org.deguet.gutils.graph.DGraphs;
import org.deguet.gutils.random.CopiableRandom;
import org.deguet.gutils.vote.preferential.Condorcet;
import org.deguet.gutils.vote.preferential.PreferentialBallot;
import org.deguet.gutils.vote.preferential.PreferentialVote;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestBallotBox {

	@Test(timeout = 10000)
	public void testBallotBoxToString(){
		PreferentialBallot bb = new PreferentialBallot();
		PreferentialVote vote = new PreferentialVote().addAtRank(1, "Paul").addAtRank(5,"Peter").addAtRank(5,"Billy");
		for (int i = 0 ; i < 100 ; i++){
			bb.add(vote);
		}
		DGraph<String, Long> pairwise = bb.computePairwise();
		System.out.println(DGraphs.toDot(pairwise, "bla"));
		String tested = bb.stringMatrix(15);
		System.out.println("Results \n" + tested);
	}
	
	
	@Test(timeout = 10000)
	public void testCondorcetVersusBallotBox(){
		PreferentialBallot bb = new PreferentialBallot();
		List<PreferentialVote> list = new ArrayList<>();
		
		// add the same votes to the Condorcet List and PreferentialBallot
		CopiableRandom r = new CopiableRandom(9875);
		String[] candidates = {"A","B","C","D","E","F","G","H","I","J"};
		for (int i = 0 ; i < 100 ; i++){
			PreferentialVote vote = PreferentialVote.atRandom(r.asRandom(),candidates);
			list.add(vote);
			bb.add(vote);
		}
		DGraph<String, Long> pairwisect = Condorcet.computePairwise(list);
		String condorcet = Condorcet.stringMatrix(pairwisect, 7);
		String ballotbox = bb.stringMatrix(7);
		System.out.println(ballotbox);
		System.out.println(condorcet);
		Assert.assertEquals(condorcet, ballotbox);
	}
	
	@Test	//(timeout = 10000)
	public void testCondorcetAndNonPreferentialResultSimple(){
		PreferentialBallot bb = new PreferentialBallot();
		// add the same votes to the Condorcet List and PreferentialBallot
		CopiableRandom r = new CopiableRandom(9875);
		for (int i = 0 ; i < 10 ; i++){
			PreferentialVote vote = //PreferentialVote.atRandom(r.asRandom(),candidates);
					new PreferentialVote().addAtRank(1, "Paul").addAtRank(5,"Peter").addAtRank(5,"Billy");
			bb.add(vote);
		}
		System.out.println("Non Pref " + bb.nonPreferentialResults());
		System.out.println("undisputed " + bb.undisputedWinners());
		Assert.assertEquals(10.0, bb.nonPreferentialResults().get("Paul"),0.0);
		Assert.assertEquals(0.0, bb.nonPreferentialResults().get("Peter"),0.0);
		Assert.assertTrue(bb.undisputedWinners().contains("Paul"));
	}

	@Test	//(timeout = 10000)
	public void testCondorcetAndNonPreferentialResultTie(){
		PreferentialBallot bb = new PreferentialBallot();
		// add the same votes to the Condorcet List and PreferentialBallot
		CopiableRandom r = new CopiableRandom(9875);
		for (int i = 0 ; i < 5 ; i++){
			PreferentialVote vote = //PreferentialVote.atRandom(r.asRandom(),candidates);
					new PreferentialVote().addAtRank(1, "Paul").addAtRank(1,"Peter").addAtRank(5,"Billy");
			bb.add(vote);
		}
		System.out.println("Non Pref " + bb.nonPreferentialResults());
		System.out.println("undisputed " + bb.undisputedWinners());
		Assert.assertEquals(2.5, bb.nonPreferentialResults().get("Paul"),0.0);
		Assert.assertEquals(2.5, bb.nonPreferentialResults().get("Peter"),0.0);
		Assert.assertTrue(bb.undisputedWinners().contains("Paul"));
	}
}
