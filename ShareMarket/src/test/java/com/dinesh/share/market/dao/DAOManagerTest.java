package com.dinesh.share.market.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.dinesh.share.market.exception.DataUpdaterException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.expectLastCall;

/**
 * 
 * @author Dinesh S
 *
 */
public class DAOManagerTest
{
    private Connection connection;
    private DAOManager daoManager;


    @Before
    public void setUp()
    {
        connection = EasyMock.createMock( Connection.class );
        daoManager = new DAOManager( connection );
    }


    @Test
    public void testGetStockHistoryDataDao()
    {
        StockHistoryDataDao stockHistoryDataDao =
            daoManager.getStockHistoryDataDao();
        assertNotNull( stockHistoryDataDao );

        StockHistoryDataDao stockHistoryDataDao2 =
            daoManager.getStockHistoryDataDao();
        assertEquals( stockHistoryDataDao, stockHistoryDataDao2 );
    }


    @Test
    public void testClose() throws SQLException
    {

        reset( connection );
        connection.close();
        expectLastCall();
        replay( connection );
        daoManager.close();

        verify( connection );

    }


    @Test( expected = DataUpdaterException.class )
    public void testCloseForDataUpdaterExceptionCausedBySQLException()
        throws SQLException
    {

        reset( connection );
        connection.close();
        expectLastCall().andThrow( new SQLException() );
        replay( connection );
        daoManager.close();

    }
}
