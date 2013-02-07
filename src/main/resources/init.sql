insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (1, 	'Transfer'			, '/transfer'								,1, 100, 1, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (2, 	'Request'			, '/transfer/request/retrieveRequestList'	,1, 100, 2, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (3, 	'Approval'			, '/transfer/approval'						,1, 200, 2, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (4, 	'Configuration'		, '/configuration'							,4, 200, 1, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (5, 	'Users'				, '/configuration/users/list'				,4, 100, 2, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (6, 	'List'				, '/configuration/users/list'				,5, 100, 3, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (7, 	'Add'				, '/configuration/users/add'				,5, 200, 3, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (8, 	'Repositories'		, '/configuration/repositories/list'		,4, 200, 2, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (9, 	'List'				, '/configuration/repositories/list'		,8, 100, 3, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (10,	'Add'				, '/configuration/repositories/add'			,8, 200, 3, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (11,	'Repository_User'	, '/configuration/repositories/listUser'	,8, 300, 3, 'menu.view.type.code.leftmenu');

insert into code (code_id, code_group, code_name, code_value, code_order) values ('menu.view.type.code.default'		,'menu.view.type.code'			,'default'		,'00'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('menu.view.type.code.leftmenu'		,'menu.view.type.code'			,'leftmenu'		,'01'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('request.type.code.normal'			,'request.type.code'			,'normal'		,'00'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('request.type.code.emergency'		,'request.type.code'			,'emergency'	,'01'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('common.boolean.yn.code.y'			,'common.boolean.yn.code'		,'Y'			,'true'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('common.boolean.yn.code.n'			,'common.boolean.yn.code'		,'N'			,'false',20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('user.auth.type.code.system-admin'	,'user.auth.type.code'			,'system-admin'	,'10'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('user.auth.type.code.reviewer'		,'user.auth.type.code'			,'reviewer'		,'20'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('user.auth.type.code.commiter'		,'user.auth.type.code'			,'commiter'		,'30'	,30 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('server.connect.type.code.local'	,'server.connect.type.code'		,'local'		,'n/a'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('server.connect.type.code.ssh'		,'server.connect.type.code'		,'ssh'			,'22'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('server.connect.type.code.telnet'	,'server.connect.type.code'		,'telnet'		,'23'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('svn.passwd.type.code.plain'		,'svn.passwd.type.code'			,'plain text'	,'10'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('svn.passwd.type.code.md5-apache'	,'svn.passwd.type.code'			,'MD5(Apache)'	,'20'	,20 );

insert into repositories (repository_seq, repository_location, active, repository_name, trunk_path, tags_path, auth_user_id, auth_user_passwd, sync_user ) values (1,'https://haksvn.googlecode.com/svn', 'common.boolean.yn.code.y', 'haksvn google repository', '/trunk', '/tags', 'haks1999', 'aW9fj8bm9Rt5','common.boolean.yn.code.n' );
insert into repositories (repository_seq, repository_location, active, repository_name, trunk_path, tags_path, auth_user_id, auth_user_passwd, sync_user ) values (2,'https://localhost/svn/main', 'common.boolean.yn.code.y', 'test local repository', '/trunk', '/tags', 'haks1999', 'haks1999','common.boolean.yn.code.y' );

insert into repository_server (repository_seq, connect_type, server_ip, user_id, user_passwd, authz_path, passwd_path, passwd_type) values (2, 'server.connect.type.code.local', null, null, null, 'c:\ss', 'c:\aa', 'svn.passwd.type.code.md5-apache' );

insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (1,'admin','administrator','common.boolean.yn.code.y','admin@gmail.com','admin', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (2,'haks','seungrin.lee','common.boolean.yn.code.y','haks1999@gmail.com','haks', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (3,'ggae','ildong.yang','common.boolean.yn.code.y','ggae123@gmail.com','ggae', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (4,'spike0201','jinyoon.kim','common.boolean.yn.code.y','spike0201@lgcns.com','1234', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (5,'user01','test-user001','common.boolean.yn.code.y','user01@gmail.com','user01', 'user.auth.type.code.commiter');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (6,'user02','test-user002','common.boolean.yn.code.y','user02@gmail.com','user02', 'user.auth.type.code.commiter');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (7,'user03','test-user003','common.boolean.yn.code.y','user03@gmail.com','user03', 'user.auth.type.code.commiter');

insert into repositories_users(repository_seq, user_seq) values (1,1);
insert into repositories_users(repository_seq, user_seq) values (1,2);
insert into repositories_users(repository_seq, user_seq) values (1,3);