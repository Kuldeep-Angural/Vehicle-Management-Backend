package com.navv.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Builder@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    private Integer id;
    private String imageName;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private String dob;

    private Date createdon;

    private Date setUpdateOn;

    @Lob
    private byte[] image;

    private  boolean active;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;
}
