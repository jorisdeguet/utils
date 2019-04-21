package org.deguet.gutils.geohash;

import junit.framework.Assert;
import org.deguet.gutils.nuplets.Duo;
import org.junit.Test;

import java.util.Random;


public class TestGeo {

	@Test
	public void testCoDec(){
		Random r = new Random(78545983);
		double maxDiff = 0.0;
		for (int i  = 0 ; i < 1000 ; i++) {
			double lat = r.nextDouble()*180 - 90;
			double lng = r.nextDouble()*90;
			GeoHash g1 = new GeoHash(lat, lng);
			
			double lat1 = g1.lat();
			double lng1 = g1.lng();
			
			//System.out.println("----------");
			if(Math.abs(lat-lat1) > maxDiff) maxDiff = Math.abs(lat-lat1);
			if(Math.abs(lng-lng1) > maxDiff) maxDiff = Math.abs(lng-lng1);
			
			GeoHash g2 = new GeoHash(lat1,lng1);
			
			double lat2 = g2.lat();
			double lng2 = g2.lng();
			
			Assert.assertEquals("Two lat  should be equal ", lat2, lat1);
			Assert.assertEquals("Two lng  should be equal ", lng2, lng1);
			Assert.assertEquals("Two geo hashes should be equal ", g2, g1);
		}
		System.out.println("Max diff is  " +maxDiff);
	}
	
	@Test
	public void testHexRepresentation() {
		Duo<Double,Double> latlon =
				Duo.d(45.477,-73.564);
		System.out.println(latlon);
		GeoHash hash = new GeoHash(latlon.get1(), latlon.get2());
		System.out.println(hash.toStringRepresentation());
		latlon = Duo.d(hash.lat(),hash.lng());
		System.out.println(latlon);
		GeoHash roundTrip = new GeoHash(latlon.get1(),latlon.get2());
		System.out.println("----------Double round trip\n"+hash+"\n"+roundTrip);
		GeoHash hexroundtrip = GeoHash.fromStringRepresentation(hash.bits().toHex());
		Assert.assertEquals("1 ", roundTrip, hexroundtrip);
		System.out.println("----------Bitline round trip\n"+hash+"\n"+hexroundtrip);
		GeoHash roundtrip01 = GeoHash.fromString(hash.bits().toRawBits());
		Assert.assertEquals("1 ", roundtrip01, hexroundtrip);
		System.out.println("----------Bitline round trip\n"+hash+"\n"+roundtrip01);
		GeoHash container = 
			GeoHash.smallestGeohashContainingPoints(
					new Duo[]{
							Duo.d(44.0,-70.0),
							Duo.d(40.0,-80.0),
							Duo.d(42.5,-72.2)
					}
			);
		System.out.println(container);
	}

	
	@Test
	public void testToFromString() {
		Random r = new Random(78545983);
		for (int i = 0 ; i < 10000 ; i++){
			GeoHash geo = GeoHash.atRandom(r);
			String string = geo.toString();
			GeoHash ge2 = GeoHash.fromString(string);
			String strin2 = ge2.toString();
			//System.out.println("string " +string);
			Assert.assertEquals("String should be equal  ", string, strin2);
			Assert.assertEquals("Hashes should be equal  ", geo, ge2);
		}
	}
	
	@Test
	public void testContains() {
		Random r = new Random(78545983);
		for (int i = 0 ; i < 1000 ; i++){
			GeoHash geo = GeoHash.atRandom(r);
			GeoHash geo2 = GeoHash.atRandom(r);
			GeoHash container = GeoHash.smallestGeohashContainingPoints(geo, geo2);
			Assert.assertTrue("Enclosure \n"+container.toStringRepresentation()+"\n contains \n"+geo.toStringRepresentation(), container.contains(geo));
			Assert.assertTrue("Enclosure \n"+container.toStringRepresentation()+"\n contains \n"+geo2.toStringRepresentation(), container.contains(geo2));
		}
		
	}
	
	@Test
	public void testMinLatMaxLng() {
		Random r = new Random(78545983);
		for (int i = 0 ; i < 1000 ; i++){
			GeoHash geo = GeoHash.atRandom(r);
			GeoHash geo2 = GeoHash.atRandom(r);
			GeoHash container = GeoHash.smallestGeohashContainingPoints(geo, geo2);
			// TODO assertions pour tester les inegalites entre les latitude et longitudes.
			Assert.assertTrue("Enclosure \n"+container.toStringRepresentation()+"\n contains \n"+geo.toStringRepresentation(), container.contains(geo));
			Assert.assertTrue("Enclosure \n"+container.toStringRepresentation()+"\n contains \n"+geo2.toStringRepresentation(), container.contains(geo2));
		}
		
	}

}
