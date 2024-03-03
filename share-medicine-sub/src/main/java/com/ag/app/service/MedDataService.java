package com.ag.app.service;


import com.ag.app.dao.MedDataRepository;
import com.ag.app.entity.MedDataEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class MedDataService {

    @Autowired
    private MedDataRepository medDataRepository;

    public record MedDataDTO(@JsonProperty("email") String email,
                             @JsonProperty("created") Date created,
                             @JsonProperty("medicine_name") String medicine_name,
                             @JsonProperty("medicine_qty") Integer medicine_qty,
                             @JsonProperty("medicine_validity") Date medicine_validity) implements Serializable {

    }

    @RabbitListener(queues = "MEDICINE.QUEUE")
    public void handleMessage(final MedDataDTO med) {
        try {
            MedDataEntity medDataEntity = new MedDataEntity();
            medDataEntity.setEmail(med.email);
            medDataEntity.setCreated(med.created);
            medDataEntity.setMedicine_name(med.medicine_name);
            medDataEntity.setMedicine_qty(med.medicine_qty);
            medDataEntity.setMedicine_validity(med.medicine_validity);
            MedDataEntity res = medDataRepository.save(medDataEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
