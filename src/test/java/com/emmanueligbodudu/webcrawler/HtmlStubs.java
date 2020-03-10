package com.emmanueligbodudu.webcrawler;

/**
 * @author Emmanuel Igbodudu
 */
public class HtmlStubs {

    public static String homePage = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Home Stub</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<a href=\"#\">Home</a>\n" +
            "<a href=\"/about\">About</a>\n" +
            "<a href=\"/contact\">Contact</a>\n" +
            "<a href=\"contact.jpg\">Contact Image</a>\n" +
            "<a href=\"https://facebook.com\">Contact</a>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    public static String getContactPage (String baseUrl) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Contact Stub</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<a href=\""+baseUrl+"\">Home</a>\n" +
                "<a href=\""+baseUrl+"/about\">Home</a>\n" +
                "<a href=\"/deeper-link\">Deeper Link</a>\n" +
                "<a href=\"#\">Contact</a>\n" +
                "<a href=\"https://twitter.com\">Contact</a>\n" +
                "<a href=\"file.pdf\">Download File</a>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }
}
