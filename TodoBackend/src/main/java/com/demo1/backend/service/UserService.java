package com.demo1.backend.service;

import com.demo1.backend.models.User;
import com.demo1.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository UserRepository;
    public User createUser(User User)
    {
        return UserRepository.save(User);
    }
    public User getUserById(Long id)
    {
        return UserRepository.findById(id).orElseThrow(()->new RuntimeException("Id not found"));
    }

}
