package com.mgl.accountsservice.dao.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgl.accountsservice.components.GetAccountsComponent;
import com.mgl.accountsservice.dao.impl.MyBatisAccountsDao;
import com.mgl.accountsservice.dao.impl.MyBatisSubAccountsDao;
import com.mgl.accountsservice.mappers.AccountsEntityMapper;
import com.mgl.accountsservice.mappers.SubAccountsEntityMapper;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.utils.RandomIdGenerator;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

/** Verifies actual SQL query count and grouping using PostgreSQL. */
@EnabledIfEnvironmentVariable(named = "ACCOUNTS_TEST_DB_URL", matches = ".+")
public class AccountListingDatabaseTests {
    /** Counts SQL statements prepared by MyBatis. */
    @Intercepts(@Signature(type = StatementHandler.class, method = "prepare",
        args = {Connection.class, Integer.class}))
    public static class QueryCounter implements Interceptor {
        private int reads;

        @Override
        public Object intercept(Invocation invocation) throws Throwable {
            reads++;
            return invocation.proceed();
        }
    }

    @Test
    public void listing_should_useTwoQueriesAndKeepEmptyAccounts() throws Exception {
        Configuration config = new Configuration(new Environment("test", new JdbcTransactionFactory(),
            new UnpooledDataSource("org.postgresql.Driver", System.getenv("ACCOUNTS_TEST_DB_URL"),
                System.getenv("ACCOUNTS_TEST_DB_USER"), System.getenv("ACCOUNTS_TEST_DB_PASSWORD"))));
        QueryCounter counter = new QueryCounter();
        config.addInterceptor(counter);
        for (String name : List.of("Accounts", "SubAccounts")) {
            String resource = "mybatis/mappers/" + name + "Mapper.xml";
            try (InputStream input = getClass().getClassLoader().getResourceAsStream(resource)) {
                new XMLMapperBuilder(input, config, resource, config.getSqlFragments()).parse();
            }
        }
        try (SqlSession session = new SqlSessionFactoryBuilder().build(config).openSession()) {
            try (Statement statement = session.getConnection().createStatement()) {
                statement.execute("CREATE SCHEMA accountsdb");
                for (String table : List.of("accounts", "sub_accounts")) {
                    try (InputStream input = getClass().getClassLoader().getResourceAsStream("db/schema/" + table + ".sql")) {
                        statement.execute(new String(input.readAllBytes(), StandardCharsets.UTF_8));
                    }
                }
                statement.execute("INSERT INTO accountsdb.accounts (id, name, account_type) VALUES "
                    + "('a', 'first', 'Capital'), ('b', 'second', 'Capital'), ('c', 'empty', 'Capital')");
                statement.execute("INSERT INTO accountsdb.sub_accounts (id, description, account_id) VALUES "
                    + "('s1', 'one', 'a'), ('s2', 'two', 'a'), ('s3', 'three', 'b')");
            }
            try {
                GetAccountsComponent component = new GetAccountsComponent(
                    new MyBatisAccountsDao(session.getMapper(AccountsMapper.class), new RandomIdGenerator()),
                    new AccountsEntityMapper(),
                    new MyBatisSubAccountsDao(session.getMapper(SubAccountsMapper.class), new RandomIdGenerator()),
                    new SubAccountsEntityMapper());
                List<Account> accounts = component.getAccounts();
                assertThat(counter.reads).isEqualTo(2);
                assertThat(accounts).hasSize(3);
                for (Account account : accounts) {
                    int expected = account.getId().equals("a") ? 2 : account.getId().equals("b") ? 1 : 0;
                    assertThat(account.getSubAccounts()).hasSize(expected);
                    List<String> expectedIds = account.getId().equals("a") ? List.of("s1", "s2")
                        : account.getId().equals("b") ? List.of("s3") : List.of();
                    assertThat(account.getSubAccounts()).extracting(child -> child.getId())
                        .containsExactlyInAnyOrderElementsOf(expectedIds);
                }
                counter.reads = 0;
                assertThat(component.getAccounts(1, 0)).extracting(Account::getId).containsExactly("a");
                assertThat(counter.reads).isEqualTo(2);
                assertThat(component.getAccounts(1, 1)).extracting(Account::getId).containsExactly("b");
                assertThat(component.getAccounts(1, 2)).extracting(Account::getId).containsExactly("c");
                assertThat(component.getAccounts(1, 3)).isEmpty();
                assertThat(component.getAccounts(1, 0)).extracting(Account::getId).containsExactly("a");
                SubAccountsMapper mapper = session.getMapper(SubAccountsMapper.class);
                assertThat(mapper.getSubAccountsForAccounts(List.of("b"))).extracting(row -> row.getId()).containsExactly("s3");
                assertThat(mapper.getSubAccountsForAccounts(List.of())).isEmpty();
                try (Statement statement = session.getConnection().createStatement()) {
                    statement.execute("DELETE FROM accountsdb.accounts");
                }
                session.clearCache();
                counter.reads = 0;
                assertThat(component.getAccounts()).isEmpty();
                assertThat(counter.reads).isEqualTo(1);
            } finally {
                session.rollback(true);
            }
        }
    }
}
