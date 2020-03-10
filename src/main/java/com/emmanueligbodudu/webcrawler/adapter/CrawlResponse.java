package com.emmanueligbodudu.webcrawler.adapter;

import java.util.Set;

/**
 * @author Emmanuel Igbodudu
 */
public class CrawlResponse {

    private String url;

    private int totalPages;

    private float durationSeconds;

    private Set<String> pages;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public Set<String> getPages() {
        return pages;
    }

    public void setPages(Set<String> pages) {
        this.pages = pages;
    }

    public float getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(float durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
