package org.example;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;

public class GenerateRandomWeather {

    private static final List<String> CITIES =
            Arrays.asList("Магадан", "Чукотка", "Питер", "Тюмень", "Новосибирск", "Якутск");
    private static final List<String> WEATHER_TYPES =
            Arrays.asList("солнечно", "облачно", "дождь", "пасмурно", "снег", "ветрено");

    public static List<Weather> generateRandomWeatherForWeek(LocalDate date) {
        Random random = new Random();
        List<Weather> weatherWeek = new ArrayList<>();

        for (int i = 0; i < CITIES.size(); i++) {
            Weather weather = new Weather();

            weather.setCity(CITIES.get(i));
            weather.setDate(String.valueOf(date.plusDays(i)));
            weather.setTemperature(random.nextInt(36));
            weather.setWeatherType(WEATHER_TYPES.get(random.nextInt(WEATHER_TYPES.size())));
        }
        return weatherWeek;
    }
}
