package main.api.response;

import lombok.Setter;
import main.model.ModerationStatus;
import main.model.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class CalendarResponse {

    @Setter
    private TreeSet<Integer> years = new TreeSet<>();
    @Setter
    private TreeMap<String, Integer> posts = new TreeMap<>();

    public static List<Post> postList;

    public TreeSet<Integer> getYears() {
        if (years.isEmpty()) {
            getPosts();
        }
        return years;
    }

    public TreeMap<String, Integer> getPosts() {
        if (posts.isEmpty()) {
            long time = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            postList.forEach(post -> {
                long postTime = post.getTime().getTime();
                if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED && time > postTime) {
                    calendar.setTime(post.getTime());
                    years.add(calendar.get(Calendar.YEAR));
                    String date = format.format(calendar.getTime());
                    posts.put(date, posts.get(date) == null ? 1 : (posts.get(date) + 1));
                }
            });
        }
        return posts;
    }

}
