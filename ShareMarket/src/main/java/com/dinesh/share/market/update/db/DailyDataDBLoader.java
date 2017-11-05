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
public class DailyDataDBLoader
    extends DBLoader
{

    public void parseAndLoadDataInDB(
        NSECSVParser nsecsvParser,
        DAOManager daoManager,
        String dailyDataPath )
    {
        StockHistoryDataDao stockHistoryDataDao =
            daoManager.getStockHistoryDataDao();

        InsertSqlPreparer insertSqlPreparer = getInsertSqlPreparer();

        nsecsvParser.parseData( new File( dailyDataPath ), insertSqlPreparer );

        try
        {
            loadDataInDB( stockHistoryDataDao, insertSqlPreparer );
        }
        catch( SQLException e )
        {
            throw new DataUpdaterException( e.getMessage(), e );
        }

    }

}
