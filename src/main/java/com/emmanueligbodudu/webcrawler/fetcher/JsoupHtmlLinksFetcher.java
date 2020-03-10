package com.emmanueligbodudu.webcrawler.fetcher;

import com.emmanueligbodudu.webcrawler.crawler.WebCrawlerConfiguration;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Emmanuel Igbodudu
 */
public class JsoupHtmlLinksFetcher implements LinksFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsoupHtmlLinksFetcher.class);

    private final WebCrawlerConfiguration configuration;

    public JsoupHtmlLinksFetcher(WebCrawlerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public CrawlResult getLinksFromPage(String baseUrl) {
        CrawlResult crawlResult = new CrawlResult();
        crawlResult.setCrawledUrl(baseUrl);

        try {
            Document doc = Jsoup.connect(baseUrl).userAgent(configuration.getUserAgent()).timeout(configuration.getConnectionTimeout()).get();

            Elements elements = doc.select("a[href]");

            Stream<String> hrefStream = elements.stream().map(element -> {
                String href = element.attr("href");

                if (Strings.isBlank(href) || href.contains("#")) {
                    return null;
                }

                href = element.attr("abs:href");
                return href.endsWith("/") ? href.substring(0, href.length() - 1) : href;
            }).filter(Objects::nonNull);

            if (configuration.getFilterPredicate() != null) {
                hrefStream = hrefStream.filter(configuration.getFilterPredicate());
            }

            Set<String> links = hrefStream.collect(Collectors.toSet());
            crawlResult.setOutgoingLinks(links);
            return crawlResult;
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            return crawlResult;
        }
    }
}
