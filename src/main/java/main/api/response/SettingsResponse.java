package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsResponse {

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
}
