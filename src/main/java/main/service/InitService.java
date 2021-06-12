package main.service;

import main.api.response.InitResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "init.yml", encoding = "UTF-8")
public class InitService {

    @Bean
    public InitResponse initResponse() {
        return new InitResponse();
    }

}
