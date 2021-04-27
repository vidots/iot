package com.vidots.lot.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "data")
public class DeviceData {
    @Id
    private Integer id;
    private String topic;
    private String content;
}
