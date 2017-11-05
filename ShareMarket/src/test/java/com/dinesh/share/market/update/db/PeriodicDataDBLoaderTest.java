package com.dinesh.share.market.update.db;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.reset;

import com.dinesh.share.market.dao.DAOManager;
import com.dinesh.share.market.dao.StockHistoryDataDao;
import com.dinesh.share.market.exception.DataUpdaterException;
import com.dinesh.share.market.parser.NSECSVParser;
import com.dinesh.share.market.sql.prepare.InsertSqlPreparer;

/**
 * 
 * @author Dinesh S
 *
 */
public class PeriodicDataDBLoaderTest
{
    private NSECSVParser nsecsvParser;
    private DAOManager daoManager;
    private StockHistoryDataDao stockHistoryDataDao;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();


    @Before
    public void setUp() throws SQLException, IOException
    {
        stockHistoryDataDao = EasyMock.createMock( StockHistoryDataDao.class );
        stockHistoryDataDao.insertData( isA( String.class ) );
        expectLastCall().times( 2 );
        replay( stockHistoryDataDao );

        nsecsvParser = EasyMock.createMock( NSECSVParser.class );
        nsecsvParser.parseData(
            isA( File.class ), isA( InsertSqlPreparer.class ) );
        expectLastCall().times( 2 );
        replay( nsecsvParser );

        daoManager = EasyMock.createMock( DAOManager.class );
        expect( daoManager.getStockHistoryDataDao() ).andReturn(
            stockHistoryDataDao );
        replay( daoManager );

        tempFolder.newFile( "INFY_DATA.csv" );
        tempFolder.newFile( "WIPRO_DATA.csv" );

    }


    @Test
    public void testParseAndLoadDataInDB()
    {
        PeriodicDataDBLoader periodicDataDBLoader = new PeriodicDataDBLoader();
        periodicDataDBLoader.parseAndLoadDataInDB(
            nsecsvParser, daoManager, tempFolder.getRoot().getAbsolutePath() );

        verify( stockHistoryDataDao, daoManager, nsecsvParser );

    }


    @Test
    public void testParseAndLoadDataInDBForDataUpdaterException()
    {
        reset( nsecsvParser );
        nsecsvParser.parseData(
            isA( File.class ), isA( InsertSqlPreparer.class ) );
        expectLastCall()
            .andThrow( new DataUpdaterException( "could not parse" ) )
            .times( 2 );
        replay( nsecsvParser );

        reset( stockHistoryDataDao );
        replay( stockHistoryDataDao );

        PeriodicDataDBLoader periodicDataDBLoader = new PeriodicDataDBLoader();
        periodicDataDBLoader.parseAndLoadDataInDB(
            nsecsvParser, daoManager, tempFolder.getRoot().getAbsolutePath() );

        verify( stockHistoryDataDao, daoManager, nsecsvParser );

    }


    @Test
    public void testParseAndLoadDataInDBForSQLException() throws SQLException
    {

        reset( stockHistoryDataDao );
        stockHistoryDataDao.insertData( isA( String.class ) );
        expectLastCall().andThrow( new SQLException() ).times( 2 );
        replay( stockHistoryDataDao );

        PeriodicDataDBLoader periodicDataDBLoader = new PeriodicDataDBLoader();
        periodicDataDBLoader.parseAndLoadDataInDB(
            nsecsvParser, daoManager, tempFolder.getRoot().getAbsolutePath() );

        verify( stockHistoryDataDao, daoManager, nsecsvParser );

    }

}
