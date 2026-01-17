package com.demo1.backend.controller;

import com.demo1.backend.models.User;
import com.demo1.backend.repository.UserRepository;
import com.demo1.backend.service.UserService;
import com.demo1.backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String,String> body)
    {
       String email=body.get("email");
       String password=body.get("password");
       password=passwordEncoder.encode(password);
       if(userRepository.findByEmail(email).isPresent())
       {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
       }
       userService.createUser(User.builder().email(email).password(password).build());
       return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String,String> body)
    {
      String email=body.get("email");
      String password=body.get("password");
      Optional<User> optionalUser=userRepository.findByEmail(email);
      if(optionalUser.isEmpty())
      {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Registered");
      }
      User user=optionalUser.get();
      if(!passwordEncoder.matches(password,user.getPassword()))
      {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Registered");
      }
      String token=jwtUtil.generateKey(email);
      return ResponseEntity.ok(Map.of("token",token));
    }

}
