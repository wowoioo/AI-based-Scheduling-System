package com.github.rayinfinite.scheduler.utils;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PublicHoliday {
    private static final String ICS_URL = "https://www.mom.gov.sg/-/media/mom/documents/employment-practices/public" +
            "-holidays/public-holidays-sg-2025.ics";
    private static final Set<Integer> parsedYears = new HashSet<>();
    private static final Map<LocalDate, String> holidays = new HashMap<>();

    private PublicHoliday() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isPublicHoliday(LocalDate date) {
        int year = date.getYear();
        if (year < 2018 || year > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("Year must be between 2018 and next year");
        }
        if (!parsedYears.contains(year)) {
            String url = ICS_URL.replace("2025", String.valueOf(year));
            try {
                parseDownloadedIcsContent(url);
                parsedYears.add(year);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return holidays.containsKey(date);
    }

    public static String details(LocalDate date) {
        return isPublicHoliday(date) ? holidays.get(date) : null;
    }

    //TODO: retry network request if failed
    private static synchronized void parseDownloadedIcsContent(String urlString) throws Exception {
        URL url = URI.create(urlString).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to download file: HTTP error code : " + responseCode);
        }

        try (InputStream inputStream = connection.getInputStream()) {
            byte[] icsContent = inputStream.readAllBytes();

            try (InputStream icsStream = new ByteArrayInputStream(icsContent)) {
                CalendarBuilder builder = new CalendarBuilder();
                Calendar calendar = builder.build(icsStream);
                for (Component component : calendar.getComponents()) {
                    if (component instanceof VEvent event) {
                        var getSummary = event.getSummary();
                        var getDateTimeStart = event.getDateTimeStart();
                        if (getSummary.isPresent() && getDateTimeStart.isPresent()) {
                            String summary = getSummary.get().getValue();
                            LocalDate start = LocalDate.from(getDateTimeStart.get().getDate());
                            holidays.put(start, summary);
                        }
//                        LocalDate end = LocalDate.from(event.getDateTimeEnd().get().getDate());
//                        System.out.println("Summary: " + summary);
//                        System.out.println("Start: " + start);
//                        System.out.println("End: " + end);
//                        System.out.println("----------------------------");
                    }
                }
            }
        }
    }
}
