SET @@global.innodb_large_prefix = 1;
SET @@global.innodb_file_format=barracuda;
SET @@global.innodb_file_per_table=true;

drop schema if exists `jhipsterapplication`;
drop schema if exists `TESTrelease`;

create database if not exists jhipsterapplication;
ALTER SCHEMA `jhipsterapplication`  DEFAULT CHARACTER SET latin1;

create database if not exists TESTrelease;
ALTER SCHEMA `TESTrelease`  DEFAULT CHARACTER SET latin1;