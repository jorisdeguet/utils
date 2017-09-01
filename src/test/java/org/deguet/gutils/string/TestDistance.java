package org.deguet.gutils.string;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.deguet.gutils.graph.Graph;
import org.deguet.gutils.graph.export.SVGthroughGraphViz;

public class TestDistance {

	@Test
	public void testEditionDistance(){
		EditionDistance ed = new EditionDistance("pipo","pipop");
		System.out.println("edition dist " +ed);
		Assert.assertEquals("distance " , 1, ed.distance());
		
		
		Assert.assertEquals("distance " , 2, (new EditionDistance("pipo","popi")).distance());
	}
	
	@Test
	public void testMatrix(){
		EditionDistance ed = new EditionDistance("marie","joris");
		System.out.println(ed.distance());
		ed.showMatrix();
		System.out.println(ed.path());
		EditionDistance.in(new String[]{"egzam","exam"});
	}
	
	
	@Test
	public void testPath(){
		EditionDistance ed = new EditionDistance("marie-claude","joris");
		List<CharSequence> path = ed.path();
		System.out.println("d = "+ ed.distance() +" p="+path);
		Assert.assertEquals("longueur du chemin ", ed.distance(), path.size()-1);
	}
	
	@Test // TODO FOR REAL 
	public void testGraph() throws IOException{
		EditionDistance ed = new EditionDistance("marie-claude","joris");
		Graph<String> g = EditionDistance.in(new String[]{"joris","Mauris"});
		SVGthroughGraphViz.saveAsDotAndConvertToSVG(g,"TestSVG/");
	}
	
}
