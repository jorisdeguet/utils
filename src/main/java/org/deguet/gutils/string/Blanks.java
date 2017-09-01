package org.deguet.gutils.string;

public class Blanks {

	/**
	 * Returns a string made of n white spaces.
	 * @param n the number of white spaces needed.
	 * @return
	 */
	public static String blanks(int n){
		StringBuilder res = new StringBuilder("");
		for (int i =0 ; i < n; i++){
			res.append(" ");
		}
		return res.toString();
	}
	
	public static String nth(int n, String s){
		StringBuilder res = new StringBuilder();
		for (int i =0 ; i < n; i++){
			res.append(s);
		}
		return res.toString();
	}
	
}
