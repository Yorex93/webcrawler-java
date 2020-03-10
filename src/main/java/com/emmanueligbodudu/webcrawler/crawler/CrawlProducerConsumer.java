package com.emmanueligbodudu.webcrawler.crawler;

import com.emmanueligbodudu.webcrawler.fetcher.CrawlResult;

/**
 * @author Emmanuel Igbodudu
 */
public interface CrawlProducerConsumer {
    String nextUrl() throws InterruptedException;
    void report(CrawlResult result);
}
