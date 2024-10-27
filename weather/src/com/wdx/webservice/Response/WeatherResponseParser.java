package com.wdx.webservice.Response;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WeatherResponseParser {

    public static String formatWeatherResponse(String xmlResponse) {
        StringBuilder formattedResponse = new StringBuilder();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new java.io.ByteArrayInputStream(xmlResponse.getBytes("utf-8")));

            NodeList weatherInfo = doc.getElementsByTagName("getWeatherResult").item(0).getChildNodes();
            for (int i = 0; i < weatherInfo.getLength(); i++) {
                String weatherDetail = weatherInfo.item(i).getTextContent();
                if (!weatherDetail.trim().isEmpty()) {
                    formattedResponse.append(weatherDetail).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "解析响应失败";
        }
        return formattedResponse.toString();
    }
}
