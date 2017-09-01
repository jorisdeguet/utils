package org.deguet.gutils.graph.export;


import java.io.File;
import java.io.IOException;

import org.deguet.gutils.file.FileString;
import org.deguet.gutils.graph.DGraph;
import org.deguet.gutils.graph.DGraphs;
import org.deguet.gutils.graph.Graph;


/**
 * Test Class for the "Parse through diagrams project"
 * 
 * TODO list:
 * - STAY SCANNERLESS
 * - PARSE FOREST
 * - SEPARATE 
 *   - FORMATS (MAKE EBNF)
 *   - RECOGNISERS/PARSERS (USE LALR IF GRAMMAR ALLOWS IT, A GENERAL ONE IN OTHER CASES)
 *   - ERROR REPORTERS (IF NOT RECOGNISED)
 *   - GENERATORS
 * - Tell me more about ambiguity (not ambiguous if can build unambiguous parser) then you can say maybe ambiguous and watch execution.
 * - add builtin alternatives for characters sets + slicing -> example is ascii['a'-'b'] or utf8['0'-'9']
 * - implement "parseGiveOne" "parseGiveAll" and "parseMaximum" for giving one sol, all sols, or the longest successful parse.
 * - think about bad parse messages to send back.
 * - put a batch of words and see which part is matched by your working grammar.
 * - Define 2 classes of diagrams, one that builds a tree and the other that just aggregates the string browsed between the in and the out
 * !!! Better if done with a simple post processing cf reduce
 * - Intern all string to make comparaison super FASSSTTTT  optimization
 * - Enable something like nontermA-nontermB matches nontermA but words that does not match nontermB
 * - As every non terminal describes a set, all potential set operators Union (|) Difference (\) Intersection (&) Cart Product (,)
 * - Embed a browsing mode for parse tree that goes through the tree and raises events when entering and leaving non terminals "a la SAX"
 * 
 * @author joris
 *
 */
public class SVGthroughGraphViz {

	public static final String dot = "/usr/local/bin/dot";
	
	/**
	 * Handles display of a grammar.
	 * @param g
	 * @param path
	 * @throws IOException 
	 */
	public static void saveAsDotAndConvertToSVG(DGraph g, String path, String todot) throws IOException{
		File resultDir = new File(path);
		resultDir.mkdirs();
		// Write Graphs
		FileString.toFile(new File(path+"graph.dot"), DGraphs.toDot(g,"pipo"));
		convertDot(resultDir.getAbsolutePath(),"graph",todot);
		// Write Diagrams
	}
	
	public static void saveAsDotAndConvertToSVG(DGraph g,String path) throws IOException{
		saveAsDotAndConvertToSVG(g,path,dot);
	}
	
	public static void saveAsDotAndConvertToSVG(Graph g, String path) throws IOException{
		saveAsDotAndConvertToSVG(g,path,dot);
	}
	
	public static void saveAsDotAndConvertToSVG(Graph g,String path,String todot) throws IOException{
		File resultDir = new File(path);
		resultDir.mkdirs();
		// Write Graphs
		FileString.toFile(new File(path+"graph.dot"),g.toDot("tostitos"));
		convertDot(resultDir.getAbsolutePath(),"graph",todot);
		// Write Diagrams
	}
	
	public static void saveDotAndConvertToSVG(String dot,String path,String todot, String fileName) throws IOException{
		File resultDir = new File(path);
		resultDir.mkdirs();
		// Write Graphs
		FileString.toFile(new File(path+fileName+".dot"),dot);
		convertDot(resultDir.getAbsolutePath(),fileName,todot);
		// Write Diagrams
	}
	
	private static void convertDot(String path, String fileName,String todot){
		try{
			String dotPath = path+File.separator+fileName+".dot";
			String destPath = path+File.separator+fileName+".svg";
			String command  = todot+" -Tsvg -v "+dotPath+" -o"+destPath ;
			Runtime.getRuntime().exec(command).waitFor();
		}
		catch(Throwable e){
			e.printStackTrace();
		}
	}
}

