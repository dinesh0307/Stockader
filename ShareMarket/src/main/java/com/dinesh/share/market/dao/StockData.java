package com.dinesh.share.market.dao;

import java.util.Date;

/**
 * 
 * @author Dinesh S
 *
 */
public class StockData
{
    private String equitySymbol;
    private String series;
    private Date date;
    private double openPrice;
    private double closePrice;
    private double highPrice;
    private double lowPrice;
    private double previousClose;
    private long volume;


    public String getEquitySymbol()
    {
        return equitySymbol;
    }


    public void setEquitySymbol( String equitySymbol )
    {
        this.equitySymbol = equitySymbol;
    }


    public String getSeries()
    {
        return series;
    }


    public void setSeries( String series )
    {
        this.series = series;
    }


    public Date getDate()
    {
        return date;
    }


    public void setDate( Date date )
    {
        this.date = date;
    }


    public double getOpenPrice()
    {
        return openPrice;
    }


    public void setOpenPrice( double openPrice )
    {
        this.openPrice = openPrice;
    }


    public double getClosePrice()
    {
        return closePrice;
    }


    public void setClosePrice( double closePrice )
    {
        this.closePrice = closePrice;
    }


    public double getHighPrice()
    {
        return highPrice;
    }


    public void setHighPrice( double highPrice )
    {
        this.highPrice = highPrice;
    }


    public double getLowPrice()
    {
        return lowPrice;
    }


    public void setLowPrice( double lowPrice )
    {
        this.lowPrice = lowPrice;
    }


    public double getPreviousClose()
    {
        return previousClose;
    }


    public void setPreviousClose( double previousClose )
    {
        this.previousClose = previousClose;
    }


    public long getVolume()
    {
        return volume;
    }


    public void setVolume( long volume )
    {
        this.volume = volume;
    }
}
