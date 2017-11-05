package com.dinesh.share.market.update.db;

import java.io.File;
import java.sql.SQLException;

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
public class PeriodicDataDBLoader
    extends DBLoader
{
    public void parseAndLoadDataInDB(
        NSECSVParser nsecsvParser,
        DAOManager daoManager,
        String periodicDataPath )
    {

        StockHistoryDataDao stockHistoryDataDao;

        stockHistoryDataDao = daoManager.getStockHistoryDataDao();

        File periodicDataDir = new File( periodicDataPath );
        int numberOfFile = periodicDataDir.listFiles().length;
        int loadedCount = 0;
        for( File dataFile : periodicDataDir.listFiles() )
        {

            InsertSqlPreparer insertSqlPreparer = getInsertSqlPreparer();
            try
            {
                nsecsvParser.parseData( dataFile, insertSqlPreparer );

                System.out.println( "Loading file : " + dataFile.getName() +
                    " in DB" );
                long startTime = System.currentTimeMillis();

                loadDataInDB( stockHistoryDataDao, insertSqlPreparer );

                long endTime = System.currentTimeMillis();
                long timeTaken = ((endTime - startTime) / 1000) / 60;

                System.out.println( "time taken for file : " +
                    dataFile.getName() + " : " + timeTaken );

                ++loadedCount;
                System.out.println( loadedCount + " out of " + numberOfFile +
                    " loaded." );
            }
            catch( DataUpdaterException e )
            {
                System.out.println( e.getMessage() );
                System.out.println( "Processing of file : " +
                    dataFile.getName() + " is skipped" );
            }
            catch( SQLException e )
            {
                System.out
                    .println( "Problem in loading Data in DB for file : " +
                        dataFile.getName() + e.getMessage() );
            }
        }

    }

}
