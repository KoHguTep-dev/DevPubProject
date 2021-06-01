package main.api.response;

import main.model.ModerationStatus;
import main.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.TreeSet;

@Component
public class CalendarResponse {

    @Autowired
    private PostsRepository postsRepository;

    private TreeSet<Integer> years = new TreeSet<>();
    private TreeMap<String, Integer> posts = new TreeMap<>();

    public TreeSet<Integer> getYears() {
        if (years.isEmpty()) {
            getPosts();
        }
        return years;
    }

    public void setYears(TreeSet<Integer> years) {
        this.years = years;
    }

    public TreeMap<String, Integer> getPosts() {
        if (posts.isEmpty()) {
            long time = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            postsRepository.findAll().forEach(post -> {
                long postTime = post.getTime().getTime();
                if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED && time > postTime) {
                    calendar.setTime(post.getTime());
                    years.add(calendar.get(Calendar.YEAR));
                    String date = format.format(calendar.getTime());
                    posts.put(date, posts.get(date) == null ? 1 : + 1);
                }
            });
        }
        return posts;
    }

    public void setPosts(TreeMap<String, Integer> posts) {
        this.posts = posts;
    }
}
