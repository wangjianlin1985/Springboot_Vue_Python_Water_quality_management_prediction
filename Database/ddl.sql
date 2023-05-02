create table role
(
  id   int auto_increment
    primary key,
  name varchar(255) null
);

create table user
(
  id       int auto_increment
    primary key,
  username varchar(50) not null,
  password varchar(50) not null,
  rid      int         null,
  constraint FK8079530ngl61ed971pmagese0
  foreign key (rid) references role (id)
);

create table model
(
  id     int auto_increment
    primary key,
  name   varchar(50) not null,
  target varchar(50) not null,
  method varchar(50) null,
  rmse   float       null,
  uid    int         not null,
  date   datetime    not null,
  constraint model_user_id_fk
  foreign key (uid) references user (id)
);

create table waterquality
(
  id      int auto_increment,
  PH      float    null,
  DO      float    null,
  NH3N    float    null,
  date    datetime not null,
  station int      null,
  constraint waterquality_id_uindex
  unique (id)
);

alter table waterquality
  add primary key (id);


