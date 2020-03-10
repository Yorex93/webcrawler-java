package com.emmanueligbodudu.webcrawler.fetcher;

import com.emmanueligbodudu.webcrawler.HtmlStubs;
import com.emmanueligbodudu.webcrawler.crawler.WebCrawlerConfiguration;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Emmanuel Igbodudu
 */
class JsoupHtmlLinksFetcherTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(new WireMockConfiguration().port(9999));

    @BeforeEach
    void beforeEach(){
        wireMockRule.start();
    }

    @AfterEach
    void afterEach(){
        wireMockRule.stop();
    }


    @Test
    void testParseLinks() {
        WebCrawlerConfiguration configuration = new WebCrawlerConfiguration();
        LinksFetcher linksFetcher = new JsoupHtmlLinksFetcher(configuration);

        String urlToCrawl = String.format("http://localhost:%d", 9999);

        wireMockRule.stubFor(
                get(urlPathMatching("/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody(HtmlStubs.homePage)
                )
        );


        CrawlResult crawlResult = linksFetcher.getLinksFromPage(urlToCrawl);
        assertEquals(4, crawlResult.getOutgoingLinks().size());
    }

    @Test
    void testWebCrawlerReturnsBlankOnError() {

        WebCrawlerConfiguration configuration = new WebCrawlerConfiguration();
        LinksFetcher linksFetcher = new JsoupHtmlLinksFetcher(configuration);

        String urlToCrawl = String.format("http://localhost:%d", 9999);

        wireMockRule.stubFor(
                get(urlPathMatching("/"))
                        .willReturn(aResponse()
                                .withStatus(404)
                        )
        );


        CrawlResult crawlResult = linksFetcher.getLinksFromPage(urlToCrawl);
        assertEquals(0, crawlResult.getOutgoingLinks().size());
    }

    @Test
    void testWebCrawlerWorksCorrectlyWithPredicate() {

        WebCrawlerConfiguration configuration = new WebCrawlerConfiguration();
        String urlToCrawl = String.format("http://localhost:%d", 9999);

        configuration.setFilterPredicate(url -> {
            String URLS_REGEX =".(jpg)";

            Pattern p = Pattern.compile(URLS_REGEX);
            return url.startsWith(urlToCrawl) && !p.matcher(url).find();
        });

        LinksFetcher linksFetcher = new JsoupHtmlLinksFetcher(configuration);
        wireMockRule.stubFor(
                get(urlPathMatching("/"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "text/html")
                                .withBody(HtmlStubs.homePage)
                        )
        );


        CrawlResult crawlResult = linksFetcher.getLinksFromPage(urlToCrawl);
        assertEquals(2, crawlResult.getOutgoingLinks().size());
    }

}
