package org.deguet.gutils.vote;

import java.util.*;

/**
 * Created by joris on 16-08-28.
 * A graded vote is a vote where the voter asssociates a grade between 0 and 10 to every
 * candidate.
 *
 * MUTABLE
 */
public class GradeVote implements Iterable<String> {



    private Map<String, Integer> grades = new HashMap<String,Integer>();

    private Comparator<String> comparator = new Comparator<String>() {
        public int compare(String o1, String o2) {
            if (grades.get(o2).compareTo(grades.get(o1)) != 0 ) return grades.get(o2).compareTo(grades.get(o1));
            return o1.compareTo(o2);
        }
    };

    public GradeVote addAGrade(int grade, String... candidates){
        if (grade < 0 || grade > 10 ) throw new IllegalArgumentException("grades go from 0 to 10");
        for (String c : candidates){
            if (grades.containsKey(c) && !grades.get(c).equals(grade))
                throw new IllegalArgumentException("try to add the same candidate twice with different grades "+c);
            grades.put(c,grade);
        }
        return this;
    }

    public Set<String> candidates() {
        return grades.keySet();
    }

    public Integer gradeFor(String candidate) {
        return grades.get(candidate);
    }

    public static GradeVote fromCondense(String vote){
        String[] rankeds = vote.split(">");
        GradeVote result  = new GradeVote();
        for (String ranked : rankeds){
            String[] members = ranked.split("=");
            //System.out.println("   members " + Arrays.toString(members));
            Integer grade = Integer.parseInt(members[0]);
            for (int i = 1 ; i< members.length ; i++){
                result.addAGrade(grade,members[i]);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GradeVote gradeVote = (GradeVote) o;

        return grades != null ? grades.equals(gradeVote.grades) : gradeVote.grades == null;
    }

    @Override
    public int hashCode() {
        return grades != null ? grades.hashCode() : 0;
    }

    public String toCondense(){
        TreeMap<String,Integer> sorted = new TreeMap<>(comparator);
        sorted.putAll(this.grades);
        //Collections.sort(candidates, comparator);
        StringBuilder sb = new StringBuilder();
        int grade  = 999;
        for (String cand : sorted.keySet()){
            if (sorted.get(cand) != grade){
                if (grade == 999) sb.append(sorted.get(cand) + "="+cand);
                else{sb.append(">"+sorted.get(cand) + "="+cand);}
                grade = sorted.get(cand);
            }
            else{
                sb.append("="+cand);
            }
        }
        return sb.toString();
    }


    public static GradeVote atRandom(Random r, String... candidateSet){
        int size = candidateSet.length;
        GradeVote result = new GradeVote();
        for (int i = 0 ; i < size ; i++){
            result.addAGrade(r.nextInt(11), candidateSet[i]);
        }
        return result;
    }

    public SortedMap<Integer,Set<String>> ranking(){
        SortedMap<Integer,Set<String>> result = new TreeMap<>(Collections.<Integer>reverseOrder());
        for (String c : this.grades.keySet()){
            int grade = grades.get(c);
            if (result.containsKey(grade)){
                result.get(grade).add(c);
            }
            else{
                Set<String> set = new HashSet<>();
                set.add(c);
                result.put(grade,set);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return this.toCondense();
    }

    @Override
    public Iterator<String> iterator() {
        TreeMap<String,Integer> sorted = new TreeMap<>(comparator);
        sorted.putAll(this.grades);
        return sorted.keySet().iterator();
    }
}
