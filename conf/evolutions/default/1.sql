# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table base_model (
  id                        bigint auto_increment not null,
  created_at                datetime not null,
  updated_at                datetime not null,
  constraint pk_base_model primary key (id))
;

create table categories (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  created_at                datetime not null,
  updated_at                datetime not null,
  constraint pk_categories primary key (id))
;

create table gifts (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               longtext,
  photo_base64              longtext,
  giver_id                  bigint,
  category_id               bigint,
  created_at                datetime not null,
  updated_at                datetime not null,
  constraint pk_gifts primary key (id))
;

create table linked_accounts (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  provider_user_id          varchar(255),
  provider_key              varchar(255),
  constraint pk_linked_accounts primary key (id))
;

create table requests (
  id                        bigint auto_increment not null,
  requester_id              bigint,
  gift_id                   bigint,
  created_at                datetime not null,
  updated_at                datetime not null,
  constraint pk_requests primary key (id))
;

create table users (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  name                      varchar(255),
  active                    tinyint(1) default 0,
  email_validated           tinyint(1) default 0,
  created_at                datetime not null,
  updated_at                datetime not null,
  constraint pk_users primary key (id))
;

alter table gifts add constraint fk_gifts_giver_1 foreign key (giver_id) references users (id) on delete restrict on update restrict;
create index ix_gifts_giver_1 on gifts (giver_id);
alter table gifts add constraint fk_gifts_category_2 foreign key (category_id) references categories (id) on delete restrict on update restrict;
create index ix_gifts_category_2 on gifts (category_id);
alter table linked_accounts add constraint fk_linked_accounts_user_3 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_linked_accounts_user_3 on linked_accounts (user_id);
alter table requests add constraint fk_requests_requester_4 foreign key (requester_id) references users (id) on delete restrict on update restrict;
create index ix_requests_requester_4 on requests (requester_id);
alter table requests add constraint fk_requests_gift_5 foreign key (gift_id) references gifts (id) on delete restrict on update restrict;
create index ix_requests_gift_5 on requests (gift_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table base_model;

drop table categories;

drop table gifts;

drop table linked_accounts;

drop table requests;

drop table users;

SET FOREIGN_KEY_CHECKS=1;

