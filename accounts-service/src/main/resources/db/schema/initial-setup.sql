-- The commands below need to be run with the admin user.
create user flyway_user with password 'flyway-user-password';
create user app_user with password 'app-user-password';

create schema accountsdb;
grant all privileges on schema accountsdb to flyway_user;
grant usage on schema accountsdb to app_user;
grant all privileges on all tables in schema accountsdb to app_user;
alter default privileges in schema accountsdb grant all on tables to app_user;