package com.example.location;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;

/**
 * Class to determine user's home location based on list of Visits
 */
public class HomeFinder {

    private static final long TARGET_STAY_HOURS = 30;
    private static final long TARGET_STAY_MINUTES = TARGET_STAY_HOURS * 60;
    private static final long TARGET_STAY_SECONDS = TARGET_STAY_MINUTES * 60;

    private static final int EVENING_START_TIME = 20 * 3600;
    private static final int MORNING_END_TIME = 8 * 3600;
    private static final int END_DAY = 24 * 3600;

    public static void main(String[] args) {
    }

    /**
     *
     * @param visits List of visits
     * @return Home location, null if none exists
     *
     * Assumptions:
     * + Only one home location. Returns first location with 30+ hours.
     */
    Location determineHome(List<Visit> visits) {
        HashMap<Location, Long> locationMap = new HashMap<>();

        if (visits == null)
            return null;

        for (Visit visit : visits) {
            Location location = visit.location;
            long totalSeconds = calculateTotalSeconds(visit);
            long newSeconds = locationMap.getOrDefault(location, 0L) + totalSeconds;
            if (newSeconds >= TARGET_STAY_SECONDS)
                return location;
            locationMap.put(location, newSeconds);
        }

        return null;
    }

    /**
     * Calculates the total seconds the person spent between 8pm to 8am on this visit
     * @param visit a visit
     * @return total seconds
     */
    long calculateTotalSeconds(Visit visit) {
        long total = 0;

        LocalDateTime arrival = visit.arrival_time_local;
        LocalDateTime departure = visit.departure_time_local;
        Period period = Period.between(arrival.toLocalDate(), departure.toLocalDate());

        int start = arrival.toLocalTime().toSecondOfDay();
        int end = departure.toLocalTime().toSecondOfDay();

        while (period.getDays() > 0) {
            if (start < MORNING_END_TIME)
                total += MORNING_END_TIME - start;

            total += END_DAY - Math.max(start, EVENING_START_TIME);

            period = period.minusDays(1);

            start = 0;
        }

        // need to set this again for multi-days
        start = Math.min(start, arrival.toLocalTime().toSecondOfDay());

        if (end <= MORNING_END_TIME) {
            total += end - start;
        } else {
            if (start < MORNING_END_TIME)
                total += MORNING_END_TIME - start;

            if (end > EVENING_START_TIME)
                total += end - Math.max(start, EVENING_START_TIME);
        }

        return total;
    }

}
