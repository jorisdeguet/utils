package org.deguet.gutils.string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// http://www-igm.univ-mlv.fr/%7elecroq/string/string.pdf
public class StringContains {

	/**
	 * Compute the indexes where we can find the specified string.
	 * @param base the text we search in.
	 * @param searched the text we search for.
	 * @return
	 */
	public static Integer[] BoyerMoore(CharSequence base, CharSequence searched){
		Map<Character,Integer> badCharShift = new HashMap<Character,Integer>();
		for (int index = searched.length()-2; index>=0;index--){
			if (!badCharShift.containsKey(searched.charAt(index))){
				badCharShift.put(searched.charAt(index), searched.length()-index-1);
			}
		}
		//System.out.println(badCharShift); // it will be searched.length() for all other characters
		Map<CharSequence,Integer> goodSuffixShift = new HashMap<CharSequence,Integer>();
		for (int suffLength = 1 ; suffLength <= searched.length(); suffLength++){
			String suffix = searched.toString().substring(searched.length()-suffLength);
			goodSuffixShift.put(suffix, searched.length());
			for (int end= 1 ; end < searched.length(); end++){
				String prefix = searched.toString().substring(0, searched.length()-end);
				//System.out.println("Suffix "+suffix+"     prefix "+prefix);
				if (prefix.endsWith(suffix) || suffix.endsWith(prefix)){
					goodSuffixShift.put(suffix, end);
					break;
				}
			}
			//System.out.println(suffix+"   "+goodSuffixShift.get(suffix));
		}
		//System.out.println(goodSuffixShift);
		int cursor = searched.length();
		List<Integer> matches = new ArrayList<Integer>();
		while (cursor < base.length()){
			//show(cursor,base,searched);
			if (base.charAt(cursor) != searched.charAt(searched.length()-1)){
				int shift = (badCharShift.get(base.charAt(cursor))==null?searched.length():badCharShift.get(base.charAt(cursor)));
				cursor = cursor + shift;
			}
			else{
				int backward = 1;
				while(backward < searched.length() && base.charAt(cursor-backward) == searched.charAt(searched.length()-1-backward)){
					backward++;
				}
				if (backward == searched.length()){
					//System.out.println("Back "+backward+" found");
					matches.add(cursor+1-backward);
					cursor = cursor + searched.length();
				}
				else{
					//System.out.println("Back "+backward);
					String suffix = searched.toString().substring(searched.length()-backward);
					cursor = cursor + goodSuffixShift.get(suffix);
				}
			}
		}
		//System.out.println(matches);
		return matches.toArray(new Integer[matches.size()]);
	}
	
	private static void show(int cursor, CharSequence base, CharSequence search){
		System.out.println("cursor  "+cursor);
		System.out.println(base);
		System.out.println(Blanks.blanks(cursor+1-search.length())+search);
	}
	
}
