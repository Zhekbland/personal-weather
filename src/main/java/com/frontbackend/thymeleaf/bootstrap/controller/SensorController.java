package com.frontbackend.thymeleaf.bootstrap.controller;

import com.frontbackend.thymeleaf.bootstrap.model.Sensor;
import com.frontbackend.thymeleaf.bootstrap.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.frontbackend.thymeleaf.bootstrap.service.SensorService;

import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/")
public class SensorController {

    private final SensorService sensorService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public String list(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                       @RequestParam(value = "size", required = false, defaultValue = "10") int size, Model model) {
        model.addAttribute("sensors", sensorService.getSensorData(pageNumber, size));

        return "index";
    }

    @GetMapping("/health")
    public String health() {
       log.info("OK");
       return "health";
    }

    @GetMapping(value = "/sensor", produces = "application/json")
    public String sensorParamUpdate(@RequestParam String temp, @RequestParam String hum) {
        cacheService.updateDataMap(temp, hum);
        log.info("ADD");
        return "health";
    }

    @GetMapping("/current")
    public String getCurrent(Model model) {
        model.addAttribute("sensor", cacheService.getActualData());
        log.info("Get current");
        return "current";
    }
}
