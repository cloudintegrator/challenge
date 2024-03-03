package com.ag.app.service;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class CloudAMQPListener {

    public record MedDataDTO(@JsonProperty("email") String email,
                             @JsonProperty("created") Date created,
                             @JsonProperty("medicine_name") String medicine_name,
                             @JsonProperty("medicine_qty") Integer medicine_qty,
                             @JsonProperty("medicine_validity") Date medicine_validity) implements Serializable {

    }

    @RabbitListener(queues = "MEDICINE.QUEUE")
    public void handleMessage(final MedDataDTO message) {
        System.out.println("Received message: " + message);
    }


}
