package com.emmanueligbodudu.webcrawler;

import com.emmanueligbodudu.webcrawler.crawler.WebCrawler;
import com.emmanueligbodudu.webcrawler.crawler.WebCrawlerConfiguration;
import com.emmanueligbodudu.webcrawler.fetcher.JsoupHtmlLinksFetcher;
import com.emmanueligbodudu.webcrawler.fetcher.LinksFetcher;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.regex.Pattern;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Emmanuel Igbodudu
 */

class WebCrawlerTest {

    private int port = 9999;

    private String testSite = String.format("http://localhost:%d", port);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(new WireMockConfiguration().port(port));

    @BeforeEach
    void beforeEach(){
        wireMockRule.stubFor(
                get(urlPathMatching("/"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "text/html")
                                .withBody(HtmlStubs.homePage)
                        )
        );
        wireMockRule.stubFor(
                get(urlPathMatching("/contact"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "text/html")
                                .withBody(HtmlStubs.getContactPage(testSite))
                        )
        );
        wireMockRule.start();
    }

    @AfterEach
    void afterEach(){
        wireMockRule.stop();
    }


    @Test
    void testWebCrawler() throws Exception {

        WebCrawlerConfiguration crawlerConfiguration = new WebCrawlerConfiguration();

        crawlerConfiguration.setFilterPredicate(url -> {
            String URLS_REGEX =".(jpg|png|gif|zip|pdf)";

            Pattern p = Pattern.compile(URLS_REGEX);
            return url.startsWith(testSite) && !p.matcher(url).find();
        });

        crawlerConfiguration.setNoOfWorkers(3);
        crawlerConfiguration.setConnectionTimeout(30000);

        LinksFetcher linksFetcher = new JsoupHtmlLinksFetcher(crawlerConfiguration);

        WebCrawler webCrawler = new WebCrawler(testSite, crawlerConfiguration, linksFetcher);
        webCrawler.buildSiteMap();
        Set<String> siteMap = webCrawler.getAllLinks();
        assertEquals(4, siteMap.size());
    }
}
