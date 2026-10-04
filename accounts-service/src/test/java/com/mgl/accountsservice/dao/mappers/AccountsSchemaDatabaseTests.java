package com.mgl.accountsservice.dao.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Verifies snapshot and migration SQL against isolated PostgreSQL. */
@EnabledIfEnvironmentVariable(named = "ACCOUNTS_TEST_DB_URL", matches = ".+")
public class AccountsSchemaDatabaseTests {
    /** Verifies both provisioning paths.
     *
     * @param migrations whether to apply migration SQL rather than the snapshot
     * @throws Exception if database setup fails
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void bothSchemas_should_accept100CharacterTitlesAndReject101(boolean migrations) throws Exception {
        try (Connection connection = DriverManager.getConnection(System.getenv("ACCOUNTS_TEST_DB_URL"),
                System.getenv("ACCOUNTS_TEST_DB_USER"), System.getenv("ACCOUNTS_TEST_DB_PASSWORD"))) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE SCHEMA accountsdb");
                String[] files = migrations ? new String[] {"migration/V1__initial_setup.sql",
                    "migration/V2__add_auditable_fields.sql", "migration/V3__increase_acount_title_length.sql",
                    "migration/V4__add_account_listing_index.sql"}
                    : new String[] {"schema/accounts.sql"};
                for (String file : files) {
                    try (InputStream input = getClass().getClassLoader().getResourceAsStream("db/" + file)) {
                        statement.execute(new String(input.readAllBytes(), StandardCharsets.UTF_8));
                    }
                }
                try (ResultSet rows = statement.executeQuery("SELECT indexdef FROM pg_indexes WHERE "
                    + "schemaname = 'accountsdb' AND indexname = 'accounts_listing_order_idx'")) {
                    assertThat(rows.next()).isTrue();
                    assertThat(rows.getString(1)).contains("created_at DESC NULLS LAST", "id");
                }
                statement.execute("INSERT INTO accountsdb.accounts (id, name, account_type) "
                    + "VALUES ('a', '" + "x".repeat(100) + "', 'Capital')");
                try (ResultSet rows = statement.executeQuery("SELECT length(name) FROM accountsdb.accounts WHERE id = 'a'")) {
                    assertThat(rows.next()).isTrue();
                    assertThat(rows.getInt(1)).isEqualTo(100);
                }
                assertThatThrownBy(() -> statement.execute("INSERT INTO accountsdb.accounts (id, name, account_type) "
                    + "VALUES ('b', '" + "x".repeat(101) + "', 'Capital')"))
                    .isInstanceOf(SQLException.class);
            } finally {
                connection.rollback();
            }
        }
    }
}
