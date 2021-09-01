package com.mgl.accountsservice.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.mgl.accountsservice.components.DeleteSubAccountComponent;
import com.mgl.accountsservice.dto.DeleteSubAccountResponse;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.models.SubAccount;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * .
 */
@ExtendWith(MockitoExtension.class)
public class SubAccountsControllerTests {

    @Mock
    private DeleteSubAccountComponent deleteSubAccountComponent;

    private SubAccountsController subAccountsController;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        subAccountsController = new SubAccountsController(deleteSubAccountComponent);
    }

    @Test
    public void deleteSubAccount_should_delete() {
        String testSubAccountId = "SomeId";

        SubAccount subAccount = EnhancedRandom.random(SubAccount.class);

        when(deleteSubAccountComponent.deleteSubAccount(testSubAccountId))
            .thenReturn(Optional.of(subAccount));

        DeleteSubAccountResponse response =
            subAccountsController.deleteSubAccount(testSubAccountId);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getDeletedSubAccount()).isEqualTo(subAccount);
    }

    @Test
    public void deleteSubAccount_should_returnEmptyResponse_when_errorOccurs() {
        String testSubAccountId = "SomeSome";

        when(deleteSubAccountComponent.deleteSubAccount(anyString()))
            .thenThrow(DatabaseException.class);

        DeleteSubAccountResponse response =
            subAccountsController.deleteSubAccount(testSubAccountId);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getDeletedSubAccount()).isNull();
    }

    @Test
    public void deleteSubAccount_should_returnEmptyResponse_when_nothingIsFound() {
        String testSubAccountId = "SomeSome";

        when(deleteSubAccountComponent.deleteSubAccount(anyString())).thenReturn(Optional.empty());

        DeleteSubAccountResponse response =
            subAccountsController.deleteSubAccount(testSubAccountId);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getDeletedSubAccount()).isNull();
    }

}
