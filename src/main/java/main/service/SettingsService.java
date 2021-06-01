package main.service;

import lombok.Data;
import main.api.response.SettingsResponse;
import main.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class SettingsService {

    @Autowired
    private SettingsRepository settingsRepository;
    private SettingsResponse settingsResponse;

    @Bean
    public SettingsResponse settingsResponse() {
        List<String> list = new ArrayList<>();
        settingsRepository.findAll().forEach(o -> list.add(o.getValue()));
        return new SettingsResponse(list);
    }

}
