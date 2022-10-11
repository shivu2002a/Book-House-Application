package com.shivu.bookhouseapp.book;

import java.util.List;

public class SearchBook {

    private String title;
    private Object description;
    private String first_publish_date;
    
    private List<String> covers;
    private List<Authors> authors;
    private List<String> subjects;
    
    public SearchBook() {
        
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Object getDescription() {
        return description;
    }
    
    public void setDescription(Object description) {
        this.description = description;
    }
    
    public String getFirst_publish_date() {
        return first_publish_date;
    }

    public void setFirst_publish_date(String first_publish_date) {
        this.first_publish_date = first_publish_date;
    }
    
    public List<String> getCovers() {
        return covers;
    }

    public void setCovers(List<String> covers) {
        this.covers = covers;
    }

    public List<Authors> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Authors> authors) {
        this.authors = authors;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

}
