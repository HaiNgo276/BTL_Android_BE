package com.example.bookshop.config.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        String CLOUD_NAME = "dnumoo4ox";
        config.put("cloud_name", CLOUD_NAME);
        String API_KEY = "871246556211856";
        config.put("api_key", API_KEY);
        String API_SECRET = "pHwLvQgSoV9sOBogBKknNoDuxZg";
        config.put("api_secret", API_SECRET);

        return new Cloudinary(config);
    }
}