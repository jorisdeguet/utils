package org.deguet.gutils.vote;

import org.deguet.gutils.vote.preferential.InstantRunoffOnBallotBox;
import org.deguet.gutils.vote.preferential.PreferentialBallot;
import org.deguet.gutils.vote.preferential.PreferentialVote;
import org.junit.Test;

import org.deguet.gutils.random.CopiableRandom;

public class TestInstantRunoff {

	@Test//(timeout = 10000)
	public void testCondorcetToString(){
		PreferentialBallot bb = new PreferentialBallot();
		CopiableRandom r = new CopiableRandom(9875);
		for (int i = 0 ; i < 10 ; i++){
			PreferentialVote vote = //PreferentialVote.atRandom(r.asRandom(),candidates);
					new PreferentialVote().addAtRank(1, "Paul").addAtRank(5,"Peter").addAtRank(5,"Billy");
			bb.add(vote);
		}
		InstantRunoffOnBallotBox instant = new InstantRunoffOnBallotBox(bb);
		System.out.println(instant.hasMajority());
		System.out.println(instant.results().toCondense());
	}
	
}
