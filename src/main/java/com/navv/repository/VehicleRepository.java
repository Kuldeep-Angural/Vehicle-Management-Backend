package com.navv.repository;

import com.navv.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {


    @Query(value = "select * from vehicle where driver_id=?1",nativeQuery = true)
    List<Vehicle> getVehicleByDriver(Integer id);

    @Query(value = "select * from vehicle where createdby=?1",nativeQuery = true)
    List<Vehicle> getVehicleByCreatedBy(String createdby);


}
