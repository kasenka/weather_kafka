package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.protocol.types.Field;

import java.time.Duration;
import java.util.*;

public class WeatherConcumer {

    private static final String TOPIC = "weather";
    private final Consumer<String, String> consumer;
    private static final ObjectMapper mapper = new ObjectMapper();


    public WeatherConcumer(){
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", "demo-group-weather");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("auto.offset.reset", "earliest");

        this.consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC));
    }

    public void statistic(List<Weather> weekWeather){
        int maxRainyDays = 0;
        int maxTemperatureOnWeeek = 0;
        String maxTemperatureCity = "";
        int lowestMeanTemperatureOnWeeek = 36;

        Map<String, Map<String, Integer>> weekStatistic = new HashMap<>();

        List<String> cities = List.of("Магадан", "Чукотка", "Питер", "Тюмень", "Новосибирск", "Якутск");

        for (String city : cities) {
            Map<String, Integer> stats = new HashMap<>();
            stats.put("rainyDays", 0);
            stats.put("maxTemperature", 0);
            stats.put("temperatureSum", 0);
            weekStatistic.put(city, stats);
        }

        for(Weather weather : weekWeather){
            String city = weather.getCity();
            Map<String, Integer> stats = weekStatistic.get(city);

            if (weather.getWeatherType().equals("дождь")){
                stats.put("rainyDays", stats.get("rainyDays") + 1);
            }if (stats.get("maxTemperature") < weather.getTemperature()){
                stats.put("maxTemperature", weather.getTemperature());
            }stats.put("temperatureSum", stats.get("temperatureSum") + weather.getTemperature());
        }

        for (Map.Entry<String, Map<String, Integer>> cityInfo : weekStatistic.entrySet()) {
            if (cityInfo.getValue().get("rainyDays") > maxRainyDays){ maxRainyDays = cityInfo.getValue().get("rainyDays");}
            if (cityInfo.getValue().get("maxTemperature") > maxTemperatureOnWeeek) {
                maxTemperatureOnWeeek = cityInfo.getValue().get("maxTemperature");
                maxTemperatureCity = cityInfo.getKey();
            }if (cityInfo.getValue().get("temperatureSum") / 7 < lowestMeanTemperatureOnWeeek){
                lowestMeanTemperatureOnWeeek = cityInfo.getValue().get("temperatureSum") / 7;
            }
        }

        System.out.println("\n===== Недельная статистика по погоде =====");

        System.out.println("Город с максимальной температурой за неделю: " + maxTemperatureCity +
                " (" + maxTemperatureOnWeeek + "°C)");

        System.out.println("Наибольшее количество дождливых дней за неделю: " + maxRainyDays);

        System.out.println("Самая низкая средняя температура за неделю: " + lowestMeanTemperatureOnWeeek + "°C");

    }

    public void run() throws JsonProcessingException {
        while(true){
            try{
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for(ConsumerRecord<String, String> rec : records) {
                    List<Weather> weekWeather = mapper.
                            readValue(rec.value(), new TypeReference<List<Weather>>() {
                            });

                    System.out.println("NEW WEEK_________________"+weekWeather.size());
                    for(Weather weather : weekWeather){
                        System.out.println(weather.toString());
                    }
                    if (!weekWeather.isEmpty()) {
                        statistic(weekWeather);
                    }
                }
            }catch (Exception e){
                System.out.println("Error while reading weather data");
            }
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        WeatherConcumer weatherConcumer = new WeatherConcumer();

        weatherConcumer.run();
    }


}
