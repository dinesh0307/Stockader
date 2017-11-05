package com.dinesh.share.market.period.update;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.dinesh.share.market.exception.DataUpdaterException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

/**
 * 
 * @author Dinesh S
 *
 */
public class PeriodicNSEDataFetcherTest
{
    private WebClient webClient;
    private HtmlPage htmlPage;
    private File stockListFile;
    private HtmlSelect dateRangeSelect;
    private HtmlSelect seriesSelect;
    private HtmlInput submitButton;
    private HtmlForm historyDataForm;
    private File periodicDataPath;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public TemporaryFolder periodicDataTempFolder = new TemporaryFolder();


    @Before
    public void setUp()
        throws FailingHttpStatusCodeException,
            MalformedURLException,
            IOException
    {
        createPeriodicDataFolder();

        createMocksAndSetExpectations();

        stockListFile = tempFolder.newFile( "stockListFile.txt" );
        try (PrintWriter pw = new PrintWriter( stockListFile ))
        {
            pw.println( "INFY" );
            pw.println( "WIPRO" );
        }

    }


    @Test
    public void testFetchPeriodicData() throws IOException
    {
        DomElement infyChild = EasyMock.createMock( DomElement.class );
        expect( infyChild.getAttribute( "id" ) ).andReturn( "csvContentDiv" );
        expect( infyChild.getTextContent() ).andReturn( "infydata" );
        replay( infyChild );

        DomElement wiproChild = EasyMock.createMock( DomElement.class );
        expect( wiproChild.getAttribute( "id" ) ).andReturn( "csvContentDiv" );
        expect( wiproChild.getTextContent() ).andReturn( "wiprodata" );
        replay( wiproChild );

        List<DomElement> childElements = new ArrayList<>();
        childElements.add( infyChild );
        childElements.add( wiproChild );

        PeriodicDataFetcher periodicDataFetcher =
            new PeriodicNSEDataFetcherMock(
                "nsePeriodicUrl", webClient, childElements );
        periodicDataFetcher.fetchPeriodicData(
            stockListFile.getAbsolutePath(), "24month",
            periodicDataPath.getAbsolutePath(), "EQ" );

        assertEquals( 2, periodicDataPath.listFiles().length );
        assertEquals(
            "PeriodicData_INFY_24month.csv",
            periodicDataPath.listFiles()[0].getName() );
        assertEquals(
            "PeriodicData_WIPRO_24month.csv",
            periodicDataPath.listFiles()[1].getName() );

        assertEquals( "infydata", getResponse( periodicDataPath.listFiles()[0] ) );

        assertEquals(
            "wiprodata", getResponse( periodicDataPath.listFiles()[1] ) );

        verify(
            webClient, htmlPage, historyDataForm, dateRangeSelect,
            seriesSelect, submitButton, infyChild, wiproChild );
    }


    @Test
    public void testFetchPeriodicDataForNoCsvContentDiv() throws IOException
    {
        DomElement infyChild = EasyMock.createMock( DomElement.class );
        expect( infyChild.getAttribute( "id" ) ).andReturn( "nocsvContentDiv" );
        replay( infyChild );

        DomElement wiproChild = EasyMock.createMock( DomElement.class );
        expect( wiproChild.getAttribute( "id" ) ).andReturn( "csvContentDiv" );
        expect( wiproChild.getTextContent() ).andReturn( "wiprodata" );
        replay( wiproChild );

        List<DomElement> childElements = new ArrayList<>();
        childElements.add( infyChild );
        childElements.add( wiproChild );

        PeriodicDataFetcher periodicDataFetcher =
            new PeriodicNSEDataFetcherMock(
                "nsePeriodicUrl", webClient, childElements );

        periodicDataFetcher.fetchPeriodicData(
            stockListFile.getAbsolutePath(), "24month",
            periodicDataPath.getAbsolutePath(), "EQ" );

        assertEquals( 1, periodicDataPath.listFiles().length );

        assertEquals(
            "PeriodicData_WIPRO_24month.csv",
            periodicDataPath.listFiles()[0].getName() );

        assertEquals(
            "wiprodata", getResponse( periodicDataPath.listFiles()[0] ) );

        verify(
            webClient, htmlPage, historyDataForm, dateRangeSelect,
            seriesSelect, submitButton, infyChild, wiproChild );
    }


    @Test
    public void testFetchPeriodicDataForNoResponseUnderCsvContentDiv()
        throws IOException
    {
        DomElement infyChild = EasyMock.createMock( DomElement.class );
        expect( infyChild.getAttribute( "id" ) ).andReturn( "csvContentDiv" );
        expect( infyChild.getTextContent() ).andReturn( null );
        replay( infyChild );

        DomElement wiproChild = EasyMock.createMock( DomElement.class );
        expect( wiproChild.getAttribute( "id" ) ).andReturn( "csvContentDiv" );
        expect( wiproChild.getTextContent() ).andReturn( "wiprodata" );
        replay( wiproChild );

        List<DomElement> childElements = new ArrayList<>();
        childElements.add( infyChild );
        childElements.add( wiproChild );

        PeriodicDataFetcher periodicDataFetcher =
            new PeriodicNSEDataFetcherMock(
                "nsePeriodicUrl", webClient, childElements );

        periodicDataFetcher.fetchPeriodicData(
            stockListFile.getAbsolutePath(), "24month",
            periodicDataPath.getAbsolutePath(), "EQ" );

        assertEquals( 1, periodicDataPath.listFiles().length );

        assertEquals(
            "PeriodicData_WIPRO_24month.csv",
            periodicDataPath.listFiles()[0].getName() );

        assertEquals(
            "wiprodata", getResponse( periodicDataPath.listFiles()[0] ) );

        verify(
            webClient, htmlPage, historyDataForm, dateRangeSelect,
            seriesSelect, submitButton, infyChild, wiproChild );
    }


    @Test
    public void testFetchPeriodicDataForDataUpdaterExceptionCausedByIOExceptionWhileSubmitForm()
        throws IOException
    {
        reset( submitButton );
        expect( submitButton.click() ).andThrow( new IOException() ).times( 2 );
        replay( submitButton );

        reset( webClient );
        expect( webClient.getPage( isA( String.class ) ) ).andReturn( htmlPage );
        webClient.closeAllWindows();
        expectLastCall().once();
        replay( webClient );

        reset( htmlPage );
        expect( htmlPage.getFormByName( "histForm" ) ).andReturn(
            historyDataForm ).times( 2 );
        replay( htmlPage );

        PeriodicDataFetcher periodicDataFetcher =
            new PeriodicNSEDataFetcherMock( "nsePeriodicUrl", webClient, null );

        periodicDataFetcher.fetchPeriodicData(
            stockListFile.getAbsolutePath(), "24month",
            periodicDataPath.getAbsolutePath(), "EQ" );

        assertEquals( 0, periodicDataPath.listFiles().length );

        verify(
            webClient, htmlPage, historyDataForm, dateRangeSelect,
            seriesSelect, submitButton );
    }


    @Test
    public void testFetchPeriodicDataForDataUpdaterExceptionCausedByIOExceptionWhileWritingIntoFile()
        throws IOException
    {
        periodicDataPath.delete();

        DomElement infyChild = EasyMock.createMock( DomElement.class );
        expect( infyChild.getAttribute( "id" ) ).andReturn( "csvContentDiv" );
        expect( infyChild.getTextContent() ).andReturn( "infydata" );
        replay( infyChild );

        DomElement wiproChild = EasyMock.createMock( DomElement.class );
        expect( wiproChild.getAttribute( "id" ) ).andReturn( "csvContentDiv" );
        expect( wiproChild.getTextContent() ).andReturn( "wiprodata" );
        replay( wiproChild );

        List<DomElement> childElements = new ArrayList<>();
        childElements.add( infyChild );
        childElements.add( wiproChild );

        PeriodicDataFetcher periodicDataFetcher =
            new PeriodicNSEDataFetcherMock(
                "nsePeriodicUrl", webClient, childElements );

        periodicDataFetcher.fetchPeriodicData(
            stockListFile.getAbsolutePath(), "24month",
            periodicDataPath.getAbsolutePath(), "EQ" );

        verify(
            webClient, htmlPage, historyDataForm, dateRangeSelect,
            seriesSelect, submitButton, infyChild, wiproChild );
    }


    @Test( expected = DataUpdaterException.class )
    public void testFetchPeriodicDataForDataUpdaterExceptionCausedByFailingHttpStatusCodeException()
        throws IOException
    {

        FailingHttpStatusCodeException exception =
            EasyMock.createMock( FailingHttpStatusCodeException.class );
        expect( exception.getStatusCode() ).andReturn( new Integer( 500 ) );
        expect( exception.getStatusMessage() ).andReturn( "Server not found" );
        expect( exception.getCause() ).andReturn( new Throwable() );
        replay( exception );

        reset( webClient );
        expect( webClient.getPage( isA( String.class ) ) ).andThrow( exception );
        replay( webClient );
        PeriodicDataFetcher periodicDataFetcher =
            new PeriodicNSEDataFetcherMock( "nsePeriodicUrl", webClient, null );

    }


    @Test( expected = DataUpdaterException.class )
    public void testFetchPeriodicDataForDataUpdaterExceptionCausedByIOException()
        throws IOException
    {

        reset( webClient );
        expect( webClient.getPage( isA( String.class ) ) ).andThrow(
            new IOException() );
        replay( webClient );
        PeriodicDataFetcher periodicDataFetcher =
            new PeriodicNSEDataFetcherMock( "nsePeriodicUrl", webClient, null );

    }


    private String getResponse( File dataFile )
        throws IOException,
            FileNotFoundException
    {
        String infyResponse;
        try (BufferedReader br =
            new BufferedReader( new FileReader( dataFile ) ))
        {
            infyResponse = br.readLine();
        }
        return infyResponse;
    }


    private void createMocksAndSetExpectations()
        throws IOException,
            MalformedURLException
    {

        createMockForDateRangeSelect();

        createMockForSeriesOptionSelect();

        createMockForSubmit();

        createMockForHistoryDataForm();

        createMockForHtmlPage();

        createMockForWebClient();
    }


    private void createMockForWebClient()
        throws IOException,
            MalformedURLException
    {
        webClient = EasyMock.createMock( WebClient.class );
        expect( webClient.getPage( isA( String.class ) ) ).andReturn( htmlPage );
        expect( webClient.waitForBackgroundJavaScript( 10000 ) ).andReturn(
            new Integer( 1 ) ).times( 2 );
        webClient.closeAllWindows();
        expectLastCall().once();
        replay( webClient );
    }


    private void createMockForHtmlPage()
    {
        htmlPage = EasyMock.createMock( HtmlPage.class );
        expect( htmlPage.getFormByName( "histForm" ) ).andReturn(
            historyDataForm ).times( 2 );
        expect( htmlPage.getElementById( "historicalData" ) ).andReturn(
            EasyMock.createMock( DomElement.class ) ).times( 2 );
        replay( htmlPage );
    }


    private void createMockForHistoryDataForm()
    {
        historyDataForm = EasyMock.createMock( HtmlForm.class );

        expect( historyDataForm.getSelectByName( "dateRange" ) ).andReturn(
            dateRangeSelect ).times( 2 );
        expect( historyDataForm.getSelectByName( "series" ) ).andReturn(
            seriesSelect ).times( 2 );
        expect( historyDataForm.getElementById( "submitMe" ) ).andReturn(
            submitButton ).times( 2 );
        replay( historyDataForm );
    }


    private void createMockForSubmit() throws IOException
    {
        submitButton = EasyMock.createMock( HtmlInput.class );
        expect( submitButton.click() ).andReturn(
            EasyMock.createMock( Page.class ) ).times( 2 );
        replay( submitButton );
    }


    private void createMockForSeriesOptionSelect()
    {
        HtmlOption seriesOption = EasyMock.createMock( HtmlOption.class );
        seriesSelect = EasyMock.createMock( HtmlSelect.class );
        expect( seriesSelect.getOptionByValue( "EQ" ) )
            .andReturn( seriesOption ).times( 2 );
        expect( seriesSelect.setSelectedAttribute( seriesOption, true ) )
            .andReturn( EasyMock.createMock( Page.class ) ).times( 2 );
        replay( seriesSelect );
    }


    private void createMockForDateRangeSelect()
    {
        HtmlOption dateRangeOption = EasyMock.createMock( HtmlOption.class );
        dateRangeSelect = EasyMock.createMock( HtmlSelect.class );
        expect( dateRangeSelect.getOptionByValue( "24month" ) ).andReturn(
            dateRangeOption ).times( 2 );
        expect( dateRangeSelect.setSelectedAttribute( dateRangeOption, true ) )
            .andReturn( EasyMock.createMock( Page.class ) ).times( 2 );
        replay( dateRangeSelect );
    }


    private void createPeriodicDataFolder() throws IOException
    {
        periodicDataPath = periodicDataTempFolder.newFolder( "PeriodicData" );
    }

}
