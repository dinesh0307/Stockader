package com.dinesh.share.market.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.dinesh.share.market.exception.DataUpdaterException;
import com.dinesh.share.market.sql.prepare.InsertSqlPreparer;

/**
 * 
 * @author Dinesh S
 *
 */
public class NSECSVParser
{
    public void parseData( File csvFile, InsertSqlPreparer insertSqlPreparer )
    {
        try (BufferedReader csvReader =
            new BufferedReader( new FileReader( csvFile ) ))
        {
            String line;
            skipDataHeader( csvReader );
            while( (line = csvReader.readLine()) != null )
            {
                String[] data = line.split( "," );
                String companyCode = data[0].trim();
                String symbol = data[1].trim();
                String date = data[2].trim();
                double previousClose = Double.parseDouble( data[3].trim() );
                double openPrice = Double.parseDouble( data[4].trim() );
                double highPrice = Double.parseDouble( data[5].trim() );
                double lowPrice = Double.parseDouble( data[6].trim() );
                double closePrice = Double.parseDouble( data[8].trim() );
                long volume = Long.parseLong( data[10].trim() );

                prepareInsertStatement(
                    insertSqlPreparer, companyCode, symbol, date,
                    previousClose, openPrice, highPrice, lowPrice, closePrice,
                    volume );

            }
            insertSqlPreparer.updateDummySelect();

        }
        catch( FileNotFoundException e )
        {
            throw new DataUpdaterException( "File doesn't exists : " +
                csvFile.getName(), e );
        }
        catch( IOException e )
        {
            throw new DataUpdaterException( "problem in reading the file : " +
                csvFile.getName(), e );
        }

    }


    private void skipDataHeader( BufferedReader csvReader ) throws IOException
    {
        csvReader.readLine();
    }


    private void prepareInsertStatement(
        InsertSqlPreparer insertSqlPreparer,
        String companyCode,
        String symbol,
        String date,
        double previousClose,
        double openPrice,
        double highPrice,
        double lowPrice,
        double closePrice,
        long volume )
    {
        insertSqlPreparer.upateRowToInsertSql(
            companyCode, symbol, date, previousClose, openPrice, highPrice,
            lowPrice, closePrice, volume );
    }

}
