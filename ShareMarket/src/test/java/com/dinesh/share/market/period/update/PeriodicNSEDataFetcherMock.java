package com.dinesh.share.market.period.update;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;

/**
 * 
 * @author Dinesh S
 *
 */
public class PeriodicNSEDataFetcherMock
    extends PeriodicNSEDataFetcher
{

    private int count = 0;
    private List<DomElement> childElements;


    public PeriodicNSEDataFetcherMock(
        String periodicURL,
        WebClient webClient,
        List<DomElement> childElements )
    {
        super( periodicURL, webClient );
        this.childElements = childElements;
    }


    @Override
    protected void setSymbolTextInput(
        String equitySymbol,
        HtmlForm historyDataForm )
    {

    }


    @Override
    protected Iterable<DomElement> getChildElementsOfAjaxResponse(
        DomElement historicDataAjaxResponse )
    {
        List<DomElement> list = new ArrayList<>();
        list.add( childElements.get( count++ ) );

        return list;
    }

}
