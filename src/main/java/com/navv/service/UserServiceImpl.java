package com.navv.service;

import com.navv.Dto.UserDto;
import com.navv.model.Driver;
import com.navv.model.Role;
import com.navv.model.Token;
import com.navv.model.User;
import com.navv.repository.TokenRepository;
import com.navv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final DriverService driverService;


    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveuser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public Optional<User> findbyid(int id) {
        return Optional.of(userRepository.findById(id).get());
    }


    @Override
    public List getAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> list = new ArrayList<>();
        for (User user : users) {
            var newUser = UserDto.builder()
                    .firstname(user.getFirstname())
                    .lastName(user.getLastname())
                    .id(user.getId())
                    .dob(user.getDob())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .imageName(user.getImageName())
                    .build();
            list.add(newUser);
        }
        return list;
    }

    @Override
    public UserDto findById(Integer id) {
        User user = userRepository.findById(id).get();
        var user1 = UserDto.builder()
                .firstname(user.getFirstname())
                .lastName(user.getLastname())
                .id(user.getId())
                .dob(user.getDob())
                .email(user.getEmail())
                .role(user.getRole())
                .imageName(user.getImageName())
                .build();
        return user1;
    }

    @Override
    public List allManagers() {
        List<User> users = findByRole(Role.MANAGER);
        List<UserDto> list = new ArrayList<>();
        ;
        for (User user : users) {
            var newUser = UserDto.builder()
                    .firstname(user.getFirstname())
                    .lastName(user.getLastname())
                    .id(user.getId())
                    .dob(user.getDob())
                    .email(user.getEmail())
                    .role(Role.MANAGER)
                    .imageName(user.getImageName())
                    .build();
            list.add(newUser);
        }
        return list;
    }


    @Override
    public String deleteuser(Integer id) {
        User user = userRepository.findById(id).get();
        if (user.getRole().equals(Role.DRIVER)) {
//           Driver driver=driverService.findByid(id);
            driverService.deleteDriver(user.getId());
            tokenRepository.deleteAll(user.getTokens());
            userRepository.delete(user);
        } else if (user.getRole().equals(Role.MANAGER)) {

            tokenRepository.deleteAll(user.getTokens());
            userRepository.delete(user);

        }

        return "dataDeleted Successfully";
    }

    @Override
    public User updateData(User user, Integer id) {

        User user1=findbyid(id).get();
        user1.setImageName(user.getImageName());
        userRepository.save(user1);
        return null;
    }


}
