package com.navv.Dto;

import lombok.Data;

@Data
public class PasswordUpdateDto {
    private String password;
    private String confirmpassword;

    private String email;


}
