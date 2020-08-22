package com.example.jpa.controller;

import com.example.jpa.service.UserService;
import com.example.jpa.service.dto.UserRequestDto;
import com.example.jpa.service.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/api/user")
    public ResponseEntity create(@RequestBody UserRequestDto request) {
        UserResponseDto serviceResult = service.create(request);
        return ResponseEntity
                .created(URI.create("/user/"+ serviceResult.getId()))
                .body(serviceResult);
    }

    @GetMapping("/api/user/{id}")
    public ResponseEntity show(@PathVariable("id") Long id) {
        UserResponseDto user = service.show(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/users")
    public ResponseEntity showAll() {
        List<UserResponseDto> articles = service.showAll();
        return ResponseEntity.ok(articles);
    }

    @PutMapping("/api/user/{id}")
    public ResponseEntity update(@PathVariable("id") Long id,
                                 @RequestBody UserRequestDto request) {
        UserResponseDto updatedArticle = service.update(id, request);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/api/user/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
