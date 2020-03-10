package com.emmanueligbodudu.webcrawler.crawler;

import java.util.function.Predicate;

/**
 * @author Emmanuel Igbodudu
 */
public class WebCrawlerConfiguration {

    private int noOfWorkers = 20;

    private Predicate<String> filterPredicate;

    /**
     * Connection Timeout in MilliSeconds
     */
    private int connectionTimeout = 20000;
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";


    public int getNoOfWorkers() {
        return noOfWorkers;
    }

    public void setNoOfWorkers(int noOfWorkers) {
        this.noOfWorkers = noOfWorkers;
    }

    public Predicate<String> getFilterPredicate() {
        return filterPredicate;
    }

    public void setFilterPredicate(Predicate<String> filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
