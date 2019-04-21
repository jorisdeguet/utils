package org.deguet.gutils.graph;

import org.deguet.gutils.graph.export.SVGthroughGraphViz;
import org.junit.Test;

import java.io.IOException;

public class TestSVG {

	
	@Test
	public void testExportSVG() throws IOException{
		//DGraphs.randomGraph();
		SVGthroughGraphViz.saveAsDotAndConvertToSVG(DGraphs.randomGraph(98766,10),"test/TestSVG/",SVGthroughGraphViz.dot);
	}
}
