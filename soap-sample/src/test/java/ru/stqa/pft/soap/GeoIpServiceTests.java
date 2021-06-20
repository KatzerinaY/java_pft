package ru.stqa.pft.soap;


import com.lavasoft.GeoIPService;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class GeoIpServiceTests {

    @Test
    public void testMyIp(){

        String ipLocation = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("5.146.192.191");
        System.out.println(ipLocation);
        Assert.assertTrue(ipLocation.contains("<Country>DE</Country>"));

    }

    @Test
    public void testInvalidIp(){
        String ipLocation = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("5.146.192.XXX");
        Assert.assertTrue(ipLocation.contains("<Country>DE</Country>"));
    }
}
