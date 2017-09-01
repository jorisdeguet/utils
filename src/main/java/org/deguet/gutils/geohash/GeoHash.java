package org.deguet.gutils.geohash;

import java.util.Random;

import org.deguet.gutils.bit.BitLine;
import org.deguet.gutils.nuplets.Duo;

/**
 * Implements a GeoHash where latitude and longitudes are cut into halves to determine bits in a String
 * 
 * 
 * 
 * 
 * TODO
 * - integrate with http://www.osgeo.org/geotools
 * @author joris
 *
 */
public strictfp class GeoHash {

	private static long factor = Long.MAX_VALUE/200;

	private static long maxLat = conv(90.0);
	private static long minLat = conv(-90.0);
	private static long maxLng = conv(180.0);
	private static long minLng = conv(-180.0);

	// the maximal precision in number of bits.
	// might never be attained as at some point bounds should collapse.
	private final static int maxPrecision = 48;

	private final BitLine bits;

	long minlat,minlng,maxlat,maxlng;

	private GeoHash(BitLine bl){
		this.bits = bl;
		computeTransient();
	}

	/**
	 * Creates a GeoHash for a single point provided the lat and lng
	 * @param lat
	 * @param lng
	 */
	public GeoHash(double lat, double lng){
		this.bits = encodeToBitLine(conv(lat),conv(lng));
		computeTransient();
	}

	private static long conv(double d){
		return (long) (d*factor);
	}

	private static double reconv(long d){
		return (d+0.0)/factor;
	}

	private static BitLine interleave2(BitLine lng, BitLine lat){
		BitLine list = new BitLine();
		int index = 0;
		while(index < lng.getSize() && index < lat.getSize()) {
			list = list.set(index*2,lng.isOn(index));
			list = list.set(index*2+1,lat.isOn(index));
			index++;
		}
		return list;
	}

	/**
	 * Tells if a GeoHash with a certain precision contains another one
	 * usually with a greater precision
	 * @param point
	 * @return
	 */
	public boolean contains(GeoHash point){
//computeTransient();
//		System.out.println(minlat+"  <=  "+  point.minlat+"   ? " +(minlat<=point.minlat));
//		System.out.println(maxlat+"  <=  "+  point.maxlat+"   ? " +(maxlat>=point.maxlat));
//		
//		System.out.println(minlng+"  <=  "+  point.minlng+"   ? " +(minlng<=point.minlng));
//		System.out.println(maxlng+"  <=  "+  point.maxlng+"   ? " +(maxlng>=point.maxlng));
		return this.bits.commonPrefixWith(point.bits).equals(this.bits);
	}

	
	public static GeoHash smallestGeohashContainingPoints(Duo<Double,Double>[] points){
		// common prefix between different point
		BitLine result=null;
		for (Duo<Double,Double> point : points){
			GeoHash hash = new GeoHash(point.get1(),point.get2());
			result = (result==null?hash.bits:result.commonPrefixWith(hash.bits));
		}
		return new GeoHash(result);
	}

	/**
	 * Returns the smallest GeoHash containing all other points with greater precision
	 * @param hashes
	 * @return
	 */
	public static GeoHash smallestGeohashContainingPoints(GeoHash... hashes) {
		BitLine result=null;
		for (GeoHash hash : hashes){
			if (hash != null){
				//System.out.println(result);
				result = (result==null?hash.bits:result.commonPrefixWith(hash.bits));
			}
		}
		return new GeoHash(result);
	}

	/**
	 * returns the bounds of this GeoHash box
	 */
	public Duo<Duo<Double,Double>,Duo<Double,Double>> toBounds(){
		return Duo.d(
				Duo.d(reconv(minlat),reconv(minlng)),
				Duo.d(reconv(maxlat),reconv(maxlng))
		);
	} 

	public static GeoHash fromStringRepresentation(String geohash){
		BitLine bline = BitLine.fromHex(geohash);
		return new GeoHash(bline);
	}

	public String toStringRepresentation(){
		computeTransient();
		return "{"+this.bits.toBits()+"  ("+minlat+"->"+maxlat+" , "+minlng+"->"+maxlng+") }";
	}

	public double lat(){
		return reconv((maxlat+minlat)/2);
	}

	public double lng(){
		return reconv((maxlng+minlng)/2);
	}

	private BitLine encodeToBitLine(long l, long m){
		BitLine latb2 = dichoInv2(l,minLat,maxLat);
		BitLine lngb2 = dichoInv2(m,minLng,maxLng);
		BitLine list2 = interleave2(lngb2,latb2);
		return list2;
	}

	private void computeTransient(){
		long latSW = GeoHash.minLat;
		long lngSW = GeoHash.minLng;
		long latNE = GeoHash.maxLat;
		long lngNE = GeoHash.maxLng;
		// When it is not longitude it is latitude
		boolean longitude = true;
		for (Boolean upper : bits) {
			//System.out.println("dicho2 "+bits + "     " +upper);
			if (longitude){
				if (upper)lngSW = (lngSW+lngNE)/2;
				else lngNE = (lngSW+lngNE)/2;
				longitude = !longitude;
			}
			else{
				if (upper){latSW = (latSW+latNE)/2;}
				else{latNE = (latSW+latNE)/2;}
				longitude = !longitude;
			}
		}
		this.minlat = latSW;
		this.minlng = lngSW;
		this.maxlat = latNE;
		this.maxlng = lngNE;
	}

	@Override
	public String toString(){
		return this.bits.toBits();
	}
	
	public static GeoHash fromString(String bits){
		return new GeoHash(BitLine.fromBits(bits));
	}


	private BitLine dichoInv2(long lat, long min, long max) {
		BitLine res = new BitLine();
		int i = 0;
		// epsilon is the difference between the two bounds we consider as null
		while(i<maxPrecision && max!=min) {
			long mid = (min + max) / 2;
			if (lat >= mid) {
				res = res.set(i,true);
				min = mid;
			} else {
				res = res.set(i,false);
				max = mid;
			}
			i++;
		}
		return res;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bits == null) ? 0 : bits.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeoHash other = (GeoHash) obj;
		if (bits == null) {
			if (other.bits != null)
				return false;
		} else if (!bits.equals(other.bits))
			return false;
		return true;
	}

	public BitLine bits() {return bits;}
	
	/**
	 * Returns the distance to another point described by a GeoHash in meters
	 * @param other
	 * @return
	 */
	public float distanceTo(GeoHash other){
		throw new RuntimeException("TODO");
	}
	
	/**
	 * Returns the area of this GeoHash in square meters
	 * at sea mean level  USE Jscience.org for coordinates
	 * @param other
	 * @return
	 */
	public float area(GeoHash other){
		throw new RuntimeException("TODO");
	}

	/**
	 * Generates a random GeoHash based on the given random generator.
	 * @param r
	 * @return
	 */
	public static  GeoHash atRandom(Random r) {
		int size = 10+r.nextInt(maxPrecision-10);
		BitLine bits = BitLine.random(r, size);
		return new GeoHash(bits);
	}

}
