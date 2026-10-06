package com.mgl.accountsservice.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mgl.accountsservice.components.CreateAccountComponent;
import com.mgl.accountsservice.components.DeleteAccountComponent;
import com.mgl.accountsservice.components.GetAccountByIdComponent;
import com.mgl.accountsservice.components.GetAccountsComponent;
import com.mgl.accountsservice.components.PutAccountComponent;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.models.Account;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** Tests the public status/envelope contract through Spring MVC. */
public class AccountsControllerTests {
    private final CreateAccountComponent create = mock(CreateAccountComponent.class);
    private final DeleteAccountComponent remove = mock(DeleteAccountComponent.class);
    private final GetAccountByIdComponent find = mock(GetAccountByIdComponent.class);
    private final GetAccountsComponent list = mock(GetAccountsComponent.class);
    private final PutAccountComponent update = mock(PutAccountComponent.class);
    private MockMvc mvc;

    private static final String CREATE = "{\"requestingUser\":\"test\",\"account\":{\"title\":\"Test\","
        + "\"accountType\":\"Capital\",\"subAccounts\":[]}}";
    private static final String UPDATE = "{\"updatingUser\":\"test\",\"updatedAccount\":{\"id\":\"a\","
        + "\"title\":\"Test\",\"accountType\":\"Capital\",\"subAccounts\":[]}}";

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new AccountsController(create, list, remove, find, update))
            .setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void createsWith201() throws Exception {
        when(create.createAccount(any(), any())).thenReturn("a");
        mvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(CREATE))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.accountId").value("a"));
    }

    @Test
    void returnsExistingAccountAndListing() throws Exception {
        Account account = Account.builder().id("a").subAccounts(List.of()).build();
        when(find.getAccount("a")).thenReturn(Optional.of(account));
        when(list.getAccounts(51, 0)).thenReturn(List.of(account));
        mvc.perform(get("/accounts/a")).andExpect(status().isOk()).andExpect(jsonPath("$.account.id").value("a"));
        mvc.perform(get("/accounts")).andExpect(status().isOk()).andExpect(jsonPath("$.accounts[0].id").value("a"));
    }

    @Test
    void missingReadAndDeleteReturn404() throws Exception {
        when(find.getAccount("missing")).thenReturn(Optional.empty());
        when(remove.deleteAccount("missing")).thenReturn(Optional.empty());
        mvc.perform(get("/accounts/missing")).andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value("NOT_FOUND"));
        mvc.perform(delete("/accounts/missing")).andExpect(status().isNotFound());
    }

    @Test
    void editsAndDeletesExistingAccount() throws Exception {
        Account account = Account.builder().id("a").subAccounts(List.of()).build();
        when(update.putAccount(any(), any())).thenReturn(account);
        when(remove.deleteAccount("a")).thenReturn(Optional.of(account));
        mvc.perform(put("/accounts").contentType(MediaType.APPLICATION_JSON).content(UPDATE))
            .andExpect(status().isOk()).andExpect(jsonPath("$.updatedAccount.id").value("a"));
        mvc.perform(delete("/accounts/a")).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void storageFailureIs500WithoutInternalMessage() throws Exception {
        when(find.getAccount("a")).thenThrow(new DatabaseException("jdbc password=secret", null));
        mvc.perform(get("/accounts/a")).andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.success").value(false)).andExpect(jsonPath("$.code").value("DATABASE_ERROR"))
            .andExpect(jsonPath("$.message").value("Storage operation failed"));
    }

    @Test
    void unexpectedFailureIsSafe500() throws Exception {
        when(list.getAccounts(51, 0)).thenThrow(new IllegalStateException("internal details"));
        mvc.perform(get("/accounts")).andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
            .andExpect(jsonPath("$.message").value("Unexpected server error"));
    }

    @Test
    void invalidWritesNeverReachComponents() throws Exception {
        for (String body : List.of("{}", CREATE.replace("Test", " "), CREATE.replace("[]", "[null]"),
            CREATE.replace("Capital", "unknown"), CREATE.replace("\"test\"", "null"))) {
            mvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
        }
        mvc.perform(put("/accounts").contentType(MediaType.APPLICATION_JSON).content("{}"))
            .andExpect(status().isBadRequest());
        verifyNoInteractions(create, update);
    }

    @Test
    void malformedJsonAndUnsupportedMediaAreClassified() throws Exception {
        mvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content("{"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
        mvc.perform(post("/accounts").contentType(MediaType.TEXT_PLAIN).content(CREATE))
            .andExpect(status().isUnsupportedMediaType()).andExpect(jsonPath("$.code").value("UNSUPPORTED_MEDIA_TYPE"));
        verifyNoInteractions(create);
    }

    @Test
    void validatesPaginationBeforeReading() throws Exception {
        for (String query : List.of("limit=0", "limit=101", "offset=-1", "offset=100001", "limit=abc")) {
            mvc.perform(get("/accounts?" + query)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
        }
        verifyNoInteractions(list);
    }

    @Test
    void lookaheadIsNotReturnedAndIndicatesNextPage() throws Exception {
        Account first = Account.builder().id("a").build();
        Account second = Account.builder().id("b").build();
        when(list.getAccounts(2, 5)).thenReturn(List.of(first, second));
        mvc.perform(get("/accounts?limit=1&offset=5")).andExpect(status().isOk())
            .andExpect(jsonPath("$.accounts.length()").value(1)).andExpect(jsonPath("$.accounts[0].id").value("a"))
            .andExpect(jsonPath("$.hasMore").value(true)).andExpect(jsonPath("$.limit").value(1))
            .andExpect(jsonPath("$.offset").value(5));
    }

    @Test
    void rejectsOversizedInputsBeforeWriting() throws Exception {
        for (String body : List.of(CREATE.replace("Test", "x".repeat(101)), CREATE.replace("test", "x".repeat(31)))) {
            mvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(create);
    }

}
