insert into menu (menu_seq, menu_name, menu_jsp_path, menu_url, parent_menu_seq, menu_order, menu_level) values (1, 'Transfer', '/WEB-INF/views/template_table.jsp', 'Transfer', 1, 100, 1);
insert into menu (menu_seq, menu_name, menu_jsp_path, menu_url, parent_menu_seq, menu_order, menu_level) values (2, 'Request', '/WEB-INF/views/template_table.jsp', 'Transfer/Request',1, 100, 2);
insert into menu (menu_seq, menu_name, menu_jsp_path, menu_url, parent_menu_seq, menu_order, menu_level) values (3, 'Approval', '/WEB-INF/views/template_table.jsp', 'Transfer/Approval',1, 200, 2);
insert into menu (menu_seq, menu_name, menu_jsp_path, menu_url, parent_menu_seq, menu_order, menu_level) values (4, 'Configuration', '/WEB-INF/views/template_table.jsp', 'Configuration', 4, 200, 1);
insert into menu (menu_seq, menu_name, menu_jsp_path, menu_url, parent_menu_seq, menu_order, menu_level) values (5, 'Users', '/WEB-INF/views/template_table.jsp', 'Configuration/Users', 4, 100, 2);
insert into menu (menu_seq, menu_name, menu_jsp_path, menu_url, parent_menu_seq, menu_order, menu_level) values (6, 'Repositories', '/WEB-INF/views/template_table.jsp', 'Configuration/Repositories', 4, 200,2 );

insert into code (code_seq, code_group, code_name, code_value, code_order) values (1,'repository.state', 'active', '10', 10 );
insert into code (code_seq, code_group, code_name, code_value, code_order) values (2,'repository.state', 'inactive', '20', 20 );