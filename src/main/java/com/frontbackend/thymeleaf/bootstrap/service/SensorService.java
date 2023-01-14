package com.frontbackend.thymeleaf.bootstrap.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.frontbackend.thymeleaf.bootstrap.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Paged;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Paging;

@Service
public class SensorService {

    @Autowired
    private CacheService cacheService;

    public Paged<Sensor> getSensors(int pageNumber, int size) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Sensor> sensors = objectMapper.readValue(getClass().getClassLoader()
                                                                        .getResourceAsStream("sensors.json"),
                    new TypeReference<List<Sensor>>() {
                    });

//            List<Sensor> paged = sensors.stream()
//                                            .skip(pageNumber)
//                                            .limit(size)
//                                            .collect(Collectors.toList());

//            int totalPages = sensors.size() / size;
            int totalPages = ( (sensors.size() - 1 ) / size ) +1 ;
            int skip = pageNumber > 1 ? (pageNumber - 1) * size : 0;
            List paged = sensors.stream()
                    .skip(skip)
                    .limit(size)
                    .collect(Collectors.toList());
            return new Paged(new Page(paged, totalPages), Paging.of(totalPages, pageNumber, size));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Paged<>();
    }

    public Paged<Sensor> getSensorData(int pageNumber, int size) {

        try {

            List<Sensor> sensors = cacheService.getData();


            int totalPages = ( (sensors.size() - 1 ) / size ) +1 ;
            int skip = pageNumber > 1 ? (pageNumber - 1) * size : 0;
            List paged = sensors.stream()
                    .skip(skip)
                    .limit(size)
                    .collect(Collectors.toList());
            return new Paged(new Page(paged, totalPages), Paging.of(totalPages, pageNumber, size));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Paged<>();
    }
}
