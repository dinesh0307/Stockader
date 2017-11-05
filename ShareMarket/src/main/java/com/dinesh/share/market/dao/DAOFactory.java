package com.dinesh.share.market.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.dinesh.share.market.exception.DataUpdaterException;
import com.dinesh.share.market.properties.PropertiesReader;

/**
 * 
 * @author Dinesh S
 *
 */
public class DAOFactory
{
    private DAOFactory()
    {

    }


    public static DAOManager getDAOManager()
    {
        Connection jdbcConnection = getJDBCConnection();
        System.out.println( "JDBC Connection created" );
        return new DAOManager( jdbcConnection );
    }


    private static Connection getJDBCConnection()

    {
        try
        {
            Class.forName( PropertiesReader.getDBDriver() );

            return DriverManager.getConnection(
                PropertiesReader.getDBConnectionURL(),
                PropertiesReader.getDBUserName(),
                PropertiesReader.getDBPassword() );
        }
        catch( ClassNotFoundException e )
        {
            throw new DataUpdaterException( "Problem with DB driver : " +
                e.getMessage(), e );
        }
        catch( SQLException e )
        {
            throw new DataUpdaterException(
                "Problem is establishing DB connection : " + e.getMessage() +
                    ". Error code : " + e.getErrorCode() + " . Sql Sate : " +
                    e.getSQLState(), e );
        }

    }
}
