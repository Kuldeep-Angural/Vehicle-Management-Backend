package com.navv.service;

import com.navv.Dto.VehicleDto;
import com.navv.model.Vehicle;
import com.navv.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService{

    private final VehicleRepository vehicleRepository;
    @Autowired
    private  ModelMapper modelMapper;


    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getAllVehicle() {
        return vehicleRepository.findAll();
    }

    @Override
    public String deleteVehicle(Integer id) {
        Vehicle vehicle=findByID(id);
        if (vehicle!=null){
            vehicleRepository.delete(vehicle);
        }
        return "success";
    }

    @Override
    public Vehicle findByID(Integer id) {
        return vehicleRepository.findById(id).get();
    }

    @Override
    public List<Vehicle> getVehicleByDriver(Integer id) {
        return vehicleRepository.getVehicleByDriver(id);
    }

    @Override
    public List<Vehicle> getVehicleByCreatedBy(String createdby) {
        return vehicleRepository.getVehicleByCreatedBy(createdby);
    }

    @Override
    public List<VehicleDto> getVehicles() {
    List<Vehicle> vehicles=vehicleRepository.findAll();
    List<VehicleDto> vehicleDtos=vehicles.stream().map((vehicle) -> this.modelMapper.map(vehicle,VehicleDto.class) ).collect(Collectors.toList());


        return vehicleDtos;
    }
}
