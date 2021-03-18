--system_permission
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (1, 1, '系統管理', '0001001111', 1001, '參數-設定', 'sys_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (2, 1, '系統管理', '0001001111', 1002, '權限-管理', 'sys_permission.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (3, 1, '系統管理', '0001001111', 1003, '帳號-管理', 'sys_user.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (4, 1, '系統管理', '0001001111', 1004, '群組-管理', 'sys_group.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (5, 1, '系統管理', '0001001111', 1005, '布告欄-管理', 'sys_none.basil');

INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (6, 2, '個人功能', '0001000001', 1101, '首頁', 'index.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (7, 2, '個人功能', '0001001101', 1102, '個人-資料', 'own_user.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (8, 2, '個人功能', '0001001101', 1103, '個人-設定', 'own_config.basil');

--system_group(sg_permission[特殊3(512),特殊2(256),特殊1(128),訪問(64),下載(32),上傳(16),新增(8),修改(4),刪除(2),查詢(1)])
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '1111111111', 1);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '1111111111', 2);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '1111111111', 3);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '1111111111', 4);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '1111111111', 5);

INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '0001000001', 6);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '0001000001', 7);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (1, '系統管理者', '0001000001', 8);

INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (2, '一般使用者', '0001000001', 6);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (2, '一般使用者', '0001000001', 7);
INSERT INTO system_group(sg_g_id, sg_name, sg_permission, sg_sp_id) VALUES (2, '一般使用者', '0001000001', 8);

--system_user
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (1,'admin','Admin_ch', 'admin@dtr.com', 'Admin_en', '123456', '超級管理者',1 );
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (2,'user','User_ch', 'user@dtr.com', 'User_en', '123456', '一般使用者',2);





--workstation_prohect


