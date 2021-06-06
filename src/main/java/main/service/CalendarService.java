package main.service;

import main.api.response.CalendarResponse;
import main.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    @Autowired
    private PostsRepository postsRepository;

    public CalendarResponse calendarResponse() {
        if (CalendarResponse.postList == null) {
            CalendarResponse.postList = postsRepository.findAll();
        }
        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.getYears();
        return calendarResponse;
    }
}
