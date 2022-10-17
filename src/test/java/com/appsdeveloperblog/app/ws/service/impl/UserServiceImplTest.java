package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception{

    }

    @Test
    final void testFindUser(){
       when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);

    }


}
