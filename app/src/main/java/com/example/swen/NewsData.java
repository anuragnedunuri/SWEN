package com.example.swen;

import java.util.ArrayList;


/* Class that defines the NewsData object
 * This class can be subclassed for future implementation and scalability
 * Contains methods to return the values for title, sectionName, authorName, publishedDate and url*/

public class NewsData {

    //Declare class variables
    private String title;
    private String sectionName;
    private ArrayList<String> authorName;
    private String publishedDate;
    private String url;

    //Class constructor to instantiate objects
    NewsData(String newsTitle, String section, ArrayList<String> author, String datePublished, String webUrl) {

        title = newsTitle;
        sectionName = section;
        authorName = author;
        publishedDate = datePublished;
        url = webUrl;

    }

    //Class constructor to instantiate objects where only newsTitle, section and webURL are available
    NewsData(String newsTitle, String section, String webUrl) {

        title = newsTitle;
        sectionName = section;
        url = webUrl;

    }

    //Class constructor to instantiate objects where only newsTitle, section, authorName and webURL are available
    NewsData(String newsTitle, String section, ArrayList<String> author, String webUrl) {

        title = newsTitle;
        sectionName = section;
        url = webUrl;
        authorName = author;

    }

    //Class constructor to instantiate objects where only newsTitle, section, datePublished and webURL are available
    NewsData(String newsTitle, String section, String datePublished, String webUrl) {

        title = newsTitle;
        sectionName = section;
        url = webUrl;
        publishedDate = datePublished;
    }

    //Method to return the news title
    String getNewsTitle() {
        return title;
    }

    //Method to return the section of the news
    String getSectionName() {
        return sectionName;
    }

    //Method to return the author title
    ArrayList<String> getAuthorName() {
        return authorName;
    }

    //Method to return the published date of the news
    String getPublishedDate() {
        return publishedDate;
    }

    //Method to return the news web url
    public String getUrl() {
        return url;
    }

}
