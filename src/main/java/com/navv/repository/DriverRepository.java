package com.navv.repository;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.navv.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Integer> {

    Driver findById(int id);


}
