package com.mgl.accountsservice.dao.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import java.io.InputStream;
import java.sql.Statement;
import java.time.LocalDateTime;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

/** Tests the production mapper in an isolated PostgreSQL database. */
@EnabledIfEnvironmentVariable(named = "ACCOUNTS_TEST_DB_URL", matches = ".+")
public class SubAccountsMapperDatabaseTests {

    @Test
    public void update_should_preserveCreationMetadataAndAdvanceUpdatedAt() throws Exception {
        UnpooledDataSource dataSource = new UnpooledDataSource("org.postgresql.Driver",
            System.getenv("ACCOUNTS_TEST_DB_URL"), System.getenv("ACCOUNTS_TEST_DB_USER"),
            System.getenv("ACCOUNTS_TEST_DB_PASSWORD"));
        Configuration configuration = new Configuration(new Environment("test",
            new JdbcTransactionFactory(), dataSource));
        String resource = "mybatis/mappers/SubAccountsMapper.xml";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resource)) {
            new XMLMapperBuilder(input, configuration, resource,
                configuration.getSqlFragments()).parse();
        }
        // All DDL and writes roll back when this non-autocommit session closes.
        try (SqlSession session = new SqlSessionFactoryBuilder().build(configuration).openSession()) {
            try (Statement statement = session.getConnection().createStatement()) {
                statement.execute("CREATE SCHEMA accountsdb");
                for (String table : new String[] {"accounts", "sub_accounts"}) {
                    try (InputStream input = getClass().getClassLoader()
                            .getResourceAsStream("db/schema/" + table + ".sql")) {
                        statement.execute(new String(input.readAllBytes(),
                            java.nio.charset.StandardCharsets.UTF_8));
                    }
                }
                statement.execute("INSERT INTO accountsdb.accounts (id, name, account_type) "
                    + "VALUES ('acct-1', 'first', 'Capital'), ('acct-2', 'second', 'Capital')");
                statement.execute("INSERT INTO accountsdb.sub_accounts "
                    + "(id, description, account_id, created_by, created_at, last_updated_at) "
                    + "VALUES ('child-1', 'original', 'acct-1', 'creator', "
                    + "TIMESTAMP '2001-01-01 00:00:00', TIMESTAMP '2002-01-01 00:00:00')");
            }
            SubAccountsMapper mapper = session.getMapper(SubAccountsMapper.class);
            mapper.putSubAccount(SubAccountEntity.builder().id("child-1")
                .description("edited").accountId("acct-2").build());
            SubAccountEntity actual = mapper.getSubAccount("child-1");

            assertThat(actual.getCreatedAt()).isEqualTo(LocalDateTime.of(2001, 1, 1, 0, 0));
            assertThat(actual.getCreatedBy()).isEqualTo("creator");
            assertThat(actual.getLastUpdatedAt()).isAfter(LocalDateTime.of(2002, 1, 1, 0, 0));
            assertThat(actual.getDescription()).isEqualTo("edited");
            assertThat(actual.getAccountId()).isEqualTo("acct-2");
        }
    }
}
