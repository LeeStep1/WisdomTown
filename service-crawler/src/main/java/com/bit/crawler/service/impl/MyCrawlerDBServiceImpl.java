package com.bit.crawler.service.impl;

import com.bit.crawler.dao.CrawlerArticleDao;
import com.bit.crawler.model.Article;
import com.bit.crawler.service.MyCrawlerDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MyCrawlerDBServiceImpl implements MyCrawlerDBService {

    @Autowired
    private CrawlerArticleDao crawlerArticleDao;

    @Override
    @Transactional
    public void saveArticle(Article article) {
        crawlerArticleDao.insert(article);
    }
}
