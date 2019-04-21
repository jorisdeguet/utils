package org.deguet.gutils.comparator;

import org.deguet.gutils.random.CopiableRandom;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeSet;

public class TestLexico {

	@Test
	public void testLexicoOrder(){
		CopiableRandom rand = new CopiableRandom(1234);
		Comparator<Integer> lexi = 
			new LexicoComparator<Integer>(new EvenComparator(),
					new LexicoComparator<Integer>(
							new InverseComparator<Integer>(new SevenComparator()),
							new SimpleComparator()));
		TreeSet<Integer> ordered = new TreeSet<Integer>(lexi);
		for (int i = 0 ; i < 50 ; i++){
			ordered.add(rand.nextInt());
		}
		for (Integer element : ordered){
			System.out.println("%2 "+element%2+" %7 "+element%7+"     " + element);
		}
	} 
	
	
	class EvenComparator implements Comparator<Integer>{
		public int compare(Integer o1, Integer o2) {
			int mod1 = o1 % 2;
			int mod2 = o2 % 2;
			if (mod1 == mod2 ) return 0;
			if (mod1 < mod2 )  return -1;
			if (mod1 > mod2 )  return 1;
			return 0;
		}
	}
	
	class SevenComparator implements Comparator<Integer>{
		public int compare(Integer o1, Integer o2) {
			int mod1 = o1 % 7;
			int mod2 = o2 % 7;
			if (mod1 == mod2 ) return 0;
			if (mod1 < mod2 )  return -1;
			if (mod1 > mod2 )  return 1;
			return 0;
		}
	}
	
	class SimpleComparator implements Comparator<Integer>{
		public int compare(Integer o1, Integer o2) {
			if (o1.equals(o2) ) return 0;
			if (o1 < o2 )  return -1;
			if (o1 > o2 )  return 1;
			return 0;
		}
	}
	
}
