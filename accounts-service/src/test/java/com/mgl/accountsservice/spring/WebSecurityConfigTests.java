package com.mgl.accountsservice.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mgl.accountsservice.controllers.HealthController;
import com.mgl.accountsservice.dao.HealthDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Checks the real security filters including preflight and proxy traffic. */
@WebMvcTest(HealthController.class)
@Import(WebSecurityConfig.class)
public class WebSecurityConfigTests {
    @Autowired private MockMvc mvc;
    @MockitoBean private HealthDao healthDao;

    @Test
    void localAnonymousRequestRemainsAccessible() throws Exception {
        mvc.perform(get("/ping")).andExpect(status().isOk());
    }

    @Test
    void unconfiguredCrossOriginPreflightIsRejected() throws Exception {
        mvc.perform(options("/ping").header("Origin", "https://untrusted.example")
            .header("Access-Control-Request-Method", "GET"))
            .andExpect(status().isForbidden());
    }

    @Test
    void proxyHttpRequiresHttps() throws Exception {
        mvc.perform(get("/ping").header("X-Forwarded-Proto", "http"))
            .andExpect(status().isFound()).andExpect(header().string("Location", "https://localhost/ping"));
    }

    @Test
    void secureProxyRequestRemainsAccessible() throws Exception {
        mvc.perform(get("/ping").secure(true).header("X-Forwarded-Proto", "https"))
            .andExpect(status().isOk());
    }
}
