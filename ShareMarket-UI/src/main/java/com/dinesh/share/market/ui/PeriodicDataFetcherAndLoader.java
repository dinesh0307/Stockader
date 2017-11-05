package com.dinesh.share.market.ui;

import com.dinesh.share.market.exception.DataUpdaterException;
import com.dinesh.share.market.injector.PeriodicDataPrepareInjector;
import com.dinesh.share.market.period.update.PeriodicDataUpdater;
import com.dinesh.share.market.properties.PropertiesReader;


public class PeriodicDataFetcherAndLoader
{
    public static void main( String[] args )
    {
        try
        {
            PeriodicDataUpdater periodicDataUpdater =
                new PeriodicDataPrepareInjector()
                    .getPeriodicDataUpdater( PropertiesReader
                        .getNSEPeriodicDataURL() );

            periodicDataUpdater
                .fetchPeriodicData(
                    "D:\\MyStuffs\\sharemarket\\data\\stockList\\NSE\\ListOfNSEStocks.txt",
                    PropertiesReader.getNSEPeriod24Month(),
                    PropertiesReader.getNSEPeriodicDataPath(),
                    PropertiesReader.getNSESeriesEQ() );

            periodicDataUpdater.loadPeriodicData( PropertiesReader
                .getNSEPeriodicDataPath() );
        }
        catch( DataUpdaterException e )
        {
            System.out.println( e.getMessage() );
            e.printStackTrace();
        }
    }
}
