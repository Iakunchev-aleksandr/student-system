package com.school.web;

import com.school.model.Lesson;

import java.time.LocalDate;
import java.util.List;

// День в недельном расписании: дата, уроки этого дня, число событий, подпись праздника.
public class WeekDay {
    private final LocalDate date;
    private final List<Lesson> lessons;
    private final int eventCount;
    private final String holiday; // название праздника/каникул или null

    public WeekDay(LocalDate date, List<Lesson> lessons, int eventCount) {
        this(date, lessons, eventCount, null);
    }

    public WeekDay(LocalDate date, List<Lesson> lessons, int eventCount, String holiday) {
        this.date = date;
        this.lessons = lessons;
        this.eventCount = eventCount;
        this.holiday = holiday;
    }

    public LocalDate getDate() { return date; }
    public List<Lesson> getLessons() { return lessons; }
    public int getEventCount() { return eventCount; }
    public String getHoliday() { return holiday; }
    public boolean isHoliday() { return holiday != null; }
    public boolean isToday() { return date.equals(LocalDate.now()); }
}
