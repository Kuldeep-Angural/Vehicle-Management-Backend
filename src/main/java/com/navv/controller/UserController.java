package com.navv.controller;

import com.navv.Dto.FeedbackResponse;
import com.navv.Dto.PasswordUpdateDto;
import com.navv.Dto.UserDto;
import com.navv.model.Driver;
import com.navv.model.Feedback;
import com.navv.model.Role;
import com.navv.model.User;
import com.navv.repository.FeedbackRepository;
import com.navv.repository.TokenRepository;
import com.navv.repository.UserRepository;
import com.navv.service.DriverService;
import com.navv.service.UserService;
import com.navv.service.VehicleService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final DriverService driverService;
    private final TokenRepository tokenRepository;
    private final VehicleService vehicleService;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    private User user1;
    private String path = "images/";
    @GetMapping("/{email}")
    public ResponseEntity<?> getuserByEmail(@PathVariable String email) {
        System.out.println(email);
        User user = userService.findByEmail(email).get();
        var userDto = UserDto.builder().firstname(user.getFirstname()).lastName(user.getLastname()).email(user.getEmail()).dob(user.getDob()).role(user.getRole()).id(user.getId())

                .build();
        System.out.println(userDto);
        return new ResponseEntity(userDto, HttpStatus.OK);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<?> updateUser(@PathVariable String email, @RequestBody UserDto userDto) {
        User user = userService.findByEmail(email).get();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastName());
        user.setDob(userDto.getDob());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setUpdateOn(new Date(System.currentTimeMillis()));


        userService.saveuser(user);

        return ResponseEntity.ok("data updated");

    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> update(@RequestBody PasswordUpdateDto dto) {
        System.out.println(dto);
        User user = userService.findByEmail(dto.getEmail()).get();
        user.setUpdateOn(new Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Driver driver = driverService.findByid(user.getId());
        if (user.getRole().equals(Role.DRIVER)) {

            driver.setSetUpdateOn(new Date(System.currentTimeMillis()));
            driver.setPassword(passwordEncoder.encode(dto.getPassword()));

        }
        userService.saveuser(user);
        driverService.saveDriver(driver);
        return ResponseEntity.ok("password updated Successfully");
    }

    @GetMapping("/allManagers")
    public List<?> getAllManagers() {

        return userService.allManagers();

    }



    @GetMapping("/all")
    public List<?> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/getUser/{id}")
    public UserDto findById(@PathVariable Integer id) {
        System.out.println(id + "inside findByID");
        return userService.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable("id") Integer id) {
        UserDto user = userService.findById(id);
        String res = userService.deleteuser(id);
        driverService.deleteDriver(id);

        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    @PostMapping("/updateuser/{id}")
    public ResponseEntity<?> updateUserByid(@PathVariable Integer id, @RequestBody UserDto userDto) {

        UserDto user = userService.findById(id);

        User user1 = userService.findByEmail(user.getEmail()).get();
        System.out.println("userEmail" + user1.getEmail());


        Driver driver = driverService.findByid(id);
        if (user.getRole().equals(Role.DRIVER)) {

            driver.setFirstname(userDto.getFirstname());
            driver.setLastname(userDto.getLastName());
            driver.setDob(userDto.getDob());
            driver.setEmail(userDto.getEmail());
            driver.setSetUpdateOn(new Date(System.currentTimeMillis()));
            driverService.saveDriver(driver);


        } else if (user.getRole().equals(Role.MANAGER)) {

            user1.setFirstname(userDto.getFirstname());
            user1.setLastname(userDto.getLastName());
            user1.setDob(userDto.getDob());
            user1.setEmail(userDto.getEmail());
            user1.setRole(userDto.getRole());
            user1.setUpdateOn(new Date(System.currentTimeMillis()));
            userService.saveuser(user1);
        }


        return ResponseEntity.ok("data updated");

    }

    @GetMapping("/feedbacks")
    public ResponseEntity<?> allFeedbacks(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "ascending", required = false) String sortDirection) {

        Sort sort = (sortDirection.equalsIgnoreCase("ascending")) ? sort = Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Feedback> page = feedbackRepository.findAll(pageable);
        List<Feedback> feedbacks = page.getContent();
        FeedbackResponse feedbackResponse = new FeedbackResponse();

        feedbackResponse.setFeedbackList(feedbacks);
        feedbackResponse.setPageNumber(page.getNumber());
        feedbackResponse.setPageSize(page.getSize());
        feedbackResponse.setTotalPages(page.getTotalPages());
        feedbackResponse.setTotalElement(page.getTotalElements());
        feedbackResponse.setLastPage(page.isLast());


        return new ResponseEntity<>(feedbackResponse, HttpStatus.OK);
    }
    //Search

    @GetMapping("/feedbacks/{keyWords}")
    public ResponseEntity<?> searchByName(@PathVariable("keyWords") String keyWords){
        System.out.println(keyWords);
        List<Feedback> feedbacks=feedbackRepository.findByName(keyWords);
        System.out.println(feedbacks);
        return new ResponseEntity<>(feedbacks,HttpStatus.OK);

    }
    @GetMapping(value = "/allDrivers")
    public ResponseEntity<List> getAllDrivers() {

        return new ResponseEntity<>(driverService.getAllDriver(), HttpStatus.OK);
    }

    //method to serve files
    @GetMapping(value = "/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {


        InputStream resource = getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream())   ;

    }
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;
        InputStream is = new FileInputStream(fullPath);
        // db logic to return inpustream
        return is;
    }

}
