package com.example.mobileshop;

import com.example.mobileshop.domain.PaymentOS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class MobileShopApplication implements WebMvcConfigurer {

    @Value("${PAYOS_CLIENT_ID}")
    private String clientId;

    @Value("${PAYOS_API_KEY}")
    private String apiKey;

    @Value("${PAYOS_CHECKSUM_KEY}")
    private String checksumKey;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600); // Max age of the CORS pre-flight request
    }

    @Bean
    public PaymentOS paymentOS() {
        return new PaymentOS(clientId, apiKey, checksumKey);
    }

    public static void main(String[] args) {
        SpringApplication.run(MobileShopApplication.class, args);
    }

}
