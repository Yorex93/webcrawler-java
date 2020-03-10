package com.emmanueligbodudu.webcrawler.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Set;

/**
 * @author Emmanuel Igbodudu
 * @since 2020-02-18 22:11
 */

@Controller()
public class WebCrawlerWebController {

    private final CrawlService crawlService;

    @Autowired
    public WebCrawlerWebController(CrawlService crawlService) {
        this.crawlService = crawlService;
    }

    @RequestMapping("")
    public ModelAndView index() {
        return new ModelAndView("index", "crawlRequest", new CrawlRequestDto());
    }

    @PostMapping(value = "/")
    public String crawl(@ModelAttribute CrawlRequestDto crawlRequest, Model model) {
        try {
            CrawlResponse response = crawlService.crawl(crawlRequest);
            model.addAttribute("response", response);
        } catch (Exception e) {
            model.addAttribute("errors", Collections.singletonList(e.getMessage()));
        }
        return "result";
    }

}
