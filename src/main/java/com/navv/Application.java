package com.navv;

import com.navv.Dto.AuthenticationResponse;
import com.navv.Dto.RegisterRequest;
import com.navv.controller.AuthController;
import com.navv.exception.AppException;
import com.navv.model.Role;
import com.navv.model.Token;
import com.navv.model.TokenType;
import com.navv.model.User;
import com.navv.repository.TokenRepository;
import com.navv.securityconfig.JwtService;
import com.navv.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;


@SpringBootApplication
public class Application {





	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

}
