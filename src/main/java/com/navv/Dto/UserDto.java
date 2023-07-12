package com.navv.Dto;

import com.navv.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;

    private String firstname;

    private String lastName;

    private String email;

    private String dob;

    private Date UpdatedOn;

    private String token;




    private String imageName;

    @Enumerated(EnumType.STRING)
    private Role role;
}
