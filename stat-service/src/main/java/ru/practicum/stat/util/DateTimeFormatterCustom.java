package ru.practicum.stat.util;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeFormatterCustom {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String dateFormatToString(@NotNull LocalDateTime date) {
        return date.format(formatter);
    }

    public LocalDateTime StringToFormatDate(@NotNull String date) {
        String[] lines = date.split(" ");
        return LocalDateTime.of(LocalDate.parse(lines[0]), LocalTime.parse(lines[1]));
    }
}
