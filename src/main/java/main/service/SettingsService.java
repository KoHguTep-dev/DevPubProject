package main.service;

import lombok.Data;
import main.api.response.SettingsResponse;
import main.model.entities.GlobalSetting;
import main.model.entities.User;
import main.repository.SettingsRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class SettingsService {

    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private UserRepository userRepository;

    public SettingsResponse settingsResponse() {
        List<String> list = new ArrayList<>();
        settingsRepository.findAll().forEach(o -> list.add(o.getValue()));
        return new SettingsResponse(list);
    }

    public void setSettings(SettingsResponse request) {
        User user = getUser();
        if (user != null && user.isModerator()) {
            List<GlobalSetting> settings = new ArrayList<>();

            boolean multiUserMode = request.isMultiUserMode();
            boolean postPreModeration = request.isPostPreModeration();
            boolean statisticsIsPublic = request.isStatisticsIsPublic();

            GlobalSetting setting1 = settingsRepository.getOne(1);
            setting1.setValue(multiUserMode ? "YES" : "NO");
            settings.add(setting1);

            GlobalSetting setting2 = settingsRepository.getOne(2);
            setting2.setValue(postPreModeration ? "YES" : "NO");
            settings.add(setting2);

            GlobalSetting setting3 = settingsRepository.getOne(3);
            setting3.setValue(statisticsIsPublic ? "YES" : "NO");
            settings.add(setting3);

            settingsRepository.saveAll(settings);
        }
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            var securityUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            return userRepository.findByEmail(securityUser.getUsername()).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

}
