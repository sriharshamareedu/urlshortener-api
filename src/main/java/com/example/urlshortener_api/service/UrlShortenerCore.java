package com.example.urlshortener_api.service;

import com.example.urlshortener_api.model.UrlMapping;
import com.example.urlshortener_api.repository.UrlMappingRepository;
import jakarta.annotation.PostConstruct;   // ✅ Add this import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UrlShortenerCore{
    private static final Logger logger=Logger.getLogger(UrlShortenerCore.class.getName());
    
    @Autowired
    private UrlMappingRepository repository;
    
    private long counter=1000000000L;
    private static final String BASE62="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    @PostConstruct
    private void initCounter(){
        long recordCount=repository.count();
        counter=1000000000L+recordCount;
        logger.info("Counter initialized to: "+counter+" (existing records: " + recordCount + ")");
    }
    
    private String encode(long num){
        StringBuilder sb=new StringBuilder();
        while(num>0){
            sb.append(BASE62.charAt((int)(num%62)));
            num/=62;
        }
        return sb.reverse().toString();
    }
    
    public String shortenUrl(String originalUrl){
        String shortCode=encode(counter);
        
        UrlMapping mapping=new UrlMapping(shortCode, originalUrl);
        repository.save(mapping);
        
        counter++;
        logger.info("Shortened: "+originalUrl+" -> "+shortCode);
        return shortCode;
    }
    
    public String getOriginalUrl(String shortCode){
        Optional<UrlMapping> mappingOpt=repository.findByShortCode(shortCode);
        if(mappingOpt.isPresent()){
            UrlMapping mapping=mappingOpt.get();
            mapping.incrementClickCount();
            repository.save(mapping);
            
            logger.info("Redirect: "+shortCode+" -> "+mapping.getOriginalUrl()+ 
                       " (Total clicks: "+mapping.getClickCount()+")");
            return mapping.getOriginalUrl();
        }else{
            logger.warning("Short code not found: " + shortCode);
            return null;
        }
    }
    
    public int getClickCount(String shortCode){
        return repository.findByShortCode(shortCode)
                .map(UrlMapping::getClickCount)
                .orElse(0);
    }
}