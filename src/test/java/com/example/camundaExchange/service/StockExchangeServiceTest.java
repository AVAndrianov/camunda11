package com.example.camundaExchange.service;

import com.example.camundaExchange.util.URLDownloader;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StockExchangeServiceTest {

    @Mock
    private DelegateExecution execution;

    @Mock
    private URLDownloader urlDownloader;

    private StockExchangeService service;

    private final String downloadUrl = "https://test";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new StockExchangeService();
        try {
            java.lang.reflect.Field field = StockExchangeService.class.getDeclaredField("downloadUrl");
            field.setAccessible(true);
            field.set(service, downloadUrl);
        } catch (Exception e) {
            fail("Failed to set downloadUrl: " + e.getMessage());
        }
    }

    @Test
    public void testExecute_withValidData_filtersCorrectly() {
        try (MockedStatic<URLDownloader> mockedDownloader = mockStatic(URLDownloader.class)) {
            String jsonResponse = "{ \"records\": [" +
                    "{\"BlockDate\": \"\", \"INN\": \"7700000000\", \"Name\": \"Компания\"}," +
                    "{\"BlockDate\": \"\", \"INN\": \"7711111111\", \"Name\": \"ИП что-то\"}," +
                    "{\"BlockDate\": \"\", \"INN\": \"7722222222\", \"Name\": \"Компания\"}," +
                    "{\"BlockDate\": \"\", \"INN\": \"7700000000\", \"Name\": \"ИП что-то\"}" +
                    "] }";
            mockedDownloader.when(() -> URLDownloader.download(anyString())).thenReturn(jsonResponse);
            when(URLDownloader.download(downloadUrl)).thenReturn(jsonResponse);
            service.execute(execution);
            verify(execution).setVariable(eq("downloadSuccessful"), eq(true));
            verify(execution).setVariable(eq("count"), anyInt());
            verify(execution).setVariable(eq("filterMessage"), anyString());
            ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
            verify(execution).setVariable(eq("filterMessage"), jsonCaptor.capture());
            String filteredJson = jsonCaptor.getValue();
            assertNotNull(filteredJson);
            assertTrue(filteredJson.contains("\"records\""));
        }
    }

    @Test
    public void testExecute_withNullResponse_setsFalse() {
        try (MockedStatic<URLDownloader> mockedDownloader = mockStatic(URLDownloader.class)) {
            mockedDownloader.when(() -> URLDownloader.download(anyString())).thenReturn(null);
            service.execute(execution);
            verify(execution).setVariable("downloadSuccessful", false);
            verify(execution, never()).setVariable(eq("filterMessage"), anyString());
        }
    }

    @Test
    public void testExecute_withEmptyResponse_setsFalse() {
        try (MockedStatic<URLDownloader> mockedDownloader = mockStatic(URLDownloader.class)) {
            mockedDownloader.when(() -> URLDownloader.download(anyString())).thenReturn("");
            service.execute(execution);
            verify(execution).setVariable("downloadSuccessful", false);
            verify(execution, never()).setVariable(eq("filterMessage"), anyString());
        }
    }

    @Test
    public void testExecute_withInvalidJson_setsFalse() {
        try (MockedStatic<URLDownloader> mockedDownloader = mockStatic(URLDownloader.class)) {
            mockedDownloader.when(() -> URLDownloader.download(anyString())).thenReturn("invalid json");
            service.execute(execution);
            verify(execution).setVariable("downloadSuccessful", false);
        }
    }
}
