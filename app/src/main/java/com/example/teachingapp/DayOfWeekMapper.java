package com.example.teachingapp;

import java.util.HashMap;
import java.util.Map;

public class DayOfWeekMapper {

    public static String mapEnglishToPolish(String englishDay) {
        Map<String, String> dayMap = new HashMap<>();
        dayMap.put("MONDAY", "Poniedziałek");
        dayMap.put("TUESDAY", "Wtorek");
        dayMap.put("WEDNESDAY", "Środa");
        dayMap.put("THURSDAY", "Czwartek");
        dayMap.put("FRIDAY", "Piątek");
        dayMap.put("SATURDAY", "Sobota");
        dayMap.put("SUNDAY", "Niedziela");

        return dayMap.getOrDefault(englishDay, "Nieznany dzień");
    }


}

