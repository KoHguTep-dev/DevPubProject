package main.api.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

@Getter @Setter
public class StatisticsResponse {

    private int postsCount;
    private int likesCount;
    private int dislikesCount;
    private int viewsCount;
    private long firstPublication;

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = Objects.requireNonNullElse(viewsCount, 0);
    }

    public void setFirstPublication(Timestamp firstPublication) {
        if (firstPublication == null) {
            this.firstPublication = 0;
        } else this.firstPublication = firstPublication.getTime() / 1000;
    }

}
