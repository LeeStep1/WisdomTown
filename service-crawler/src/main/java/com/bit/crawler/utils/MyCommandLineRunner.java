package com.bit.crawler.utils;

import com.bit.crawler.service.MyCrawlerDBService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Value("${config.urls}")
    private String[] crawlDomains;

    @Autowired
    MyCrawlerDBService myCrawlerDBService;

    @Override
    public void run(String... args) throws Exception {
        int numberOfCrawlers = Integer.parseInt("2");
        CrawlConfig config = new CrawlConfig();
        /*
         * Since images are binary content, we need to set this parameter to
         * true to make sure they are included in the crawl.
         */
        config.setIncludeBinaryContentInCrawling(true);

        String rootFolder = "/tjzzb/党建研究/root";
        String storageFolder = "/tjzzb/党建研究/djyjhtml/";
        config.setCrawlStorageFolder(rootFolder);
        config.setCrawlStorageFolder(storageFolder);
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        for (String domain : crawlDomains) {
            controller.addSeed(domain);
        }
        MyCrawler.configure(crawlDomains);
        MyCrawlerFactory factory = new MyCrawlerFactory(myCrawlerDBService);
        controller.startNonBlocking(factory, numberOfCrawlers);
    }
}
