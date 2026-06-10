package com.smartlogix;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMicroserviceSmartlogixApplicationTest {

    @Test
    void contextLoads() {
        // This test ensures the Spring application context loads successfully.
    }

    @Test
    void testMain() {
        UserMicroserviceSmartlogixApplication.main(new String[]{});
    }
}
