package com.example.camundadebitapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class JsonToQueryString {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String json = "{\n" +
                "  \"result\": \"OK\",\n" +
                "  \"records\": [\n" +
                "    {\n" +
                "      \"ID\": \"0\",\n" +
                "      \"Name\": \"Компания не определена\",\n" +
                "      \"INN\": \"Не определен\",\n" +
                "      \"Residence\": \"Не определен\",\n" +
                "      \"StoreDate\": \"2000-01-01T00:00:00\",\n" +
                "      \"BlockDate\": \"2000-01-01T00:00:00\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ID\": \"1\",\n" +
                "      \"Name\": \"СП Урожай ОООМосква\",\n" +
                "      \"INN\": \"0205008145\",\n" +
                "      \"Residence\": \"Резидент РФ\",\n" +
                "      \"StoreDate\": \"2021-01-01T00:00:00\",\n" +
                "      \"BlockDate\": \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        String encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.toString());
        String queryString = encodedJson;

        System.out.println(queryString);
    }
}
