package org.deguet.gutils.graph;

import java.io.IOException;

import org.junit.Test;

import org.deguet.gutils.graph.export.SVGthroughGraphViz;

public class TestSVG {

	
	@Test
	public void testExportSVG() throws IOException{
		//DGraphs.randomGraph();
		SVGthroughGraphViz.saveAsDotAndConvertToSVG(DGraphs.randomGraph(98766,10),"test/TestSVG/",SVGthroughGraphViz.dot);
	}
}
