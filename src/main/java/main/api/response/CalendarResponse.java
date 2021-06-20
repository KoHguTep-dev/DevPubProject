package main.api.response;

import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter @Setter
public class CalendarResponse {

    private TreeSet<Integer> years = new TreeSet<>();
    private TreeMap<String, Integer> posts = new TreeMap<>();

    public TreeSet<Integer> getYears(List<Date> list) {
        if (years.isEmpty()) {
            getPosts(list);
        }
        return years;
    }

    public TreeMap<String, Integer> getPosts(List<Date> list) {
        if (posts.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (Date d : list) {
                calendar.setTime(d);
                years.add(calendar.get(Calendar.YEAR));
                String date = format.format(calendar.getTime());
                posts.put(date, posts.get(date) == null ? 1 : (posts.get(date) + 1));
            }
        }
        return posts;
    }

}
