package com.emmanueligbodudu.webcrawler.fetcher;

/**
 * @author Emmanuel Igbodudu
 */
public interface LinksFetcher {
    CrawlResult getLinksFromPage(String url);
}
