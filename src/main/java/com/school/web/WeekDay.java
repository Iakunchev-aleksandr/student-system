package com.school.web;

import com.school.model.Lesson;

import java.time.LocalDate;
import java.util.List;

// День в недельном расписании: дата, уроки этого дня, число событий.
public class WeekDay {
    private final LocalDate date;
    private final List<Lesson> lessons;
    private final int eventCount;

    public WeekDay(LocalDate date, List<Lesson> lessons, int eventCount) {
        this.date = date;
        this.lessons = lessons;
        this.eventCount = eventCount;
    }

    public LocalDate getDate() { return date; }
    public List<Lesson> getLessons() { return lessons; }
    public int getEventCount() { return eventCount; }
    public boolean isToday() { return date.equals(LocalDate.now()); }
}
