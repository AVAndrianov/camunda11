package com.example.camundaExchange.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Класс для маппинга JSON ответа с данными об участниках торгов.
 */
public class JsonMapper implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Класс, представляющий структуру ответа API.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        public String result;
        public List<Record> records;
    }

    /**
     * Класс, представляющий структуру записи об участнике торгов.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Record implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        public String ID;
        public String Name;
        public String INN;
        public String Residence;
        public String StoreDate;
        public String BlockDate;

        @Override
        public String toString() {
            return String.format("ID: %s, Name: %s, INN: %s, Residence: %s, StoreDate: %s, BlockDate: %s", ID, Name, INN, Residence, StoreDate, BlockDate);
        }
    }
}
