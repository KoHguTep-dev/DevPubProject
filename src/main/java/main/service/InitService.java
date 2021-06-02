package main.service;

import lombok.Data;
import main.api.response.InitResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("init.yml")
public class InitService {

    private InitResponse initResponse;

    @Bean
    public InitResponse initResponse() {
        return new InitResponse();
    }

}
