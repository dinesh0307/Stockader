package com.dinesh.share.market.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;


/**
 * 
 * @author Dinesh S
 *
 */
public class StockHistoryDataDaoTest
{
    private Connection connection;


    @Before
    public void setUp()
    {
        connection = EasyMock.createMock( Connection.class );
    }


    @Test
    public void testInsertData() throws SQLException
    {
        Statement statement = EasyMock.createMock( Statement.class );
        expect( statement.executeUpdate( isA( String.class ) ) ).andReturn(
            new Integer( 0 ) );
        statement.close();
        expectLastCall();
        replay( statement );

        expect( connection.createStatement() ).andReturn( statement );
        replay( connection );

        StockHistoryDataDao stockHistoryDataDao =
            new StockHistoryDataDao( connection );
        stockHistoryDataDao.insertData( "INSERT QUERY" );

        verify( statement, connection );

    }


    @Test( expected = SQLException.class )
    public void testInsertDataForSQLException() throws SQLException
    {

        expect( connection.createStatement() ).andThrow( new SQLException() );
        replay( connection );

        StockHistoryDataDao stockHistoryDataDao =
            new StockHistoryDataDao( connection );
        stockHistoryDataDao.insertData( "INSERT QUERY" );

    }
}
