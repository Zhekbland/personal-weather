package com.frontbackend.thymeleaf.bootstrap.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontbackend.thymeleaf.bootstrap.model.Sensor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CacheService {

    private final Map<Long, Sensor> dataMap = new TreeMap<>(Comparator.reverseOrder());

    AtomicLong atomicLong = new AtomicLong(0);

    @Getter
    private Sensor actualData;

    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Sensor> sensors = objectMapper.readValue(getClass().getClassLoader()
                            .getResourceAsStream("sensors.json"),
                    new TypeReference<List<Sensor>>() {
                    });
            sensors.forEach(sensor -> {
                dataMap.put(atomicLong.incrementAndGet(), sensor);
                actualData = sensor;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public List<Sensor> getData() {

        List<Sensor> result = new LinkedList<>();

        for (Map.Entry<Long, Sensor> entry : dataMap.entrySet()) {
            Long id = entry.getKey();
            Sensor value = entry.getValue();
            value.setId(id);
            result.add(value);
        }

        return result;
    }

    public synchronized void updateDataMap(String temp, String hum) {
        if (dataMap.size() >= 20000) {
            dataMap.clear();
            atomicLong.set(0);
        }
        Sensor sensor = Sensor.builder()
                .id(atomicLong.incrementAndGet())
                .startDate(new Date())
                .temperature("Temp: " + temp + " C")
                .humidity("Hum: " + hum)
                .build();

        actualData = sensor;
        dataMap.put(sensor.getId(), sensor);
    }
}
