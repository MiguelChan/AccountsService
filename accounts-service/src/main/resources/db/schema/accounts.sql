create table accountsdb.accounts (
    id varchar(20) not null primary key,
    name varchar(10) not null,
    account_type varchar(10) not null,
    created_by varchar(30),
    created_at timestamp without time zone,
    last_updated_at timestamp without time zone
);