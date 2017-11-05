package com.dinesh.share.market.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.dinesh.share.market.exception.DataUpdaterException;

/**
 * 
 * @author Dinesh S
 *
 */
public class DAOManager
{
    private Connection connection;
    private StockHistoryDataDao stockHistoryDataDao;


    public DAOManager( Connection connection )
    {
        this.connection = connection;
    }


    public StockHistoryDataDao getStockHistoryDataDao()
    {
        if( stockHistoryDataDao == null )
        {
            stockHistoryDataDao = new StockHistoryDataDao( connection );
        }
        return stockHistoryDataDao;
    }


    public void close()
    {
        try
        {
            connection.close();
        }
        catch( SQLException e )
        {
            throw new DataUpdaterException(
                "Problem in closing the DB connection :" + e.getMessage() +
                    ". Error code : " + e.getErrorCode() + ". Sql State : " +
                    e.getSQLState(), e );
        }
    }
}
