package com.api_spring.service;

import com.api_spring.entity.User;
import com.api_spring.exception.UserAlreadyExistException;
import com.api_spring.exception.UserNotFoundException;
import com.api_spring.model.UserModel;
import com.api_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void registration(User user) throws UserAlreadyExistException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (userRepo.findByUsername(user.getUsername()) == null) {
            user.setRole("ROLE_USER");
            user.setPassword(encoder.encode(user.getPassword()));
            userRepo.save(user);
        }

        throw new UserAlreadyExistException("Пользователь уже существует");
    }

    public UserModel findUserByUsername(String username) throws UserNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user != null) {
            return UserModel.toModel(user);
        }

        throw new UserNotFoundException("Пользователь не найден");
    }

    public List<User> allUsers() {
        return userRepo.findAll();
    }

    public Long deleteUser(Long id) {
        userRepo.deleteById(id);
        return id;
    }
}
