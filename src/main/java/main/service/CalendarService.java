package main.service;

import main.api.response.CalendarResponse;
import main.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CalendarService {

    @Autowired
    private PostsRepository postsRepository;

    public CalendarResponse calendarResponse() {
        List<Date> list = postsRepository.getAllPostTime(new Date());

        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.getYears(list);
        return calendarResponse;
    }
}
