package com.dinesh.share.market.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author Dinesh S
 *
 */
public final class PropertiesReader
{
    private static Properties properties;


    private PropertiesReader()
    {

    }


    static
    {
        properties = new Properties();
        try (InputStream propertiesStream =
            PropertiesReader.class
                .getResourceAsStream( "StockTrader.properties" ))
        {
            properties.load( propertiesStream );
        }
        catch( IOException e )
        {
            System.out
                .println( "Trouble in reading the DailyUpdater properties file" );
        }
    }


    public static String getNSEDailyDataURL()
    {
        return (String)properties.get( "NSE_DAILY_DATA_URL" );
    }


    public static String getNSEDailyDataFilePath()
    {
        return (String)properties.getProperty( "NSE_DAILY_DATA_FILE" );
    }


    public static String getNSEPeriodicDataURL()
    {
        return (String)properties.get( "NSE_PERIODIC_DATA_URL" );
    }


    public static String getNSEPeriodicDataPath()
    {
        return (String)properties.get( "NSE_PERIODIC_DATA_PATH" );
    }


    public static String getNSEPeriod24Month()
    {
        return (String)properties.get( "NSE_PERIOD_24_MONTH" );
    }


    public static String getDBDriver()
    {
        return (String)properties.get( "DB_DRIVER" );
    }


    public static String getDBConnectionURL()
    {
        return (String)properties.get( "DB_CONNECTION_URL" );
    }


    public static String getDBUserName()
    {
        return (String)properties.get( "DB_USER_NAME" );
    }


    public static String getDBPassword()
    {
        return (String)properties.get( "DB_PASSWORD" );
    }


    public static String getNSEPeriodWeek()
    {
        return (String)properties.get( "NSE_PERIOD_WEEK" );
    }


    public static String getNSESeriesEQ()
    {
        return (String)properties.get( "SERIES_EQ" );
    }
}
