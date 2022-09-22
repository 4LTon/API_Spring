package com.api_spring.service;

import com.api_spring.entity.Todo;
import com.api_spring.entity.User;
import com.api_spring.exception.DenialOfAccessException;
import com.api_spring.model.TodoModel;
import com.api_spring.repository.TodoRepository;
import com.api_spring.repository.UserRepository;
import com.api_spring.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepo;

    @Autowired
    private UserRepository userRepo;

    public TodoModel createTodo(Todo todo, Long userId) {
        User user = userRepo.findById(userId).get();
        todo.setUser(user);
        return TodoModel.toModel(todoRepo.save(todo));
    }

    public TodoModel changeTodo(String todoText, Long todoId, Authentication authentication)
        throws DenialOfAccessException {
        Todo todo = todoRepo.findById(todoId).get();

        if (checkUser(todo, authentication)) {
            todo.setTitle(todoText);
            return TodoModel.toModel(todoRepo.save(todo));
        }

        throw new DenialOfAccessException("В доступе отказано");
    }

    public TodoModel completeTodo(Long todoId, Authentication authentication) throws DenialOfAccessException {
        Todo todo = todoRepo.findById(todoId).get();

        if (checkUser(todo, authentication)) {
            todo.setCompleted(!todo.getCompleted());
            return TodoModel.toModel(todoRepo.save(todo));
        }

        throw new DenialOfAccessException("В доступе отказано");
    }
    
    public Long deleteTodo(Long id) throws DenialOfAccessException {
        todoRepo.deleteById(id);
        return id;
    }

    public boolean checkUser(Todo todo, Authentication authentication) {
        AppUserDetails userPrincipal = (AppUserDetails) authentication.getPrincipal();
        return (todo.getUser().getId().equals(userPrincipal.getId())) || userPrincipal.getRole().equals("ROLE_ADMIN");
    }
}
