insert into t_application_user(id, c_username, c_password)
values ('5d730ada-9c91-11ed-bef5-631db7f28980', 'user1', '{noop}password1'),
       ('5da0a544-9c91-11ed-a601-23bef39fb8a6', 'user2', '{noop}password2');

insert into t_task(id, c_details, c_completed, id_application_user)
values ('71117396-8694-11ed-9ef6-77042ee83937', 'Первая задача', false, '5d730ada-9c91-11ed-bef5-631db7f28980'),
       ('7172d834-8694-11ed-8669-d7b17d45fba8', 'Вторая задача', true, '5d730ada-9c91-11ed-bef5-631db7f28980'),
       ('0c32044a-9c92-11ed-b56f-67b8de28d2dc', 'Третья задача', false, '5da0a544-9c91-11ed-a601-23bef39fb8a6');
