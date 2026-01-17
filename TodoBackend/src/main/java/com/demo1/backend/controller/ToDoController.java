package com.demo1.backend.controller;

import com.demo1.backend.service.ToDoService;
import com.demo1.backend.models.Todo;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todo")
@Slf4j
@Validated
public class ToDoController {
    @Autowired
    ToDoService toDoService;

    // Get Specific Id data using Id
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved todo"),
            @ApiResponse(responseCode = "404", description = "Todo not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    ResponseEntity<Object> getToDoById(@PathVariable long id) {  // Changed from ResponseEntity<?>
        try {
            Todo getTodo = toDoService.getTodoById(id);
            return ResponseEntity.status(HttpStatus.OK).body(getTodo);
        } catch (RuntimeException exception) {
            log.error("Error retrieving todo: {}", exception.getMessage());
            if (exception.getMessage().contains("Access denied")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }
    }

    // Get Full ToDoList
    @GetMapping
    ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(toDoService.getAllTodos());
    }

    // Get Full List By Pages
    @GetMapping("/page")
    ResponseEntity<Page<Todo>> getAllTodosByPages(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.status(HttpStatus.OK).body(toDoService.getAllTodosByPages(page, size));
    }

    // Create an entry
    @PostMapping("/create")
    ResponseEntity<Todo> createUser(@RequestBody Todo todo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toDoService.createTodo(todo));
    }

    // Update an entry
    @PutMapping
    ResponseEntity<Object> updateTodo(@RequestBody Todo todo) {  // Changed from ResponseEntity<Todo> to allow mixed types
        try {
            return ResponseEntity.status(HttpStatus.OK).body(toDoService.updateTodo(todo));
        } catch (RuntimeException exception) {
            log.error("Error updating todo: {}", exception.getMessage());
            if (exception.getMessage().contains("Access denied")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }
    }

    // Delete an entry
    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteToDoById(@PathVariable long id) {  // Changed from ResponseEntity<?>
        try {
            toDoService.deleteTodoById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException exception) {
            log.error("Error deleting todo: {}", exception.getMessage());
            if (exception.getMessage().contains("Access denied")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }
    }
}