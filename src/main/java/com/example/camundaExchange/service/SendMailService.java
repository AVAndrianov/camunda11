package com.example.camundaExchange.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 • Сервис для отправки электронных писем в процессе Camunda.
 • Реализует интерфейс {@link JavaDelegate} и предназначен для использования в Service Task.
 • Отправляет письмо с использованием Spring Mail.
 */
@Service("sendMailService")
@Slf4j
public class SendMailService implements JavaDelegate {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${mail.recipient}")
    private String toAddress;

    @Autowired
    public SendMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    /**
     * Выполняет логику отправки электронного письма.
     * Извлекает необходимые данные из {@link DelegateExecution}, формирует сообщение и отправляет его.
     *
     * @param execution Объект {@link DelegateExecution}, содержащий информацию о текущем выполнении процесса.
     */
    @Override
    public void execute(DelegateExecution execution) {
        try {
            String processInstanceId = execution.getProcessInstanceId();
            String subject = "Уведомление о процессе Camunda: " + processInstanceId;
            String messageBody = "Процесс Camunda с ID " + processInstanceId + " завершен или требует вашего внимания.";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(subject);
            message.setText(messageBody);

            mailSender.send(message);

            log.info("Письмо успешно отправлено на {}", toAddress);

        } catch (Exception e) {
            log.error("Ошибка при отправке письма: {}", e.getMessage(), e);
        }
    }
}
