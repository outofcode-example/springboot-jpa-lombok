package com.example.jpa.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {

    private Long id;
    private String name;
    private String address;
    private int age;
    private String contents;
}
