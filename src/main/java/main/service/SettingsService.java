package main.service;

import main.api.response.SettingsResponse;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiUserMode(true);
        settingsResponse.setPostPreModeration(true);
        settingsResponse.setStatisticsIsPublic(true);
        return settingsResponse;
    }
}
