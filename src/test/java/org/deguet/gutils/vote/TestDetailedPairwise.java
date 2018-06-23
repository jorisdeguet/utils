package org.deguet.gutils.vote;

import org.deguet.gutils.vote.preferential.PreferentialBallot;
import org.deguet.gutils.vote.preferential.PreferentialVote;
import org.junit.Test;

public class TestDetailedPairwise {

    @Test(timeout = 10000)
    public void testNeverFirstButWinsWithBallotBox(){
        PreferentialBallot bb = new PreferentialBallot();
        bb.add(PreferentialVote.fromCondense("A>B=C"));
        bb.add(PreferentialVote.fromCondense("C>B>D"));
        bb.add(PreferentialVote.fromCondense("D=B>A"));
        bb.add(PreferentialVote.fromCondense("E>B>D"));
        bb.computeDetailedPairwise();
        //System.out.println("Results " + tideman.results());
    }

    @Test
    public void test1(){
        PreferentialBallot bb = new PreferentialBallot();
        bb.add(PreferentialVote.fromCondense("La paix dans le monde>La croissance économique=Le réchauffement climatique=la pauvreté"), 15L);
        bb.add(PreferentialVote.fromCondense("Le réchauffement climatique=la pauvreté>La croissance économique=La paix dans le monde"), 1L);
        bb.add(PreferentialVote.fromCondense("la pauvreté>La croissance économique=La paix dans le monde=Le réchauffement climatique"), 2L);
        bb.computeDetailedPairwise();
    }

}
