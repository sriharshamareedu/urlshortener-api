package com.example.urlshortener_api.model;

import jakarta.persistence.*;

@Entity
@Table(name="url_mappings")
public class UrlMapping {
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String shortCode;

    @Column(nullable=false)
    private String originalUrl;

    private int clickCount;

    public UrlMapping(){}

    public UrlMapping(String shortCode, String originalUrl){
        this.shortCode=shortCode;
        this.originalUrl=originalUrl;
        this.clickCount=0;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id=id;}

    public String getShortCode() {return shortCode;}
    public void setShortCode(String shortCode) {this.shortCode=shortCode;}

    public String getOriginalUrl() {return originalUrl;}
    public void setOriginalUrl(String originalUrl) {this.originalUrl=originalUrl;}

    public int getClickCount() {return clickCount;}
    public void setClickCount(int clickCount) {this.clickCount=clickCount;}

    public void incrementClickCount() {this.clickCount++;}
}
