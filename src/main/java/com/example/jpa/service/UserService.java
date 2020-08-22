package com.example.jpa.service;

import com.example.jpa.domain.user.User;
import com.example.jpa.domain.user.UserRepository;
import com.example.jpa.service.dto.UserRequestDto;
import com.example.jpa.service.dto.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserResponseDto create(UserRequestDto request) {
        User user = new User(
                request.getName(),
                request.getAddress(),
                request.getAge(),
                request.getContents()
        );

        User saved = repository.save(user);

        return UserResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .address(saved.getAddress())
                .age(saved.getAge())
                .contents(saved.getContents())
                .build();
    }

    @Transactional(readOnly = true)
    public UserResponseDto show(Long userId) {
        User source = repository
                .findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 User를 찾을 수 없습니다."));

        return UserResponseDto.builder()
                .id(source.getId())
                .name(source.getName())
                .address(source.getAddress())
                .age(source.getAge())
                .contents(source.getContents())
                .build();
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> showAll() {
        List<User> articles = repository.findAll();

        return articles.stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .address(user.getAddress())
                        .age(user.getAge())
                        .contents(user.getContents())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto update(Long userId, UserRequestDto request) {
        User source = repository
                .findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 User를 찾을 수 없습니다."));
        User target = new User(
                request.getName(),
                request.getAddress(),
                request.getAge(),
                request.getContents()
        );
        source.update(target);

        return UserResponseDto.builder()
                .id(source.getId())
                .name(source.getName())
                .address(source.getAddress())
                .age(source.getAge())
                .contents(source.getContents())
                .build();
    }

    @Transactional
    public void delete(Long userId) {
        User source = repository
                .findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 User를 찾을 수 없습니다."));
        repository.delete(source);
    }
}
