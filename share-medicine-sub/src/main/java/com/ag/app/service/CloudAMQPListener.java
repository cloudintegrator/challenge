package com.ag.app.service;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CloudAMQPListener {
    @RabbitListener(queues = "MEDICINE.QUEUE")
    public void handleMessage(String message) {
        System.out.println("Received message: " + message);
    }
}
