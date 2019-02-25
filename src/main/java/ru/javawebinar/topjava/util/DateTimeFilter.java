package ru.javawebinar.topjava.util;


import org.springframework.cglib.core.Local;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Predicate;

public class DateTimeFilter {
    private Comparable start = null;
    private Comparable end = null;
    public DateTimeFilter(String start, String end){
        if(start != null || end != null)
            try{
                this.start = DateAndTime.parse(start);
                this.end = DateAndTime.parse(end);
            } catch (NullPointerException e){

            }
    }
    public static Predicate<Meal> apply(DateTimeFilter f) {
        return (m -> {
            if(f.start instanceof LocalDate && f.end instanceof LocalDate)
                return DateTimeUtil.isBetweenDate(m.getDate(), (LocalDate) f.start, (LocalDate) f.end);
            else if(f.start instanceof LocalDate )
                return ((LocalDate)f.start).isBefore(m.getDate());
            else if(f.end instanceof LocalDate )
                return ((LocalDate)f.end).isAfter(m.getDate());
            else if(f.start instanceof LocalTime && f.end instanceof LocalTime)
                return DateTimeUtil.isBetween(m.getTime(), (LocalTime) f.start, (LocalTime) f.end);
            else if(f.start instanceof LocalTime )
                return ((LocalTime)f.start).isBefore(m.getTime());
            else if(f.end instanceof LocalTime )
                return ((LocalTime)f.end).isAfter(m.getTime());
            else return true;
        });
    }

    public static Predicate<MealTo> applyTimeFilter (DateTimeFilter f) {
        return (m -> {
            if(f.start instanceof LocalTime && f.end instanceof LocalTime)
                return DateTimeUtil.isBetween(m.getDateTime().toLocalTime(), (LocalTime) f.start, (LocalTime) f.end);
            else if(f.start instanceof LocalTime )
                return ((LocalTime)f.start).isBefore(m.getDateTime().toLocalTime());
            else if(f.end instanceof LocalTime )
                return ((LocalTime)f.end).isAfter(m.getDateTime().toLocalTime());
            else return true;
        });
    }

    public boolean isNull() {
        return this.start == null && this.end == null;
    }
}
