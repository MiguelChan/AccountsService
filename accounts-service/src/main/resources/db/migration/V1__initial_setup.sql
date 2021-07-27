create table accountsdb.accounts (
    id varchar(20) not null primary key,
    name varchar(10) not null,
    account_type varchar(10) not null
);

create table accountsdb.sub_accounts (
    id varchar(20) not null primary key,
    description varchar(100) not null,
    account_id varchar(20) not null,
    constraint fk_account foreign key (account_id) references accountsdb.accounts(id) on delete cascade
);