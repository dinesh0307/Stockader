package com.dinesh.share.market.daily.update;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.dinesh.share.market.exception.DataUpdaterException;

/**
 * 
 * @author Dinesh S
 *
 */
public class DailyNSEDataFetcher
{
    public void fetchNSEData( URL dailyUpdateURL, String dailyDataFile )
    {
        copyFileFromURLToLocal( dailyUpdateURL, dailyDataFile );
    }


    private void copyFileFromURLToLocal(
        URL nseDailyDataUrl,
        String dailyDataFile )
    {
        try (InputStream in = nseDailyDataUrl.openStream())
        {
            Files.copy(
                in, new File( dailyDataFile ).toPath(),
                StandardCopyOption.REPLACE_EXISTING );
        }
        catch( IOException e )
        {
            throw new DataUpdaterException(
                "Trouble in copying NSE daily data file from URL : " +
                    nseDailyDataUrl, e );
        }
    }

}
