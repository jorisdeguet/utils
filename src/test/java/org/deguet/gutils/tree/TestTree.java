package org.deguet.gutils.tree;

import java.util.Arrays;
import java.util.List;

import org.junit.*;

public class TestTree {

	@Test
	public void testTree(){
		Tree<Integer> tree = new Tree<Integer>(5);
		Assert.assertEquals(1, tree.depth());
		
		for (int i = 10 ; i < 40 ; i+=10){
			tree = tree.add(i);
		}
		Assert.assertEquals(tree.leaves().size(), 3);
		Assert.assertEquals("equality", (int) tree.siblingAt(1).getContent(), 20);
		Assert.assertEquals("equality", (int) tree.elementAt(new int[]{1}), 20);
		Assert.assertEquals("siblings ", (int) tree.numberOfSiblings(), 3);
		System.out.println("================================================ toString");
		System.out.println(tree);
		System.out.println("================================================ toDot");
		System.out.println(tree.toDot());
		System.out.println("================================================ toXML");
		System.out.println(tree.toXML());
	}
	
	@Test
	public void testTreeBottomUp(){
		Tree<Integer> tree = new Tree<Integer>(1);
		Assert.assertEquals(1, tree.depth());
		
		int size=18;
		for (int i = 2 ; i < 2+size ; i+=1){
			tree = tree.add(i);
			tree = new Tree<Integer>(i,tree);
		}
		System.out.println("================================================ toString");
		System.out.println(tree);
		System.out.println("================================================ toDot");
		System.out.println(tree.toDot());
		System.out.println("================================================ toXML");
		System.out.println(tree.toXML());
		List<int[]> pathsToLeaves = tree.pathsToleaves();
		for(int[] path : pathsToLeaves ){
			System.out.println("Path " + Arrays.toString(path));
		}
		Assert.assertEquals("nombre de feuilles ", size, pathsToLeaves.size());
	}
	
}
