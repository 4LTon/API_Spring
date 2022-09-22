package com.api_spring.controller;

import com.api_spring.exception.DenialOfAccessException;
import com.api_spring.exception.UserNotFoundException;
import com.api_spring.model.UserModel;
import com.api_spring.security.AppUserDetails;
import com.api_spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private AppUserDetails userPrincipal;

    @GetMapping
    public ResponseEntity<UserModel> getOneUser(@RequestParam String username) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }


    @DeleteMapping
    public ResponseEntity<Long> deleteUser(@RequestParam Long id, Authentication authentication) throws DenialOfAccessException {
        userPrincipal = (AppUserDetails) authentication.getPrincipal();
        if (userPrincipal.getId().equals(id) || userPrincipal.getRole().equals("ROLE_ADMIN")) {
            return ResponseEntity.ok(userService.deleteUser(id));
        }

        throw new DenialOfAccessException("В доступе отказано");
    }
}
