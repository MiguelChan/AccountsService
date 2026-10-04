package com.mgl.accountsservice.components;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mgl.accountsservice.controllers.AccountsController;
import com.mgl.accountsservice.controllers.ApiExceptionHandler;
import com.mgl.accountsservice.controllers.SubAccountsController;
import com.mgl.accountsservice.dao.AccountsDao;
import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.mappers.AccountsEntityMapper;
import com.mgl.accountsservice.mappers.SubAccountsEntityMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** Exercises real components through HTTP, including failure and absence paths. */
public class DatabaseFailureSemanticsTests {
    private final AccountsDao accounts = mock(AccountsDao.class);
    private final SubAccountsDao children = mock(SubAccountsDao.class);
    private final AccountsEntityMapper accountMapper = new AccountsEntityMapper();
    private final SubAccountsEntityMapper childMapper = new SubAccountsEntityMapper();
    private final DatabaseException failure = new DatabaseException("database unavailable", null);
    private final GetAccountByIdComponent getAccount = new GetAccountByIdComponent(
        accounts, accountMapper, children, childMapper);
    private final DeleteAccountComponent deleteAccount = new DeleteAccountComponent(
        accounts, accountMapper, children, childMapper);
    private final DeleteSubAccountComponent deleteChild = new DeleteSubAccountComponent(children, childMapper);

    private MockMvc http() {
        return MockMvcBuilders.standaloneSetup(new AccountsController(
            mock(CreateAccountComponent.class), mock(GetAccountsComponent.class), deleteAccount,
            getAccount, mock(PutAccountComponent.class)), new SubAccountsController(deleteChild)).setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    public void missingRows_should_returnNotFoundWithoutDeleting() throws Exception {
        http().perform(get("/accounts/a")).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Account not found"));
        http().perform(delete("/accounts/a")).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Account not found"));
        http().perform(delete("/subAccounts/s")).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("SubAccount not found"));
        verify(accounts, never()).deleteAccount("a");
        verify(children, never()).deleteSubAccount("s");
    }

    @Test
    public void lookupFailures_should_reachControllerErrorPathWithoutDeleting() throws Exception {
        when(accounts.getAccount("a")).thenThrow(failure);
        when(children.getSubAccount("s")).thenThrow(failure);
        http().perform(get("/accounts/a")).andExpect(status().isInternalServerError()).andExpect(jsonPath("$.message").value("Storage operation failed"));
        http().perform(delete("/accounts/a")).andExpect(status().isInternalServerError()).andExpect(jsonPath("$.message").value("Storage operation failed"));
        http().perform(delete("/subAccounts/s")).andExpect(status().isInternalServerError()).andExpect(jsonPath("$.message").value("Storage operation failed"));
        assertThatThrownBy(() -> deleteChild.deleteSubAccount("s")).isSameAs(failure);
        verify(accounts, never()).deleteAccount("a");
        verify(children, never()).deleteSubAccount("s");
    }

    @Test
    public void childReadFailure_should_propagateAndPreventAccountDeletion() {
        when(accounts.getAccount("a")).thenReturn(AccountEntity.builder().id("a").build());
        when(children.getSubAccounts("a")).thenThrow(failure);
        assertThatThrownBy(() -> getAccount.getAccount("a")).isSameAs(failure);
        assertThatThrownBy(() -> deleteAccount.deleteAccount("a")).isSameAs(failure);
        verify(accounts, never()).deleteAccount("a");
    }

    @Test
    public void deleteFailures_should_reachControllerErrorPath() throws Exception {
        when(accounts.getAccount("a")).thenReturn(AccountEntity.builder().id("a").accountType("Capital").build());
        when(children.getSubAccounts("a")).thenReturn(List.of());
        when(children.getSubAccount("s")).thenReturn(SubAccountEntity.builder().id("s").build());
        doThrow(failure).when(accounts).deleteAccount("a");
        doThrow(failure).when(children).deleteSubAccount("s");
        http().perform(delete("/accounts/a")).andExpect(status().isInternalServerError()).andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Storage operation failed"));
        http().perform(delete("/subAccounts/s")).andExpect(status().isInternalServerError()).andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Storage operation failed"));
    }
}
