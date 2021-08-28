package com.mgl.accountsservice.controllers;

import com.mgl.accountsservice.components.CreateAccountComponent;
import com.mgl.accountsservice.components.GetAccountsComponent;
import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.GetAccountsResponse;
import com.mgl.accountsservice.models.Account;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Accounts REST Controller.
 */
@Log4j2
@RestController
@RequestMapping(
    value = "/",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class AccountsController {

    private final CreateAccountComponent createAccountComponent;
    private final GetAccountsComponent getAccountsComponent;

    /**
     * .
     *
     * @param createAccountComponent .
     */
    @Autowired
    public AccountsController(CreateAccountComponent createAccountComponent,
                              GetAccountsComponent getAccountsComponent) {
        this.createAccountComponent = createAccountComponent;
        this.getAccountsComponent = getAccountsComponent;
    }

    /**
     * .
     *
     * @param request .
     *
     * @return .
     */
    @PostMapping("/accounts")
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

    /**
     * .
     *
     * @return .
     */
    @GetMapping(value = "/accounts", consumes = MediaType.ALL_VALUE)
    public GetAccountsResponse getAccounts() {
        log.info("Attempting to fetch All the Accounts");
        try {
            List<Account> allAccounts = getAccountsComponent.getAccounts();
            return GetAccountsResponse.builder()
                .accounts(allAccounts)
                .build();
        } catch (Exception e) {
            return GetAccountsResponse.builder()
                .message(e.getMessage())
                .build();
        }
    }

}
