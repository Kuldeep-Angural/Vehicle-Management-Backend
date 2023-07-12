package com.navv.service;

import com.navv.Dto.UserDto;
import com.navv.model.Role;
import com.navv.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User>findByEmail(String email);

    public   User saveuser(User user);

    public List<User> findByRole(Role role);

    public Optional<User>findbyid(int id);

   public List<User> getAll();

   public UserDto findById(Integer id);

   public List<User> allManagers();
    public String deleteuser(Integer id);


    User updateData(User user, Integer id);
}
