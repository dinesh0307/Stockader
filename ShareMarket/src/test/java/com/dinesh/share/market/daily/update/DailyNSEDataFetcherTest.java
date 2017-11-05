package com.dinesh.share.market.daily.update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.dinesh.share.market.exception.DataUpdaterException;

import static org.junit.Assert.assertEquals;


/**
 * 
 * @author Dinesh S
 *
 */
public class DailyNSEDataFetcherTest
{
    private URL dailyDataUrl;
    private String dailyDataFile;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();


    @Before
    public void setUp() throws IOException
    {
        File remoteDataFile = tempFolder.newFile( "remoteData.csv" );
        try (PrintWriter pw = new PrintWriter( remoteDataFile ))
        {
            pw
                .println( "INFY,EQ,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200" );
        }

        dailyDataUrl = new URL( "file:\\" + remoteDataFile.getAbsolutePath() );

        File localDataFile = tempFolder.newFile( "localData.csv" );
        dailyDataFile = localDataFile.getAbsolutePath();
    }


    @Test
    public void testFetchNSEData() throws FileNotFoundException, IOException
    {
        DailyNSEDataFetcher dailyNSEDataFetcher = new DailyNSEDataFetcher();
        dailyNSEDataFetcher.fetchNSEData( dailyDataUrl, dailyDataFile );

        String data = null;
        try (BufferedReader br =
            new BufferedReader( new FileReader( dailyDataFile ) ))
        {
            data = br.readLine();
        }

        assertEquals(
            "INFY,EQ,21-oct-2017,120.6,121.7,127.9,119.6,126.5,1200", data );

    }


    @Test( expected = DataUpdaterException.class )
    public void testFetchNSEDataForDataUpdaterExceptionCausedByIOException()
        throws MalformedURLException
    {
        DailyNSEDataFetcher dailyNSEDataFetcher = new DailyNSEDataFetcher();
        dailyNSEDataFetcher.fetchNSEData(
            new URL( "file:\\notexists" ), dailyDataFile );

    }
}
