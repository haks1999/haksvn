insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (1, 'Transfer', '/transfer', 1, 100, 1, '00');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (2, 'Request', '/transfer/request/retrieveRequestList',1, 100, 2, '00');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (3, 'Approval', '/transfer/approval',1, 200, 2, '00');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (4, 'Configuration', '/configuration', 4, 200, 1, '00');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (5, 'Users', '/configuration/users', 4, 100, 2, '00');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (6, 'Repositories', '/configuration/repositories/list', 4, 200,2, '01' );
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (7, 'List', '/configuration/repositories/list', 6, 100,3 , '01');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (8, 'Add', '/configuration/repositories/add', 6, 200,3 , '01');

insert into code (code_seq, code_group, code_name, code_value, code_order) values (1,'repository.status', 'active', '10', 10 );
insert into code (code_seq, code_group, code_name, code_value, code_order) values (2,'repository.status', 'inactive', '20', 20 );

insert into repositories (repository_seq, repository_location, repository_status, repository_name) values (1,'svn://127.0.0.1/svn', '20', 'test svn' );