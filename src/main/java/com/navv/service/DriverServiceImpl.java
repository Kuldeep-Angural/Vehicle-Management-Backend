package com.navv.service;

import com.navv.Dto.UserDto;
import com.navv.model.Driver;
import com.navv.model.Role;
import com.navv.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService{
    private final DriverRepository driverRepository;
    @Override
    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public Driver findByid(int id) {
        return driverRepository.findById(id);
    }

    @Override
    public List getAllDriver() {
        List<Driver> drivers=driverRepository.findAll();
        List<UserDto> list=new ArrayList<>();
        for (Driver driver:drivers){

                var newDriver= UserDto.builder()
                        .firstname(driver.getFirstname())
                        .lastName(driver.getLastname())
                        .id(driver.getId())
                        .dob(driver.getDob())
                        .email(driver.getEmail())
                        .role(Role.DRIVER)
                        .imageName(driver.getImageName())
                        .build();

                list.add(newDriver);
        }
        return list;
    }

    @Override
    public void deleteDriver(Integer id) {
        Driver driver=findByid(id);
        driverRepository.delete(driver);
    }
}
