package com.mattmartin.faithbible.audiosearchapi.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FaithDateTimeFormatter
{
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static LocalDate getLocalDate(String dateTimeString) {
        if (dateTimeString == null) {
            return null;
        }
        return LocalDate.parse(dateTimeString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
