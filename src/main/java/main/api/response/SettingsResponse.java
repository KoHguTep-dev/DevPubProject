package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SettingsResponse {

    @Autowired
    private SettingsRepository settingsRepository;

    @JsonProperty("MULTIUSER_MODE")
    private boolean multiUserMode;
    @JsonProperty("POST_PREMODERATION")
    private boolean postPreModeration;
    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean statisticsIsPublic;

    public boolean isMultiUserMode() {
        return multiUserMode;
    }

    public void setMultiUserMode(boolean multiUserMode) {
        this.multiUserMode = multiUserMode;
    }

    public boolean isPostPreModeration() {
        return postPreModeration;
    }

    public void setPostPreModeration(boolean postPreModeration) {
        this.postPreModeration = postPreModeration;
    }

    public boolean isStatisticsIsPublic() {
        return statisticsIsPublic;
    }

    public void setStatisticsIsPublic(boolean statisticsIsPublic) {
        this.statisticsIsPublic = statisticsIsPublic;
    }

    public void getSettings() {
        List<String> list = new ArrayList<>();
        settingsRepository.findAll().forEach(o -> list.add(o.getValue()));
        multiUserMode = parseBool(list.get(0));
        postPreModeration = parseBool(list.get(1));
        statisticsIsPublic = parseBool(list.get(2));
    }

    private boolean parseBool(String value) {
        return value.equals("YES");
    }

}
