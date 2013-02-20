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
--insert into code (code_id, code_group, code_name, code_value, code_order) values ('server.connect.type.code.ssh'		,'server.connect.type.code'		,'ssh'			,'22'	,20 );
--insert into code (code_id, code_group, code_name, code_value, code_order) values ('server.connect.type.code.telnet'	,'server.connect.type.code'		,'telnet'		,'23'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('svn.passwd.type.code.plain'		,'svn.passwd.type.code'			,'plain text'	,'10'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('svn.passwd.type.code.md5-apache'	,'svn.passwd.type.code'			,'MD5(Apache)'	,'20'	,20 );

insert into properties (property_key, property_value) values ('svn.authz.template.default'	,'[groups]%nsystem-admin=#system-admin#%ncommiter=#commiter#%nreviewer=#reviewer#%n%n[#svn_name#:/]%n@system-admin=rw%n%n[#svn_name#:#trunk_path#]%n@reviewer=rw%n@commiter=rw%n%n[#svn_name#:#branches_path#]%n@reviewer=r%n@commiter=r%n%n[#svn_name#:#tags_path#]%n@reviewer=rw%n@commiter=r');

-- haks1999 // aW9fj8bm9Rt5
insert into repositories (repository_seq, repository_location, active, repository_name, svn_root, svn_name, trunk_path, tags_path, branches_path, auth_user_id, auth_user_passwd, sync_user ) values (1,'https://haksvn.googlecode.com/svn', 'common.boolean.yn.code.y', 'haksvn google repository', 'https://haksvn.googlecode.com/svn', 'svn', '/trunk', '/tags', '/branches/production', 'haks1999', 'nijy14K6p4n1oleemgofSw==','common.boolean.yn.code.n' );
-- haks1999 // haks1999
insert into repositories (repository_seq, repository_location, active, repository_name, svn_root, svn_name, trunk_path, tags_path, branches_path, auth_user_id, auth_user_passwd, sync_user, connect_type, server_ip, server_user_id, server_user_passwd, authz_path, passwd_path, passwd_type, authz_template) values (2,'https://localhost/svn/main/haksvn', 'common.boolean.yn.code.y', 'test local repository', 'https://localhost/svn/main', 'main', '/trunk', '/tags', '/branches/production', 'haks1999', 'ty0VNz1v8qkwfc8Cpz1NXA==','common.boolean.yn.code.y', 'server.connect.type.code.local', null, null, null, 'D:\MyDev\haks1999\repositories\authz', 'D:\MyDev\haks1999\repositories\htpasswd', 'svn.passwd.type.code.md5-apache', '[groups]%nsystem-admin=#system-admin#%ncommiter=#commiter#%nreviewer=#reviewer#%n%n[#svn_name#:/]%n@system-admin=rw%n%n[#svn_name#:#trunk_path#]%n@reviewer=rw%n@commiter=rw%n%n[#svn_name#:#branches_path#]%n@reviewer=r%n@commiter=r%n%n[#svn_name#:#tags_path#]%n@reviewer=rw%n@commiter=r' );

insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (1,'admin','administrator','common.boolean.yn.code.y','admin@gmail.com','CinA5MJWDvBTvOJSvluE4g==', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (2,'haks','seungrin.lee','common.boolean.yn.code.y','haks1999@gmail.com','HpY9T+GzO+pGi4KuzUuSjg==', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (3,'ggae','ildong.yang','common.boolean.yn.code.y','ggae123@gmail.com','twYwNKOTe6WqcnUZlQWy0Q==', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (4,'spike0201','jinyoon.kim','common.boolean.yn.code.y','spike0201@lgcns.com','w9oKTqKTtvBuRUVbhQP/qw==', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (5,'user01','test-user001','common.boolean.yn.code.y','user01@gmail.com','O1C0K2pANKmoLtYUBBqryw==', 'user.auth.type.code.commiter');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (6,'user02','test-user002','common.boolean.yn.code.y','user02@gmail.com','ej3AUzTKsETeEyDwsmeBbQ==', 'user.auth.type.code.commiter');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (7,'user03','test-user003','common.boolean.yn.code.y','user03@gmail.com','MvglwVcxLRl64J1COgxx2g==', 'user.auth.type.code.commiter');

insert into repositories_users(repository_seq, user_seq) values (1,1);
insert into repositories_users(repository_seq, user_seq) values (1,2);
insert into repositories_users(repository_seq, user_seq) values (1,3);