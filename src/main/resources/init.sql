delete from transfer_source;
delete from transfer;
delete from transfer_group;
delete from tagging;
delete from properties;
delete from menu_authority;
delete from repositories_users;
delete from menu;
delete from repositories;
delete from users;
delete from code;

insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (100, 	'Transfer'			, '/transfer'								,100	,100, 1, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (110, 	'Request'			, '/transfer/request/list'					,100	,100, 2, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (120, 	'Request_Group'		, '/transfer/requestGroup/list'				,100	,200, 2, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (130, 	'Tagging'			, '/transfer/tagging/list'					,100	,300, 2, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (200, 	'Source'			, '/source'									,200	,200, 1, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (210, 	'Browse'			, '/source/browse'							,200	,100, 2, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (220, 	'Changes'			, '/source/changes'							,200	,200, 2, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (300, 	'Configuration'		, '/configuration'							,300	,300, 1, 'menu.view.type.code.default');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (310, 	'General'			, '/configuration/general/commitLog'		,300	,100, 2, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (311, 	'Commit_Log'		, '/configuration/general/commitLog'		,310	,100, 3, 'menu.view.type.code.leftmenu');
--insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (312, 	'Data_Management'	, '/configuration/general/dataManagement'	,310	,200, 3, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (320, 	'Users'				, '/configuration/users/list'				,300	,200, 2, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (321, 	'List'				, '/configuration/users/list'				,320	,100, 3, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (322, 	'Add'				, '/configuration/users/add'				,320	,200, 3, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (330, 	'Repositories'		, '/configuration/repositories/list'		,300	,300, 2, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (331, 	'List'				, '/configuration/repositories/list'		,330	,100, 3, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (332,		'Add'				, '/configuration/repositories/add'			,330	,200, 3, 'menu.view.type.code.leftmenu');
insert into menu (menu_seq, menu_name, menu_url, parent_menu_seq, menu_order, menu_level, view_type) values (333,		'Repository_User'	, '/configuration/repositories/listUser'	,330	,300, 3, 'menu.view.type.code.leftmenu');

insert into code (code_id, code_group, code_name, code_value, code_order) values ('menu.view.type.code.default'		,'menu.view.type.code'			,'default'		,'00'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('menu.view.type.code.leftmenu'		,'menu.view.type.code'			,'leftmenu'		,'01'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfer.type.code.normal'			,'transfer.type.code'			,'Normal'		,'00'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfer.type.code.emergency'		,'transfer.type.code'			,'Emergency'	,'01'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfer.state.code.keep'			,'transfer.state.code'			,'Keep'			,'00'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfer.state.code.request'		,'transfer.state.code'			,'Request'		,'10'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfer.state.code.approved'		,'transfer.state.code'			,'Approved'		,'20'	,30 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfer.state.code.standby'		,'transfer.state.code'			,'Stand by'		,'30'	,40 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfer.state.code.transfered'	,'transfer.state.code'			,'Transfered'	,'40'	,50 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfer.state.code.rejected'		,'transfer.state.code'			,'Rejected'		,'40'	,50 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfer.source.type.code.add'		,'transfer.source.type.code'	,'Add'			,'00'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfer.source.type.code.modify'	,'transfer.source.type.code'	,'Modify'		,'10'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfer.source.type.code.delete'	,'transfer.source.type.code'	,'Delete'		,'20'	,30 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfergroup.type.code.normal'	,'transfergroup.type.code'		,'Normal'		,'00'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfergroup.type.code.emergency'	,'transfergroup.type.code'		,'Emergency'	,'10'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfergroup.type.code.scheduled'	,'transfergroup.type.code'		,'Scheduled'	,'20'	,30 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfergroup.state.code.standby'	,'transfergroup.state.code'		,'Stand by'		,'00'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('transfergroup.state.code.transfered'	,'transfergroup.state.code'	,'Transfered'	,'10'	,20 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('tagging.type.code.create'			,'tagging.type.code'			,'Create'		,'00'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('tagging.type.code.restore'			,'tagging.type.code'			,'Restore'		,'10'	,20 );
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
insert into code (code_id, code_group, code_name, code_value, code_order) values ('log.template.type.code.request'	,'log.template.type.code'		,'Request'		,'10'	,10 );
insert into code (code_id, code_group, code_name, code_value, code_order) values ('log.template.type.code.tagging'	,'log.template.type.code'		,'Tagging'		,'20'	,20 );

insert into properties (property_key, property_value) values ('svn.authz.template.default'			,'[groups]%nsystem-admin=#system-admin#%ncommiter=#commiter#%nreviewer=#reviewer#%n%n[#svn_name#:/]%n@system-admin=rw%n@reviewer=r%n@commiter=r%n%n[#svn_name#:#trunk_path#]%n@reviewer=rw%n@commiter=rw%n%n[#svn_name#:#branches_path#]%n%n[#svn_name#:#tags_path#]%n@reviewer=rw%n');
insert into properties (property_key, property_value) values ('commit.log.template.request.default'	,'[Request Group ID]: #request-group-id#%n[Request ID]: #request-id#%n[Request User]: #request-user-name#(#request-user-id#)%n[Approve User]: #approve-user-name#(#approve-user-id#)%n[Transfer User]: #transfer-user-name#(#transfer-user-id#)%n[Description]:%n#description#');
insert into properties (property_key, property_value) values ('commit.log.template.tagging.default'	,'[Tagging ID]: #tagging-id#%n[Tagging User]: #tagging-user-name#(#tagging-user-id#)%n[Description]:%n#description#');
insert into properties (property_key, property_value) values ('application.version'					,'0.2.1');

-- haks1999 // aW9fj8bm9Rt5
insert into repositories (repository_key, repository_location, active, repository_name, svn_root, svn_name, trunk_path, tags_path, branches_path, auth_user_id, auth_user_passwd, sync_user, repository_order ) values ('HAKSVNG','https://haksvn.googlecode.com/svn', 'common.boolean.yn.code.y', 'haksvn google repository', 'https://haksvn.googlecode.com/svn', 'svn', '/trunk', '/tags', '/branches/production', 'haks1999', 'nijy14K6p4n1oleemgofSw==','common.boolean.yn.code.n', 1 );
-- haks1999 // haks1999
insert into repositories (repository_key, repository_location, active, repository_name, svn_root, svn_name, trunk_path, tags_path, branches_path, auth_user_id, auth_user_passwd, sync_user, repository_order, connect_type, server_ip, server_user_id, server_user_passwd, authz_path, passwd_path, passwd_type, authz_template) values ('HAKSVNL','https://localhost/svn/main/haksvn', 'common.boolean.yn.code.y', 'test local repository', 'https://localhost/svn/main', 'main', '/trunk', '/tags', '/branches/production', 'haks1999', 'ty0VNz1v8qkwfc8Cpz1NXA==','common.boolean.yn.code.y', 2, 'server.connect.type.code.local', null, null, null, 'D:\MyDev\haks1999\repositories\authz', 'D:\MyDev\haks1999\repositories\htpasswd', 'svn.passwd.type.code.md5-apache', '[groups]%nsystem-admin=#system-admin#%ncommiter=#commiter#%nreviewer=#reviewer#%n%n[#svn_name#:/]%n@system-admin=rw%n%n[#svn_name#:#trunk_path#]%n@reviewer=rw%n@commiter=rw%n%n[#svn_name#:#branches_path#]%n@reviewer=r%n@commiter=r%n%n[#svn_name#:#tags_path#]%n@reviewer=rw%n@commiter=r' );

insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (1,'admin','administrator','common.boolean.yn.code.y','admin@gmail.com','CinA5MJWDvBTvOJSvluE4g==', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (2,'haks','seungrin.lee','common.boolean.yn.code.y','haks1999@gmail.com','HpY9T+GzO+pGi4KuzUuSjg==', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (3,'ggae','ildong.yang','common.boolean.yn.code.y','ggae123@gmail.com','twYwNKOTe6WqcnUZlQWy0Q==', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (4,'spike0201','jinyoon.kim','common.boolean.yn.code.y','spike0201@lgcns.com','w9oKTqKTtvBuRUVbhQP/qw==', 'user.auth.type.code.system-admin');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (5,'user01','test-user001','common.boolean.yn.code.y','user01@gmail.com','O1C0K2pANKmoLtYUBBqryw==', 'user.auth.type.code.commiter');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (6,'user02','test-user002','common.boolean.yn.code.y','user02@gmail.com','ej3AUzTKsETeEyDwsmeBbQ==', 'user.auth.type.code.commiter');
insert into users(user_seq, user_id, user_name, active, email, user_passwd, auth_type) values (7,'user03','test-user003','common.boolean.yn.code.y','user03@gmail.com','MvglwVcxLRl64J1COgxx2g==', 'user.auth.type.code.commiter');

insert into repositories_users(repository_key, user_seq) values ('HAKSVNG',1);
insert into repositories_users(repository_key, user_seq) values ('HAKSVNG',2);
insert into repositories_users(repository_key, user_seq) values ('HAKSVNG',3);

insert into menu_authority(menu_seq, code_id) values ( 100, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 110, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 120, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 130, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 200, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 210, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 220, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 300, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 310, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 311, 'user.auth.type.code.system-admin');
--insert into menu_authority(menu_seq, code_id) values ( 312, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 320, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 321, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 322, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 330, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 331, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 332, 'user.auth.type.code.system-admin');
insert into menu_authority(menu_seq, code_id) values ( 333, 'user.auth.type.code.system-admin');

insert into menu_authority(menu_seq, code_id) values ( 100, 'user.auth.type.code.reviewer');
insert into menu_authority(menu_seq, code_id) values ( 110, 'user.auth.type.code.reviewer');
insert into menu_authority(menu_seq, code_id) values ( 120, 'user.auth.type.code.reviewer');
insert into menu_authority(menu_seq, code_id) values ( 130, 'user.auth.type.code.reviewer');
insert into menu_authority(menu_seq, code_id) values ( 200, 'user.auth.type.code.reviewer');
insert into menu_authority(menu_seq, code_id) values ( 210, 'user.auth.type.code.reviewer');
insert into menu_authority(menu_seq, code_id) values ( 220, 'user.auth.type.code.reviewer');

insert into menu_authority(menu_seq, code_id) values ( 100, 'user.auth.type.code.commiter');
insert into menu_authority(menu_seq, code_id) values ( 110, 'user.auth.type.code.commiter');
insert into menu_authority(menu_seq, code_id) values ( 120, 'user.auth.type.code.commiter');
insert into menu_authority(menu_seq, code_id) values ( 130, 'user.auth.type.code.commiter');
insert into menu_authority(menu_seq, code_id) values ( 200, 'user.auth.type.code.commiter');
insert into menu_authority(menu_seq, code_id) values ( 210, 'user.auth.type.code.commiter');
insert into menu_authority(menu_seq, code_id) values ( 220, 'user.auth.type.code.commiter');

