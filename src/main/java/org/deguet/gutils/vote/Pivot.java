package org.deguet.gutils.vote;

import java.util.Map;
import java.util.Set;

/**
 * Created by joris on 17-03-29.
 */
public class Pivot {

    public static GradeVote from(PreferentialVote ranked){
        throw new UnsupportedOperationException();
    }


    public static PreferentialVote from(GradeVote grade){
        Map<Integer, Set<String>> ranking = grade.ranking();
        int position = 1;
        PreferentialVote res = new PreferentialVote();
        for (Integer grad : ranking.keySet()){
            for (String c : ranking.get(grad)){
                res = res.addAtRank(position,c);
            }
            position++;
        }
        return res;
    }

    public static PreferentialBallot from(GradeBallot gradeB){
        PreferentialBallot result = new PreferentialBallot();
        for (GradeVote vote : gradeB.getBallots()){
            PreferentialVote pref = Pivot.from(vote);
            result.add(pref);
            System.out.println("                                 " + vote + "        " + pref);
            //System.out.println("                                 " + pref);
        }
        return result;
    }

}
