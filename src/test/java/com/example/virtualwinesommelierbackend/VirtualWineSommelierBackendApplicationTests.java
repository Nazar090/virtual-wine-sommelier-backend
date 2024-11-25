package com.example.virtualwinesommelierbackend;

import com.example.virtualwinesommelierbackend.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class VirtualWineSommelierBackendApplicationTests {

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void contextLoads() {
    }

}
