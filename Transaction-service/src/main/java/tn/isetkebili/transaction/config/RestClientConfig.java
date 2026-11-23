package tn.isetkebili.transaction.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient accountRestClient(@Value("${services.account.url}") String url) {
        return RestClient.builder().baseUrl(url).build();
    }

    @Bean
    public RestClient notificationRestClient(@Value("${services.notification.url}") String url) {
        return RestClient.builder().baseUrl(url).build();
    }
}