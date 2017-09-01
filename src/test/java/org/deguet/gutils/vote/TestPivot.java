package org.deguet.gutils.vote;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by joris on 17-03-29.
 */
public class TestPivot {


    @Test
    public void testGradeToRank(){
        GradeVote grade = GradeVote.fromCondense("8=Mo=To>5=Joris>2=Alex>0=Evariste");
        System.out.println(grade.toCondense());
        PreferentialVote ranked = Pivot.from(grade);
        System.out.println(ranked.toCondense());
    }


    @Test
    public void testPreferentialBallotToGrade(){
        Random r  = new Random(1234);
        List<String> candidates = Arrays.asList("Jo","Mo","To","Bo");
        GradeBallot ballot = new GradeBallot();
        for (int i = 0 ; i < 5 ; i++){
            GradeVote vote = new GradeVote();
            // grade all candidates
            for (String cand : candidates){
                vote = vote.addAGrade(r.nextInt(11),cand);
            }
            System.out.println("vote >> " +vote);
            ballot.add(vote);
        }
        System.out.println(ballot.results());
        PreferentialBallot pref = Pivot.from(ballot);
        System.out.println(new TidemanOnBallotBox(pref).results());
        System.out.println(pref.toString());
    }
}
