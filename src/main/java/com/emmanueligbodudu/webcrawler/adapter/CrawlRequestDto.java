package com.emmanueligbodudu.webcrawler.adapter;

/**
 * @author Emmanuel Igbodudu
\ */
public class CrawlRequestDto {
    private String url;
    private int noOfWorkers = 50;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNoOfWorkers() {
        return noOfWorkers;
    }

    public void setNoOfWorkers(int noOfWorkers) {
        this.noOfWorkers = noOfWorkers;
    }
}
