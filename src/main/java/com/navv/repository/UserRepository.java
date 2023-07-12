package com.navv.repository;

import com.navv.model.Role;
import com.navv.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository< User,Integer> {

    Optional<User> findByEmail(String email);


//    @Query(value = "select * from User where role=?1",nativeQuery = true)
    List<User> findByRole(Role role);
















}
