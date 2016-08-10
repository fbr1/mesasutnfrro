package com.fbr1.mesasutnfrro.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="visitedURLs")
public class VisitedURL{

    @Id
    @Column(name = "url", unique=true)
    private String url;

    public VisitedURL(String url) {
        this.url = url;
    }

    public VisitedURL() {}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
