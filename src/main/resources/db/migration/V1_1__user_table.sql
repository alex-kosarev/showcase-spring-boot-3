create table t_application_user
(
    id         uuid primary key,
    c_username varchar(255) not null,
    c_password text
);

create unique index idx_application_user_username on t_application_user (c_username);

alter table t_task
    add column id_application_user uuid not null references t_application_user (id);

create index idx_task_application_user on t_task (id_application_user);
