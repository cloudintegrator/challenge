package com.ag.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class MedData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String email;
    private Date created;
    private String medicine_name;
    private Integer medicine_qty;
    private Date medicine_validity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public Integer getMedicine_qty() {
        return medicine_qty;
    }

    public void setMedicine_qty(Integer medicine_qty) {
        this.medicine_qty = medicine_qty;
    }

    public Date getMedicine_validity() {
        return medicine_validity;
    }

    public void setMedicine_validity(Date medicine_validity) {
        this.medicine_validity = medicine_validity;
    }
}
