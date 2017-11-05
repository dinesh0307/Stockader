package com.dinesh.share.market.exception;

/**
 * 
 * @author Dinesh S
 *
 */
public class DataUpdaterException
    extends RuntimeException
{
    private final String message;
    private final Throwable cause;
    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    public DataUpdaterException( String message )
    {
        this( message, null );
    }


    public DataUpdaterException( String message, Throwable cause )
    {
        this.message = message;
        this.cause = cause;
    }


    @Override
    public String getMessage()
    {
        return message;
    }


    @Override
    public synchronized Throwable getCause()
    {
        return cause;
    }

}
