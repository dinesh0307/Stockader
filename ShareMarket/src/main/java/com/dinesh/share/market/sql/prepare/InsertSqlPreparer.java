package com.dinesh.share.market.sql.prepare;

/**
 * 
 * @author Dinesh S
 *
 */
public class InsertSqlPreparer
{
    private StringBuilder multiRowInsertSql;
    private String tableName;


    public InsertSqlPreparer( String tableName )
    {
        this.tableName = tableName;
        multiRowInsertSql = new StringBuilder();
        multiRowInsertSql.append( "INSERT ALL" );
    }


    public void upateRowToInsertSql(
        String companySymbol,
        String series,
        String date,
        double previousClose,
        double openPrice,
        double highPrice,
        double lowPrice,
        double closePrice,
        long volume )
    {
        multiRowInsertSql.append( " INTO " + tableName + " VALUES(" );
        multiRowInsertSql.append( "'" + companySymbol + "'," );
        multiRowInsertSql.append( "'" + series + "'," );
        multiRowInsertSql.append( "TO_DATE('" + date + "','DD-MON-YYYY')," );
        multiRowInsertSql.append( previousClose + "," );
        multiRowInsertSql.append( openPrice + "," );
        multiRowInsertSql.append( highPrice + "," );
        multiRowInsertSql.append( lowPrice + "," );
        multiRowInsertSql.append( closePrice + "," );
        multiRowInsertSql.append( volume + ")\n" );

    }


    public void updateDummySelect()
    {
        multiRowInsertSql.append( " SELECT * FROM DUAL" );
    }


    public String getPreparedInsertStatement()
    {
        return multiRowInsertSql.toString();
    }
}
