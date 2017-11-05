package com.dinesh.share.market.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.dinesh.share.market.exception.DataUpdaterException;
import com.dinesh.share.market.sql.prepare.InsertSqlPreparer;
import com.dinesh.share.market.sql.prepare.SQLTables;

/**
 * 
 * @author Dinesh S
 *
 */
public class NSECSVParserTest
{
    private File dataFile;
    private InsertSqlPreparer insertSqlPreparer;
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();


    @Before
    public void setUp() throws IOException
    {
        dataFile = tempFolder.newFile( "data.csv" );
        try (PrintWriter pw = new PrintWriter( dataFile ))
        {
            pw
                .println( "SYMBOL,SERIES,DATE1,PREV_CLOSE,OPEN_PRICE,HIGH_PRICE,LOW_PRICE,LAST_PRICE,CLOSE_PRICE,AVG_PRICE,TTL_TRD_QNTY,TURNOVER_LACS,NO_OF_TRADES,DELIV_QTY,DELIV_PER" );

            pw
                .println( "INFY,EQ,21-oct-2017,120.6,121.7,127.9,119.6,126.1,126.5,123.5,1200,1000,555,100,20" );
        }

        insertSqlPreparer = new InsertSqlPreparer( SQLTables.NSE_HISTORY_TABLE );
    }


    @Test
    public void testParseData()
    {
        NSECSVParser nsecsvParser = new NSECSVParser();
        nsecsvParser.parseData( dataFile, insertSqlPreparer );

        String expectedInsertQuery =
            "INSERT ALL INTO HISTORICAL_DATA_TAB_NSE VALUES('INFY','EQ',TO_DATE('21-oct-2017','DD-MON-YYYY'),120.6,121.7,127.9,119.6,126.5,1200)\n"
                + " SELECT * FROM DUAL";
        String insertQuery = insertSqlPreparer.getPreparedInsertStatement();

        assertEquals( expectedInsertQuery, insertQuery );
    }


    @Test( expected = DataUpdaterException.class )
    public void testParseDataForDataUpdaterExceptionCausedByFileNotFoundException()
    {
        dataFile.delete();
        NSECSVParser nsecsvParser = new NSECSVParser();
        nsecsvParser.parseData( dataFile, insertSqlPreparer );

    }
}
