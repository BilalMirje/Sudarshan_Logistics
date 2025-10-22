package com.example.sudharshanlogistics.service;


import java.util.List;

public interface EmailService {

    void sendInvoiceEmail(List<String> recipients, String subject, String body, byte[] attachment, String attachmentName);

}