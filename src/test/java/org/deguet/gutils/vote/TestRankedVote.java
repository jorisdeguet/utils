package org.deguet.gutils.vote;

import org.deguet.gutils.vote.preferential.PreferentialVote;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class TestRankedVote {

	@Test(timeout = 12000)
	public void testVoteCodec(){
		String[] candidates = {"Paul", "Joris", "Evariste", "Malcolm", "Alexandre"};
		Random r = new Random(653267);
		for (int i = 0 ; i < 1000 ; i++){
			PreferentialVote vote = PreferentialVote.atRandom(r, candidates);
			Long encoded = vote.encode(candidates);
			System.out.println("Encoded " + encoded + "  vote  "+ vote);
			PreferentialVote decoded  = PreferentialVote.decode(encoded, candidates);
			Assert.assertEquals("Même vote ", decoded, vote);
		}
	}
	
	@Test(timeout = 12000, expected = IllegalArgumentException.class)
	public void testWrongRank(){
		PreferentialVote vote = new PreferentialVote();
		vote.addAtRank(0, "99");
	}
	
	@Test(timeout = 12000, expected = IllegalArgumentException.class)
	public void testDoubleInsertion(){
		PreferentialVote vote = new PreferentialVote();
		vote.addAtRank(1, "99");
		vote.addAtRank(3, "99");
	}
	
	@Test(timeout = 12000, expected = IllegalArgumentException.class)
	public void testCondenseError(){
		PreferentialVote vote = new PreferentialVote();
		vote.addAtRank(1, "pipi>");
		vote.toCondense();
	}
	
	@Test(timeout = 12000)
	public void testVoteCondensate(){
		String[] candidates = {"Joris", "Evariste", "Malcolm", "Alexandre"};
		Random r = new Random(653267);
		for (int i = 0 ; i < 5 ; i++){
			PreferentialVote vote = PreferentialVote.atRandom(r, candidates);
			System.out.println("==============================================");
			System.out.println(vote.toCondense());
			System.out.println(vote);
		}
	}
	
	@Test(timeout = 12000)
	public void testVoteFromCondensate(){
		List<PreferentialVote> votes = PreferentialVote.fromCondenseList("55|a>b=c\n99|c>b>a");
		System.out.println("Votes " + votes.size()+" "+votes);
		PreferentialVote vote = PreferentialVote.fromCondense("a>b=c");
		System.out.println("Vote " + vote);
	}
	
	@Test(timeout = 12000)
	public void testSetHashEquals(){
		Set<PreferentialVote> set = new HashSet<>();
		List<PreferentialVote> list = new ArrayList<>();
		for (int i = 0 ; i < 100 ; i++){
			List<PreferentialVote> votes = PreferentialVote.fromCondenseList("10|a>b=c\n5|c>b>a");
			set.addAll(votes);
			list.addAll(votes);
		}
		System.out.println("Set i " +set);
		Assert.assertEquals("Set ", set.size() , 2);
		Assert.assertEquals("List ", list.size() , 100*15);
	}
	
	/**
	 * this test shows that this encoding is too slow
	 */
	//@Test(timeout = 12000)
	public void testVoteCodec2(){
		String[] candidates = {"Peter", "Marcel", "Joris", "Evariste", "Malcolm", "Alexandre"};
		Random r = new Random(653267);
		for (int i = 0 ; i < 1000 ; i++){
			PreferentialVote vote = PreferentialVote.atRandom(r, candidates);
			Long encoded = vote.encode(candidates);
			//System.out.println("Encoded " + encoded + "  vote  "+ vote);
			PreferentialVote decoded  = PreferentialVote.decode(encoded, candidates);
			Assert.assertEquals("Même vote ", decoded, vote);
		}
	}
	
	@Test(timeout = 12000)
	public void testVoteCodec2Condensate(){
		String[] candidates = {"Peter", "Marcel", "Joris", "Evariste", "Malcolm", "Alexandre"};
		Random r = new Random(653267);
		for (int i = 0 ; i < 1000 ; i++){
			PreferentialVote vote = PreferentialVote.atRandom(r, candidates);
			String s = vote.toCondense();
			PreferentialVote decoded  = PreferentialVote.fromCondense(s);
			String s2 = decoded.toCondense();
			PreferentialVote decoded2  = PreferentialVote.fromCondense(s2);
			Assert.assertEquals("Même vote ", decoded2, decoded);
		}
	}
	
	
	@Test(timeout = 10000)
	public void testVoteAtRandom(){
		String[] candidates = {"Paul", "Bob", "John", "Peter", "Marcel", "Joris", "Evariste", "Malcolm", "Alexandre"};
		Random r = new Random(653267);
		for (int i = 0 ; i < 100 ; i++){
			PreferentialVote vote = PreferentialVote.atRandom(r, candidates);
			System.out.println(vote);
		}
	}
	
	@Test(timeout = 100)
	public void testAsListofSet(){
		PreferentialVote vote = PreferentialVote.fromCondense("A>C=D>E");
		List<Set<String>> listset = vote.asListOfSet();
		PreferentialVote recov = PreferentialVote.fromListOfSet(listset);
		System.out.println(vote.toCondense()+"  "+ recov.toCondense());
	}
	
	@Test(timeout = 100)
	public void testAsListofSetCanonicalForm(){
		PreferentialVote vote = new PreferentialVote().addAtRank(3, "A","B").addAtRank(1, "D");
		List<Set<String>> listset = vote.asListOfSet();
		PreferentialVote recov = PreferentialVote.fromListOfSet(listset);
		System.out.println(vote.toCondense()+"  "+ recov.toCondense());
		System.out.println(vote+"  "+ recov);
	}

}
