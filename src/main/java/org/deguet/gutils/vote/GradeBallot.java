package org.deguet.gutils.vote;

import java.util.*;

// inspired by http://rangevoting.org
public final strictfp class GradeBallot {

    public List<GradeVote> getBallots() {
        List<GradeVote> result = new ArrayList<GradeVote>();
        for (GradeVote v : ballots.keySet()){
            for (int i = 0 ; i < ballots.get(v) ; i++){
                result.add(v);
            }
        }
        return result;
    }

    private final Map<GradeVote,Long> ballots;

    public GradeBallot(){
        this.ballots = new HashMap<>();
    }

    public Map<String,Long> results(){
        Map<String,Long> total = new TreeMap<String,Long>();
        Map<String,Long> count = new TreeMap<String,Long>();
        for (GradeVote vote : ballots.keySet()){
            long qty = ballots.get(vote);
            for (String candidate : vote.candidates()){
                if (!total.containsKey(candidate)){
                    total.put(candidate,0L);
                    count.put(candidate,0L);
                }
                total.put(candidate, total.get(candidate)+vote.gradeFor(candidate)*qty);
            }
        }
        return total;
    }


    public void add(GradeVote vote) {
        if (ballots.containsKey(vote)){
            ballots.put(vote , ballots.get(vote) + 1);
        }
        else{
            ballots.put(vote , 1L);
        }
    }

    public long totalVotes(){
        long res = 0 ;
        for (GradeVote vote: this.ballots.keySet()){
            res += ballots.get(vote);
        }
        return res;
    }

    public void add(GradeVote vote, long n) {
        if (ballots.containsKey(vote)){
            ballots.put(vote , ballots.get(vote) + n);
        }
        else{
            ballots.put(vote , n);
        }
    }
}