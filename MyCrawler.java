package com.bit.crawler.utils;

import com.bit.crawler.service.MyCrawlerDBService;
import com.bit.crawler.model.Article;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Set;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler{

    private static MyCrawlerDBService myCrawlerDBService;

    private static final Pattern filters = Pattern.compile(
            ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
                    "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private static String[] crawlDomains;

    public MyCrawler(MyCrawlerDBService myCrawlerDBService) {
        this.myCrawlerDBService = myCrawlerDBService;
    }

    public static void configure(String[] domain) {
        crawlDomains = domain;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if (filters.matcher(href).matches()) {
            return false;
        }
        for (String domain : crawlDomains) {
            if (href.startsWith(domain)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            String title = htmlParseData.getTitle();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            Document document = Jsoup.parse(html);
            Elements elements = document.getElementsByTag("p");
            String content = elements.text();

            Article article = new Article();
            article.setUrl(url);
            article.setTitle(title);
            article.setContent(content);
            myCrawlerDBService.saveArticle(article);
        }

        Header[] responseHeaders = page.getFetchResponseHeaders();
        if (responseHeaders != null) {logger.debug("Response headers:");

            for (Header header : responseHeaders) {
                logger.debug("\t{}: {}", header.getName(), header.getValue());
            }
        }

        logger.debug("=============");

    }

}
