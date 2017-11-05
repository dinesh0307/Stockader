package com.dinesh.share.market.update.db;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.sql.SQLException;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.dinesh.share.market.dao.DAOManager;
import com.dinesh.share.market.dao.StockHistoryDataDao;
import com.dinesh.share.market.exception.DataUpdaterException;
import com.dinesh.share.market.parser.NSECSVParser;
import com.dinesh.share.market.sql.prepare.InsertSqlPreparer;
import com.dinesh.share.market.update.db.DBLoader;

/**
 * 
 * @author Dinesh S
 *
 */
public class DailyDataDBLoaderTest
{
    private DAOManager daoManager;
    private NSECSVParser nseCSVParser;
    private StockHistoryDataDao stockHistoryDataDao;


    @Before
    public void setUp() throws SQLException
    {
        stockHistoryDataDao = EasyMock.createMock( StockHistoryDataDao.class );
        stockHistoryDataDao.insertData( isA( String.class ) );
        expectLastCall().once();
        replay( stockHistoryDataDao );

        daoManager = EasyMock.createMock( DAOManager.class );
        expect( daoManager.getStockHistoryDataDao() ).andReturn(
            stockHistoryDataDao ).once();
        replay( daoManager );

        nseCSVParser = EasyMock.createMock( NSECSVParser.class );
        nseCSVParser.parseData(
            isA( File.class ), isA( InsertSqlPreparer.class ) );
        expectLastCall().once();
        replay( nseCSVParser );

    }


    @Test
    public void testParseAndLoadDataInDB()
    {
        DBLoader dailyDataDbLoader = new DailyDataDBLoader();
        dailyDataDbLoader.parseAndLoadDataInDB(
            nseCSVParser, daoManager, "dailyData.csv" );

        verify( stockHistoryDataDao, daoManager, nseCSVParser );
    }


    @Test( expected = DataUpdaterException.class )
    public void testParseAndLoadDataInDBForDataUpdaterExceptionCausedBySQLException()
        throws SQLException
    {
        reset( stockHistoryDataDao );
        stockHistoryDataDao.insertData( isA( String.class ) );
        expectLastCall().andThrow( new SQLException() );
        replay( stockHistoryDataDao );

        DBLoader dailyDataDbLoader = new DailyDataDBLoader();
        dailyDataDbLoader.parseAndLoadDataInDB(
            nseCSVParser, daoManager, "dailyData.csv" );

    }
}
