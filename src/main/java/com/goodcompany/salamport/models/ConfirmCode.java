package com.goodcompany.salamport.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ConfirmCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int code;

    public ConfirmCode() {
    }

    public ConfirmCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
