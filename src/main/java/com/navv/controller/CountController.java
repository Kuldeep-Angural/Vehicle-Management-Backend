package com.navv.controller;

import com.navv.model.Feedback;
import com.navv.model.Role;
import com.navv.model.User;
import com.navv.model.Vehicle;
import com.navv.repository.FeedbackRepository;
import com.navv.service.UserService;
import com.navv.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class CountController {

    private final UserService userService;
    private final VehicleService vehicleService;
    private final FeedbackRepository feedbackRepository;



    @GetMapping("/managerCount")
    public ResponseEntity<?> countManager(){
        List<User> user=userService.findByRole(Role.MANAGER);
        int count=0;
        for (Object o:user){
            count++;

        }
        return ResponseEntity.ok(count);

    }


    @GetMapping("/driverCount")
    public ResponseEntity<?> countDriver(){
        List<User> user=userService.findByRole(Role.DRIVER);
        int count=0;
        for (Object o:user){
            count++;

        }
        return ResponseEntity.ok(count);

    }

    @GetMapping("/vehicleCount")
    public ResponseEntity<?>countvehicle(){
        List<Vehicle>vehicles=vehicleService.getAllVehicle();
        int count=0;
        for (Object o:vehicles){
            count++;
        }
        System.out.println(count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/countFeedback")
    public ResponseEntity<?>countFeedback(){
        List< Feedback> list=feedbackRepository.findAll();
        int count=0;
        for (Object o:list){
            count++;
        }
        System.out.println("Feedback count"+count);
        return ResponseEntity.ok(count);
    }
    @GetMapping("/vehicleCreatedBy/{createdBy}")
    public ResponseEntity<?>countvehicleCreatedBy(@PathVariable("createdby") String createdby){
        List<Vehicle>vehicles=vehicleService.getVehicleByCreatedBy(createdby);
        int count=0;
        for (Object o:vehicles){
            count++;
        }
        System.out.println(count);
        return ResponseEntity.ok(count);
    }

}
