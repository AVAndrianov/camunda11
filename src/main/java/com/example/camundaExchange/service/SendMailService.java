package com.example.camundaExchange.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

/**
 • Сервис для отправки электронных писем в процессе Camunda.
 • Реализует интерфейс {@link JavaDelegate} и предназначен для использования в Service Task.
 • В текущей реализации только выводит сообщение в консоль, имитируя отправку письма.
 */
@Service("sendMailService")
@Slf4j
public class SendMailService implements JavaDelegate {

    /**
     * Выполняет логику отправки электронного письма.
     * В текущей реализации только выводит сообщение "Письмо отправлено" в консоль.
     * В реальной реализации здесь должен быть код для отправки электронного письма
     * с использованием, например, JavaMail API или Spring Mail.
     *
     * @param execution Объект {@link DelegateExecution}, содержащий информацию о текущем выполнении процесса.
     */
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("Письмо отправлено");
    }
}
