package com.mgl.accountsservice.controllers;

import com.mgl.accountsservice.components.CreateAccountComponent;
import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Accounts REST Controller.
 */
@RestController
@RequestMapping(
    value = "/",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class AccountsController {

    private final CreateAccountComponent createAccountComponent;

    /**
     * .
     *
     * @param createAccountComponent .
     */
    @Autowired
    public AccountsController(CreateAccountComponent createAccountComponent) {
        this.createAccountComponent = createAccountComponent;
    }

    /**
     * .
     *
     * @param request .
     *
     * @return .
     */
    @PostMapping
    @RequestMapping("/accounts")
    public CreateAccountResponse createAccount(@RequestBody CreateAccountRequest request) {
        Account account = request.getAccount();
        String requestingUser = request.getRequestingUser();
        try {
            String accountId = createAccountComponent.createAccount(account, requestingUser);
            return CreateAccountResponse.builder()
                .success(true)
                .accountId(accountId)
                .build();
        } catch (Exception e) {
            return CreateAccountResponse.builder()
                .success(false)
                .message(e.getMessage())
                .build();
        }
    }

}
