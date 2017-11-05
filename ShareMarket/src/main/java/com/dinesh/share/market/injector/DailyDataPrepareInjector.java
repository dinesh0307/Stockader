package com.dinesh.share.market.injector;

import com.dinesh.share.market.daily.update.DailyDataUpdater;
import com.dinesh.share.market.daily.update.DailyNSEDataFetcher;
import com.dinesh.share.market.dao.DAOFactory;
import com.dinesh.share.market.dao.DAOManager;
import com.dinesh.share.market.parser.NSECSVParser;
import com.dinesh.share.market.update.db.DailyDataDBLoader;

/**
 * 
 * @author Dinesh S
 *
 */
public class DailyDataPrepareInjector
{
    public DailyDataUpdater getDailyDataUpdater()
    {
        DailyDataDBLoader dailyDataDBLoader = new DailyDataDBLoader();
        NSECSVParser nsecsvParser = new NSECSVParser();
        DAOManager daoManager = DAOFactory.getDAOManager();
        DailyNSEDataFetcher dailyNSEDataFetcher = new DailyNSEDataFetcher();

        return new DailyDataUpdater(
            dailyDataDBLoader, nsecsvParser, daoManager, dailyNSEDataFetcher );
    }

}
