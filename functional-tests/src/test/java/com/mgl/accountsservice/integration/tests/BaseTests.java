package com.mgl.accountsservice.integration.tests;

import com.mgl.accountsservice.integration.clients.AccountsServiceClient;
import com.mgl.accountsservice.integration.modules.ClientModule;
import javax.inject.Inject;
import org.testng.annotations.Guice;

/**
 * The BaseTest class.
 */
@Guice(modules = {
    ClientModule.class,
})
public class BaseTests {

    @Inject
    protected AccountsServiceClient serviceClient;

}
