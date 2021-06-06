package main.service;

import lombok.Data;
import main.api.response.InitResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class InitService {

    @Bean
    public InitResponse initResponse() {
        return new InitResponse();
    }

}
