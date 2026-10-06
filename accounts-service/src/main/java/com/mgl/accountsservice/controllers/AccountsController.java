package com.mgl.accountsservice.controllers;

import com.mgl.accountsservice.components.CreateAccountComponent;
import com.mgl.accountsservice.components.DeleteAccountComponent;
import com.mgl.accountsservice.components.GetAccountByIdComponent;
import com.mgl.accountsservice.components.GetAccountsComponent;
import com.mgl.accountsservice.components.PutAccountComponent;
import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.DeleteAccountResponse;
import com.mgl.accountsservice.dto.GetAccountByIdResponse;
import com.mgl.accountsservice.dto.GetAccountsResponse;
import com.mgl.accountsservice.dto.PutAccountRequest;
import com.mgl.accountsservice.dto.PutAccountResponse;
import com.mgl.accountsservice.exceptions.ResourceNotFoundException;
import com.mgl.accountsservice.models.Account;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** The Accounts REST API; failures are classified by ApiExceptionHandler. */
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountsController {

    private final CreateAccountComponent createAccountComponent;
    private final GetAccountsComponent getAccountsComponent;
    private final DeleteAccountComponent deleteAccountComponent;
    private final GetAccountByIdComponent getAccountByIdComponent;
    private final PutAccountComponent putAccountComponent;

    /** Connects the HTTP layer to transactional components. */
    public AccountsController(CreateAccountComponent createAccountComponent,
                              GetAccountsComponent getAccountsComponent,
                              DeleteAccountComponent deleteAccountComponent,
                              GetAccountByIdComponent getAccountByIdComponent,
                              PutAccountComponent putAccountComponent) {
        this.createAccountComponent = createAccountComponent;
        this.getAccountsComponent = getAccountsComponent;
        this.deleteAccountComponent = deleteAccountComponent;
        this.getAccountByIdComponent = getAccountByIdComponent;
        this.putAccountComponent = putAccountComponent;
    }

    /** Creates a validated account and returns HTTP 201. */
    @PostMapping(value = "/accounts", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAccountResponse createAccount(@RequestBody CreateAccountRequest request) {
        AccountInputValidator.validate(request.getAccount(), request.getRequestingUser(), false);
        String accountId = createAccountComponent.createAccount(request.getAccount(), request.getRequestingUser());
        return CreateAccountResponse.builder().success(true).accountId(accountId).build();
    }

    /** Retrieves accounts and their children. */
    @GetMapping("/accounts")
    public GetAccountsResponse getAccounts(@RequestParam(defaultValue = "50") int limit,
                                           @RequestParam(defaultValue = "0") int offset) {
        AccountInputValidator.validatePage(limit, offset);
        List<Account> accounts = getAccountsComponent.getAccounts(limit + 1, offset);
        boolean hasMore = accounts.size() > limit;
        List<Account> page = hasMore ? accounts.subList(0, limit) : accounts;
        return GetAccountsResponse.builder().accounts(page).limit(limit).offset(offset).hasMore(hasMore).build();
    }

    /** Deletes an account or reports HTTP 404. */
    @DeleteMapping("/accounts/{accountId}")
    public DeleteAccountResponse deleteAccount(@PathVariable String accountId) {
        Account account = deleteAccountComponent.deleteAccount(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return DeleteAccountResponse.builder().deletedAccount(account).success(true).build();
    }

    /** Retrieves an account or reports HTTP 404. */
    @GetMapping("/accounts/{accountId}")
    public GetAccountByIdResponse getAccount(@PathVariable String accountId) {
        Account account = getAccountByIdComponent.getAccount(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return GetAccountByIdResponse.builder().account(account).success(true).build();
    }

    /** Updates a validated existing account. */
    @PutMapping(value = "/accounts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PutAccountResponse putAccount(@RequestBody PutAccountRequest request) {
        AccountInputValidator.validate(request.getUpdatedAccount(), request.getUpdatingUser(), true);
        Account updatedAccount = putAccountComponent.putAccount(request.getUpdatedAccount(), request.getUpdatingUser());
        return PutAccountResponse.builder().success(true).updatedAccount(updatedAccount).build();
    }
}
