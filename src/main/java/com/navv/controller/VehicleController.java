package com.navv.controller;

import com.navv.Dto.VehicleDto;
import com.navv.model.Driver;
import com.navv.model.Vehicle;
import com.navv.repository.DriverRepository;
import com.navv.service.DriverService;
import com.navv.service.UserService;
import com.navv.service.VehicleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final DriverRepository driverRepository;

    private final UserService userService;

    @PostMapping("/add-vehicle")
    public ResponseEntity<?> addVehicle(@RequestBody VehicleDto vehicleDto){
        System.out.println(vehicleDto);
        Optional<Driver> driver = driverRepository.findById(vehicleDto.getDriverid());


        if(driver.isPresent()){
            var vehicle=Vehicle.builder()
                    .name(vehicleDto.getName())
                    .model(vehicleDto.getModel())
                    .year(vehicleDto.getYear())
                    .type(vehicleDto.getType())
                    .chassinumber(vehicleDto.getChassinumber())
                    .registrationnumber(vehicleDto.getRegistrationnumber())
                    .description(vehicleDto.getDescription())
                    .createdby(vehicleDto.getCreatedby())
                    .driver(driver.get())
                    .createdon(new Date(System.currentTimeMillis()))
                    .updatedon(new Date(System.currentTimeMillis()))
                    .build();
            System.out.println(driver);
            var savedVehicle=vehicleService.saveVehicle(vehicle);
            return ResponseEntity.ok("saved Successfully");
        }
        else {
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }




    }

    @GetMapping("/allVehicles")
    public ResponseEntity<?> getAllVehicles(){
        List<Vehicle> vehicles =vehicleService.getAllVehicle();
        List<VehicleDto> list=new ArrayList<>();
        for (Vehicle vehicle:vehicles){
            var newVehicle=VehicleDto.builder()
                    .name(vehicle.getName())
                    .model(vehicle.getModel())
                    .registrationnumber(vehicle.getRegistrationnumber())
                    .type(vehicle.getType())
                    .chassinumber(vehicle.getChassinumber())
                    .createdby(vehicle.getCreatedby())
                    .driverid(vehicle.getDriver().getId())
                    .id(vehicle.getId())
                    .build();
            list.add(newVehicle);
        }
        return new ResponseEntity<>(list,HttpStatus.OK);

    }

    @GetMapping("/vehicles")
    public ResponseEntity<?> vehicles(){
        return new ResponseEntity<>(vehicleService.getVehicles(),HttpStatus.OK);
    }

    @GetMapping("/getVehicle/{id}")
    public ResponseEntity<?> getVehicleById(@PathVariable("id") Integer id){
            Vehicle vehicle=vehicleService.findByID(id);
            return new  ResponseEntity<>(vehicle,HttpStatus.OK);
    }
    @PutMapping("/edit-vehicle")
    public ResponseEntity<?> editVehicle(@RequestBody VehicleDto vehicleDto){
        Vehicle vehicle=vehicleService.findByID(vehicleDto.getId());
        System.out.println(vehicle);
        return new ResponseEntity<>("Data Update Successfully",HttpStatus.OK);

    }
    @DeleteMapping("/deleteVehicle/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable("id") Integer id){

        String res= vehicleService.deleteVehicle(id);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
    @GetMapping("/getVehicleByDriverId/{id}")
    public ResponseEntity<?> getVehiclesByDriverId(@PathVariable("id")Integer id){
        List<Vehicle> vehicles=vehicleService.getVehicleByDriver(id);
        List <VehicleDto> list=new ArrayList<>();
        for (Vehicle vehicle:vehicles){
            var newVehicle=VehicleDto.builder()
                    .id(vehicle.getId())
                    .name(vehicle.getName())
                    .model(vehicle.getModel())
                    .driverid(vehicle.getDriver().getId())
                    .assignedDate(new Date(System.currentTimeMillis()))
                    .assignedBy(vehicle.getCreatedby())
                    .build();
            list.add(newVehicle);
        }

        return new ResponseEntity<>(list,HttpStatus.OK);
    }
}
