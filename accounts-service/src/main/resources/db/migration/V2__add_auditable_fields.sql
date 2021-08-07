ALTER TABLE accountsdb.accounts ADD COLUMN created_by varchar(30);
ALTER TABLE accountsdb.accounts ADD COLUMN created_at timestamp without time zone;
ALTER TABLE accountsdb.accounts ADD COLUMN last_updated_at timestamp without time zone;

ALTER TABLE accountsdb.sub_accounts ADD COLUMN created_by varchar(30);
ALTER TABLE accountsdb.sub_accounts ADD COLUMN created_at timestamp without time zone;
ALTER TABLE accountsdb.sub_accounts ADD COLUMN last_updated_at timestamp without time zone;