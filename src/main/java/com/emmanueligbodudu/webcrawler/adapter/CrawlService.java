package com.emmanueligbodudu.webcrawler.adapter;

import com.emmanueligbodudu.webcrawler.crawler.WebCrawler;
import com.emmanueligbodudu.webcrawler.crawler.WebCrawlerConfiguration;
import com.emmanueligbodudu.webcrawler.fetcher.JsoupHtmlLinksFetcher;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.regex.Pattern;

/**
 * @author Emmanuel Igbodudu
 */

@Service
public class CrawlService {

    CrawlResponse crawl(CrawlRequestDto crawlRequestDto) throws URISyntaxException {
        WebCrawlerConfiguration configuration = new WebCrawlerConfiguration();
        String urlToCrawl = crawlRequestDto.getUrl();
        configuration.setNoOfWorkers(crawlRequestDto.getNoOfWorkers());
        configuration.setFilterPredicate(url -> {
            Pattern p = Pattern.compile(".(jpg|png|gif|zip|pdf|jpeg)");
            return url.startsWith(urlToCrawl) && !p.matcher(url).find();
        });
        WebCrawler webCrawler = new WebCrawler(crawlRequestDto.getUrl(), configuration, new JsoupHtmlLinksFetcher(configuration));

        CrawlResponse response = new CrawlResponse();
        webCrawler.buildSiteMap();

        response.setPages(webCrawler.getAllLinks());
        response.setTotalPages(webCrawler.getTotalLinks());
        response.setUrl(urlToCrawl);
        response.setDurationSeconds(webCrawler.getDuration().toSeconds());
        return response;
    }
}
