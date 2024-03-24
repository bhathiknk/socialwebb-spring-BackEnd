package com.socialwebbspring.service;

import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.Todo;
import com.socialwebbspring.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

// TodoService.java
@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public void saveTodo(int userId, String date, String time, String todo) {

        Todo todoEntity = new Todo();
        todoEntity.setUserId(userId);
        todoEntity.setDate(date);
        todoEntity.setTime(time);
        todoEntity.setTodo(todo);
        todoRepository.save(todoEntity);
    }

    // TodoService.java
    public List<Todo> getAllTodos(int userId) {
        return todoRepository.findByUserId(userId);
    }

    public void deleteTodoById(int userId, int todoId) throws AuthenticationFailException {
        Optional<Todo> todoOptional = todoRepository.findById(todoId);
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            if (todo.getUserId() == userId) {
                todoRepository.delete(todo);
            } else {
                throw new AuthenticationFailException("User not authorized to delete this todo");
            }
        } else {
            throw new RuntimeException("Todo not found");
        }
    }
}
