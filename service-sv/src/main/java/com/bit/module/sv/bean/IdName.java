package com.bit.module.sv.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdName implements Serializable {
    private Long id;
    private String name;
}
