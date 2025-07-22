package com.example.camundaExchange.util;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 • Утилитарный класс для загрузки данных с URL.
 */
@Component
public class URLDownloader {

    /**
     * Загружает данные с указанного URL и возвращает их в виде строки.
     *
     * @param urlString URL для загрузки данных.
     * @return Строка, содержащая данные, загруженные с URL, или null в случае ошибки.
     * @throws IOException В случае ошибок при подключении или чтении данных с URL.
     */
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
