package com.bit.crawler.dao;

import com.bit.crawler.model.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlerArticleDao {

        /**
         *
         * @param article
         */
        void insert(Article article);

}
