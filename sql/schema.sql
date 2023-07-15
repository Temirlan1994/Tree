create table tree
(
    id serial8,
    left_key int4 not null ,
    right_key int4 not null ,
    level int2 not null ,
    name varchar not null ,
    primary key (id)
);


