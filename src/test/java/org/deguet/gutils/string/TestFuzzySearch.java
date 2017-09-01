package org.deguet.gutils.string;

import org.junit.Test;

public class TestFuzzySearch {

	@Test
	public void testFuzzy(){
		String src =  "je suis le gars qui fait les tests";
		String ptn =  "puis";
		FuzzySearch fs  = new FuzzySearch(src, ptn);
		System.out.println(fs.results(0));
		
		FuzzySearch fs1  = new FuzzySearch(src, "suis");
		System.out.println(fs1.results(0));
	}
	
}
