create table kingdom (id bigint not null, name varchar(255), user_id bigint, primary key (id)) engine=MyISAM
create table user_kingdoms (user_id bigint not null, kingdoms_id bigint not null) engine=MyISAM
alter table user_kingdoms drop index UK_q7ep27p72iiemngsyoiddgw71
alter table user_kingdoms add constraint UK_q7ep27p72iiemngsyoiddgw71 unique (kingdoms_id)
alter table user_kingdoms add constraint FKqqj71hc6fihk6o8eygtqtwmwk foreign key (kingdoms_id) references kingdom (id)
alter table user_kingdoms add constraint FKq6jx4j8pdk52j1ch9x1apglsd foreign key (user_id) references user (id)
create table kingdom (id bigint not null, name varchar(255), user_id bigint, primary key (id)) engine=MyISAM
create table user_kingdoms (user_id bigint not null, kingdoms_id bigint not null) engine=MyISAM
alter table user_kingdoms drop index UK_q7ep27p72iiemngsyoiddgw71
alter table user_kingdoms add constraint UK_q7ep27p72iiemngsyoiddgw71 unique (kingdoms_id)
alter table user_kingdoms add constraint FKqqj71hc6fihk6o8eygtqtwmwk foreign key (kingdoms_id) references kingdom (id)
alter table user_kingdoms add constraint FKq6jx4j8pdk52j1ch9x1apglsd foreign key (user_id) references user (id)
