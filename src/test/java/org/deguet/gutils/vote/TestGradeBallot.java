package org.deguet.gutils.vote;

import org.deguet.gutils.vote.grade.GradeBallot;
import org.deguet.gutils.vote.grade.GradeVote;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by joris on 16-08-28.
 */
public class TestGradeBallot {


    @Test
    public void testSimpleRangeBallot(){
        GradeBallot ballot  = new GradeBallot();
        for (int i = 0 ; i < 100 ; i++){
            GradeVote vote = new GradeVote();
            vote.addAGrade(5, "a","b").addAGrade(1, "c").addAGrade(3,"d");
            ballot.add(vote);
        }

        for (int i = 0 ; i < 50 ; i++){
            GradeVote vote = new GradeVote();
            vote.addAGrade(4, "a","b").addAGrade(3, "c");
            ballot.add(vote);
        }
        System.out.println(ballot.results());
    }

    @Test
    public void testGradeVoteFromCondense(){
        GradeVote vote = GradeVote.fromCondense("5=Jo=To>3=a>0=Mo");
        System.out.println(vote);
        GradeVote rec = GradeVote.fromCondense(vote.toCondense());
        Assert.assertEquals(vote, rec);
        vote.addAGrade(4, "b").addAGrade(3, "c");
        System.out.println(vote);

    }



}
