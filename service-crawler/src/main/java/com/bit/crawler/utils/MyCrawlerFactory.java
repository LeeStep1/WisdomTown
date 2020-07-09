package com.bit.crawler.utils;

import com.bit.crawler.service.MyCrawlerDBService;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;

public class MyCrawlerFactory implements CrawlController.WebCrawlerFactory {

    @Autowired
    private MyCrawlerDBService myCrawlerDBService;

    public MyCrawlerFactory(MyCrawlerDBService myCrawlerDBService) {
        this.myCrawlerDBService = myCrawlerDBService;

    }

    @Override
    public WebCrawler newInstance() throws Exception {
        return new MyCrawler(myCrawlerDBService);
    }
}
