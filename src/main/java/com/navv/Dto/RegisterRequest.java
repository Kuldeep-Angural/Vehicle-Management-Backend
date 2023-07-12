package com.navv.Dto;

import com.navv.model.Role;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String firstname;

    private String lastname;
    private String email;
    private Role role;
    private String password;
    private String dob;




//    For Driver only



}
