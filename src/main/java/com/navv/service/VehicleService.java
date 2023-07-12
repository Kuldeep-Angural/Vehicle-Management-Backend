package com.navv.service;

import com.navv.Dto.VehicleDto;
import com.navv.model.Vehicle;

import java.util.List;

public interface VehicleService {

    public Vehicle saveVehicle(Vehicle vehicle);
    List<Vehicle> getAllVehicle();

    public String deleteVehicle(Integer id);

    public Vehicle findByID(Integer id);

    public   List<Vehicle> getVehicleByDriver(Integer id);

    public List<Vehicle> getVehicleByCreatedBy(String createdby);

    List<VehicleDto> getVehicles();
}
