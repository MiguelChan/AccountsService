package com.mgl.accountsservice.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mgl.accountsservice.components.DeleteSubAccountComponent;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.models.SubAccount;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** Verifies child deletion statuses through MVC. */
public class SubAccountsControllerTests {
    private final DeleteSubAccountComponent remove = mock(DeleteSubAccountComponent.class);
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new SubAccountsController(remove))
            .setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void deletesExistingChild() throws Exception {
        when(remove.deleteSubAccount("s")).thenReturn(Optional.of(SubAccount.builder().id("s").build()));
        mvc.perform(delete("/subAccounts/s")).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void missingChildIs404() throws Exception {
        when(remove.deleteSubAccount("s")).thenReturn(Optional.empty());
        mvc.perform(delete("/subAccounts/s")).andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void databaseFailureIs500() throws Exception {
        when(remove.deleteSubAccount("s")).thenThrow(new DatabaseException("private db", null));
        mvc.perform(delete("/subAccounts/s")).andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message").value("Storage operation failed"));
    }
}
