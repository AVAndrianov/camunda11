package com.example.camundadebitapp.model;

import java.io.Serializable;
import java.util.List;

public class JsonMapper implements Serializable {
    private static final long serialVersionUID = 1L;

    public static class Response {
        public String result;
        public List<Record> records;
    }

    public static class Record implements Serializable{
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
