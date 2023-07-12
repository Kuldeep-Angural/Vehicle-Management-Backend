package com.navv.controller;

import com.navv.Dto.*;
import com.navv.exception.AppException;
import com.navv.model.*;
import com.navv.repository.FeedbackRepository;
import com.navv.repository.TokenRepository;
import com.navv.securityconfig.JwtService;
import com.navv.service.DriverService;
import com.navv.service.FileService;
import com.navv.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private String path = "images/";

    private final UserService userService;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CountController countController;
    private final FileService fileService;
    private final FeedbackRepository feedbackRepository;
    private final DriverService driverService;
    @Autowired
    private  ModelMapper modelMapper;


    @Value("${uploadDir}")
    String DIR_ECTORY;


    private static String imageDirectory = System.getProperty("user.dir") + "/images/";

    @PostMapping(
            path = "/registerWithImage",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE


    )
    public ResponseEntity<?> addUser(@RequestParam("image") MultipartFile image, @RequestBody RegisterRequest request
    ) {
        System.out.println(request.getEmail());
        String name = image.getOriginalFilename();
        System.out.println(name);

        makeDirectoryIfNotExist(imageDirectory);
        Path fileNamePath = Paths.get(imageDirectory,
                name.concat(".").concat(FilenameUtils.getExtension(image.getOriginalFilename())));
        System.out.println(fileNamePath);

        try {
            Files.write(fileNamePath, image.getBytes());
            return new ResponseEntity<>(name + "image Upload Successfully", HttpStatus.CREATED);
        } catch (IOException ex) {
            return new ResponseEntity<>("Image is not uploaded", HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping(
            path = "/Register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE


    )
    public ResponseEntity<?> RegisterUserWithImage(@RequestParam("image") MultipartFile file,
                                             @RequestParam("email") String email,
                                             @RequestParam("firstname") String firstname,
                                             @RequestParam("lastname") String lastname,
                                             @RequestParam("password") String password,
                                             @RequestParam("role") Role role,
                                             @RequestParam("dob") String dob

    ) throws IOException {
        String name = file.getOriginalFilename();



        if (email.equals(userService.findByEmail(email))) {

            throw new AppException("user already registered with same email", HttpStatus.BAD_REQUEST);
        }


        var user = User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .dob(dob)
                .createdon(new Date(System.currentTimeMillis()))
                .active(true)
                .imageName(file.getOriginalFilename())
                .image(file.getBytes())
                .build();


        var savedUser = userService.saveuser(user);
        var jwtToken = jwtService.createToken(user);
        saveUserToken(savedUser, jwtToken);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();


        if (role.equals(Role.DRIVER)) {
            var driver =

                    Driver.builder()
                            .firstname(firstname)
                            .lastname(lastname)
                            .email(email)
                            .password(passwordEncoder.encode(password))
                            .id(savedUser.getId())
                            .dob(dob)
                            .createdon(new Date(System.currentTimeMillis()))
                            .active(true)
                            .imageName(file.getOriginalFilename())
                            .image(file.getBytes())
                            .build();

            Driver driver1 = driverService.saveDriver(driver);


        }


        makeDirectoryIfNotExist(imageDirectory);
        Path fileNamePath = Paths.get(imageDirectory,
               file.getOriginalFilename());
        System.out.println(fileNamePath);

        try {
            Files.write(fileNamePath, file.getBytes());
            return new ResponseEntity<>(name + "image Upload Successfully", HttpStatus.CREATED);
        } catch (IOException ex) {
            return new ResponseEntity<>("Image is not uploaded", HttpStatus.BAD_REQUEST);
        }
    }

    private void makeDirectoryIfNotExist(String imageDirectory) {
        File directory = new File(imageDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())

        );
        var user = userService.findByEmail(request.getEmail()).orElseThrow(() -> new AppException("email not found", HttpStatus.NOT_FOUND));

        var jwtToken = jwtService.createToken(user);
        var newRole = user.getRole();

        revokeAllUserToken(user);
        saveUserToken(user, jwtToken);
        String token=jwtToken;
        System.out.println(jwtToken);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        System.out.println(countController.countManager());
        var userDto = UserDto.builder().firstname(user.getFirstname()).imageName(user.getImageName()).lastName(user.getLastname()).token(jwtToken).id(user.getId()).email(user.getEmail()).role(user.getRole())


                .dob(user.getDob()).build();
        authenticationResponse.setUserDto(userDto);
        System.out.println(userDto.getImageName());

        return ResponseEntity.ok(authenticationResponse);


    }

//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws IOException {
//
//
//        if (request.getEmail().equals(userService.findByEmail(request.getEmail()))) {
//
//            throw new AppException("user already registered with same email", HttpStatus.BAD_REQUEST);
//        }
//
//
//        var user = User.builder().firstname(request.getFirstname()).lastname(request.getLastname()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(request.getRole()).dob(request.getDob()).createdon(new Date(System.currentTimeMillis())).active(true)
////                .imageName(file.getOriginalFilename())
//                .build();
//
//
//        System.out.println(request);
//        var savedUser = userService.saveuser(user);
//        var jwtToken = jwtService.createToken(user);
//        saveUserToken(savedUser, jwtToken);
//        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
//
//
//        if (request.getRole().equals(Role.DRIVER)) {
//            var driver = Driver.builder().id(user.getId()).email(request.getEmail()).firstname(request.getFirstname()).lastname(request.getLastname()).dob(request.getDob()).password(passwordEncoder.encode(request.getPassword())).active(true)
////                    .imageName(file.getOriginalFilename())
//                    .build();
//            Driver driver1 = driverService.saveDriver(driver);
//
//
//        }
//        return ResponseEntity.ok(authenticationResponse);
//    }

   /* @PostMapping("image/upload/{id}")
    public ResponseEntity<?> uploadPostImage(@RequestParam("image") MultipartFile image,
                                                   @PathVariable Integer id) throws IOException {

        System.out.println(image.getOriginalFilename()  );
        User user = this.userService.findbyid(id).get();

        String fileName = this.fileService.uploadImage(path, image);
        user.setImageName(fileName);
        User updatePost = this.userService.updateData(user, id);
        return new ResponseEntity<>("Data Submit", HttpStatus.OK);

    }*/

    private void revokeAllUserToken(User user) {

        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setActive(false);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).active(true).expireDate(jwtService.expirationDate(jwtToken)).activateDate(new Date()).build();
        tokenRepository.save(token);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Loggout successFully");
    }
    @PostMapping("/sendFeedback")
    public ResponseEntity<?> sendFeedback(@RequestBody FeedbackDto feedbackDto){
        System.out.println(feedbackDto);
        Feedback feedback= this.modelMapper.map(feedbackDto,Feedback.class);

        feedbackRepository.save(feedback);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getValidToken/{id}")
    public ResponseEntity<?> getValidToken(@PathVariable("id") Integer id){
        System.out.println("user id for token is :"+id);
       List list= tokenRepository.findAllValidTokenByUser(id);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

}
