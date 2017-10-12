package org.deguet.gutils.vote.preferential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.deguet.gutils.permutation.PrimeBase;

/**
 * Represents a preferential vote establish with ranks.
 * Two candidates can have the same rank.
 * The same candidate cannot exist twice.
 * @author joris
 */
public final class PreferentialVote {

	private final Map<String, Integer> ranks;

	transient Comparator<String> comparator = new Comparator<String>(){
		public int compare(String o1, String o2) {
			int comp = ranks.get(o1).compareTo(ranks.get(o2));
			if (comp != 0) return comp;
			return o1.compareTo(o2);
		}};

		public PreferentialVote(){
			ranks = new HashMap<String, Integer>();
		}

		public PreferentialVote addAtRank(int rank, String... candidates){
			if (rank < 1 ) throw new IllegalArgumentException("ranks starts from 1 to whatever you want");
			for (String c : candidates){
				if (ranks.containsKey(c) && !ranks.get(c).equals(rank))
					throw new IllegalArgumentException("try to add the same candidate twice with different ranks "+c);
				ranks.put(c,rank);
			}
			return this;
		}

		public Integer rankFor(String c){
			return this.ranks.get(c);
		}

		public Set<String> candidates() {
			return this.ranks.keySet();
		}

		public int score(String a, String b){
			if (this.isPreferred(a, b)) return 1;
			if (this.isPreferred(b, a)) return -1;
			return 0;
		}

		public boolean isPreferred(String a, String b){
			if (rankFor(a) == null && rankFor(b) == null) return false;
			if (rankFor(b) == null) return true;
			if (rankFor(a) == null) return false;
			return rankFor(a) < rankFor(b);
		}

		public List<Set<String>> asListOfSet(){
			List<Set<String>> result = new ArrayList<>();
			List<String> candidates = new ArrayList<>(this.ranks.keySet());
			Collections.sort(candidates, comparator);
			if (candidates.size() == 0) return result;
            long rank = rankFor(candidates.get(0));
			Set<String> set = new HashSet<String>();
			result.add(set);
			for (String c : candidates){
				if (rankFor(c) == rank){
					set.add(c);
				}
				else{
					set = new HashSet<>();
					result.add(set);
					set.add(c);
					rank = rankFor(c);
				}
			}
			return result;
		}
		
		public static PreferentialVote fromListOfSet(List<Set<String>> list){
			PreferentialVote result = new PreferentialVote();
			int count = 1;
			for (Set<String> set : list){
				for (String elt : set){
					result = result.addAtRank(count, elt);
				}
				count++;
			}
			return result;
		}

		/**
		 * Encodes one vote on the format described at http://www.cs.wustl.edu/~legrand/rbvote/
		 * @param
		 * @return
		 */
		public String toCondense(){
			List<String> candidates = new ArrayList<String>(this.ranks.keySet());
			Collections.sort(candidates, comparator);
			StringBuilder sb = new StringBuilder();
			for (int i = 0 ; i < candidates.size() ; i++){
				String a = candidates.get(i);
				if (a.toString().contains(">") || (a.toString().contains("=")))
					throw new IllegalArgumentException("Ambiguous string contains either = or > "+a);
				String b = i < candidates.size()-1?candidates.get(i+1):null;
				sb.append(a);
				if (b != null){
					int aa = rankFor(a);
					int bb = rankFor(b);
					//System.out.println(" condense " + a+ " @ " +aa + "    and  " +b + "  @" +bb);
					//System.out.println(aa<bb);
					if (aa < bb) {sb.append(">");}
					else {sb.append("=");}
				}
			}
			return sb.toString();
		}
		
		/**
		 * parse votes following the format from http://www.cs.wustl.edu/~legrand/rbvote/
		 * Votes can be separated by either a new line or a |
		 * @param votes
		 * @return
		 */
		public static List<PreferentialVote> fromCondenseList(String votes){
			String[] lines = votes.split("\n");
			List<PreferentialVote> result = new ArrayList<PreferentialVote>();
			for (String line : lines){
				int n = 1;
				if (line.contains("|")){
					String[] split = line.split("\\|");
					n = Integer.parseInt(split[0]);
					line = split[1];
				}
				PreferentialVote vote = fromCondense(line);
				for (int i = 0 ; i < n ; i++){
					result.add(vote);
				}
			}
			return result;
		}
		
		/**
		 * parse a single vote following the format from http://www.cs.wustl.edu/~legrand/rbvote/
		 * a>b=d>c meaning that we prefer a over b and d equally, c is the least favored
		 * @param vote
		 * @return
		 */
		public static PreferentialVote fromCondense(String vote){
			String[] rankeds = vote.split(">");
			//System.out.println("rankeds " + Arrays.toString(rankeds));
			PreferentialVote result  = new PreferentialVote();
			int rank = 1;
			for (String ranked : rankeds){
				String[] members = ranked.split("=");
				//System.out.println("   members " + Arrays.toString(members));
				for (String member : members){
					result.addAtRank(rank, member);
				}
				rank++;
			}
			return result;
		}

		/**
		 * Encode a preferential vote into one single long
		 * CANNOT REALLY USE WHEN NUMBER OF CANDIDATES GOES BEYOND 5
		 * could be used for 5 people of love like neutral 
		 * @param candidateSet an array to determine indexes of candidates
		 * @return
		 */
		public Long encode(String... candidateSet){
			PrimeBase b = new PrimeBase();
			int[] toEncode = new int[candidateSet.length+1];
			for (int i  = 0 ; i < candidateSet.length ; i++){
				Integer nullable = this.rankFor(candidateSet[i]);
				int real = nullable==null?0:nullable;
				//System.out.println("Encode candidate " + i + " avec exposant " +real);
				// got to shift by 1 cause exponents on 1 are useless
				toEncode[i+1] = real;
			}
			//System.out.println(Arrays.toString(toEncode));
			return b.from(toEncode);
		}

		
		/**
		 * decode a prefered vote when encoded with PrimeBase
		 * @param coded
		 * @param candidateSet
		 * @return
		 */
		public static  PreferentialVote decode(Long coded, String... candidateSet){
			PrimeBase b = new PrimeBase();
			int[] longs = b.to(coded);
			//System.out.println(Arrays.toString(longs));
			PreferentialVote result = new PreferentialVote();
			for (int i = 1 ; i < longs.length ; i++){
				long l = longs[i];
				if (l != 0){
					// compensate the shift
					result.addAtRank((int)l, candidateSet[i-1]);
				}
			}
			return result;
		}

		public static PreferentialVote atRandom(Random r, String... candidateSet){
			int size = candidateSet.length;
			PreferentialVote result = new PreferentialVote();
			for (int i = 0 ; i < size ; i++){
				result.addAtRank(1+r.nextInt(size-1), candidateSet[i]);
			}
			return result;
		}

		@Override
		public String toString(){
			return toCondense();
		}
		
		public PreferentialVote without(String... candidates){
			PreferentialVote result = new PreferentialVote();
			List<String> list = Arrays.asList(candidates);
			for (String t : this.candidates()){
				if (!list.contains(t)){
					result = result.addAtRank(this.rankFor(t), t);
				}
			}
			return result;
		}

		@Override
		public int hashCode() {
			return this.toCondense().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PreferentialVote other = (PreferentialVote) obj;
			if (ranks == null) {
				if (other.ranks != null)
					return false;
			} else if (!toCondense().equals(other.toCondense()))
				return false;
			return true;
		}
}
