package com.wdx.webservice.test2service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WeatherForecast {

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("请输入城市代码（例如：792代表北京）：");
            String cityCode = reader.readLine().trim();

            // Optional: You can ask for user ID, or leave it empty
            String userID = ""; // 这里可以留空

            WeatherService weatherService = new WeatherService();
            String response = weatherService.getWeather(cityCode, userID);
            System.out.println("Weather Response:\n" + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
