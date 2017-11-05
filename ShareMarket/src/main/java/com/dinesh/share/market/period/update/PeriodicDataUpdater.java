package com.dinesh.share.market.period.update;

import java.io.IOException;

import com.dinesh.share.market.dao.DAOManager;
import com.dinesh.share.market.exception.DataUpdaterException;
import com.dinesh.share.market.parser.NSECSVParser;
import com.dinesh.share.market.update.db.PeriodicDataDBLoader;

/**
 * 
 * @author Dinesh S
 *
 */
public class PeriodicDataUpdater
{

    private PeriodicDataDBLoader periodicDataDBLoader;
    private NSECSVParser nsecsvParser;
    private DAOManager daoManager;
    private PeriodicDataFetcher periodicDataFetcher;


    public PeriodicDataUpdater(
        PeriodicDataDBLoader periodicDataDBLoader,
        NSECSVParser nsecsvParser,
        DAOManager daoManager,
        PeriodicDataFetcher periodicDataFetcher )
    {
        this.periodicDataDBLoader = periodicDataDBLoader;
        this.nsecsvParser = nsecsvParser;
        this.daoManager = daoManager;
        this.periodicDataFetcher = periodicDataFetcher;
    }


    public void loadPeriodicData( String periodicDataOutputPath )
    {

        try
        {
            periodicDataDBLoader.parseAndLoadDataInDB(
                nsecsvParser, daoManager, periodicDataOutputPath );
        }
        finally
        {
            daoManager.close();
        }
    }


    public void fetchPeriodicData(
        String stocksToFetch,
        String period,
        String periodicDataOutputPath,
        String series )
    {

        try
        {
            periodicDataFetcher.fetchPeriodicData(
                stocksToFetch, period, periodicDataOutputPath, series );
        }
        catch( IOException e )
        {
            daoManager.close();
            throw new DataUpdaterException(
                "Problem in reading list of stocks file for periodic data update :" +
                    e.getMessage(), e );
        }

    }
}
