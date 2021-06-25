package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class SettingsResponse {

    @JsonProperty("MULTIUSER_MODE")
    private boolean multiUserMode;
    @JsonProperty("POST_PREMODERATION")
    private boolean postPreModeration;
    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean statisticsIsPublic;

    public SettingsResponse() {}

    public SettingsResponse(List<String> list) {
        multiUserMode = parseBool(list.get(0));
        postPreModeration = parseBool(list.get(1));
        statisticsIsPublic = parseBool(list.get(2));
    }

    private boolean parseBool(String value) {
        return value.equals("YES");
    }

}
