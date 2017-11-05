package com.dinesh.share.market.period.update;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.dinesh.share.market.exception.DataUpdaterException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * 
 * @author Dinesh S
 *
 */
public class PeriodicNSEDataFetcher
    extends PeriodicDataFetcher
{

    public PeriodicNSEDataFetcher( String periodicURL, WebClient webClient )
    {
        super( periodicURL, webClient );
    }


    protected void fetchData(
        String equitySymbol,
        String period,
        String series,
        String periodicDataFilePath )
    {
        String historicData = getHistoricData( equitySymbol, period, series );

        if( historicData != null )
        {

            historicData = historicData.replace( ":", "\n" );
            historicData = historicData.replace( "\"", "" );

            writeHistoricDataToFile(
                historicData, equitySymbol, period, periodicDataFilePath );
        }

    }


    private String getHistoricData(
        String equitySymbol,
        String period,
        String series )
    {
        String historicData = null;

        manipulatePeriodicDataPage(
            equitySymbol, period, periodicDataPage, series );

        webClient.waitForBackgroundJavaScript( 10000 );

        DomElement historicDataAjaxResponse =
            periodicDataPage.getElementById( "historicalData" );
        historicData = getCSVContent( historicDataAjaxResponse, equitySymbol );

        return historicData;
    }


    private void writeHistoricDataToFile(
        String historicData,
        String equitySymbol,
        String period,
        String periodicDataFilePath )
    {
        try (BufferedWriter bw =
            new BufferedWriter( new FileWriter( getPeriodicDataFileName(
                equitySymbol, period, periodicDataFilePath ) ) ))
        {
            bw.write( historicData );
        }
        catch( IOException e )
        {
            throw new DataUpdaterException(
                "Trouble in writing periodic data to CSV file for Equity symbol : " +
                    equitySymbol + e.getMessage(), e );
        }
    }


    private String getCSVContent(
        DomElement historicDataAjaxResponse,
        String equitySymbol )
    {
        for( DomElement responseChildElement : getChildElementsOfAjaxResponse( historicDataAjaxResponse ) )
        {
            if( "csvContentDiv".equals( responseChildElement
                .getAttribute( "id" ) ) )
            {
                String csvContent = responseChildElement.getTextContent();
                if( csvContent == null )
                {
                    System.out
                        .println( "There is no periodic data content under csvContentDiv for Equity Symbol : " +
                            equitySymbol );
                }
                return csvContent;

            }
        }
        System.out.println( "There is no CSV Content for Equity symbol : " +
            equitySymbol );

        return null;
    }


    protected Iterable<DomElement> getChildElementsOfAjaxResponse(
        DomElement historicDataAjaxResponse )
    {
        return historicDataAjaxResponse.getChildElements();
    }


    private void manipulatePeriodicDataPage(
        String equitySymbol,
        String period,
        HtmlPage periodicDataPage,
        String series )
    {
        HtmlForm historyDataForm = periodicDataPage.getFormByName( "histForm" );
        setSymbolTextInput( equitySymbol, historyDataForm );

        HtmlSelect dateRangeSelect =
            historyDataForm.getSelectByName( "dateRange" );
        HtmlOption dateRangeOption = dateRangeSelect.getOptionByValue( period );
        dateRangeSelect.setSelectedAttribute( dateRangeOption, true );

        HtmlSelect seriesSelect = historyDataForm.getSelectByName( "series" );
        HtmlOption seriesOption = seriesSelect.getOptionByValue( series );
        seriesSelect.setSelectedAttribute( seriesOption, true );

        HtmlInput submitButton = historyDataForm.getElementById( "submitMe" );
        try
        {
            submitButton.click();
        }
        catch( IOException e )
        {
            throw new DataUpdaterException(
                "Trouble in submitting form to fetch periodic data for Equity Symbol : " +
                    equitySymbol + e.getMessage(), e );
        }
    }


    protected void setSymbolTextInput(
        String equitySymbol,
        HtmlForm historyDataForm )
    {
        HtmlTextInput equitySymbolTextInput =
            historyDataForm.getInputByName( "symbol" );
        equitySymbolTextInput.setValueAttribute( equitySymbol );
    }


    private String getPeriodicDataFileName(
        String equitySymbol,
        String period,
        String periodicDataFilePath )
    {

        return periodicDataFilePath + "\\PeriodicData_" + equitySymbol + "_" +
            period + ".csv";
    }

}
