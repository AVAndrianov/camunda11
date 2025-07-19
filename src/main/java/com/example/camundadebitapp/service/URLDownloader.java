package com.example.camundadebitapp.service;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class URLDownloader {

    public static String download(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            InputStream inputStream = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append('\n');
                System.out.println(line);
                break;
            }
            reader.close();
            inputStream.close();

        } catch (MalformedURLException e) {
            System.err.println("Некорректный URL: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке данных: " + e.getMessage());
        }
        return result.toString();
    }
}
