package org.deguet.gutils.string;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deguet.gutils.graph.GraphTiny;
import org.deguet.gutils.nuplets.Duo;

/**
 * Immutable class to compute the edit distance distance or Levensthein distance between two CharSequences
 * It will give information about the distance but also transformation paths to go from the inital word to the final one.
 * @author joris
 *
 */
public class EditionDistance {

	final int[][] distanceMatrix;

	final CharSequence a,b;

	public EditionDistance(CharSequence aa, CharSequence bb){
		a = aa; b = bb;
		distanceMatrix = new int[a.length()+1][b.length()+1];
		// it goes ascending i and j we need first column and first row
		for (int i = 0; i <= a.length(); i++) distanceMatrix[i][0] = i;
		for (int j = 0; j <= b.length(); j++) distanceMatrix[0][j] = j;
		// The invariant is that the [1..i] prefix of a and the [1..j] prefix of b can be obtain with distanceMatrix[i,j] operations
		// Could be parallelized by diagonals could also be done recursively
		fillArray(distanceMatrix,a,b);
	}

	static void fillArray(int[][] distMat, CharSequence a, CharSequence b){
		for (int i = 1; i <= a.length(); i++){
			for (int j = 1; j <= b.length(); j++){
				int abovePlusDelete = distMat[i - 1][j] + 1;
				int leftPlusInsert  = distMat[i][j - 1] + 1;
				int diagPlusReplace = distMat[i - 1][j - 1]+ ((a.charAt(i-1) == b.charAt(j-1)) ? 0: 1);//-1 in charAt is just because we shifted indexes between string and the array
				distMat[i][j] = minimum(abovePlusDelete,leftPlusInsert,diagPlusReplace);
			}
		}
	}
	
	public static int minimum(int a, int b, int c) {
		return ((a<b&&a<c)?a:(b<c?b:c));
	}

	public void showMatrix(){
		showMatrix(this.distanceMatrix,this.a, this.b);
	}

	public static void showMatrix(int[][] tab, CharSequence a, CharSequence b){
		System.out.print("   -1");
		for (int j = 0; j < b.length(); j++){
			System.out.print(" "+b.charAt(j));
		}
		System.out.println();
		for (int i = 0; i <= a.length(); i++){
			if (i-1>=0) 
				System.out.print(a.charAt(i-1)+"  ");
			else
				System.out.print("-1 ");
			for (int j = 0; j <= b.length(); j++){
				System.out.print(" "+tab[i][j]);
			}
			System.out.println();
		}
	}
	
	/**
	 * Compute a list a tranformed strings based on a path in the matrix
	 * TODO make it less ugly please codewise
	 * @return
	 */
	public List<CharSequence> path(){
		List<CharSequence> result = new ArrayList<CharSequence>();
		result.add(a);
		int i = a.length();
		int j = b.length();
		CharSequence current = a;
		while (i>=1 && j>=1){
			int abovePlusDelete = distanceMatrix[i - 1][j] + 1;
			int leftPlusInsert  = distanceMatrix[i][j - 1] + 1;
			int diagPlusReplace = distanceMatrix[i - 1][j - 1]+ ((a.charAt(i - 1) == b.charAt(j - 1)) ? 0: 1);
			// non deterministic choice of one path
			int min = minimum(abovePlusDelete,leftPlusInsert,diagPlusReplace);
			if (min == diagPlusReplace){
				//System.out.println("diag bef "+current+"   i="+i+"    j="+j+"     d="+distanceMatrix[i][j]);
				if (a.charAt(i - 1) != b.charAt(j - 1)){
					current = 
						current.toString().substring(0, i-1)+
						b.charAt(j-1)+
						current.toString().substring(i-0);
					result.add(current);
				}
				//System.out.println("diag aft "+current);
				i--;j--;
			}
			else if (min == leftPlusInsert){
				//System.out.println("left bef "+current+"   i="+i+"    j="+j+"     d="+distanceMatrix[i][j]);
				current = 
					current.toString().substring(0, i)+b.charAt(j-1)+current.toString().substring(i);
				//System.out.println("left aft "+current);
				j--;
				result.add(current);
			}
			else{
				//System.out.println("abov bef "+current+"   i="+i+"    j="+j+"     d="+distanceMatrix[i][j]);
				current = 
					current.toString().substring(0, i-1)+
					current.toString().substring(i);
				//System.out.println("abov aft "+current);
				i--;
				result.add(current);
			}
			
		}
		// We have rached the point where a prefix of one the two strings has to be appended
		if (i==0){
			while (j>=1){
				current = ""+b.charAt(j-1)+current;
				result.add(current);
				j--;
			}
		}
		if (j==0){
			while (i>=1){
				current = current.toString().substring(1);
				result.add(current);
				i--;
			}
		}
		//System.out.println(result);
		return result;
	}
	
	public Set<Duo<String,String>> graph(){
		Set<Duo<String,String>> edges = recgraph(a.length(),b.length(),a.toString());
		//System.out.println(graph);
		return edges;
	}
	
	// will stop as i+j monotonously decreases
	public Set<Duo<String,String>> recgraph(int i, int j, String a){
		Set<Duo<String,String>> result = new HashSet<Duo<String,String>>();
		if (j==0 && i==0){
			return result;
		}
		else if (j==0){
			String pred = a.toString().substring(1);
			result.add(Duo.d(a,pred));
			result.addAll(recgraph(i-1,j,pred));
		}
		else if (i==0){
			String pred = ""+b.charAt(j-1)+a;
			result.add(Duo.d(a,pred));
			result.addAll(recgraph(i,j-1,pred));
		}
		else{
			int abovePlusDelete = distanceMatrix[i - 1][j] + 1;
			int leftPlusInsert  = distanceMatrix[i][j - 1] + 1;
			int diagPlusReplace = distanceMatrix[i - 1][j - 1]+ ((a.charAt(i - 1) == b.charAt(j - 1)) ? 0: 1);
			// non deterministic choice of one path
			int min = minimum(abovePlusDelete,leftPlusInsert,diagPlusReplace);
			if (min == diagPlusReplace){
				String pred = 
					a.toString().substring(0, i-1)+
					b.charAt(j-1)+
					a.toString().substring(i-0);
				//if (a.charAt(i - 1) != b.charAt(j - 1)){
					result.add(Duo.d(a,pred));
				//}
				result.addAll(recgraph(i-1,j-1,pred));
			}
			if (min == leftPlusInsert){
				String pred = 
					a.toString().substring(0, i)+b.charAt(j-1)+a.toString().substring(i);
				result.addAll(recgraph(i,j-1,pred));
				result.add(Duo.d(a,pred));
			}
			if (min == abovePlusDelete){
				String pred = 
					a.toString().substring(0, i-1)+
					a.toString().substring(i);
				result.addAll(recgraph(i-1,j,pred));
				result.add(Duo.d(a,pred));
			}
		}
		return result;
	}

	public int distance() {
		return distanceMatrix[a.length()][b.length()];
	}

	public static GraphTiny<String> in(String[] words){
		GraphTiny<String> result =
			new GraphTiny<String>();
		for(String a : words){
			for(String b : words){
				EditionDistance ed = new EditionDistance(a,b);
				Set<Duo<String,String>> edges = ed.graph();
				for (Duo<String,String> edge : edges){
					result = result.addEdge(edge.get1(),edge.get2());	
				}
			}
		}
		//System.out.println("Graph is \n"+result);
		Set<String> middlers = result.vertices();
		for(String a : middlers){
			for(String b : middlers){
				EditionDistance ed = new EditionDistance(a,b);
				Set<Duo<String,String>> edges = ed.graph();
				for (Duo<String,String> edge : edges){
					result = result.addEdge(edge.get1(),edge.get2());	
				}
			}
		}
		//System.out.println("Graph is \n"+result);
		return result;
	}

}
