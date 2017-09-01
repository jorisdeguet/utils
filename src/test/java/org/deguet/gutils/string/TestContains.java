package org.deguet.gutils.string;

import junit.framework.Assert;

import org.junit.Test;

public class TestContains {

	@Test
	public void TestBoyerMoore(){
		String source = "here is a simple example with two occurrences of example";
		String searched = "mple";
		Integer[] res = StringContains.BoyerMoore(source,searched);
		
		for (int i : res){
			String found = source.substring(i, i+4);
			Assert.assertEquals("found is searched? ", found, searched);
			System.out.println(found);
		}
	}
	
}
