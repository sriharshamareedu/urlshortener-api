package com.example.urlshortener_api.service;

import org.springframework.stereotype.Service;
import java.net.URL;
import java.net.HttpURLConnection;

@Service
public class UrlValidator {
    public boolean isValidUrl(String url){
        if(url==null||url.trim().isEmpty()){
            return false;
        }

        try{
            URL obj=new URL(url);
            obj.toURI();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean isReachableUrl(String url){
        try{
            URL obj=new URL(url);
            HttpURLConnection connection=(HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            int responseCode=connection.getResponseCode();
            return responseCode>=200&&responseCode<400;
        }
        catch(Exception e){
            return false;
        }
    }
}
