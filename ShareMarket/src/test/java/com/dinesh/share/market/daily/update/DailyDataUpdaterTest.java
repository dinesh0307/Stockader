package com.dinesh.share.market.daily.update;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.dinesh.share.market.dao.DAOManager;
import com.dinesh.share.market.exception.DataUpdaterException;
import com.dinesh.share.market.parser.NSECSVParser;
import com.dinesh.share.market.update.db.DailyDataDBLoader;

/**
 * 
 * @author Dinesh S
 *
 */
public class DailyDataUpdaterTest
{
    private DailyDataDBLoader dailyDataDBLoader;
    private DAOManager daoManager;
    private NSECSVParser nsecsvParser;
    private DailyNSEDataFetcher dailyNSEDataFetcher;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();


    @Before
    public void setUp()
    {

        createMocks();
        dailyNSEDataFetcher = new DailyNSEDataFetcher();

    }


    private void createMocks()
    {
        daoManager = EasyMock.createStrictMock( DAOManager.class );
        nsecsvParser = EasyMock.createMock( NSECSVParser.class );
        dailyDataDBLoader = EasyMock.createMock( DailyDataDBLoader.class );
    }


    @Test
    public void testFetchDailyData() throws IOException
    {
        String remoteData =
            "INFY,EQ,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200";
        File remoteFile = tempFolder.newFile( "remote_stock.txt" );
        try (PrintWriter pw = new PrintWriter( remoteFile ))
        {
            pw.println( remoteData );
        }

        URL dailyDataUrl = new URL( "file:\\" + remoteFile.getAbsolutePath() );

        File dailyDataPath = tempFolder.newFile( "dailydata.csv" );

        DailyDataUpdater dailyDataUpdater =
            new DailyDataUpdater(
                dailyDataDBLoader, nsecsvParser, daoManager,
                dailyNSEDataFetcher );

        dailyDataUpdater.fetchDailyData(
            dailyDataUrl, dailyDataPath.getAbsolutePath() );

        String data;
        try (BufferedReader br =
            new BufferedReader(
                new FileReader( dailyDataPath.getAbsolutePath() ) ))
        {
            data = br.readLine();
        }

        assertEquals( remoteData, data );

    }


    @Test( expected = DataUpdaterException.class )
    public void testFetchDailyDataWithDataUpdaterExceptionCausedByIOException()
        throws IOException
    {

        URL dailyDataUrl = new URL( "file:\\notExistingFile" );
        dailyNSEDataFetcher = EasyMock.createMock( DailyNSEDataFetcher.class );
        dailyNSEDataFetcher.fetchNSEData( dailyDataUrl, "data.csv" );
        expectLastCall().andThrow( new DataUpdaterException( "abc" ) );
        replay( dailyNSEDataFetcher );

        daoManager.close();
        expectLastCall().once();
        replay( daoManager );

        DailyDataUpdater dailyDataUpdater =
            new DailyDataUpdater(
                dailyDataDBLoader, nsecsvParser, daoManager,
                dailyNSEDataFetcher );

        dailyDataUpdater.fetchDailyData( dailyDataUrl, "data.csv" );

        verify( dailyNSEDataFetcher, daoManager );

    }


    @Test
    public void testLoadDailyData() throws SQLException
    {

        dailyDataDBLoader.parseAndLoadDataInDB(
            nsecsvParser, daoManager, "data.csv" );
        expectLastCall().once();
        replay( dailyDataDBLoader );

        daoManager.close();
        expectLastCall().once();
        replay( daoManager );

        DailyDataUpdater dailyDataUpdater =
            new DailyDataUpdater(
                dailyDataDBLoader, nsecsvParser, daoManager,
                dailyNSEDataFetcher );
        dailyDataUpdater.loadDailyData( "data.csv" );

        verify( dailyDataDBLoader, daoManager );
    }


    @Test( expected = DataUpdaterException.class )
    public void testLoadDailyDataForDataUpdaterException() throws SQLException
    {

        dailyDataDBLoader.parseAndLoadDataInDB(
            nsecsvParser, daoManager, "data.csv" );
        expectLastCall().andThrow(
            new DataUpdaterException( "could not load in db" ) );
        replay( dailyDataDBLoader );

        daoManager.close();
        expectLastCall().once();
        replay( daoManager );

        DailyDataUpdater dailyDataUpdater =
            new DailyDataUpdater(
                dailyDataDBLoader, nsecsvParser, daoManager,
                dailyNSEDataFetcher );
        dailyDataUpdater.loadDailyData( "data.csv" );

        verify( dailyDataDBLoader, daoManager );
    }


    @Test
    public void testFilterDataToLoad() throws IOException
    {
        String nseDailyData =
            "INFY,EQ,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200\n"
                + "HCLTECH,EQ,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200\n"
                + "WIPRO,BS,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200\n"
                + "TCS,BL,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200";

        File nseDailyDataFile = tempFolder.newFile( "nseDailyData.csv" );
        try (PrintWriter pw = new PrintWriter( nseDailyDataFile ))
        {
            pw.println( nseDailyData );
        }

        String stockToLoad = "INFY\n" + "WIPRO\n" + "TCS";
        File stocksToLoadFile = tempFolder.newFile( "stocksToLoad.txt" );
        try (PrintWriter pw = new PrintWriter( stocksToLoadFile ))
        {
            pw.println( stockToLoad );
        }

        File filteredNSEDailyDataFile =
            tempFolder.newFile( "filteredNseDailyData.csv" );

        List<String> seriesList = new ArrayList<>();
        seriesList.add( "EQ" );
        seriesList.add( "BS" );

        DailyDataUpdater dailyDataUpdater =
            new DailyDataUpdater(
                dailyDataDBLoader, nsecsvParser, daoManager,
                dailyNSEDataFetcher );

        dailyDataUpdater.filterDataToLoad(
            nseDailyDataFile.getAbsolutePath(),
            stocksToLoadFile.getAbsolutePath(),
            filteredNSEDailyDataFile.getAbsolutePath(), seriesList );

        List<String> filteredDataList = new ArrayList<>();
        try (BufferedReader br =
            new BufferedReader( new FileReader( filteredNSEDailyDataFile ) ))
        {
            String line;
            while( (line = br.readLine()) != null )
            {
                filteredDataList.add( line );
            }
        }

        assertTrue( filteredDataList
            .contains( "INFY,EQ,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200" ) );
        assertTrue( filteredDataList
            .contains( "WIPRO,BS,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200" ) );
        assertEquals( 2, filteredDataList.size() );

    }


    @Test
    public void testFilterDataToLoadForSereies_ALL() throws IOException
    {
        String nseDailyData =
            "INFY,EQ,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200\n"
                + "HCLTECH,EQ,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200\n"
                + "WIPRO,BS,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200\n"
                + "TCS,BL,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200";

        File nseDailyDataFile = tempFolder.newFile( "nseDailyData.csv" );
        try (PrintWriter pw = new PrintWriter( nseDailyDataFile ))
        {
            pw.println( nseDailyData );
        }

        String stockToLoad = "INFY\n" + "WIPRO\n" + "TCS";
        File stocksToLoadFile = tempFolder.newFile( "stocksToLoad.txt" );
        try (PrintWriter pw = new PrintWriter( stocksToLoadFile ))
        {
            pw.println( stockToLoad );
        }

        File filteredNSEDailyDataFile =
            tempFolder.newFile( "filteredNseDailyData.csv" );

        List<String> seriesList = new ArrayList<>();
        seriesList.add( "ALL" );

        DailyDataUpdater dailyDataUpdater =
            new DailyDataUpdater(
                dailyDataDBLoader, nsecsvParser, daoManager,
                dailyNSEDataFetcher );

        dailyDataUpdater.filterDataToLoad(
            nseDailyDataFile.getAbsolutePath(),
            stocksToLoadFile.getAbsolutePath(),
            filteredNSEDailyDataFile.getAbsolutePath(), seriesList );

        List<String> filteredDataList = new ArrayList<>();
        try (BufferedReader br =
            new BufferedReader( new FileReader( filteredNSEDailyDataFile ) ))
        {
            String line;
            while( (line = br.readLine()) != null )
            {
                filteredDataList.add( line );
            }
        }

        assertTrue( filteredDataList
            .contains( "INFY,EQ,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200" ) );
        assertTrue( filteredDataList
            .contains( "WIPRO,BS,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200" ) );
        assertTrue( filteredDataList
            .contains( "TCS,BL,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200" ) );
        assertEquals( 3, filteredDataList.size() );

    }


    @Test( expected = DataUpdaterException.class )
    public void testFilterDataToLoadForDataUpdaterExceptionCausedByIOException()
        throws IOException
    {
        daoManager.close();
        expectLastCall().once();
        replay( daoManager );

        DailyDataUpdater dailyDataUpdater =
            new DailyDataUpdater(
                dailyDataDBLoader, nsecsvParser, daoManager,
                dailyNSEDataFetcher );

        dailyDataUpdater.filterDataToLoad(
            "nsedailydata.csv", "stocksToLoad.txt", "fileterdata.csv", null );

        verify( daoManager );
    }

}
