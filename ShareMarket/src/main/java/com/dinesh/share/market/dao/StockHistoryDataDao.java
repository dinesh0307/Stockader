package com.dinesh.share.market.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Dinesh S
 *
 */
public class StockHistoryDataDao
{

    private Connection connection;


    public StockHistoryDataDao( Connection connection )
    {
        this.connection = connection;
    }


    public void insertData( String insertQuery ) throws SQLException
    {
        Statement insertStatement = null;
        try
        {
            insertStatement = connection.createStatement();
            insertStatement.executeUpdate( insertQuery );

        }
        catch( SQLException e )
        {
            throw new SQLException( "Problem during inserting data in db." +
                e.getMessage() + " . Error code: " + e.getErrorCode() +
                " . SQl State : " + e.getSQLState(), e );
        }
        finally
        {
            if( insertStatement != null )
            {
                insertStatement.close();
            }
        }
    }

}
