package com.emmanueligbodudu.webcrawler.crawler;

import com.emmanueligbodudu.webcrawler.fetcher.CrawlResult;
import com.emmanueligbodudu.webcrawler.fetcher.LinksFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Emmanuel Igbodudu
 */
public class WebCrawler implements CrawlProducerConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawler.class);

    private final URI baseUrl;
    private final BlockingQueue<String> queuedUrls = new LinkedBlockingQueue<>();
    private final ExecutorService crawlerQueueExecutorService;

    /**
     * Sets to hold visited urls and queued urls to prevent duplicate tasks
     */
    private final Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> queuedUrlsSet = Collections.synchronizedSet(new HashSet<>());
    private final WebCrawlerConfiguration configuration;

    /**
     * TO hold a list of currently processing links, useful for terminating executor service when it becomes zero
     */
    private final AtomicLong currentlyProcessing = new AtomicLong();

    private final LinksFetcher linksFetcher;

    private int totalLinks;

    private Duration duration = Duration.ZERO;

    private AtomicBoolean shouldContinue;

    public WebCrawler(String siteToCrawl, WebCrawlerConfiguration configuration, LinksFetcher linksFetcher) throws URISyntaxException {
        this.baseUrl = new URI(siteToCrawl);
        this.configuration = configuration;
        this.linksFetcher = linksFetcher;
        this.crawlerQueueExecutorService = Executors.newFixedThreadPool(configuration.getNoOfWorkers());
    }

    public void buildSiteMap() {
        Instant start = Instant.now();
        this.beginCrawl();
        Instant finish = Instant.now();
        duration = Duration.between(start, finish);
        this.totalLinks = this.visitedUrls.size();
        LOGGER.debug(String.format("Crawled %d links in %d seconds", totalLinks, duration.toSeconds()));
    }


    private void beginCrawl(){
        this.queuedUrls.add(baseUrl.toString());
        for (int i = 0; i < configuration.getNoOfWorkers(); i++){
            crawlerQueueExecutorService.execute(new CrawlTask(this, this.linksFetcher));
        }
        crawlerQueueExecutorService.shutdown();
        while(true) {
            if(currentlyProcessing.get() == 0 || !shouldContinue.get()) {
                break;
            }
        }
        crawlerQueueExecutorService.shutdownNow();
    }

    @Override
    public void report(CrawlResult crawlResult) {
        Set<String> foundUrls = crawlResult.getOutgoingLinks();
        if(crawlResult.isShouldBackOff()) {
            this.shouldContinue.getAndSet(false);
        }
        synchronized (this) {
            visitedUrls.add(crawlResult.getCrawledUrl());

            foundUrls.forEach(url -> {
                if(!visitedUrls.contains(url) && !queuedUrlsSet.contains(url)) {
                    queuedUrlsSet.add(url);
                    queuedUrls.add(url);
                }
            });

            LOGGER.debug(String.format("Total Visited URLS %d, Queued URLS %d", visitedUrls.size(), queuedUrls.size()));
        }

         // Decrement whenever a worker reports that it is done processing
        currentlyProcessing.decrementAndGet();
    }


    @Override
    public String nextUrl() throws InterruptedException {
        String url = queuedUrls.take();
        queuedUrlsSet.remove(url);

        // Whenever a worker takes from the queue increment processing count
        currentlyProcessing.incrementAndGet();
        return url;
    }

    public Set<String> getAllLinks() {
        return this.visitedUrls;
    }

    public int getTotalLinks() {
        return totalLinks;
    }


    public Duration getDuration() {
        return duration;
    }
}
