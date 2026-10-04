create table accountsdb.accounts (
    id varchar(20) not null primary key,
    name varchar(100) not null,
    account_type varchar(10) not null,
    created_by varchar(30),
    created_at timestamp without time zone,
    last_updated_at timestamp without time zone
);
CREATE INDEX accounts_listing_order_idx ON accountsdb.accounts (created_at DESC NULLS LAST, id ASC);
