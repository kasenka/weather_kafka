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
            for (int j = 0; j < 7; j++) {
                Weather weather = new Weather();

                weather.setCity(CITIES.get(i));
                weather.setDate(String.valueOf(date.plusDays(j)));
                weather.setTemperature(random.nextInt(36));
                weather.setWeatherType(WEATHER_TYPES.get(random.nextInt(WEATHER_TYPES.size())));

                weatherWeek.add(weather);
            }
        }
        return weatherWeek;
    }

//    public static void main(String[] args) {
//        for (Weather weather: generateRandomWeatherForWeek(LocalDate.now())){
//            System.out.println(weather.toString());
//        };
//    }
}
