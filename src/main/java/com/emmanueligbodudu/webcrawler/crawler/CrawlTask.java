package com.emmanueligbodudu.webcrawler.crawler;

import com.emmanueligbodudu.webcrawler.fetcher.CrawlResult;
import com.emmanueligbodudu.webcrawler.fetcher.LinksFetcher;

/**
 * @author Emmanuel Igbodudu
 */
public class CrawlTask implements Runnable {

    private final CrawlProducerConsumer producerConsumer;
    private final LinksFetcher linksFetcher;


    CrawlTask (CrawlProducerConsumer producerConsumer, LinksFetcher linksFetcher) {
        this.producerConsumer = producerConsumer;
        this.linksFetcher = linksFetcher;
    }


    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            while(true) {
                String url = producerConsumer.nextUrl();
                CrawlResult result = this.linksFetcher.getLinksFromPage(url);
                producerConsumer.report(result);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
