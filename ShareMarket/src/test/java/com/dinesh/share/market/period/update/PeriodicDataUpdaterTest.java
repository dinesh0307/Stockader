package com.dinesh.share.market.period.update;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.dinesh.share.market.dao.DAOManager;
import com.dinesh.share.market.exception.DataUpdaterException;
import com.dinesh.share.market.parser.NSECSVParser;
import com.dinesh.share.market.update.db.PeriodicDataDBLoader;

/**
 * 
 * @author Dinesh S
 *
 */
public class PeriodicDataUpdaterTest
{

    private PeriodicDataDBLoader periodicDataDBLoader;
    private NSECSVParser nsecsvParser;
    private DAOManager daoManager;
    private PeriodicDataFetcher periodicDataFetcher;
    private PeriodicDataUpdater periodicDataUpdater;


    @Before
    public void setUp()
    {
        periodicDataDBLoader = EasyMock.createMock( PeriodicDataDBLoader.class );
        nsecsvParser = EasyMock.createMock( NSECSVParser.class );
        daoManager = EasyMock.createMock( DAOManager.class );
        periodicDataFetcher = EasyMock.createMock( PeriodicDataFetcher.class );

        periodicDataUpdater =
            new PeriodicDataUpdater(
                periodicDataDBLoader, nsecsvParser, daoManager,
                periodicDataFetcher );
    }


    @Test
    public void testFetchPeriodicData() throws IOException
    {

        periodicDataFetcher.fetchPeriodicData(
            isA( String.class ), isA( String.class ), isA( String.class ),
            isA( String.class ) );
        expectLastCall().once();
        replay( periodicDataFetcher );

        periodicDataUpdater.fetchPeriodicData(
            "stocks.txt", "24month", "periodicdata.csv", "EQ" );

        verify( periodicDataFetcher );

    }


    @Test( expected = DataUpdaterException.class )
    public void testFetchPeriodicDataForDataUpdaterExceptionCausedByIOException()
        throws IOException
    {

        periodicDataFetcher.fetchPeriodicData(
            isA( String.class ), isA( String.class ), isA( String.class ),
            isA( String.class ) );
        expectLastCall().andThrow( new IOException() );
        replay( periodicDataFetcher );

        periodicDataUpdater.fetchPeriodicData(
            "stocks.txt", "24month", "periodicdata.csv", "EQ" );

    }


    @Test
    public void testLoadPeriodicData()
    {

        periodicDataDBLoader.parseAndLoadDataInDB(
            nsecsvParser, daoManager, "periodicdata.csv" );
        expectLastCall().once();
        replay( periodicDataDBLoader );

        daoManager.close();
        expectLastCall().once();
        replay( daoManager );

        periodicDataUpdater.loadPeriodicData( "periodicdata.csv" );

        verify( daoManager );
        verify( periodicDataDBLoader );

    }
}
