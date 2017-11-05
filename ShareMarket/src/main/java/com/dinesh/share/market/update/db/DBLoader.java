package com.dinesh.share.market.update.db;

import java.sql.SQLException;

import com.dinesh.share.market.dao.DAOManager;
import com.dinesh.share.market.dao.StockHistoryDataDao;
import com.dinesh.share.market.parser.NSECSVParser;
import com.dinesh.share.market.sql.prepare.InsertSqlPreparer;
import com.dinesh.share.market.sql.prepare.SQLTables;

/**
 * 
 * @author Dinesh S
 *
 */
public abstract class DBLoader
{
    public abstract void parseAndLoadDataInDB(
        NSECSVParser nsecsvParser,
        DAOManager daoManager,
        String dataPath );


    protected InsertSqlPreparer getInsertSqlPreparer()
    {
        return new InsertSqlPreparer( SQLTables.NSE_HISTORY_TABLE );
    }


    protected void loadDataInDB(
        StockHistoryDataDao stockHistoryDataDao,
        InsertSqlPreparer insertSqlPreparer ) throws SQLException
    {
        String insertQuery = insertSqlPreparer.getPreparedInsertStatement();

        stockHistoryDataDao.insertData( insertQuery );
    }
}
