package com.dinesh.share.market.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.dinesh.share.market.daily.update.DailyDataUpdater;
import com.dinesh.share.market.exception.DataUpdaterException;
import com.dinesh.share.market.injector.DailyDataPrepareInjector;
import com.dinesh.share.market.properties.PropertiesReader;


public class DailyDataFetcherAndLoader
{
    private static final int MAX_ARGS = 8;
    private static String dailyDataPath;
    private static String filteredDailyDataPath;
    private static List<String> seriesList = new ArrayList<>();
    private static String stockListFile;


    public static void main( String[] args )
    {
        parseArgs( args );
        try
        {
            DailyDataUpdater dailyDataUpdater =
                new DailyDataPrepareInjector().getDailyDataUpdater();

            dailyDataUpdater
                .fetchDailyData(
                    new URL( PropertiesReader.getNSEDailyDataURL() ),
                    dailyDataPath );

            dailyDataUpdater
                .filterDataToLoad(
                    dailyDataPath, stockListFile, filteredDailyDataPath,
                    seriesList );

            dailyDataUpdater.loadDailyData( filteredDailyDataPath );
        }
        catch( MalformedURLException e )
        {
            System.out.println( "URL of NSE daily data is malformed" );
            e.printStackTrace();
        }
        catch( DataUpdaterException e )
        {
            System.out.println( e.getMessage() );
            e.printStackTrace();
        }
    }


    private static void parseArgs( String[] args )
    {
        if( args.length != MAX_ARGS )
        {
            System.out
                .println( "Arguments passed are not in acceptable format. Please give in below format : " );
            System.out
                .println( "-dailydatapath <path> -filtereddailydatapath <path> -stocklistfile <path> -series <comma separated>" );
            System.exit( 8 );
        }

        for( int i = 0; i < args.length; i++ )
        {
            if( "-dailydatapath".equals( args[i] ) )
            {
                dailyDataPath = args[++i];
            }
            else if( "-filtereddailydatapath".equals( args[i] ) )
            {
                filteredDailyDataPath = args[++i];
            }
            else if( "-stocklistfile".equals( args[i] ) )
            {
                stockListFile = args[++i];
            }
            else if( "-series".equals( args[i] ) )
            {
                String filterSeriesList = args[++i];
                for( String series : filterSeriesList.split( "," ) )
                {
                    seriesList.add( series );
                }
            }
        }

    }
}
