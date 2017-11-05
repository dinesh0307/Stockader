package com.dinesh.share.market.properties;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 
 * @author Dinesh S
 *
 */
public class PropertiesReaderTest
{
    @Test
    public void testGetNSEDailyDataURL()
    {
        String expected =
            "https://www.nseindia.com/products/content/sec_bhavdata_full.csv";
        assertEquals( expected, PropertiesReader.getNSEDailyDataURL() );
    }


    @Test
    public void testGetNSEDailyDataFilePath()
    {
        String expected =
            "D:\\MyStuffs\\sharemarket\\data\\DailyData\\dailydata_NSE.csv";
        assertEquals( expected, PropertiesReader.getNSEDailyDataFilePath() );
    }


    @Test
    public void testGetNSEPeriodicDataURL()
    {
        String expected =
            "https://www.nseindia.com/products/content/equities/equities/eq_security.htm";
        assertEquals( expected, PropertiesReader.getNSEPeriodicDataURL() );
    }


    @Test
    public void testGetNSEPeriodicDataPath()
    {
        String expected = "D:\\MyStuffs\\sharemarket\\data\\PeriodicData";
        assertEquals( expected, PropertiesReader.getNSEPeriodicDataPath() );
    }


    @Test
    public void testGetNSEPeriod24Month()
    {
        String expected = "24month";
        assertEquals( expected, PropertiesReader.getNSEPeriod24Month() );
    }


    @Test
    public void testGetDBDriver()
    {
        String expected = "oracle.jdbc.driver.OracleDriver";
        assertEquals( expected, PropertiesReader.getDBDriver() );
    }


    @Test
    public void testGetDBConnectionURL()
    {
        String expected = "jdbc:oracle:thin:@localhost:1521:stockader";
        assertEquals( expected, PropertiesReader.getDBConnectionURL() );
    }


    @Test
    public void testGetDBUserName()
    {
        String expected = "stockader";
        assertEquals( expected, PropertiesReader.getDBUserName() );
    }


    @Test
    public void testGetDBPassword()
    {
        String expected = "Mani03Veera";
        assertEquals( expected, PropertiesReader.getDBPassword() );
    }


    @Test
    public void testGetNSEPeriodWeek()
    {
        String expected = "week";
        assertEquals( expected, PropertiesReader.getNSEPeriodWeek() );
    }


    @Test
    public void testGetNSESeriesEQ()
    {
        String expected = "EQ";
        assertEquals( expected, PropertiesReader.getNSESeriesEQ() );
    }
}
