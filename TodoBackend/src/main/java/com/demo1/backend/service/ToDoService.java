package com.demo1.backend.service;

import com.demo1.backend.models.Todo;
import com.demo1.backend.models.User;
import com.demo1.backend.repository.ToDoRepository;
import com.demo1.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {
    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private UserRepository userRepository;

    // Helper to get the current authenticated user
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Todo createTodo(Todo todo) {
        todo.setUser(getCurrentUser());  // Associate with current user
        return toDoRepository.save(todo);
    }

    public Todo getTodoById(Long id) {
        Todo todo = toDoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        // Ensure the todo belongs to the current user
        if (!todo.getUser().getId().equals(getCurrentUser().getId())) {
            throw new RuntimeException("Access denied: Todo does not belong to you");
        }
        return todo;
    }

    public List<Todo> getAllTodos() {
        return toDoRepository.findByUser(getCurrentUser());
    }

    public Page<Todo> getAllTodosByPages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return toDoRepository.findByUser(getCurrentUser(), pageable);
    }

    public Todo updateTodo(Todo todo) {
        // Fetch and validate ownership
        Todo existing = getTodoById(todo.getId());  // This checks ownership
        todo.setUser(existing.getUser());  // Preserve user association
        return toDoRepository.save(todo);
    }

    public void deleteTodoById(Long id) {
        Todo todo = getTodoById(id);  // Checks ownership
        toDoRepository.delete(todo);
    }

    public void deleteTodo(Todo todo) {
        // This method is unused in your code, but if called, ensure ownership
        if (!todo.getUser().getId().equals(getCurrentUser().getId())) {
            throw new RuntimeException("Access denied");
        }
        toDoRepository.delete(todo);
    }
}