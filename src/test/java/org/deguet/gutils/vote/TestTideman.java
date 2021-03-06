package org.deguet.gutils.vote;

import junit.framework.Assert;
import org.deguet.gutils.vote.preferential.PreferentialBallot;
import org.deguet.gutils.vote.preferential.PreferentialVote;
import org.deguet.gutils.vote.preferential.TidemanOnBallotBox;
import org.junit.Test;

public class TestTideman {

	@Test(timeout = 10000)
	public void testNeverFirstButWinsWithBallotBox(){
		PreferentialBallot bb = new PreferentialBallot();
		bb.add(PreferentialVote.fromCondense("A>B>C"));
		bb.add(PreferentialVote.fromCondense("C>B>D"));
		bb.add(PreferentialVote.fromCondense("D>B>A"));
		bb.add(PreferentialVote.fromCondense("E>B>D"));
		System.out.println("Results " + TidemanOnBallotBox.resultsFromBallot(bb));
	}
	
	@Test(timeout = 10000)
	public void testNeverFirstButWinsWithBallotBox2(){
		PreferentialBallot   bb = new PreferentialBallot  ();
		bb.add(PreferentialVote.fromCondense("A>B>C"),7L);
		bb.add(PreferentialVote.fromCondense("C>B>D"),13L);
		bb.add(PreferentialVote.fromCondense("D>B>A"),44L);
		bb.add(PreferentialVote.fromCondense("E>B>D"),3L);
		System.out.println("Results " + TidemanOnBallotBox.resultsFromBallot(bb));
	}
	
	@Test(timeout = 10000)
	public void testTidemanConsensus2(){
		PreferentialBallot bb = new PreferentialBallot  ();
		PreferentialVote   vote = new PreferentialVote().addAtRank(1, "Paul").addAtRank(4,"Peter").addAtRank(5,"Billy");
		for (int i = 0 ; i < 10 ; i++){
			bb.add(vote);
		}
		System.out.println("Results " + TidemanOnBallotBox.resultsFromBallot(bb));
		Assert.assertEquals("Result is the same as the unique vote", vote.toCondense(), TidemanOnBallotBox.resultsFromBallot(bb).toCondense());
		
	}
	
}
