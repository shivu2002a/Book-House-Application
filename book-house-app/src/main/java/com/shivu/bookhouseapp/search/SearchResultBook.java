package com.shivu.bookhouseapp.search;

import java.util.List;

public class SearchResultBook {

    private String key;
    private String title;
    private List<String> author_name;
    private String first_publish_year;
    private String number_of_pages_median;
    private String cover_i;
    private List<String> first_sentence;
    private List<String> subject;

    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(List<String> author_name) {
        this.author_name = author_name;
    }

    public String getFirst_publish_year() {
        return first_publish_year;
    }

    public void setFirst_publish_year(String first_publish_year) {
        this.first_publish_year = first_publish_year;
    }

    public String getNumber_of_pages_median() {
        return number_of_pages_median;
    }

    public void setNumber_of_pages_median(String number_of_pages_median) {
        this.number_of_pages_median = number_of_pages_median;
    }

    public String getCover_i() {
        return cover_i;
    }

    public void setCover_i(String cover_i) {
        this.cover_i = cover_i;
    }

    public List<String> getFirst_sentence() {
        return first_sentence;
    }

    public void setFirst_sentence(List<String> first_sentence) {
        this.first_sentence = first_sentence;
    }

    public List<String> getSubject() {
        return subject;
    }

    public void setSubject(List<String> subject) {
        this.subject = subject;
    }

    

}
