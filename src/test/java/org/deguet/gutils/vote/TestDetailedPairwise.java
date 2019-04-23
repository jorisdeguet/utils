package org.deguet.gutils.vote;

import org.deguet.gutils.graph.DGraph;
import org.deguet.gutils.nuplets.Trio;
import org.deguet.gutils.vote.preferential.Condorcet;
import org.deguet.gutils.vote.preferential.PreferentialBallot;
import org.deguet.gutils.vote.preferential.PreferentialVote;
import org.deguet.gutils.vote.preferential.TidemanOnBallotBox;
import org.junit.Assert;
import org.junit.Test;

public class TestDetailedPairwise {

    @Test(timeout = 10000)
    public void testNeverFirstButWinsWithBallotBox(){
        PreferentialBallot bb = new PreferentialBallot();
        bb.add(PreferentialVote.fromCondense("A>B=C"));
        bb.add(PreferentialVote.fromCondense("C>B>D"));
        bb.add(PreferentialVote.fromCondense("D=B>A"));
        bb.add(PreferentialVote.fromCondense("E>B>D"));
        String mat = Condorcet.stringMatrix(bb.computeDetailedPairwise(),15) ;
        System.out.println(mat);
    }

    @Test
    public void test1(){
        PreferentialBallot bb = new PreferentialBallot();
        bb.add(PreferentialVote.fromCondense("La paix dans le monde>La croissance économique=Le réchauffement climatique=la pauvreté"), 15L);
        bb.add(PreferentialVote.fromCondense("Le réchauffement climatique=la pauvreté>La croissance économique=La paix dans le monde"), 1L);
        bb.add(PreferentialVote.fromCondense("la pauvreté>La croissance économique=La paix dans le monde=Le réchauffement climatique"), 2L);
        String mat = Condorcet.stringMatrix(bb.computeDetailedPairwise(),15) ;
        System.out.println(mat);
    }

    @Test(timeout = 10000)
    public void testEquivalence(){
        PreferentialBallot   b1 = new PreferentialBallot  ();
        PreferentialBallot   b2 = new PreferentialBallot  ();

        b1.add(PreferentialVote.fromCondense("A>B>C=D=E"),7L);
        b1.add(PreferentialVote.fromCondense("C>B>D=A=E"),13L);
        b2.add(PreferentialVote.fromCondense("D>B>A=C=E"),44L);
        b2.add(PreferentialVote.fromCondense("E>B>D=A=C"),3L);

        PreferentialBallot   b3 = new PreferentialBallot  ();
        b3.add(PreferentialVote.fromCondense("A>B>C=D=E"),7L);
        b3.add(PreferentialVote.fromCondense("C>B>D=A=E"),13L);
        b3.add(PreferentialVote.fromCondense("D>B>A=C=E"),44L);
        b3.add(PreferentialVote.fromCondense("E>B>D=A=C"),3L);
        DGraph<String, Trio<Long,Long,Long>> g3 = b3.computeDetailedPairwise();
        String str3 = Condorcet.stringMatrix(g3, 15);
        System.out.println(str3);

        DGraph<String, Trio<Long,Long,Long>> g1 = b1.computeDetailedPairwise();
        String str1 = Condorcet.stringMatrix(g1, 15);
        System.out.println(str1);
        DGraph<String, Trio<Long,Long,Long>> g2 = b2.computeDetailedPairwise();
        String str2 = Condorcet.stringMatrix(g2, 15);
        System.out.println(str2);
        DGraph<String, Trio<Long,Long,Long>> g12 = PreferentialBallot.merge(g1,g2);
        String str12 = Condorcet.stringMatrix(g12, 15);
        System.out.println(str12);

        Assert.assertEquals(g12.triplets(), g3.triplets());


        PreferentialVote r3 = TidemanOnBallotBox.resultsFromBallot(b3);
        PreferentialVote r12 = TidemanOnBallotBox.resultsFromPairWise(PreferentialBallot.pairwiseFromDetailed(g12));
        Assert.assertEquals(r3, r12);
        System.out.println("Results " + r3);
    }

}
