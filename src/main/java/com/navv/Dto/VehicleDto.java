package com.navv.Dto;

import com.navv.model.Driver;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {

        private Integer id;

    private String name;
    private String model;
    private String year;
    private String type;
    private String chassinumber;
    private String registrationnumber;
    private String description;
    private Integer driverid;
    private String createdby;
    private Date assignedDate;
    private String assignedBy;
}
