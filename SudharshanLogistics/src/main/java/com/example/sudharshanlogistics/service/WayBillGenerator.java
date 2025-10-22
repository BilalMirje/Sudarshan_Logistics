package com.example.sudharshanlogistics.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class WayBillGenerator {

    private final Random random = new Random();

    public String generateWayBillNo() {
        int year = LocalDate.now().getYear();
        int randomNumber = 100000 + random.nextInt(900000); // 6 digit random number
        return "WB-" + year + "-" + randomNumber;
    }
}
