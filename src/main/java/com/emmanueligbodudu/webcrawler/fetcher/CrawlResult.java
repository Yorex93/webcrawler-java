package com.emmanueligbodudu.webcrawler.fetcher;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Emmanuel Igbodudu
 */
public class CrawlResult {

    private String crawledUrl;

    private Set<String> outgoingLinks = new HashSet<>();

    private boolean shouldRetry;

    private boolean shouldBackOff;

    public Set<String> getOutgoingLinks() {
        return outgoingLinks;
    }

    void setOutgoingLinks(Set<String> outgoingLinks) {
        this.outgoingLinks = outgoingLinks;
    }

    public String getCrawledUrl() {
        return crawledUrl;
    }

    public void setCrawledUrl(String crawledUrl) {
        this.crawledUrl = crawledUrl;
    }

    public boolean isShouldRetry() {
        return shouldRetry;
    }

    public void setShouldRetry(boolean shouldRetry) {
        this.shouldRetry = shouldRetry;
    }

    public boolean isShouldBackOff() {
        return shouldBackOff;
    }

    public void setShouldBackOff(boolean shouldBackOff) {
        this.shouldBackOff = shouldBackOff;
    }
}
