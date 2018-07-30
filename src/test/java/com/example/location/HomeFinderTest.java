package com.example.location;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeFinderTest {

    private HomeFinder mHomeFinder;

    @Before
    public void setup() {
        mHomeFinder = new HomeFinder();
    }

    @Test
    public void testEmptyVisitList() {
        Assert.assertNull(mHomeFinder.determineHome(null));
        Assert.assertNull(mHomeFinder.determineHome(new ArrayList<>()));
    }

    @Test
    public void testCalculateSeconds() {
        // same day
        Visit visit = new Visit();
        visit.arrival_time_local = LocalDateTime.of(LocalDate.now(), LocalTime.of(5, 0));
        visit.departure_time_local = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 0));

        visit.location = new Location(1, 2);

        Assert.assertEquals(21600, mHomeFinder.calculateTotalSeconds(visit));
    }

    @Test
    public void testCalculateSecondsMultiDays() {
        Visit visit = new Visit();
        visit.location = new Location(1, 2);
        visit.arrival_time_local = LocalDateTime.of(LocalDate.now(), LocalTime.of(5, 0));
        visit.departure_time_local = LocalDateTime.of(LocalDate.of(2018, 7, 30), LocalTime.of(5, 0));

        Assert.assertEquals(43200, mHomeFinder.calculateTotalSeconds(visit));
    }

}
