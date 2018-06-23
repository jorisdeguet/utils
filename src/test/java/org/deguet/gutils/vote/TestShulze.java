package org.deguet.gutils.vote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.deguet.gutils.vote.preferential.PreferentialBallot;
import org.deguet.gutils.vote.preferential.PreferentialVote;
import org.deguet.gutils.vote.preferential.ShulzeOnBallotBox;
import org.junit.Test;

import org.deguet.gutils.random.CopiableRandom;


/**
 * see http://www.cs.wustl.edu/~legrand/rbvote/calc.html
 * for tests and reference
 * @author joris
 *
 */
public class TestShulze {

	@Test(timeout = 10000)
	public void testNeverFirstButWinsWithBallotBox(){
		PreferentialBallot bb = new PreferentialBallot  ();
		bb.add(PreferentialVote.fromCondense("A>B>C"));
		bb.add(PreferentialVote.fromCondense("C>B>D"));
		bb.add(PreferentialVote.fromCondense("D>B>A"));
		bb.add(PreferentialVote.fromCondense("E>B>D"));
		ShulzeOnBallotBox shulze = new ShulzeOnBallotBox  (bb);
		System.out.println("Results " + shulze.results());
	}


	@Test(timeout = 10000)
	public void testShulze1(){
		PreferentialBallot   bb = new PreferentialBallot  ();
		ShulzeOnBallotBox   shulze = new ShulzeOnBallotBox  (bb);
		for (int i = 0 ; i < 45 ; i++){
			PreferentialVote   vote = new PreferentialVote  ()
					.addAtRank(1, "Paul")
					.addAtRank(5,"Peter")
					.addAtRank(5,"Billy");
			bb.add(vote);
		}
		for (int i = 0 ; i < 30 ; i++){
			PreferentialVote   vote = new PreferentialVote  ()
					.addAtRank(1, "Peter").addAtRank(2,"Billy").addAtRank(5,"Paul");
			bb.add(vote);
		}
		for (int i = 0 ; i < 16 ; i++){
			PreferentialVote   vote = new PreferentialVote  ()
					.addAtRank(1, "Billy").addAtRank(2,"Paul");
			bb.add(vote);
		}
		System.out.println("Results " + shulze.results());
		Assert.assertEquals("Billy", shulze.results().get(0).iterator().next());
	}

	/**
	 * when everyone votes the same, result shoud be that vote.
	 */
	@Test(timeout = 10000)
	public void testShulzeConsensus(){
		PreferentialBallot   bb = new PreferentialBallot  ();
		ShulzeOnBallotBox   shulze = new ShulzeOnBallotBox  (bb);
		PreferentialVote   vote = new PreferentialVote  ();
		vote.addAtRank(1, "Paul").addAtRank(5,"Peter").addAtRank(5,"Billy");
		for (int i = 0 ; i < 10 ; i++){
			bb.add(vote);
		}
		Assert.assertEquals("Result is the same as the unique vote", vote.asListOfSet(), shulze.results());
		//System.out.println("Results " + rs.results());
	}

	@Test(timeout = 10000)
	public void testShulzeConsensus2(){
		PreferentialBallot   bb = new PreferentialBallot  ();
		PreferentialVote   vote = new PreferentialVote  ().addAtRank(1, "Paul").addAtRank(4,"Peter").addAtRank(5,"Billy");
		bb.add(vote,10L);
		ShulzeOnBallotBox   shulze = new ShulzeOnBallotBox  (bb);
		List<Set<String>> results = shulze.results();
		System.out.println("Results " + results);
		System.out.println("Vote " + vote+"  "+vote.asListOfSet()+" "+vote.toCondense());
		Assert.assertEquals("Result is the same as the unique vote", vote.asListOfSet(), results);

	}

	@Test( timeout = 3000)
	public void testShulzeWikipediaSmall(){
		testShulzeWikipedia(1);
	}


	@Test( timeout = 20000)
	public void testShulzeWikipediaBigger(){
		testShulzeWikipedia(1000);
	}

	public void testShulzeWikipedia(int amount){
		int mult = amount;
		PreferentialBallot   bb = new PreferentialBallot  ();
		bb.add(PreferentialVote.fromCondense("A>C>B>E>D"), 5L*mult);
		bb.add(PreferentialVote.fromCondense("A>D>E>C>B"), 5L*mult);
		bb.add(PreferentialVote.fromCondense("B>E>D>A>C"), 8L*mult);
		bb.add(PreferentialVote.fromCondense("C>A>B>E>D"), 3L*mult);
		bb.add(PreferentialVote.fromCondense("C>A>E>B>D"), 7L*mult);
		bb.add(PreferentialVote.fromCondense("C>B>A>D>E"), 2L*mult);
		bb.add(PreferentialVote.fromCondense("D>C>E>B>A"), 7L*mult);
		bb.add(PreferentialVote.fromCondense("E>B>A>D>C"), 8L*mult);
		ShulzeOnBallotBox   shulze = new ShulzeOnBallotBox  (bb);
		List<Set <String> > r = shulze.results();
		List<Set <String> > expected = new ArrayList< >();
		for (String s : (new String[]{"E","A","C","B","D"})){
			Set   element = new HashSet  ();
			element.add(s);
			expected.add(element);
		}
		Assert.assertEquals("We know result should be E A C B D", r, expected);
		System.out.println("Results " + r);
	}


	//@Test
	public void testShulzeRandom() throws IOException{
		CopiableRandom r = new CopiableRandom(9875);
		String[] candidates = {"A","B","C","D","E","F","G","H","I","J"};
		for (int vote = 0 ; vote < 4 ; vote++){
			System.out.println("New One iteration " +vote  );
			PreferentialBallot   bb = new PreferentialBallot  ();
			ShulzeOnBallotBox   shulze = new ShulzeOnBallotBox  (bb);
			System.out.println("Adding votes to " + shulze + " ...");
			for (int i = 0 ; i < 100000 ; i++){
				PreferentialVote   v = PreferentialVote.atRandom(r.asRandom(), candidates);
				bb.add(v);
			}
			System.out.println("Computing results ...");
			List<Set<String>  > results = shulze.results();
		}
	}

	@Test
	public void testEqualityBetweenCandidates() throws IOException{
		PreferentialBallot   bb = new PreferentialBallot  ();
		ShulzeOnBallotBox   shulze = new ShulzeOnBallotBox  (bb);
		String[] candidates = {"A","B","C","D","E"};
		for (int i = 0 ; i < candidates.length ; i++){
			PreferentialVote   v = new PreferentialVote  ();
			v.addAtRank(1, candidates[i]);
			bb.add(v);
		}
		System.out.println("Computing results ...");
		List<Set<String>  > results = shulze.results();
		System.out.println("Results " + results);
	}



}
