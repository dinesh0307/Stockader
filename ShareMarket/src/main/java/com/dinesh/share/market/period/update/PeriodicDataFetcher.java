package com.dinesh.share.market.period.update;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.dinesh.share.market.exception.DataUpdaterException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 
 * @author Dinesh S
 *
 */
public abstract class PeriodicDataFetcher
{

    protected WebClient webClient;
    protected HtmlPage periodicDataPage;


    public PeriodicDataFetcher( String periodicURL, WebClient webClient )
    {
        this.webClient = webClient;
        periodicDataPage = getPeriodicDataPage( periodicURL );
    }


    public void fetchPeriodicData(
        String stockListFile,
        String periodRange,
        String periodicDataPath,
        String series ) throws IOException
    {

        try (BufferedReader stockListReader =
            new BufferedReader( new FileReader( stockListFile ) ))
        {
            String equitySymbol;
            while( (equitySymbol = stockListReader.readLine()) != null )
            {
                try
                {
                    fetchData(
                        equitySymbol, periodRange, series, periodicDataPath );
                }
                catch( DataUpdaterException e )
                {
                    System.out.println( e.getMessage() );
                }
            }
        }
        finally
        {
            close();
        }

    }


    private void close()
    {
        webClient.closeAllWindows();

    }


    private HtmlPage getPeriodicDataPage( String periodicURL )
    {
        try
        {
            return webClient.getPage( periodicURL );
        }
        catch( FailingHttpStatusCodeException e )
        {
            throw new DataUpdaterException( "Fetching periodic Data page " +
                " from URL -> " + periodicURL + " failed with status code : " +
                e.getStatusCode() + ". Status Message is : " +
                e.getStatusMessage() + ".", e );
        }
        catch( IOException e )
        {
            throw new DataUpdaterException( "Trouble in connecting to URL -> " +
                periodicURL + ". " + e.getMessage() );
        }
    }


    protected abstract void fetchData(
        String equitySymbol,
        String period,
        String series,
        String periodicDataFilePath );
}
