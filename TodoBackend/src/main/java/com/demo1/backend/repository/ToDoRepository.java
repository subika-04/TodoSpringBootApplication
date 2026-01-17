package com.demo1.backend.repository;

import com.demo1.backend.models.Todo;
import com.demo1.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);
    Page<Todo> findByUser(User user, Pageable pageable);
}
