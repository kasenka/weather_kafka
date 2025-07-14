package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class WeatherProducer {

    private static final String TOPIC = "weather";
    private LocalDate date = LocalDate.of(2025,1,1);
    private final Producer<String, String> produser;
    private static final ObjectMapper mapper = new ObjectMapper();

    public WeatherProducer() {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.produser = new KafkaProducer<String, String>(properties);
    }

    private List<Weather> generateWeatherData() {
        List<Weather> weatherData = GenerateRandomWeather.generateRandomWeatherForWeek(date);
        this.date = date.plusDays(7);
        return weatherData;
    }

    public void sendData(){
        try {
            List<Weather> data = generateWeatherData();
            String jsonData = mapper.writeValueAsString(data);

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, jsonData);
            produser.send(record);

        }catch (Exception e){}

    }

    public void close(){
        this.produser.close();
    }

    public static void main(String[] args) throws InterruptedException {
        WeatherProducer producer = new WeatherProducer();
        while (true){
            producer.sendData();
            Thread.sleep(1000);
        }
//        producer.close();
    }

}
