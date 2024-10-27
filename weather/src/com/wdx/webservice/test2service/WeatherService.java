package com.wdx.webservice.test2service;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {

    private static final String SOAP_ACTION = "http://WebXml.com.cn/getWeather";
    private static final String URL = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx";

    public String getWeather(String cityCode, String userID) throws Exception {
        String soapMessage = createSoapMessage(cityCode, userID);
        System.out.println("SOAP Request: " + soapMessage);
        String response = sendRequest(soapMessage);
        return formatWeatherResponse(response);
    }

    private String createSoapMessage(String cityCode, String userID) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<getWeather xmlns=\"http://WebXml.com.cn/\">" +
                "<theCityCode>" + cityCode + "</theCityCode>" +
                "<theUserID>" + userID + "</theUserID>" +
                "</getWeather>" +
                "</soap:Body>" +
                "</soap:Envelope>";
    }

    private String sendRequest(String soapMessage) throws Exception {
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        connection.setRequestProperty("SOAPAction", SOAP_ACTION);
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(soapMessage.getBytes());
            os.flush();
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        return response.toString();
    }

    private String formatWeatherResponse(String xmlResponse) {
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
