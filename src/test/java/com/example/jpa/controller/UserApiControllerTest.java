package com.example.jpa.controller;

import com.example.jpa.service.dto.UserRequestDto;
import com.example.jpa.service.dto.UserResponseDto;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebClient
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserApiControllerTest {

    private static final String LOCATION = "Location";

    @Autowired
    private WebTestClient testClient;

    private UserRequestDto getUser() {
        return UserRequestDto.builder()
                .id(1L)
                .name("Bear")
                .address("서울시")
                .age(39)
                .contents("코로나 조심하세요")
                .build();
    }

    private UserRequestDto getUpdateUser() {
        UserRequestDto copy = getUser();
        copy.setAddress("강릉시");
        copy.setAge(29);
        copy.setContents("감기 조심하세요");
        return copy;
    }

    @Test
    @DisplayName("User 생성, 조회, 변경, 삭제")
    void test_crud() {
        // Post
        UserRequestDto requestDto = getUser();

        UserResponseDto responseDto = testClient
                .post()
                .uri("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestDto), UserRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().valueMatches(LOCATION, "\\/user\\/\\d")
                .expectBody(UserResponseDto.class)
                .returnResult()
                .getResponseBody();

        assert responseDto != null;
        assertThat(responseDto.getId()).isEqualTo(requestDto.getId());
        assertThat(responseDto.getName()).isEqualTo(requestDto.getName());
        assertThat(responseDto.getAddress()).isEqualTo(requestDto.getAddress());
        assertThat(responseDto.getAge()).isEqualTo(requestDto.getAge());
        assertThat(responseDto.getContents()).isEqualTo(requestDto.getContents());

        // Get
        responseDto = testClient
                .get()
                .uri("/api/user/" + requestDto.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponseDto.class)
                .returnResult()
                .getResponseBody();

        assert responseDto != null;
        assertThat(responseDto.getId()).isEqualTo(requestDto.getId());
        assertThat(responseDto.getName()).isEqualTo(requestDto.getName());
        assertThat(responseDto.getAddress()).isEqualTo(requestDto.getAddress());
        assertThat(responseDto.getAge()).isEqualTo(requestDto.getAge());
        assertThat(responseDto.getContents()).isEqualTo(requestDto.getContents());

        // Update
        UserRequestDto updateDto = getUpdateUser();

        responseDto = testClient
                .put()
                .uri("/api/user/" + requestDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateDto), UserRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponseDto.class)
                .returnResult()
                .getResponseBody();

        assert responseDto != null;
        assertThat(responseDto.getId()).isEqualTo(updateDto.getId());
        assertThat(responseDto.getName()).isEqualTo(updateDto.getName());
        assertThat(responseDto.getAddress()).isEqualTo(updateDto.getAddress());
        assertThat(responseDto.getAge()).isEqualTo(updateDto.getAge());
        assertThat(responseDto.getContents()).isEqualTo(updateDto.getContents());

        // Delete
        testClient
                .delete()
                .uri("/api/user/" + getUser().getId())
                .exchange()
                .expectStatus().isOk();
    }
}
