# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table contacts (
  id                        bigint auto_increment not null,
  inviter_id                bigint,
  invitee_id                bigint,
  type                      integer,
  created_at                datetime not null,
  updated_at                datetime not null,
  constraint ck_contacts_type check (type in (0,1,2)),
  constraint pk_contacts primary key (id))
;

create table gifts (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  photo_base64              longtext,
  giver_id                  bigint,
  given                     tinyint(1) default 0,
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

alter table contacts add constraint fk_contacts_inviter_1 foreign key (inviter_id) references users (id) on delete restrict on update restrict;
create index ix_contacts_inviter_1 on contacts (inviter_id);
alter table contacts add constraint fk_contacts_invitee_2 foreign key (invitee_id) references users (id) on delete restrict on update restrict;
create index ix_contacts_invitee_2 on contacts (invitee_id);
alter table gifts add constraint fk_gifts_giver_3 foreign key (giver_id) references users (id) on delete restrict on update restrict;
create index ix_gifts_giver_3 on gifts (giver_id);
alter table linked_accounts add constraint fk_linked_accounts_user_4 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_linked_accounts_user_4 on linked_accounts (user_id);
alter table requests add constraint fk_requests_requester_5 foreign key (requester_id) references users (id) on delete restrict on update restrict;
create index ix_requests_requester_5 on requests (requester_id);
alter table requests add constraint fk_requests_gift_6 foreign key (gift_id) references gifts (id) on delete restrict on update restrict;
create index ix_requests_gift_6 on requests (gift_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table contacts;

drop table gifts;

drop table linked_accounts;

drop table requests;

drop table users;

SET FOREIGN_KEY_CHECKS=1;

