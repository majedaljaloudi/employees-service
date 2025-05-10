package com.example.employees.controller.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IdResponse {
    private int id;

    public IdResponse(int id) {
        this.id = id;
    }

}
