
creates:

            AAA
          /     \
        BBB     CCC
      /           \
    DDD            GGG
   /   \
 EEE   FFF


create table p_tree (pk int primary key, name varchar(32), parent_pk int);

create table c_tree (pk int primary key, name varchar(32));
create table c_tree_closure (ancestor int, descendant int, distance int);

delete from p_tree;

insert into p_tree (name,pk,parent_pk) values ('AAA',1,NULL);
insert into p_tree (name,pk,parent_pk) values ('BBB',2,1);
insert into p_tree (name,pk,parent_pk) values ('CCC',3,1);
insert into p_tree (name,pk,parent_pk) values ('DDD',4,2);
insert into p_tree (name,pk,parent_pk) values ('EEE',5,4);
insert into p_tree (name,pk,parent_pk) values ('FFF',6,4);
insert into p_tree (name,pk,parent_pk) values ('GGG',7,3);

delete from c_tree;
delete from c_tree_closure;

-- AAA
insert into c_tree (name,pk) values ('AAA',1);
insert into c_tree_closure (ancestor, descendant, distance) values (1,1,0);

-- BBB
insert into c_tree (name,pk) values ('BBB',2);
insert into c_tree_closure (ancestor, descendant, distance) values (2,2,0);
insert into c_tree_closure (ancestor, descendant, distance) values (1,2,1);

-- CCC
insert into c_tree (name,pk) values ('CCC',3);
insert into c_tree_closure (ancestor, descendant, distance) values (3,3,0);
insert into c_tree_closure (ancestor, descendant, distance) values (1,3,1);

-- DDD
insert into c_tree (name,pk) values ('DDD',4);
insert into c_tree_closure (ancestor, descendant, distance) values (4,4,0);
insert into c_tree_closure (ancestor, descendant, distance) values (2,4,1);
insert into c_tree_closure (ancestor, descendant, distance) values (1,4,2);

-- EEE
insert into c_tree (name,pk) values ('EEE',5);
insert into c_tree_closure (ancestor, descendant, distance) values (5,5,0);
insert into c_tree_closure (ancestor, descendant, distance) values (4,5,1);
insert into c_tree_closure (ancestor, descendant, distance) values (2,5,2);
insert into c_tree_closure (ancestor, descendant, distance) values (1,5,3);

-- FFF
insert into c_tree (name,pk) values ('FFF',6);
insert into c_tree_closure (ancestor, descendant, distance) values (6,6,0);
insert into c_tree_closure (ancestor, descendant, distance) values (4,6,1);
insert into c_tree_closure (ancestor, descendant, distance) values (2,6,2);
insert into c_tree_closure (ancestor, descendant, distance) values (1,6,3);

-- GGG
insert into c_tree (name,pk) values ('GGG',7);
insert into c_tree_closure (ancestor, descendant, distance) values (7,7,0);
insert into c_tree_closure (ancestor, descendant, distance) values (3,7,1);
insert into c_tree_closure (ancestor, descendant, distance) values (1,7,2);

-- example: descendents of 'BBB' are those with ancestor = 2;

-- example: ancestors of 'GGG' are those with descendent = 7;


