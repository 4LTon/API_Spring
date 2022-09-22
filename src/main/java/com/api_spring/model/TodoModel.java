package com.api_spring.model;

import com.api_spring.entity.Todo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class TodoModel {
    private Long id;
    private String title;
    private Boolean completed;

    public static TodoModel toModel(Todo entity) {
        TodoModel model = new TodoModel();

        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setCompleted(entity.getCompleted());

        return model;
    }
}
