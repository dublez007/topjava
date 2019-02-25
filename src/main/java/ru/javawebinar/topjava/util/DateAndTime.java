package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class DateAndTime{
    public static Comparable parse(String s) throws NullPointerException{
        Comparable result = null;
      try{
          result = LocalDate.parse(s);
      } catch (DateTimeParseException g){
          try {
              result = LocalTime.parse(s);
          } catch (DateTimeParseException f){
          };
      }


      return result;
    };

}
