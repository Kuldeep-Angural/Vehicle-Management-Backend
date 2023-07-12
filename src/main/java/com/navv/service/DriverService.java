package com.navv.service;


import com.navv.model.Driver;

import java.util.List;

public interface DriverService {
    public Driver saveDriver(Driver driver);
    public Driver findByid(int id);

    public List<Driver> getAllDriver();

    void deleteDriver(Integer id);
}
