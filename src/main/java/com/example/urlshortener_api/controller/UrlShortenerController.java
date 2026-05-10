package com.example.urlshortener_api.controller;

import com.example.urlshortener_api.service.UrlShortenerCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UrlShortenerController{

    @Autowired
    private UrlShortenerCore shortenerCore;

    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> shortenUrl(@RequestBody String originalUrl){
        if(originalUrl==null||originalUrl.trim().isEmpty()){
            Map<String,String> error=new HashMap<>();
            error.put("error","URL cannot be empty");
            return ResponseEntity.badRequest().body(error);
        }

        if(!originalUrl.startsWith("http://")&&!originalUrl.startsWith("https://")){
            Map<String,String> error=new HashMap<>();
            error.put("error","Invalid URL format. URL must start with http:// or https://");

            return ResponseEntity.badRequest().body(error);
        }

        String shortCode=shortenerCore.shortenUrl(originalUrl);
        Map<String,String> response=new HashMap<>();
        response.put("shortUrl","http://localhost:8080/short/"+shortCode);
        response.put("originalUrl",originalUrl);
        response.put("shortCode",shortCode);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/short/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode){
        String originalUrl=shortenerCore.getOriginalUrl(shortCode);
        
        if(originalUrl==null){
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}