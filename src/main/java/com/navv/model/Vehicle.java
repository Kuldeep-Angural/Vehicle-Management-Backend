package com.navv.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    @Lob
//    private byte[] imagedata;
//    private String imageName;

    private String name;
    private String model;
    private String year;
    private String type;
    private String chassinumber;
    private String registrationnumber;
    private String description;
    private Date createdon;
    private String createdby;
    private Date updatedon;
    private boolean updatedby;
    private String assignedBy;

    @ManyToOne
    private Driver driver;

}
