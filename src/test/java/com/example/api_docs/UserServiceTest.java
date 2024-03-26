package com.example.api_docs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void register() {
        // given
        User user = new User("user1", "Lee");

        // when
        boolean result = userService.register(user);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void findUserById() {
        //given
        String id = "user";

        //when
        User result = userService.findUserById(id);

        //then
        assertThat(result).isNotNull();
    }
}