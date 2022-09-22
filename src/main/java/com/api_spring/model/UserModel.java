package com.api_spring.model;

import com.api_spring.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter @Setter
public class UserModel {
    private Long id;
    private String username;
    private List<TodoModel> todos;

    public static UserModel toModel(User entity) {
        UserModel model = new UserModel();

        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setTodos(entity.getTodos().stream()
                        .map(TodoModel::toModel)
                        .collect(Collectors.toList()));

        return model;
    }

}
