package com.dinesh.share.market.injector;

import com.dinesh.share.market.dao.DAOFactory;
import com.dinesh.share.market.dao.DAOManager;
import com.dinesh.share.market.parser.NSECSVParser;
import com.dinesh.share.market.period.update.PeriodicDataFetcher;
import com.dinesh.share.market.period.update.PeriodicDataUpdater;
import com.dinesh.share.market.period.update.PeriodicNSEDataFetcher;
import com.dinesh.share.market.update.db.PeriodicDataDBLoader;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * 
 * @author Dinesh S
 *
 */
public class PeriodicDataPrepareInjector
{
    public PeriodicDataUpdater getPeriodicDataUpdater( String periodicURL )
    {
        PeriodicDataDBLoader periodicDataDBLoader = new PeriodicDataDBLoader();
        NSECSVParser nsecsvParser = new NSECSVParser();
        DAOManager daoManager = DAOFactory.getDAOManager();

        disableHtmlUnitLoggers();
        WebClient webClient = getWebClient();

        PeriodicDataFetcher periodicDataFetcher =
            new PeriodicNSEDataFetcher( periodicURL, webClient );

        return new PeriodicDataUpdater(
            periodicDataDBLoader, nsecsvParser, daoManager, periodicDataFetcher );
    }


    private void disableHtmlUnitLoggers()
    {
        java.util.logging.Logger
            .getLogger( "com.gargoylesoftware.htmlunit" ).setLevel(
                java.util.logging.Level.OFF );
        java.util.logging.Logger.getLogger( "org.apache.http" ).setLevel(
            java.util.logging.Level.OFF );
    }


    private WebClient getWebClient()
    {
        WebClient webClient = new WebClient( BrowserVersion.CHROME );

        setWebClientOptions( webClient );
        return webClient;
    }


    private void setWebClientOptions( WebClient webClient )
    {
        webClient.getOptions().setJavaScriptEnabled( true );
        webClient.getOptions().setThrowExceptionOnScriptError( false );
        webClient.getOptions().setCssEnabled( false );
        webClient.setAjaxController( new NicelyResynchronizingAjaxController() );
    }
}
