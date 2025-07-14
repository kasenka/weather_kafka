package org.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weather {
    private String city;
    private String date;
    private int temperature;
    private String weatherType;
}
