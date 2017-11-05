package com.dinesh.share.market.sql.prepare;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * 
 * @author Dinesh S
 *
 */
public class InsertSqlPreparerTest
{

    @Test
    public void testInsertSQLPreparerForSingleRow()
    {
        InsertSqlPreparer insertSqlPreparer =
            new InsertSqlPreparer( SQLTables.NSE_HISTORY_TABLE );
        insertSqlPreparer.upateRowToInsertSql(
            "INFY", "EQ", "21-OCT-2017", 180, 190, 194, 187, 192, 1000 );
        insertSqlPreparer.updateDummySelect();

        String expectedQuery =
            "INSERT ALL INTO HISTORICAL_DATA_TAB_NSE VALUES('INFY','EQ',TO_DATE('21-OCT-2017','DD-MON-YYYY'),180.0,190.0,194.0,187.0,192.0,1000)\n"
                + " SELECT * FROM DUAL";
        assertEquals(
            expectedQuery, insertSqlPreparer.getPreparedInsertStatement() );
    }


    @Test
    public void testInsertSQLPreparerForMultipleRows()
    {
        InsertSqlPreparer insertSqlPreparer =
            new InsertSqlPreparer( SQLTables.NSE_HISTORY_TABLE );
        insertSqlPreparer.upateRowToInsertSql(
            "INFY", "EQ", "21-OCT-2017", 180, 190, 194, 187, 192, 1000 );
        insertSqlPreparer.upateRowToInsertSql(
            "WIPRO", "EQ", "21-OCT-2017", 180.9, 190.1, 194.3, 187.5, 192.2,
            1000 );
        insertSqlPreparer.updateDummySelect();

        String expectedQuery =
            "INSERT ALL INTO HISTORICAL_DATA_TAB_NSE VALUES('INFY','EQ',TO_DATE('21-OCT-2017','DD-MON-YYYY'),180.0,190.0,194.0,187.0,192.0,1000)\n"
                + " INTO HISTORICAL_DATA_TAB_NSE VALUES('WIPRO','EQ',TO_DATE('21-OCT-2017','DD-MON-YYYY'),180.9,190.1,194.3,187.5,192.2,1000)\n"
                + " SELECT * FROM DUAL";
        assertEquals(
            expectedQuery, insertSqlPreparer.getPreparedInsertStatement() );
    }
}
