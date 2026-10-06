# Contributing

Use small, verifiable changes on feature branches. Open pull requests as drafts.
The first PR in a stack targets `mainline`; dependent PRs target the preceding branch.

## Issues

Use the bug report template with these sections:

- **Overview**: the issue, actual and expected behavior, impact, and evidence.
- **Reproduction**: local prerequisites, steps, code samples or unit tests, and results.
  For production bugs, explain how to reproduce locally without real secrets or customer data.
  Be explicit when a finding has not yet been reproduced.
- **Acceptance Criteria**: observable, testable conditions that define completion.

## Pull requests and commits

Use this format for PR titles and commit subjects:

```text
feat|fix|bug|docs|chores(moduleName): <description>
```

Choose exactly one of `feat`, `fix`, `bug`, `docs`, or `chores`. Replace `moduleName`
with the affected module, such as `accounts` or `repo`. Keep the description under
80 characters. Example:

```text
fix(accounts): preserve created_at when updating subaccounts
```

PR bodies use exactly these sections:

- **What is changing**: both behavior and code changes, with the related issue.
- **Why is it changing**: a concise trace of the observations, investigation, and
  decisions that led to this change.
- **Testing done**: tests actually run and their results, plus relevant gaps.

GitHub loads the PR and issue templates from `.github`. They become the repository
defaults once merged into `mainline`. Templates guide writing; they do not enforce
title or commit validation.

To enable the commit template for a checkout, run:

```sh
git config --local commit.template "$(git rev-parse --show-toplevel)/.gitmessage"
```

This is optional, local configuration. Git does not automatically activate a tracked
commit template. Commands using `git commit -m` must follow the format explicitly.

## PostgreSQL mapper tests

The mapper test runs against a disposable PostgreSQL database. Supply
`ACCOUNTS_TEST_DB_URL` (JDBC URL), `ACCOUNTS_TEST_DB_USER`, and
`ACCOUNTS_TEST_DB_PASSWORD` for a dedicated, empty test database, then run:

```sh
bash gradlew :accounts-service:test --no-daemon --max-workers=1
```

Without the URL, database tests are skipped. The test creates `accountsdb` inside
an uncommitted transaction and rolls it back when the session closes. It fails if
that schema already exists; never point it at a shared or production database.

## Database configuration

Provide application connection values through `JDBC_DATABASE_URL` (JDBC URL),
`JDBC_DATABASE_USERNAME`, and `JDBC_DATABASE_PASSWORD` in both dev and prod profiles.
Provide migrations with `FLYWAY_URL`, `FLYWAY_USER`, and `FLYWAY_PASSWORD`.
For example, after supplying those variables in your shell or secret manager:

```sh
bash gradlew :accounts-service:flywayMigrate --no-daemon
```

Keep deployment values out of tracked files and command history. Local `.env`
files are ignored, but are not loaded automatically by Gradle or Spring.
The bootstrap SQL under `db/schema/initial-setup.sql` contains example local roles;
use dedicated deployment users with credentials managed outside the repository.

Remote credentials previously committed in Gradle must be revoked or rotated by
the database owner. Removing them from the current tree does not remove historical
copies. Check provider access logs and decide whether coordinated history cleanup
is needed; this change does not rewrite Git history.

## Isolated HTTP regression suite

With Docker and the configured JDK available, run `python3 scripts/test-http.py`.
It creates an isolated PostgreSQL container, applies migrations, builds and starts
the packaged app on loopback with an ephemeral port, runs the functional suite and
a deterministic CRUD workflow, then stops the app and removes the container even
on failure. It does not connect to the database specified in your normal shell.
`ACCOUNTS_SERVICE_BASE_URL` lets the functional client target this instance.
The CI workflow runs this suite for every PR, including intermediate stack bases.

## Java and Gradle

Use JDK 25 and the root `./gradlew` wrapper for every module. Set `JAVA_HOME`
to your JDK 25 installation before invoking the isolated regression harness.
Module-local wrapper copies have been removed to keep one authoritative version.
The Java toolchain makes local compilation and tests use the same baseline as CI.

## Framework baseline

The service uses Spring Boot 3.5.16, MyBatis starter 3.0.5, Flyway 11.20.3,
and Gradle 9.8.0 on Java 25. Flyway includes the PostgreSQL database module
in the Gradle migration classpath. Migrations remain an explicit deployment step;
starting the application does not run schema changes.
SecurityFilterChain preserves anonymous API access, disabled CSRF for the existing
API, MVC CORS handling and the existing proxy HTTPS requirement. This migration
does not introduce authentication or broaden allowed cross-origin access.

## HTTP contract

Creation returns 201; successful reads, edits and deletes return 200 with the
existing success payload. Missing accounts/children (including PUT) return 404.
Invalid or malformed requests return 400; unsupported media types/methods return
415/405. Database and unexpected server failures return 500. Errors use
`{"success":false,"code":"NOT_FOUND","message":"Account not found"}`;
500 responses contain safe messages, never SQL/connection exception details.
This intentionally changes the old always-200 failure contract: clients must
check status before parsing successful responses. The functional client throws
HttpStatusException with the actual status for non-2xx responses.

POST/PUT require the user, title, accountType and subAccounts (an empty list is
valid); children require descriptions. PUT also requires the account ID and
nonblank unique child IDs when updating existing children. Null/blank inputs
are rejected before transactional writes.

## Account pages and input bounds

`GET /api/accounts?limit=50&offset=0` returns `accounts`, `limit`, `offset` and
`hasMore`. The default is 50, the maximum 100; offset is 0..100000. Invalid or
nonnumeric bounds return 400. Fetch the next page with offset plus limit when
hasMore is true. Ordering is created_at descending (nulls last), then ID ascending;
V4 adds the matching index and the bootstrap schema includes it too. A page reads
at most limit+1 parents for lookahead and performs one batched child query.
The no-argument component/DAO convenience methods now return the first 50.
This changes the former all-accounts response; callers needing all rows must page.
Offset pages are deterministic for an unchanged dataset, not a snapshot across
concurrent insertions/deletions. Child rows are ordered by their IDs.

Titles/descriptions are limited to 100 Unicode characters, user names to 30 and
update IDs to 20, matching PostgreSQL column sizes. Each write accepts at most
100 children and PUT cannot grow an account past 100 children or reassign children
from another account. PUT locks the parent row before reading children, so concurrent
additions cannot race past the cumulative limit. The JSON collection limit is checked after deserialization;
this is not a raw HTTP payload byte limit.
