package com.dinesh.share.market.daily.update;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dinesh.share.market.dao.DAOManager;
import com.dinesh.share.market.exception.DataUpdaterException;
import com.dinesh.share.market.parser.NSECSVParser;
import com.dinesh.share.market.update.db.DailyDataDBLoader;

/**
 * 
 * @author Dinesh S
 *
 */
public class DailyDataUpdater
{
    private DailyDataDBLoader dailyDataDBLoader;
    private DAOManager daoManager;
    private NSECSVParser nsecsvParser;
    private DailyNSEDataFetcher dailyNSEDataFetcher;


    public DailyDataUpdater(
        DailyDataDBLoader dailyDataDBLoader,
        NSECSVParser nsecsvParser,
        DAOManager daoManager,
        DailyNSEDataFetcher dailyNSEDataFetcher )
    {
        this.dailyDataDBLoader = dailyDataDBLoader;
        this.nsecsvParser = nsecsvParser;
        this.daoManager = daoManager;
        this.dailyNSEDataFetcher = dailyNSEDataFetcher;

    }


    public void loadDailyData( String dailyDataFile )
    {

        try
        {
            dailyDataDBLoader.parseAndLoadDataInDB(
                nsecsvParser, daoManager, dailyDataFile );
        }
        finally
        {
            daoManager.close();
        }

    }


    public void fetchDailyData( URL dailyDataURL, String dailyDataFile )
    {

        try
        {
            dailyNSEDataFetcher.fetchNSEData( dailyDataURL, dailyDataFile );
        }
        catch( DataUpdaterException e )
        {
            daoManager.close();
            throw e;
        }

    }


    public void filterDataToLoad(
        String nseDailyDataFilePath,
        String stocksToLoadFile,
        String filteredNseDailyDataPath,
        List<String> seriesList )
    {
        try
        {
            Set<String> stocksToLoad = getStocksToLoadList( stocksToLoadFile );
            String filteredData =
                getFilteredData( nseDailyDataFilePath, stocksToLoad, seriesList );
            writeFilteredDataToFile( filteredData, filteredNseDailyDataPath );
        }
        catch( DataUpdaterException e )
        {
            daoManager.close();
            throw e;
        }

    }


    private void writeFilteredDataToFile(
        String filteredData,
        String filteredNseDailyDataPath )
    {
        try (BufferedWriter filteredDataWriter =
            new BufferedWriter( new FileWriter( filteredNseDailyDataPath ) ))
        {
            filteredDataWriter.write( filteredData );
        }
        catch( IOException e )
        {
            throw new DataUpdaterException( "Problem in writing the file : " +
                filteredNseDailyDataPath + " ." + e.getMessage(), e );
        }

    }


    private String getFilteredData(
        String nseDailyDataFilePath,
        Set<String> stocksToLoad,
        List<String> seriesList )
    {
        StringBuilder filteredData = new StringBuilder();
        try (BufferedReader dailyDataReader =
            new BufferedReader( new FileReader( nseDailyDataFilePath ) ))
        {
            String data;
            filteredData.append( dailyDataReader.readLine() + "\n" );
            while( (data = dailyDataReader.readLine()) != null )
            {
                if( isValidData( data, stocksToLoad, seriesList ) )
                {
                    filteredData.append( data + "\n" );
                }
            }

        }
        catch( IOException e )
        {
            throw new DataUpdaterException( "Problem in reading the file : " +
                nseDailyDataFilePath + " ." + e.getMessage(), e );
        }
        return filteredData.toString();
    }


    private boolean isValidData(
        String data,
        Set<String> stocksToLoad,
        List<String> seriesList )
    {
        int firstCommaIndex = data.indexOf( ',' );
        String stockName = data.substring( 0, firstCommaIndex ).trim();

        if( !stocksToLoad.contains( stockName ) )
        {
            return false;
        }

        if( seriesList.contains( "ALL" ) )
        {
            return true;
        }

        String series =
            data
                .substring(
                    firstCommaIndex + 1,
                    data.indexOf( ',', firstCommaIndex + 1 ) ).trim();

        if( !seriesList.contains( series ) )
        {
            return false;
        }
        return true;
    }


    private Set<String> getStocksToLoadList( String stocksToLoadFile )
    {
        Set<String> stocksToLoad = new HashSet<>();
        try (BufferedReader stockListReader =
            new BufferedReader( new FileReader( stocksToLoadFile ) ))
        {
            String stockName;
            while( (stockName = stockListReader.readLine()) != null )
            {
                stocksToLoad.add( stockName );
            }
        }
        catch( IOException e )
        {
            throw new DataUpdaterException( "Problem in reading the file : " +
                stocksToLoadFile + " ." + e.getMessage(), e );
        }
        return stocksToLoad;
    }
}
