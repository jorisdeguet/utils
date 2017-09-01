package org.deguet.gutils.geohash;

import java.util.Date;

/**
 * Embed the information relative to a space time cylinder meaning a connected polygon
 * multiplied by a time interval.
 * 
 * Immutable
 * 
 * This is earth space and time meaning we are interested in Earth time and Earth coordinates for the polygon.
 * @author joris
 *
 */
public final class SpaceTime {

	final GeoHash[] polygon;
	
	final Date start,end;
	
	public SpaceTime(){
		polygon = new GeoHash[0];
		start = end = null;
	}
	
	public static boolean intersect(SpaceTime one, SpaceTime two){
		boolean timeIntersect = !(one.end.before(two.start) || two.end.before(one.start));
		return timeIntersect && polyIntersect(one.polygon,two.polygon);
	}
	
	/**
	 * Should be done with great circles
	 * @param a
	 * @param b
	 * @return
	 */
	private static boolean polyIntersect(GeoHash[] a, GeoHash[] b){
		return true;//TODO
	}
	
}
