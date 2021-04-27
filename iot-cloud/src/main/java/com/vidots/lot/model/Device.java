package com.vidots.lot.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "device")
public class Device {
    @Id
    private Integer id;
    private String deviceKey;
    private String deviceSecret;
    private String name;
    private String properties;
}
