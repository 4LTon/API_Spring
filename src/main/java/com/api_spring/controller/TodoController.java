package com.api_spring.controller;

import com.api_spring.entity.Todo;
import com.api_spring.exception.DenialOfAccessException;
import com.api_spring.model.TodoModel;
import com.api_spring.security.AppUserDetails;
import com.api_spring.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoModel> createTodo(@RequestBody Todo todo,
                                                @RequestParam Long userId, Authentication authentication) throws DenialOfAccessException {

        if (checkUser(userId, authentication)) {
            return ResponseEntity.ok(todoService.createTodo(todo, userId));
        }

        throw new DenialOfAccessException("В доступе отказано");
    }

    @PatchMapping
    public ResponseEntity<TodoModel> changeTodo(@RequestBody String title,
                                                @RequestParam Long todoId, Authentication authentication) throws DenialOfAccessException {

        return ResponseEntity.ok(todoService.changeTodo(title, todoId, authentication));
    }

    @PutMapping
    public ResponseEntity<TodoModel> completeTodo(@RequestParam Long todoId, Authentication authentication) throws DenialOfAccessException {

        return ResponseEntity.ok(todoService.completeTodo(todoId, authentication));
    }

    @DeleteMapping
    public ResponseEntity<Long> deleteTodo(@RequestParam Long todoId,
                                           @RequestParam Long userId, Authentication authentication) throws DenialOfAccessException {
        if (checkUser(userId, authentication)) {
            return ResponseEntity.ok(todoService.deleteTodo(todoId));
        }

        throw new DenialOfAccessException("В доступе отказано");
    }

    public boolean checkUser(Long userId, Authentication authentication) {
        AppUserDetails userPrincipal = (AppUserDetails) authentication.getPrincipal();
        return userPrincipal.getId().equals(userId) || userPrincipal.getRole().equals("ROLE_ADMIN");
    }
}
