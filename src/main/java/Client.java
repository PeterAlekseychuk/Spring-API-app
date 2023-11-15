import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import java.util.Random;

public class Client {
    public static void main(String[] args) {
        final String sensor = "Test Client Sensor";
        sensorRegistration(sensor);
        Random rn = new Random();
        double maxTemperature = 45.0;
        String measureGet = "http://localhost:8080/measurements";
        final RestTemplate restTemplate = new RestTemplate();

        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
            sendMeasurement(rn.nextDouble() * maxTemperature, rn.nextBoolean(), sensor);
        }
        String get = restTemplate.getForObject(measureGet, String.class);
        System.out.println(get);

    }
    private static void sensorRegistration(String name) {
        final String sensorUrl = "http://localhost:8080/sensors/registration";

       //
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", name);
        makePostRequestWithJson(sensorUrl, jsonData);

    }
    private static void sendMeasurement(double value, boolean raining, String sensorName) {
        final String measureAdd = "http://localhost:8080/measurements/add";
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("value", value);
        jsonData.put("raining", raining);
        jsonData.put("sensor", Map.of("name", sensorName));
        makePostRequestWithJson(measureAdd, jsonData);
    }

    private static void makePostRequestWithJson(String sensorUrl, Map<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(jsonData, httpHeaders);
        try {
            restTemplate.postForObject(sensorUrl, request, String.class);

            System.out.println("Измерение успешно отправлено на сервер!");
        } catch (HttpClientErrorException e) {
            System.out.println("ОШИБКА!");
            System.out.println(e.getMessage());
        }
    }
}
