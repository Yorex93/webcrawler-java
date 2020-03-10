package com.emmanueligbodudu.webcrawler.adapter;

import com.emmanueligbodudu.webcrawler.HtmlStubs;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Emmanuel Igbodudu
 * @since 2020-02-19 00:30
 */
class CrawlServiceTest {

    private int port = 9999;

    private String testSite = String.format("http://localhost:%d", port);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(new WireMockConfiguration().port(port));

    @BeforeEach
    void setUp() {
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
    void tearDown() {
        wireMockRule.stop();
    }

    @Test
    void shouldCrawlWithoutError() throws Exception{
        CrawlService crawlService = new CrawlService();

        CrawlRequestDto crawlRequestDto = new CrawlRequestDto();
        crawlRequestDto.setUrl(testSite);
        crawlRequestDto.setNoOfWorkers(2);
        CrawlResponse crawlResponse = crawlService.crawl(crawlRequestDto);

        assertEquals(4, crawlResponse.getTotalPages());
        assertEquals(testSite, crawlResponse.getUrl());
        assertTrue(crawlResponse.getPages().contains(testSite + "/contact"));
        assertFalse(crawlResponse.getPages().contains(testSite + "/contact.jpg"));

    }

//    @Test
//    void shouldCrawlMonzoWithoutError() throws Exception{
//        CrawlService crawlService = new CrawlService();
//
//        CrawlRequestDto crawlRequestDto = new CrawlRequestDto();
//        crawlRequestDto.setUrl("https://monzo.com");
//        crawlRequestDto.setNoOfWorkers(200);
//        CrawlResponse crawlResponse = crawlService.crawl(crawlRequestDto);
//        assertTrue(crawlResponse.getTotalPages() > 0);
//
//    }
}
