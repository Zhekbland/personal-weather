package com.frontbackend.thymeleaf.bootstrap.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class Sensor {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("start_date")
    private Date startDate;
    private Long id;
    private String humidity;
    private String temperature;

}
