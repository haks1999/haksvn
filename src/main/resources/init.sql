insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (1, 	'Transfer'			, '/transfer'								,1, 100, 1, '00');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (2, 	'Request'			, '/transfer/request/retrieveRequestList'	,1, 100, 2, '00');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (3, 	'Approval'			, '/transfer/approval'						,1, 200, 2, '00');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (4, 	'Configuration'		, '/configuration'							,4, 200, 1, '00');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (5, 	'Users'				, '/configuration/users/list'				,4, 100, 2, '01');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (6, 	'List'				, '/configuration/users/list'				,5, 100, 3, '01');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (7, 	'Add'				, '/configuration/users/add'				,5, 200, 3, '01');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (8, 	'Repositories'		, '/configuration/repositories/list'		,4, 200, 2, '01');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (9, 	'List'				, '/configuration/repositories/list'		,8, 100, 3, '01');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (10,	'Add'				, '/configuration/repositories/add'			,8, 200, 3, '01');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (11,	'Repository_User'	, '/configuration/repositories/listUser'	,8, 300, 3, '01');

insert into code (code_seq, code_group, code_name, code_value, code_order) values (1,'menu_view_type_code', 'default', '00', 10 );
insert into code (code_seq, code_group, code_name, code_value, code_order) values (2,'menu_view_type_code', 'leftmenu', '01', 20 );
insert into code (code_seq, code_group, code_name, code_value, code_order) values (3,'request_type_code', 'normal', '00', 10 );
insert into code (code_seq, code_group, code_name, code_value, code_order) values (4,'request_type_code', 'emergency', '01', 20 );
insert into code (code_seq, code_group, code_name, code_value, code_order) values (5,'common_boolean_yn_code', 'Y', 'true', 10 );
insert into code (code_seq, code_group, code_name, code_value, code_order) values (6,'common_boolean_yn_code', 'N', 'false', 20 );
insert into code (code_seq, code_group, code_name, code_value, code_order) values (7,'user_auth_type_code', 'system-admin', '10', 10 );
insert into code (code_seq, code_group, code_name, code_value, code_order) values (8,'user_auth_type_code', 'reviewer', '20', 20 );
insert into code (code_seq, code_group, code_name, code_value, code_order) values (9,'user_auth_type_code', 'commiter', '30', 30 );

insert into repositories (repository_seq, repository_location, active, repository_name, trunk_path, tags_path, auth_user_id, auth_user_passwd) values (1,'https://haksvn.googlecode.com/svn', 'Y', 'test svn', '/trunk', '/tags', 'haks1999', 'aW9fj8bm9Rt5' );

insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (1,'admin','administrator','Y','admin@gmail.com','admin', '10');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type ) values (2,'haks','seungrin.lee','Y','haks1999@gmail.com','haks', '10');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type ) values (3,'ggae','ildong.yang','Y','ggae123@gmail.com','ggae', '10');